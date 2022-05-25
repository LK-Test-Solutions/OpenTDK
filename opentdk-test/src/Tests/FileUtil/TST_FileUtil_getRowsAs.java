package Tests.FileUtil;

import java.io.File;
import org.opentdk.api.io.FileUtil;
import org.opentdk.api.meter.EMeter;


public class TST_FileUtil_getRowsAs {

	private static final File testdataFile = new File("testdata/RegressionTestData/ParserTestdataHuge.txt");

	public static void main(String[] args) {
		if (FileUtil.isHidden(testdataFile.toPath())) {
			System.err.println("Test data file is hidden '" + testdataFile.getPath() + "' ==> terminate test case " + TST_FileUtil_getRowsAs.class.getSimpleName());
			return;
		}
		EMeter.TRANSACTION.start("getRowsAsString");
		FileUtil.getRowsAsString(testdataFile);
//		String content = FileUtil.getRowsAsString(testdataFile);
		System.out.println("getRowsAsString duration (BufferedReader): " +EMeter.TRANSACTION.end("getRowsAsString"));
//		System.out.println(content);
		
		EMeter.TRANSACTION.start("getRowsAsList");
		FileUtil.getRowsAsList(testdataFile);
//		List<String> listContent = FileUtil.getRowsAsList(testdataFile);
		System.out.println("getRowsAsList duration (BufferedReader): " + EMeter.TRANSACTION.end("getRowsAsList"));
//		for(String row : listContent) {
//			System.out.println(row);
//		}
		
		EMeter.TRANSACTION.start("getContent");
		FileUtil.getContent(testdataFile.getPath());
//		String content = FileUtil.getContent(testdataFile.getPath());
		System.out.println("getContent duration (BufferedInputStream): " + EMeter.TRANSACTION.end("getContent"));
//		System.out.println(content);
	}

}
