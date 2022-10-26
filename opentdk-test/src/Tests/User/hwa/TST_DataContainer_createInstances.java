package Tests.User.hwa;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import org.opentdk.api.datastorage.DataContainer;
import org.opentdk.api.io.FileUtil;

public class TST_DataContainer_createInstances {

	public static void main(String[] args) {
//		InputStream inStream;
//		try {
//			inStream = new FileInputStream("./testdata/XML/Personen.xml");
//			BufferedReader in = new BufferedReader(new InputStreamReader(inStream, StandardCharsets.UTF_8));
//			DataContainer dc = new DataContainer("./testdata/XML/Personen.xml");
////			DataContainer dc = new DataContainer("./testdata/XML/Personen_UFT-8.xml");
//			System.out.println(String.join(";", dc.getRow(0)));
//
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
//		DataContainer dc = new DataContainer("./testdata/XML/Personen_UFT-8.xml");
//		System.out.println(String.join(";", dc.getRow(0)));
		
		String inString = FileUtil.getContent("./testdata/XML/Personen.xml");
		InputStream inStream = new ByteArrayInputStream(inString.getBytes(StandardCharsets.UTF_8));
		DataContainer dc = new DataContainer(inString);
		System.out.println(String.join(";", dc.getRow(0)));
		
		
//		InputStream inStream;
//		try {
//			inStream = new FileInputStream(new File("./testdata/XML/Personen_UFT-8.xml"));
//			DataContainer dc = new DataContainer(inStream);
//			System.out.println("test");
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

}
