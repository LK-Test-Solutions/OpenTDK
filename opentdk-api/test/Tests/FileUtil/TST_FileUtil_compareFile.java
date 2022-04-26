package Tests.FileUtil;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.junit.Test;

public class TST_FileUtil_compareFile {
	private String s1, s2;
	
	@Test
	public void test() {
		try {
			s1 = readFile("");
			s2 = readFile("");
			System.out.println(s1.equals(s2));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private String readFile(String first) throws UnsupportedEncodingException, FileNotFoundException, IOException {
		String s;
		try(InputStreamReader in = new InputStreamReader(new FileInputStream(first), "ISO-8859-1")) {
			int c;
			StringBuilder sa = new StringBuilder();
			while((c = in.read()) != -1) {
				sa.append((char) c);
			}
			s = sa.toString();
		}
		return s;
	}
	
	@SuppressWarnings("unused")
	private void print() {
		System.out.println(s1);
		System.out.println("---------------------------------------");
		System.out.println(s2);
	}

}
