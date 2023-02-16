package Tests.JSONContainer;

import org.opentdk.api.datastorage.EContainerFormat;
import org.opentdk.api.dispatcher.BaseDispatcher;
import org.opentdk.api.io.FileUtil;

import RegressionTest.BaseRegression;
import RegressionTest.JSONContainer.EJsonValues;
import RegressionTest.YAMLContainer.EYamlValues;

public class TST_transform extends BaseRegression {

	public static void main(String[] args) {
		new TST_transform();
	}
	
	@Override
	protected void runTest() {
		BaseDispatcher.setDataContainer(EJsonValues.class, "testdata/RegressionTestData/JsonExample.json");
		String yaml = BaseDispatcher.getDataContainer(EJsonValues.class).asString(EContainerFormat.YAML);
		String compareYaml = FileUtil.getContent("testdata/RegressionTestData/YamlExample.yaml");
		System.out.println(yaml);
		System.out.println();
		System.out.println("-----------------------------------------");
		System.out.println();
		System.out.println(compareYaml);
		
		System.out.println();
		System.out.println("-----------------------------------------");
		System.out.println();
		
		BaseDispatcher.setDataContainer(EYamlValues.class, "testdata/RegressionTestData/YamlExample.yaml");
		String json = BaseDispatcher.getDataContainer(EYamlValues.class).asString(EContainerFormat.JSON);
		String compareJson = FileUtil.getContent("testdata/RegressionTestData/JsonExample.json");
		System.out.println(json);
		System.out.println();
		System.out.println("-----------------------------------------");
		System.out.println();
		System.out.println(compareJson);
	}

}
