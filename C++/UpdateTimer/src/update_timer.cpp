#include <iostream>
#include <pthread.h>

using namespace std;

#ifdef ERR1
extern "C" {
    void thrilleAssertC(int);
}
#endif

int bug_mode;

int *simulate_lock;
volatile int counter = 0;

class nsNSSComponent
{
	public:
		bool mUpdateTimerInitialized;
		int dummy;
	public:
		nsNSSComponent();
		~nsNSSComponent();
		void StopCRLUpdateTimer();
};

nsNSSComponent::nsNSSComponent()
{
	mUpdateTimerInitialized = true;
}

nsNSSComponent::~nsNSSComponent()
{
	if (mUpdateTimerInitialized == true) {
		/* destroy the lock */
		//free(simulate_lock);
        __sync_fetch_and_add(&counter, 1);
		mUpdateTimerInitialized = false;
	}
	/* int cnt;
	while(1) {
		cnt++;
		if (cnt == 10000)
			return;
	}; */
}

void nsNSSComponent::StopCRLUpdateTimer()
{
	if (mUpdateTimerInitialized == true) {
		/* destroy the lock */
		free(simulate_lock);
        __sync_fetch_and_add(&counter, 1);
		if (bug_mode)
			usleep(1);
		mUpdateTimerInitialized = false;
	}
}

void *thread(void *data)
{
	nsNSSComponent *comp = (nsNSSComponent *)data;
	comp->StopCRLUpdateTimer();
	return NULL;
}

int main(int argc, char *argv[])
{
	/* if (argc == 1)
		bug_mode = 0;
	else
		bug_mode = 1; */
	bug_mode = 0;
	simulate_lock = new int;
	int rc;
	pthread_t thd;
	nsNSSComponent *comp = new nsNSSComponent;
	rc = pthread_create(&thd, NULL, thread, comp);
	// usleep(1000*10);
	delete comp;
	pthread_join(thd, NULL);
	cout << "counter = " << counter << endl;
#ifdef ERR1 
    thrilleAssertC(counter==1);
#endif
	return 0;
}
