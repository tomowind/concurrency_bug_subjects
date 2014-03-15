#include <iostream>
#include <pthread.h>

using namespace std;

#ifdef ERR1
extern "C" {
    void thrilleAssertC(int);
}
#endif

#define INIT_NAME "init_name"
#define INIT_FILE "init_file"
#define MOD_NAME "mod_name"
#define MOD_FILE "mod_file"

struct Log_event
{
public:
	const char* log_file_name;
	const char* log_file;
	Log_event() 
	{
		log_file_name = INIT_NAME;
		log_file = INIT_FILE;
	}
};

/* global variables */
bool bug_mode;
Log_event* relay_log = NULL;
const char* acc1 = INIT_NAME;
const char* acc2 = INIT_FILE;

void new_file()
{
	// add artificial delay
	usleep(1);
		
	// BUG: ACCESS 2 & 3
	relay_log->log_file_name = MOD_NAME;
	relay_log->log_file = MOD_FILE;
}

void *thread(void *data)
{
	new_file();
	return NULL;	
}

Log_event* next_event(Log_event* relay_log)
{
	// BUG: ACCESS 1
	acc1 = relay_log->log_file_name;
	printf("%s\n", acc1);
	
	// add artificial delay
	if (bug_mode)
		usleep(10);
	
	// BUG: ACCESS 4
	acc2 = relay_log->log_file;
	printf("%s\n", acc2);
	
	return relay_log;
}

int main(int argc, char *argv[])
{
	int rc;
	pthread_t thd;
	if (argc == 1)
		bug_mode = false;
	else
		bug_mode = true;
	
	relay_log = new Log_event;	

	/* create thread */
	rc = pthread_create(&thd, NULL, thread, NULL);
	if (rc) {
		printf("create thread failed. error code = %d\n", rc);
	}	

	next_event(relay_log);
	pthread_join(thd, NULL);
	delete relay_log;

#ifdef ERR1
	bool fail = (memcmp(acc1, INIT_NAME, 5) && memcmp(acc2, MOD_FILE, 5));
	thrilleAssertC(!fail);
#endif	
	return 0;
}
