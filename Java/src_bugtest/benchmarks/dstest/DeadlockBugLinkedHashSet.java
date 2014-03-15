package benchmarks.dstest;

import java.util.Random;

import benchmarks.instrumented.java.util.Collections;
import benchmarks.instrumented.java.util.HashSet;
import benchmarks.instrumented.java.util.Set;

public class DeadlockBugLinkedHashSet extends Thread {
	Set s1,s2;
	public static Random rand = new Random();
	static boolean falcon_exception = false;
	
	public DeadlockBugLinkedHashSet(Set s1, Set s2) {
		this.s1 = s1;
		this.s2 = s2;
	}
	
	public static int getInput() {
		switch(Math.abs(rand.nextInt()%3)) {
		case 0: return 0;
		case 1: return 13;
		default:
		}
		return Math.abs(rand.nextInt()%13);
    }

	public void run() {
		try{
			for (int i = 0; i < 1; i++) {
				switch(getInput()){
				case 0:
					s1.add(new SimpleObject(getInput()));
					break;
				case 1:
					s1.size();
					break;
				case 2:
					s1.clear();
					break;
				case 3:
					s1.contains(new SimpleObject(getInput()));
					break;
				case 4:
					s1.remove(new SimpleObject(getInput()));
					break;
				case 5:
					s1.toArray();
					break;
				case 6:
					s1.isEmpty();
					break;
				case 7:
					s1.iterator();
					break;
				case 8:
					s1.addAll(s2);
					break;
				case 9:
					s1.equals(s2);
					break;
				case 10:
					s1.retainAll(s2);
					break;
				case 11:
					s1.containsAll(s2);
					break;
				default:
					s1.removeAll(s2);
					break;
				}
			}
		} catch(Exception e) {
			falcon_exception = true;
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		falcon_exception = false;
		Set l1 = Collections.synchronizedSet(new HashSet());
        Set l2 = Collections.synchronizedSet(new HashSet());
        l1.add(new SimpleObject(getInput()));
        l1.add(new SimpleObject(getInput()));
        l1.add(new SimpleObject(getInput()));
        l2.add(new SimpleObject(getInput()));
        l2.add(new SimpleObject(getInput()));
        l2.add(new SimpleObject(getInput()));
        System.out.println("-l1:"+l1);
		System.out.println("-l2:"+l2);
        try {
            Thread t1 = new DeadlockBugLinkedHashSet(l1, l2);
            Thread t2 = new DeadlockBugLinkedHashSet(l2, l1);            
            t1.start();            
            t2.start();
            t1.join();
			t2.join();
			System.out.println("+l1:"+l1);
			System.out.println("+l2:"+l2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (falcon_exception)
			throw new RuntimeException("Automicity bug found.");
	}
}
