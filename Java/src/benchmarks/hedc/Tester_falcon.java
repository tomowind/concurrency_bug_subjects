package benchmarks.hedc;

public class Tester_falcon {
	private static int testcnt = 100;
	
	public static void main(String[] args) {
		if (System.getProperty("falcon.testcnt")!=null)
			testcnt = Integer.parseInt(System.getProperty("falcon.testcnt"));
		
    	// falcon.profile.Call.startProgram();
		int succ=0, fail=0;
		long time = System.currentTimeMillis();

//		while (true) {
		for (int i=0; i<testcnt; i++) {
			// falcon.profile.Call.startTestcase();
			try {
				Tester.main(args);
				if (!Tester.falcon_exception) {
				//	falcon.profile.Call.endTestcase(true);
					succ++;
				}
				else {
				//	falcon.profile.Call.endTestcase(false);
					fail++;
//					break;
				}	
			} catch (Exception e) {
				e.printStackTrace();
				fail++;
				//falcon.profile.Call.endTestcase(false);
//				break;
			}
		}
		System.out.println("succ: "+succ+"("+(succ*100/(fail+succ))+"%), fail: "+fail+"("+(fail*100/(fail+succ))+"%)");
		// falcon.util.SummaryPrinter.printTime(time, succ+fail);
		// falcon.profile.Call.endProgram();
	}
}
