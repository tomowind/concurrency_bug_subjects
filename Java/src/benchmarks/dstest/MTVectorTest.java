package benchmarks.dstest;

import benchmarks.instrumented.java.util.Vector;
import benchmarks.jpf_test_cases.MyRandom;

/**
 * Created by IntelliJ IDEA.
 * User: Koushik Sen (ksen@cs.uiuc.edu)
 * Date: Dec 26, 2005
 * Time: 5:05:28 PM
 */
public class MTVectorTest extends Thread {
    Vector v1;
    Vector v2;
    int c;

    public MTVectorTest(Vector v1, Vector v2,int c) {
        this.v1 = v1;
        this.v2 = v2;
        this.c = c;
    }

    public void run() {
        SimpleObject o1 = new SimpleObject(MyRandom.nextInt(3));
        switch(c){
            case 0:
                v1.add(o1);
                break;
            case 1:
                v1.addAll(v2);
                break;
            case 2:
                v1.clear();
                break;
            case 3:
                v1.contains(o1);
                break;
            case 4:
                v1.containsAll(v2);
                break;
            case 5:
                v1.remove(o1);
                break;
            default:
                v1.removeAll(v2);
                break;
        }
    }

    public static void main(String[] args) {
        Vector v1 = new Vector();
        Vector v2 = new Vector();
        (new MTVectorTest(v2,v1,5)).start();
        (new MTVectorTest(v1,v2,6)).start();
        (new MTVectorTest(v1,v2,0)).start();
        (new MTVectorTest(v2,v1,1)).start();
        (new MTVectorTest(v1,v2,2)).start();
        (new MTVectorTest(v2,v1,3)).start();
        (new MTVectorTest(v1,v2,4)).start();
    }
}
