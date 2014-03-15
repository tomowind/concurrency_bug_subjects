#include <iostream>
#include <pthread.h>

using namespace std;

#ifdef ERR1
extern "C" {
    void thrilleAssertC(int);
}
#endif

class nsZipItem
{
	public:
		bool hasDataOffset;
	public:
		nsZipItem();
};

nsZipItem::nsZipItem()
	: hasDataOffset(false)
{
	// do nothing here
}

int bug_mode;

volatile int counter = 0;

void SeekToItem(nsZipItem *aItem)
{
	if (!aItem->hasDataOffset) {
		/* do some stuff that only need to do once */
        __sync_fetch_and_add(&counter, 1);
		// counter++;
		if (bug_mode)
			usleep(1);
		aItem->hasDataOffset = true;
	}
	/* do some other stuff */
}

void *thread1(void *data)
{
	nsZipItem *aItem = (nsZipItem *)data;
	SeekToItem(aItem);
	return NULL;
}

void *thread2(void *data)
{
	nsZipItem *aItem = (nsZipItem *)data;
	SeekToItem(aItem);
	return NULL;
}

int main(int argc, char *argv[])
{
	if (argc == 1)
		bug_mode = 0;
	else
		bug_mode = 1;
	int rc1, rc2;
	pthread_t thd1, thd2;
	nsZipItem *aItem = new nsZipItem;
	rc1 = pthread_create(&thd1, NULL, thread1, aItem);
	rc2 = pthread_create(&thd2, NULL, thread2, aItem);
	pthread_join(thd1, NULL);
	pthread_join(thd2, NULL);
	cout << "counter = " << counter << endl;
#ifdef ERR1 
    thrilleAssertC(counter==1);
#endif
	return 0;
}
