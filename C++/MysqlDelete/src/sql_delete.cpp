#include <iostream>
#include <pthread.h>

using namespace std;

#ifdef ERR1
extern "C" {
    void thrilleAssertC(int);
}
#endif

#define TABLE int
#define TABLE_DELETE 1
#define TABLE_INSERT 2

#define LOG int
#define LOG_DELETE 3
#define LOG_INSERT 4

/* global variables */
bool bug_mode;
TABLE t;
LOG log;
pthread_mutex_t lock_open;
pthread_mutex_t lock_log;

void mysql_insert()
{
	// add artificial delay
	/* if (bug_mode)
		usleep(5);*/
		
	// BUG: ACCESS 2 & 3
	pthread_mutex_lock(&lock_open);
	t = TABLE_INSERT;
	printf("access 2\n");
	pthread_mutex_unlock(&lock_open);
	
	pthread_mutex_lock(&lock_log);
	log = LOG_INSERT;
	printf("access 3\n");
	pthread_mutex_unlock(&lock_log);

}

void *thread(void *data)
{
	mysql_insert();
	return NULL;	
}

void generate_table()
{
	// BUG: ACCESS 1
	pthread_mutex_lock(&lock_open);
	t = TABLE_DELETE;
	printf("access 1\n");
	pthread_mutex_unlock(&lock_open);
	
	// add artificial delay
	if (bug_mode) {
		usleep(100);
		// printf ("sleeping... \n");
	}
	
	// BUG: ACCESS 4
	pthread_mutex_lock(&lock_log);
	log = LOG_DELETE;
	printf("access 4\n");
	pthread_mutex_unlock(&lock_log);
}

int main(int argc, char *argv[])
{
	int rc;
	pthread_t thd;
	if (argc == 1)
		bug_mode = false;
	else
		bug_mode = true;
	
	pthread_mutex_init(&lock_open, NULL);
	pthread_mutex_init(&lock_log, NULL);

	/* create thread */
	rc = pthread_create(&thd, NULL, thread, NULL);
	if (rc) {
		printf("create thread failed. error code = %d\n", rc);
	}	

	generate_table();
	pthread_join(thd, NULL);

	printf("Final state: table %s, log %s\n", 
		(t == TABLE_DELETE) ? "DELETE" : "INSERT", 
		(log == LOG_DELETE) ? "DELETE" : "INSERT");
#ifdef ERR1
	bool fail = (t == TABLE_INSERT && log == LOG_DELETE);
	thrilleAssertC(!fail);
#endif
	
	return 0;
}
