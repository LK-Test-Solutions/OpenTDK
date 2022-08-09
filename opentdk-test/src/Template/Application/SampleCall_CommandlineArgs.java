package Template.Application;

public class SampleCall_CommandlineArgs {

	public static void main(String[] args) {
		new SampleCall_CommandlineArgs();
	}
	
	public SampleCall_CommandlineArgs() {
		String[] args = new String[] {"-homedir=CMD /users/holger", "-baseurl=CMD https://www.lk-test.de"};
		try {
			Application app = new Application(args);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
