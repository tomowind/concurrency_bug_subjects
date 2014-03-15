package benchmarks.dstest;

import benchmarks.instrumented.java.util.ArrayList;
import benchmarks.instrumented.java.util.Collections;
import benchmarks.instrumented.java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ksen
 * Date: Jun 7, 2007
 * Time: 11:34:35 AM
 * To change this template use File | Settings | File Templates.
 */
public class MTArrayListTest {
    public static void main(String args[]){
        List al1 = Collections.synchronizedList(new ArrayList());
        List al2 = Collections.synchronizedList(new ArrayList());
        (new MTListTest(al2,al1,5)).start();
        (new MTListTest(al1,al2,6)).start();
        (new MTListTest(al1,al2,0)).start();
        (new MTListTest(al2,al1,1)).start();
        (new MTListTest(al1,al2,2)).start();
        (new MTListTest(al2,al1,3)).start();
        (new MTListTest(al1,al2,4)).start();
    }
}
