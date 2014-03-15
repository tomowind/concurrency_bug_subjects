#include <iostream>
#include <pthread.h>

using namespace std;

#ifdef ERR1
extern "C" {
    void thrilleAssertC(int);
}
#endif

#define LOG_INIT 0
#define LOCAL_LOG 1
#define LOG_CLOSED 1023

bool bug_mode;
bool is_pass;

class MYSQL_LOG
{
	public:
		int log_type;
	public:
        MYSQL_LOG () { log_type = LOG_INIT; }
		void new_file();
};

void MYSQL_LOG::new_file()
{
	// cloas old bin log
	log_type = LOG_CLOSED;
	//if (bug_mode)
	//	usleep(1);
	log_type = LOCAL_LOG;
}

/* global variables */
MYSQL_LOG mysql_bin_log;

void sql_insert()
{
	// do table update
	usleep(100);
	
	// log into bin_log_file
	if (mysql_bin_log.log_type == LOG_CLOSED) {
		// do nothing
		cout << "error! miss log" << endl;
        // cout << mysql_bin_log.log_type;
#ifdef ERR1
        is_pass = 0;
#endif
	} else {
		cout << "log into binlog" << endl;
#ifdef ERR1
        is_pass = 1;
#endif
	}
}

void *thread(void *data)
{
	sql_insert();
	return NULL;
}

int main(int argc, char *argv[])
{
	if (argc == 1)
		bug_mode = false;
	else
		bug_mode = true;

#ifdef ERR1
    is_pass = 1;
#endif
	int rc;
	pthread_t thd;
	rc = pthread_create(&thd, NULL, thread, NULL);
	if (rc) {
		printf("create thread error. error code = %d\n", rc);
	}
	mysql_bin_log.new_file();
	pthread_join(thd, NULL);
#ifdef ERR1
    thrilleAssertC(is_pass);
#endif
	return 0;
}
