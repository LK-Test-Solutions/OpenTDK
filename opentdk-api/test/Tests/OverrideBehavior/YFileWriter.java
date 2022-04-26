package Tests.OverrideBehavior;

import java.io.File;
import java.io.IOException;

import org.opentdk.api.io.XFileWriter;

public class YFileWriter extends XFileWriter {

	public YFileWriter(File f) throws IOException {
		super(f);
	}

	public void writeLine(String s) throws IOException {
		writeLine(new String[] { s }, "");
		System.out.println("Additional operation");
	}
}
