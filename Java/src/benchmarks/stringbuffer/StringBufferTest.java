package benchmarks.stringbuffer;

/**
 * Created by IntelliJ IDEA.
 * User: ksen
 * Date: Jun 2, 2007
 * Time: 2:08:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class StringBufferTest extends Thread {
	static boolean falcon_exception = false;
    StringBuffer al1, al2;
    int c = 0;

    public StringBufferTest(StringBuffer al1, StringBuffer al2, int choice) {
        this.al1 = al1;
        this.al2 = al2;
        this.c = choice;
    }

    public void run() {
    	try {
    		switch(c){
            case 0:
                al2.append(al1);
                break;
            case 1:
                al1.delete(0,al1.length());
                break;
            default:
                al1.insert(0,'a');
                break;
    		}
    	} catch(Exception e) {
    		e.printStackTrace();
    		falcon_exception = true;
    	}
    }

    public static void main(String args[]){
    	falcon_exception = false;
        StringBuffer al1 = new benchmarks.stringbuffer.StringBuffer("Hello");
        StringBuffer al2 = new benchmarks.stringbuffer.StringBuffer("World");
        
     // set thread number
        int numthreads = 3;
    	if (args.length > 0) {
    		numthreads = Integer.parseInt(args[0]);
    		if (numthreads <= 3) numthreads = 3;
    	}
    	
    	// execute threads
    	Thread[] threads = new StringBufferTest[numthreads];
    	for (int i=0; i<numthreads; i++) {
    		threads[i] = new StringBufferTest(al1,al2, i%3);
    		threads[i].start();
    	}
    	for (int i=0; i<numthreads; i++) {
    		try {
    			threads[i].join();
    		} catch (InterruptedException e) {
    			e.printStackTrace();
    		}
    	}
//        System.out.println(al1);
//        System.out.println(al2);
        
        if (falcon_exception)
        	throw new RuntimeException("atomicity bug found");
    }
}
