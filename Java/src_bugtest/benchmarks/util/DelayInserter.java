package benchmarks.util;

import benchmarks.jpf_test_cases.MyRandom;

/**
 * This class adds artificial delay to the threads. 
 * The idea was first suggested from the ConTest tool. 
 * 
 * @author tomowind
 *
 */
public class DelayInserter {
        
	public static void addRandomDelay(int max) {
    	try {
    		int randomdelay = MyRandom.nextInt(max);
    		if (randomdelay == 0)
    			Thread.yield();
    		else
    			Thread.sleep(randomdelay);
    		
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }
    
    public static void addDelay(int delay) {
    	try {
    		if (delay == 0)
    			Thread.yield();
    		else
    			Thread.sleep(delay);
    		
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }    
}
