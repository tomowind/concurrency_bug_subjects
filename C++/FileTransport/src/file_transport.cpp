#include <iostream>
#include <pthread.h>

using namespace std;

#ifdef ERR1
extern "C" {
    void thrilleAssertC(int);
}
#endif

#define NS_OK 1
#define NS_BASE_STREAM_WOULD_BLOCK 2

int bug_mode;
int mStatus = 0;
bool is_pass;

void OnDataAvailable()
{
#ifdef ERR1
	int temp1, temp2 = 0;
	temp2 = temp1;
	usleep(10);
	temp1 = temp2;
	usleep(10);
#endif 
	if (mStatus != NS_OK) {
#ifdef ERR1
        is_pass = 0;
#endif
		return; // ignore the event
	}
	else {
		cout << "handle event" << endl;
	}
}

void *thread(void *data)
{
	/* process the data */
	OnDataAvailable();
    return NULL;
}

int WriteFrom(pthread_t *thd)
{
	/* do some stuff here */
	int rc;
	rc = pthread_create(thd, NULL, thread, NULL);
	return NS_BASE_STREAM_WOULD_BLOCK;
}

void Process(pthread_t *thd)
{
	mStatus = WriteFrom(thd);
	/* if (bug_mode)
		usleep(1); */
	if (mStatus == NS_BASE_STREAM_WOULD_BLOCK) {
		mStatus = NS_OK;
	}
}

int main(int argc, char *argv[])
{
	if (argc == 1)
		bug_mode = 0;
	else
		bug_mode = 1;

#ifdef ERR1
    is_pass = 1;
#endif
	pthread_t thd;
	Process(&thd);
	pthread_join(thd, NULL);
#ifdef ERR1
    thrilleAssertC(is_pass);
#endif
	return 0;
}
