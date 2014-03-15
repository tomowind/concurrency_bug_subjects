package benchmarks.dstest;

import benchmarks.instrumented.java.util.ArrayList;

public class MTSangminTest {
    public static void main(String args[]){
    	int max = 10;
    	ArrayList a1 = new ArrayList();
    	for (int i = 0; i < max; i++)
    		if (i % 2 == 0)
    			a1.add(i);
    		else if (a1.contains(i))
    			a1.remove(i);
    	System.out.println(a1);
    }
}
