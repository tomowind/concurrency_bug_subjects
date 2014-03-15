package benchmarks.dstest;

import benchmarks.instrumented.java.util.Collections;
import benchmarks.instrumented.java.util.Set;
import benchmarks.instrumented.java.util.TreeSet;

/**
 * Created by IntelliJ IDEA.
 * User: ksen
 * Date: Jun 7, 2007
 * Time: 11:54:07 AM
 * To change this template use File | Settings | File Templates.
 */
public class MTTreeSetTest {
    public static void main(String[] args) {
        Set s1 = Collections.synchronizedSet(new TreeSet());
        Set s2 = Collections.synchronizedSet(new TreeSet());
        (new MTSetTest(s2,s1,5)).start();
        (new MTSetTest(s1,s2,6)).start();
        (new MTSetTest(s1,s2,0)).start();
        (new MTSetTest(s2,s1,1)).start();
        (new MTSetTest(s1,s2,2)).start();
        (new MTSetTest(s2,s1,3)).start();
        (new MTSetTest(s1,s2,4)).start();
    }

}
