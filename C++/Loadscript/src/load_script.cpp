#include <iostream>
#include <pthread.h>

using namespace std;

class nsScript
{
	public:
		nsScript();
		void compile();
	private:
		string file;
};

nsScript::nsScript()
{
	file = "script";
}

void nsScript::compile()
{
	cout << "Compiling " << file << "... ";
}

/* global variables */
bool bug_mode;
pthread_mutex_t mutex;
nsScript *gCurrentScript;

void OnLoadComplete()
{
	if (bug_mode)
		usleep(1);
    /* callback function */
    pthread_mutex_lock(&mutex);
	gCurrentScript->compile();
	cout << "finish!" << endl;
    pthread_mutex_unlock(&mutex);
}

/* the forked thread doing script loading */
void *load_thread(void *data)
{
	cout << "Start loading..." << endl;
	return NULL;
}

void LaunchLoad(nsScript *aspt, pthread_t *thd)
{
	/* create a new thread to do this */
	int rc;
	rc = pthread_create(thd, NULL, load_thread, NULL);
	if (rc) {
		printf("create thread failed. error code = %d\n", rc);
	}
}

void LoadScript(nsScript *aspt, pthread_t *thd)
{
	pthread_mutex_lock(&mutex);
	gCurrentScript = aspt;
	LaunchLoad(aspt, thd);
	pthread_mutex_unlock(&mutex);
}

void *thread(void *data)
{
	nsScript *aspt = new nsScript;
	pthread_t thd;
	LoadScript(aspt, &thd);
	/* wait until compile finish */
	pthread_join(thd, NULL);
	OnLoadComplete();
	return NULL;	
}

/* called by main thread */
void thread2()
{
	if (bug_mode)
		usleep(1);
	pthread_mutex_lock(&mutex);
	gCurrentScript = NULL;
	pthread_mutex_unlock(&mutex);
}

int main(int argc, char *argv[])
{
	int rc;
	pthread_t thd;
	if (argc == 1)
		bug_mode = false;
	else
		bug_mode = true;
	
	pthread_mutex_init(&mutex, NULL);

	/* create thread */
	rc = pthread_create(&thd, NULL, thread, NULL);
	if (rc) {
		printf("create thread failed. error code = %d\n", rc);
	}
	
	/* call remote write */
	thread2();

	pthread_join(thd, NULL);

	return 0;
}
