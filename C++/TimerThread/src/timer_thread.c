#include <stdio.h>
#include <unistd.h>
#include <pthread.h>

#define TRUE 1
#define FALSE 0

int bug_mode;

pthread_mutex_t mutex;
pthread_cond_t cond;

int mProcessing;
int mWaiting = FALSE;
pthread_t mThread;

void *run(void *data)
{
	pthread_mutex_lock(&mutex);
	mProcessing = TRUE;
	while (mProcessing) {
		// do some stuff
		mWaiting = TRUE;
		pthread_cond_wait(&cond, &mutex);
		mWaiting = FALSE;
	}
	pthread_mutex_unlock(&mutex);
	return NULL;
}

void shutdown()
{
	if (!bug_mode)
		usleep(1);
	pthread_mutex_lock(&mutex);
	mProcessing = FALSE;
	if (mWaiting) {
		pthread_cond_signal(&cond);
	}
	pthread_mutex_unlock(&mutex);
	/* join the thread */
	pthread_join(mThread, NULL);
}

int main(int argc, char *argv[])
{
	int rc;
	if (argc == 1)
		bug_mode = FALSE; // training
	else
		bug_mode = TRUE; // bug mode

	/* Initialize mutex and condition variable objects */
	pthread_mutex_init(&mutex, NULL);
	pthread_cond_init(&cond, NULL);

	/* create thread */
	rc = pthread_create(&mThread, NULL, run, NULL);
	if (rc) {
		printf("create thread failed. error code = %d\n", rc);
	}

	/* call shutdown */
	shutdown();

	printf("Bug free execution!\n");
	return 0;
}
