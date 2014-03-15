package benchmarks.dstest;

public class AtomicityBugVector_tester {

	/*
     * main test routine
     */
    public static void main(String[] args) {
    	int succ=0, fail=0;
        for(int i=1; i <= 1000; i++)
        {
        	try {
        		AtomicityBugVector.main(args);
        	} catch (RuntimeException e) {
    			e.printStackTrace();
    			fail++; continue;
    		} 
        	succ++; continue;
        }
        System.out.println("succ: "+succ+"("+(succ*100/(fail+succ))+"%), fail: "+fail+"("+(fail*100/(fail+succ))+"%)");
    }    
}
