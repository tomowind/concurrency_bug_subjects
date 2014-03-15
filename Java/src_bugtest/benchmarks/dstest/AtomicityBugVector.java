package benchmarks.dstest;


import benchmarks.instrumented.java.util.Vector;

class ExceptionObject {
	private boolean exception = false;
	public ExceptionObject() { exception = false; }
	public boolean isException() { return exception; }
	public void setException() { exception = true; }
}

/**
 * This test is exactly same as ArrayList test,
 * but this one throws exception, while ArrayList one does not.
 * 
 * This example is borrowed from the following paper.
 * Kidd, N., Reps, T., Dolby, J., and Vaziri, M., Static detection of atomic-set serializability violations. TR-1623, Computer Sciences Department, University of Wisconsin, Madison, WI, October 2007
 * 
 * @author tomowind
 *
 */
public class AtomicityBugVector {
	public static void main(String[] args) {
    	final ExceptionObject exceptionObj = new ExceptionObject();
    	final Vector v = new Vector();    	
		v.add(new Integer(1));

		Thread t1 = new Thread(new Runnable(){
			public void run() {
				try {
					v.clear();					
				} catch(Exception e) {
					exceptionObj.setException();
					e.printStackTrace();
				}
			}
		});
		Thread t2 = new Thread(new Runnable(){
			public void run() {
				try {
					Vector w = new Vector(v);
					if( !w.isEmpty() )
						System.out.println(((Integer)w.get(0)).intValue()); 
					// null pointer exception... but cannot catch. dunno why. :(
				} catch(Exception e) {
					exceptionObj.setException();
					e.printStackTrace();
				}
			}
		});
		t1.start(); t2.start();
    	try {
			t1.join(); t2.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (exceptionObj.isException())
			new RuntimeException("Atomicity Exception is found.");
    }   
}
