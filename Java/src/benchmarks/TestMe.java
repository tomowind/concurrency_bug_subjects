package benchmarks;

/**
 * Created by IntelliJ IDEA.
 * User: ksen
 * Date: May 30, 2007
 * Time: 11:01:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestMe {

    public synchronized  void foo(){
        try {
            Thread.sleep(100);
            wait(100,100);
            Thread.yield();
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public static void main(String[] args) {
        System.out.println("Foo");
    }
}
