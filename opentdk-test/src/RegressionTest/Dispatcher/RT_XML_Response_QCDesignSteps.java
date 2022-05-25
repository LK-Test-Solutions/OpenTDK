package RegressionTest.Dispatcher;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import RegressionTest.BaseRegression;

public class RT_XML_Response_QCDesignSteps extends BaseRegression {

	public static void main(String[] args) {
		new RT_XML_Response_QCDesignSteps();
	}
	
	@Override
	public void runTest() {
		EDesignSteps.setDataContainer(EDesignSteps.class, "testdata/RegressionTestData/design-steps.xml");
        String[] stepNames = EDesignSteps.STEP_NAME.getValues();
        String[] stepDescriptions = EDesignSteps.STEP_DESCRIPTION.getValues();
        String[] stepExpected = EDesignSteps.STEP_EXPECTED.getValues();

        for(int i=0; i<stepNames.length; i++) {
        	System.out.println("Step Name " + i + ": " + stepNames[i]);

        	InputStream stream = new ByteArrayInputStream(stepDescriptions[i].getBytes(StandardCharsets.UTF_8));
        	EQCStepField.setDataContainer(EQCStepField.class, stream);
        	System.out.println("Step Description " + i + ": " + EQCStepField.SPAN.getValue());
        	
        	InputStream stream1 = new ByteArrayInputStream(stepExpected[i].getBytes(StandardCharsets.UTF_8));
            EQCStepField.setDataContainer(EQCStepField.class, stream1);
        	System.out.println("Step Expected " + i + ": " + EQCStepField.SPAN.getValue());
        }
        
	}

}
