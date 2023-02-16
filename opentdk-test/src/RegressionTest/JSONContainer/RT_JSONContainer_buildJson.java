package RegressionTest.JSONContainer;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.json.JSONException;
import org.opentdk.api.dispatcher.BaseDispatcher;

import RegressionTest.BaseRegression;

public class RT_JSONContainer_buildJson extends BaseRegression {
	
	public static final String expectedResult = "{\"expectedResult\":\"App ist offen\",\"name\":\"Step1\",\"description\":\"Oeffne App\"}";

	public static void main(String[] args) {
		new RT_JSONContainer_buildJson();
	}
	
	@Override
	protected void runTest() {
		// Initialize an empty container and set values to it
        BaseDispatcher.setDataContainer(EWebControlStep.class, new ByteArrayInputStream("{}".getBytes()));
        EWebControlStep.STEPNAME.setValue("Step1");
        EWebControlStep.STEPDESCRIPTION.setValue("Oeffne App");
        EWebControlStep.STEPEXPECTEDRESULT.setValue("App ist offen");
        
        // Initialize another empty container and set values to it, where one of the values is the content of the first container
        BaseDispatcher.setDataContainer(EWebControlRequest.class, new ByteArrayInputStream("{}".getBytes()));
        try {
        	BaseDispatcher.getDataContainer(EWebControlStep.class).readData();
        	System.out.println("Exception NOT correctly catched ==> Stream can only be consumed once");
        } catch(JSONException | IOException e) {
        	System.out.println("Exception correctly catched ==> Stream can only be consumed once");
        }
        EWebControlRequest.BROWSERNAME.setValue("chrome");
        EWebControlRequest.OSNAME.setValue("Windows 10");
        EWebControlRequest.TESTNAME.setValue("First Test");
//        EWebControlRequest.TESTSTEPS.setValue("[]");
        EWebControlRequest.TESTSTEPS.addValue(BaseDispatcher.getDataContainer(EWebControlStep.class).asString());
        EWebControlRequest.TESTSTEPS.addValue("{\"expectedResult\":\"App ist geschlossen\",\"name\":\"Step2\",\"description\":\"Schliesse App\"}");
        
        System.out.println(BaseDispatcher.getDataContainer(EWebControlRequest.class).asString());
       
        BaseRegression.testResult(EWebControlRequest.TESTSTEPS_NAME.getValue(), "Test step at index 1", "Step1");       
        BaseRegression.testResult(EWebControlRequest.TESTSTEPS.getValues()[0], "Test steps", expectedResult);	
	}

}
