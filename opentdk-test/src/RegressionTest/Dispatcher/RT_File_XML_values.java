package RegressionTest.Dispatcher;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.opentdk.api.application.EBaseSettings;
import org.opentdk.api.dispatcher.BaseDispatcher;
import org.opentdk.api.io.FileUtil;
import org.opentdk.api.util.ListUtil;

import RegressionTest.BaseRegression;

public class RT_File_XML_values extends BaseRegression {
	
	private String xmlContent = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\r\n"
			+ "<AppSettings>\r\n"
			+ "    <ProjectLocations>\r\n"
			+ "        <ProjectLocation>Project 1</ProjectLocation>\r\n"
			+ "        <ProjectLocation>Project 2</ProjectLocation>\r\n"
			+ "        <ProjectLocation>Project 3</ProjectLocation>\r\n"
			+ "    </ProjectLocations>\r\n"
			+ "</AppSettings>\r\n"
			+ "";

	public static void main(String[] args) {
		new RT_File_XML_values();
	}

	@Override
	public void runTest() {
		final String file = "output/File_XML_values.xml";
		try {
			FileUtil.deleteFileOrFolder(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		BaseDispatcher.setDataContainer(EBaseSettings.class, file, true);
		
		// Gets the default values of dispatcher components that are declared within EBaseSettings class
		testResult(E_XMLFile_Dispatcher_values.LOGFILE.getValue(), "APP_LOGFILE", "./logs/Application.log");
		testResult(E_XMLFile_Dispatcher_values.LOGFILE_LIMIT.getValue(), "APP_LOGFILE_LIMIT", "4000");
		testResult(E_XMLFile_Dispatcher_values.TRACE_LEVEL.getValue(), "APP_TRACE_LEVEL", "1");
		
		// Adds Items to the associated XML Container
		E_XMLFile_Dispatcher_values.PROJECT_LOCATION.addValue("Project 1", true);
		E_XMLFile_Dispatcher_values.PROJECT_LOCATION.addValue("Project 2", true);
		E_XMLFile_Dispatcher_values.PROJECT_LOCATION.addValue("Project 3", true);
		
		testResult(BaseDispatcher.getDataContainer(EBaseSettings.class).asString(), "XML content" , xmlContent);
		
		// Gets the first value
		testResult(E_XMLFile_Dispatcher_values.PROJECT_LOCATION.getValue(), "PROJECT_LOCATION", "Project 1");
		
		// Replaces existing value needs to be implemented!
		E_XMLFile_Dispatcher_values.PROJECT_LOCATION.setValue("", "Project 2", "Project 2.1");
		List<String> lstPL = Arrays.asList(E_XMLFile_Dispatcher_values.PROJECT_LOCATION.getValues());
		if(lstPL.contains("Project 2.1")) {
			System.out.println("Success: PROJECT_LOCATION == Project 2.1");
		} else {
			System.out.println("Failure: PROJECT_LOCATION Project 2.1 not found");
		}
		
		// Sets all values under PROJECT_LOCATIONS to "Project 4"
		E_XMLFile_Dispatcher_values.PROJECT_LOCATION.setValue("Project 4");
		testResult(E_XMLFile_Dispatcher_values.PROJECT_LOCATION.getValue(), "PROJECT_LOCATION", "Project 4");
		
		// Size stays the same
		testResult(String.valueOf(E_XMLFile_Dispatcher_values.PROJECT_LOCATION.getValues().length), "PROJECT_LOCATIONS.length", "3");
		
		// Delete whole setting
		E_XMLFile_Dispatcher_values.PROJECT_LOCATIONS.delete();
		testResult(ListUtil.asString(E_XMLFile_Dispatcher_values.PROJECT_LOCATIONS.getValues(), ";"), "PROJECT_LOCATIONS.delete", "");

		// Add value for tags with attribute
		E_XMLFile_Dispatcher_values.BACKGROUND_IMAGE.addValue("Dark Theme", "Sunset.jpg", false);
		E_XMLFile_Dispatcher_values.BACKGROUND_IMAGE.addValue("Nature Theme", "Trees.jpg", false);
		E_XMLFile_Dispatcher_values.BACKGROUND_IMAGE.addValue("Cars Theme", "EQA.jpg", false);
		
		System.out.println(BaseDispatcher.getDataContainer(EBaseSettings.class).asString());
		
		testResult(E_XMLFile_Dispatcher_values.BACKGROUND_IMAGE.getValue("Dark Theme"), "BACKGROUND_IMAGE", "Sunset.jpg");
		testResult(E_XMLFile_Dispatcher_values.BACKGROUND_IMAGE.getValue("Nature Theme"), "BACKGROUND_IMAGE", "Trees.jpg");
		testResult(E_XMLFile_Dispatcher_values.BACKGROUND_IMAGE.getValue("Cars Theme"), "BACKGROUND_IMAGE", "EQA.jpg");
		
		// Replace single value by
		E_XMLFile_Dispatcher_values.BACKGROUND_IMAGE.setValues("Trees.jpg", "Lake.jpg", false);
		List<String> lstBI = Arrays.asList(E_XMLFile_Dispatcher_values.BACKGROUND_IMAGE.getValues());
		Boolean lakeFound = false;
		Boolean duplicates = false;
		for(int i=0; i<lstBI.size(); i++) {
			if(lstBI.get(i).equals("Lake.jpg")) {
				if(lakeFound == false) {
					lakeFound = true;
				}else {
					duplicates = true;
					break;
				}
			}
		}
		if((lakeFound) && (!duplicates)) {
			System.out.println("Success: BACKGROUND_IMAGE == Lake.jpg");
		} else if(duplicates){
			System.out.println("Failure: BACKGROUND_IMAGE multiple tags found with Lake.jpg");
		} else {
			System.out.println("Failure: BACKGROUND_IMAGE Lake.jpg not found");
		}
		
		// Replace all matching values
		E_XMLFile_Dispatcher_values.BACKGROUND_IMAGE.setValue("", "EQA.jpg","Lake.jpg");
		E_XMLFile_Dispatcher_values.BACKGROUND_IMAGE.setValues("Lake.jpg", "replaceAll.jpg", true);
		testResult(E_XMLFile_Dispatcher_values.BACKGROUND_IMAGE.getValue("Dark Theme"), "BACKGROUND_IMAGE", "Sunset.jpg");
		testResult(E_XMLFile_Dispatcher_values.BACKGROUND_IMAGE.getValue("Nature Theme"), "BACKGROUND_IMAGE", "replaceAll.jpg");
		testResult(E_XMLFile_Dispatcher_values.BACKGROUND_IMAGE.getValue("Cars Theme"), "BACKGROUND_IMAGE", "replaceAll.jpg");
		
		try {
			BaseDispatcher.getDataContainer(EBaseSettings.class).writeData(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		E_XMLFile_Dispatcher_values.THEMES.delete(); // Prepare for RT_noFile_XML_values
	}

}
