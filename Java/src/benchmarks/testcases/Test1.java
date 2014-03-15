package benchmarks.testcases;

/**
 * Created by IntelliJ IDEA.
 * User: ksen
 * Date: Jun 6, 2007
 * Time: 8:58:54 PM
 * To change this template use File | Settings | File Templates.
 */

class Thread1a extends Thread {
    public Thread1a(String name) {
        super(name);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public void run() {
            synchronized(Test1.lock1){
                System.out.println(Thread.currentThread().getName()+":lock1");
            }
        }
    }

class Thread1b extends Thread {
    public Thread1b(String name) {
        super(name);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public void run() {
        synchronized(Test1.lock2){
            System.out.println(Thread.currentThread().getName()+":lock2");
        }
    }
}

public class Test1 {
    public static Object lock1 = new Object();
    public static Object lock2 = new Object();


    public static void main(String[] args) {
        (new Thread1a("1")).start();
        (new Thread1a("2")).start();
        (new Thread1b("3")).start();
        (new Thread1b("4")).start();
    }
}
