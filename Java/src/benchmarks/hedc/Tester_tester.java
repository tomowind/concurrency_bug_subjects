package benchmarks.hedc;

public class Tester_tester {
	private static int testcnt = 100;
	
	public static void main(String[] args) {
		long time = System.currentTimeMillis();
		if (System.getProperty("falcon.testcnt")!=null)
			testcnt = Integer.parseInt(System.getProperty("falcon.testcnt"));
		int succ=0, fail=0;
		
//		while (true) {
		for (int i=0; i<testcnt; i++) {
			try {
				Tester.main(args);
				if (!Tester.falcon_exception)
					succ++;
				else {
					System.out.println("falcon exception is thrown");
					fail++;
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
				fail++;
				break;
			}
		}
		System.out.println("succ: "+succ+"("+(succ*100/(fail+succ))+"%), fail: "+fail+"("+(fail*100/(fail+succ))+"%)");
		// falcon.util.SummaryPrinter.printTime(time, succ+fail);
	}
}
