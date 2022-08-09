package Template.Application;

public class SampleCall_SettingsFile {

	public static void main(String[] args) {
		new SampleCall_SettingsFile();
	}
	
	public SampleCall_SettingsFile() {
		String[] args = new String[] {"-settingsfile=./conf/TemplateApplicationSettings.xml"};
		try {
			Application app = new Application(args);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
