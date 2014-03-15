#include <iostream>
#include <pthread.h>

using namespace std;

#ifdef ERR1
extern "C" {
    void thrilleAssertC(int);
}
#endif

#define JSPropertyCacheEntry int
#define JSBool bool
#define SIZE 4
#define index 1
#define obj 1

struct JSPropertyCache
{
	JSPropertyCacheEntry table[SIZE];
	JSBool empty; /* whether the table is empty */
};

/* global variables */
bool bug_mode;
pthread_mutex_t t;
pthread_mutex_t e;
JSPropertyCache *cache;

void js_FlushPropertyCache()
{	
	// add artificial delay
	usleep(1);
	
	// BUG: ACCESS 2
	pthread_mutex_lock(&t);
	memset(cache->table, 0, SIZE);
	pthread_mutex_unlock(&t);
	
	// BUG: ACCESS 3
	// using different locks to protect correlated variable accesses 
	// leads to multi-variable concurrency bugs
	pthread_mutex_lock(&e);
	cache->empty = true;
	pthread_mutex_unlock(&e);
}

void *thread(void *data)
{
	js_FlushPropertyCache();
	return NULL;	
}

void js_PropertyCacheFill(JSPropertyCache *cache)
{
	// BUG: ACCESS 1
	pthread_mutex_lock(&t);
	cache->table[index] = obj;
	pthread_mutex_unlock(&t);
	
	// add artificial delay
	if (bug_mode)
		usleep(10);
	
	// BUG: ACCESS 4
	// using different locks to protect correlated variable accesses 
	// leads to multi-variable concurrency bugs
	pthread_mutex_lock(&e);
	cache->empty = false;
	pthread_mutex_unlock(&e);
}

int main(int argc, char *argv[])
{
	int rc;
	pthread_t thd;
	if (argc == 1)
		bug_mode = false;
	else
		bug_mode = true;
	
	pthread_mutex_init(&t, NULL);
	pthread_mutex_init(&e, NULL);
	
	cache = new JSPropertyCache;

	/* create thread */
	rc = pthread_create(&thd, NULL, thread, NULL);
	if (rc) {
		printf("create thread failed. error code = %d\n", rc);
	}	

	js_PropertyCacheFill(cache);
	pthread_join(thd, NULL);
	
	/* check access sequence */
	printf("Final state: table %d, empty %d\n", 
		cache->table[index], 
		cache->empty);

#ifdef ERR1
	bool fail = (cache->table[index] == obj && cache->empty == false);
	thrilleAssertC(!fail);
#endif	

	delete cache;	
	return 0;
}
