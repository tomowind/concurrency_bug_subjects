package benchmarks.dstest;

import benchmarks.instrumented.java.util.Collections;
import benchmarks.instrumented.java.util.LinkedList;
import benchmarks.instrumented.java.util.List;
import benchmarks.jpf_test_cases.MyRandom;

/**
 * Created by IntelliJ IDEA.
 * User: Koushik Sen (ksen@cs.uiuc.edu)
 * Date: Dec 26, 2005
 * Time: 9:46:07 AM
 */
public class MTListTest extends Thread {
    List al1, al2;
    int c;

    public MTListTest(List al1, List al2,int c) {
        this.al1 = al1;
        this.al2 = al2;
        this.c = c;
    }

    public void run() {
        SimpleObject o1 = new SimpleObject(MyRandom.nextInt(3));
        switch(c){
            case 0:
                al1.add(o1);
                break;
            case 1:
                al1.addAll(al2);
                break;
            case 2:
                al1.clear();
                break;
            case 3:
                al1.contains(o1);
                break;
            case 4:
                al1.containsAll(al2);
                break;
            case 5:
                al1.remove(o1);
                break;
            default:
                al1.removeAll(al2);
                break;
        }
    }

//    public static void arrayList(){
//        List al1 = Collections.synchronizedList(new ArrayList());
//        List al2 = Collections.synchronizedList(new ArrayList());
//        (new MTListTest(al1,al2)).start();
//        (new MTListTest(al2,al1)).start();
//        (new MTListTest(al1,al2)).start();
//        (new MTListTest(al2,al1)).start();
//    }
//
    public static void main(String args[]){
        List al1 = Collections.synchronizedList(new LinkedList());
        List al2 = Collections.synchronizedList(new LinkedList());
        (new MTListTest(al2,al1,5)).start();
        (new MTListTest(al1,al2,6)).start();
        (new MTListTest(al1,al2,0)).start();
        (new MTListTest(al2,al1,1)).start();
        (new MTListTest(al1,al2,2)).start();
        (new MTListTest(al2,al1,3)).start();
        (new MTListTest(al1,al2,4)).start();
    }
}
