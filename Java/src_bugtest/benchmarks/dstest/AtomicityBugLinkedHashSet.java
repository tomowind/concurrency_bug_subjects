package benchmarks.dstest;

import benchmarks.instrumented.java.util.Collections;
import benchmarks.instrumented.java.util.LinkedHashSet;
import benchmarks.instrumented.java.util.Set;

/**
 * This testcase throws exception once in 1000 iterations in average. 
 * benchmarks.instrumented.java.util.ConcurrentModificationException is thrown from
 * benchmarks.instrumented.java.util.AbstractSet.removeAll(AbstractSet.java:142).
 * 
 * This bug is found in most "bulk" methods in Collections, such as 
 * addAll(Collection) and removeAll(Collection c). 
 * 
 * For the iterator synchronization bugs, the comment in JavaDoc
 * states that users should synchronize iterators.
 * 
 * This bug is found from most automicity papers. 
 * 
 * @author tomowind
 *
 */
public class AtomicityBugLinkedHashSet extends Thread {	
	static boolean falcon_exception = false;
	Set s1,s2;
    int c;
    
    public AtomicityBugLinkedHashSet(Set s1, Set s2,int c) {
        this.s1 = s1;
        this.s2 = s2;
        this.c = c;
    }
    
    /*
     * Thread entry
     */
    public void run() {
    	SimpleObject o1 = new SimpleObject(1);
    	SimpleObject o2 = new SimpleObject(2);
    	SimpleObject o3 = new SimpleObject(3);
    	try{
    		switch(c){
    		case 0:
    			s1.add(o1);
    			s1.add(o2);
    			s1.add(o3);
    			
    			s2.add(o1);
    			s2.add(o2);
    			s2.add(o3);
    			break;
    			
    		case 1:
    			s1.removeAll(s2);
    			break;
    			
    		case 2:
    			s2.clear();
    			break;
    			
    		case 3:
    			s1.contains(o1);
    			break;
    			
    		case 4:
    			s1.contains(o2);
    			break;
    			
    		case 5:
    			s1.contains(o1);
    			break;
    			
    		case 6:
    			s1.contains(o2);
    			break;
    			
    		case 7:
    			s2.contains(o1);
    			break;
    			
    		case 8:
    			s2.contains(o2);
    			break;
    			
    		case 9:
    			s2.contains(o3);
    			break;
    			
    		case 10:
    			s1.contains(o3);
    			break;
    			
			default:
				break;
    		}
    	} catch(Exception e) {
    		falcon_exception = true;
    		e.printStackTrace();
    	}
    }
	
    /*
     * main test routine
     */
    public static void main(String[] args) {
    	falcon_exception = false;
        Set s1 = Collections.synchronizedSet(new LinkedHashSet());
        Set s2 = Collections.synchronizedSet(new LinkedHashSet());
        
        // set thread number
        int numthreads = 3;
        if (args.length > 0) {
    		numthreads = Integer.parseInt(args[0]);
    		if (numthreads <= 3) numthreads = 3;
    	}
    	
    	// execute threads
    	Thread[] threads = new AtomicityBugLinkedHashSet[numthreads];
    	for (int i=0; i<numthreads; i++) {
    		int k=i%11;
    		threads[i] = new AtomicityBugLinkedHashSet(s1,s2,k);
    		threads[i].start();
    	}
    	for (int i=0; i<numthreads; i++) {
    		try {
    			threads[i].join();
    		} catch (InterruptedException e) {
    			e.printStackTrace();
    		}
    	} 
		
		if (falcon_exception)
			throw new RuntimeException("Automicity bug found.");
    }    
}

