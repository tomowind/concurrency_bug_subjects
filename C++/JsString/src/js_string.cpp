#include <iostream>
#include <pthread.h>

using namespace std;

#ifdef ERR1
extern "C" {
    void thrilleAssertC(int);
}
#endif

#define uint32 int

struct JSRuntime
{
	uint32 totalStrings;	/* # of allocated strings */
	double lengthSum; /* Total length of allocated strings */
};

/* global variables */
bool bug_mode;
pthread_mutex_t mutex1;
pthread_mutex_t mutex2;
JSRuntime *rt;
uint32 s_count;
double s_mean;

void printJSStringStats()
{
	// add artificial delay
	usleep(1);
		
	// BUG: ACCESS 2 & 3
	s_count = rt->totalStrings;	
	s_mean = rt->lengthSum / s_count;
	printf("%d strings, mean length %g\n", s_count, s_mean);
}

void *thread(void *data)
{
	printJSStringStats();
	return NULL;	
}

void js_NewString(JSRuntime* rt)
{
	// BUG: ACCESS 1
	// allocate a new string
	pthread_mutex_lock(&mutex1);
	rt->totalStrings = 1;
	pthread_mutex_unlock(&mutex1);
	
	// add artificial delay
	if (bug_mode)
		usleep(10);
	
	// BUG: ACCESS 4
	// using different locks to protect correlated variable accesses 
	// leads to multi-variable concurrency bugs
	pthread_mutex_lock(&mutex2);
	rt->lengthSum += 1;
	pthread_mutex_unlock(&mutex2);
}

int main(int argc, char *argv[])
{
	int rc;
	pthread_t thd;
	if (argc == 1)
		bug_mode = false;
	else
		bug_mode = true;
	
	pthread_mutex_init(&mutex1, NULL);
	pthread_mutex_init(&mutex2, NULL);
	
	rt = new JSRuntime;

	/* create thread */
	rc = pthread_create(&thd, NULL, thread, NULL);
	if (rc) {
		printf("create thread failed. error code = %d\n", rc);
	}	

	js_NewString(rt);
	pthread_join(thd, NULL);
	delete rt;

#ifdef ERR1
	bool fail = (s_count == 1 && s_mean == 0);
	thrilleAssertC(!fail);
#endif	
	
	return 0;
}
