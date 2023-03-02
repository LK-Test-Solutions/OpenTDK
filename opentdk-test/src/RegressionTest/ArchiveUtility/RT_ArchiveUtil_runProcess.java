package RegressionTest.ArchiveUtility;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import org.opentdk.api.io.FileUtil;
import org.opentdk.api.util.ArchiveUtil;
import org.opentdk.api.util.ArchiveUtil.ArchiveCommand;

import RegressionTest.BaseRegression;

public class RT_ArchiveUtil_runProcess extends BaseRegression {

	public static void main(String[] args) {
		new RT_ArchiveUtil_runProcess();
	}
	
	@Override
	protected void runTest() {
		// Empty output folder
		try {
			FileUtil.deleteFileOrFolder(location + "output/RegressionTestData_Extracted");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		ArchiveUtil archive = ArchiveUtil.newInstance();
		
		archive.setFileNames("*.json *.yaml *.properties *.xml *.csv");
		archive.setSwitches("-t7z -m0=BCJ2 -m1=LZMA:d25:fb255 -m2=LZMA:d19 -m3=LZMA:d19 -mb0:1 -mb0s1:2 -mb0s2:3 -mx");		
		int success = archive.runProcess(location + "testdata\\RegressionTestData", "C:\\Program Files\\7-Zip\\7z.exe", "..\\RegressionTestData.7z");
		BaseRegression.testResult(success, "Compression state", 0);
		BaseRegression.testResult(String.valueOf(new File(location + "testdata/RegressionTestData.7z").exists()), "Compressed folder exists", "true");
		
		archive.setSwitches("-o\"RegressionTestData_Extracted\" -t7z -y -x!*.cmd -x!config.txt -x!*.html");
		success = archive.runProcess(location + "testdata", "C:\\Program Files\\7-Zip\\7z.exe", "RegressionTestData.7z", ArchiveCommand.Extract, true);
		BaseRegression.testResult(success, "Extraction state", 0);
		BaseRegression.testResult(String.valueOf(new File(location + "testdata/RegressionTestData_Extracted").exists()), "Extracted folder exists", "true");
		
		// Move the results to the output folder afterwards
		Path source = new File(location + "testdata/RegressionTestData.7z").toPath();
		Path target = new File(location + "output/RegressionTestData.7z").toPath();
		try {
			Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		source = new File(location + "testdata/RegressionTestData_Extracted").toPath();
		target = new File(location + "output/RegressionTestData_Extracted").toPath();
		try {
			Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
