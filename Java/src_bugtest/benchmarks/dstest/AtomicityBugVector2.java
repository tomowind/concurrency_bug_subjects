package benchmarks.dstest;

import benchmarks.atomicity.SimpleObject;
import benchmarks.instrumented.java.util.Vector;

public class AtomicityBugVector2 extends Thread {
	static boolean falcon_exception = false;
	Vector l1,l2,l3;
    int c;
    
	SimpleObject o1 = new SimpleObject(1);
	SimpleObject o2 = new SimpleObject(2);
	SimpleObject o3 = new SimpleObject(3);
	SimpleObject o4 = new SimpleObject(4);
	SimpleObject o5 = new SimpleObject(5);
	SimpleObject o6 = new SimpleObject(6);

    
    public AtomicityBugVector2(Vector l1, Vector l2,Vector l3,int c) {
        this.l1 = l1;
        this.l2 = l2;
        this.l3 = l3;
        this.c = c;
    }

    /*
     * Thread entry
     */
    public void run() {
    	try{
    		switch(c){
    		case 0:    			
    			l2.add(o1);
    			l2.add(o2);
    			l2.add(o3);
    			l2.add(o4);
    			l2.add(o5);
    			l2.add(o6);
    			break;
    			
    		case 1:    			
    			l3.add(o1);
    			l3.add(o2);
    			l3.add(o3);
    			l3.add(o4);
    			l3.add(o5);
    			l3.add(o6);
    			break;
    			
    		case 2:    			
    			l2.addAll(l3);
    			break;
    			
    		case 3:
    			l1.addAll(new Vector(l2));
    			break;
    			
    		case 4:
    			l2.removeAll(l3);
    			break;
    			
    		case 5:
    			l1.contains(o2);
    			break;
    			
    		case 6:
    			l3.contains(o2);
    			break;
    			
    		case 7:
    			l3.contains(o2);
    			break;
    			
    		case 8:
    			l2.contains(o1);
    			break;
    			
    		case 9:
    			l1.removeAll(l3);
    			break;
    			
    		case 10:
    			l1.removeAll(l2);
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
    	Vector l1 = new Vector();
    	Vector l2 = new Vector();
    	Vector l3 = new Vector();
    	
        // set thread number
        int numthreads = 4;
        if (args.length > 0) {
    		numthreads = Integer.parseInt(args[0]);
    		if (numthreads <= 4) numthreads = 4;
    	}
    	
    	// execute threads
    	Thread[] threads = new AtomicityBugVector2[numthreads];
    	for (int i=0; i<numthreads; i++) {
    		int k=i%11;
    		threads[i] = new AtomicityBugVector2(l1,l2,l3,k);
    	}
        
    	threads[0].start();
    	threads[1].start();
    	try {
    		threads[0].join();
    		threads[1].join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		for (int i=2; i<numthreads; i++) {
			threads[i].start();
    	}
		
		for (int i=2; i<numthreads; i++) {
			try {
	    		threads[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    	}
//		System.out.println("l1: "+l1+"\tl2: "+l2+"\tl3: "+l3);
    	if (l1.toString().contains("null") || l1.contains(null))
    		throw new RuntimeException("Automicity bug found. l1 has a null element. : "+ l1);
		if (falcon_exception)
			throw new RuntimeException("Automicity bug found.");

    }    
}
