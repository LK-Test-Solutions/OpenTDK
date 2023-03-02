package RegressionTest.XMLContainer;

import org.opentdk.api.datastorage.DataContainer;
import org.opentdk.api.filter.Filter;
import org.opentdk.api.mapping.EOperator;

import RegressionTest.BaseRegression;

import java.io.File;

/**
 * Tests if reading from a XML file with the {@link org.opentdk.api.datastorage.DataContainer} works
 * using its get methods.
 * 
 * @author LK Test Solutions
 *
 */
public class RT_XMLContainer_readFile extends BaseRegression {

	public static void main(String[] args) {
		new RT_XMLContainer_readFile();
	}

	@Override
	public void runTest() {

		DataContainer dc = new DataContainer(new File(location + "testdata/RegressionTestData/XMLContainer_Data.xml"));
		// General checks
		BaseRegression.testResult(dc.getRootNode(), "Root Node", "parserRules");
		BaseRegression.testResult(dc.getContainerFormat().name(), "Container Format", "XML");

		// Get with argument name
		BaseRegression.testResult(dc.get("patternenabled")[0], "Get First Tag", "false");

		// Get with argument name and filter
		Filter fltr = new Filter();
		fltr.addFilterRule("XPath", "/parserRules/rule[@name='Orbit']/element[@name='PARAMETER']/attributelist", EOperator.EQUALS);
		BaseRegression.testResult(String.join(",", dc.get("item", fltr)), "Attribute List", "XML_REQUEST,SELECT_STATEMENT,PREPARED_STATEMENT");

		fltr = new Filter();
		fltr.addFilterRule("lbpattern", "", EOperator.NOT_EQUALS);
		BaseRegression.testResult(dc.get("lbpattern", fltr).length, "All None Empty 'lbpattern'", 12);

		// Get with argument name and attribute
		BaseRegression.testResult(dc.treeInstance().get("/parserRules/rule[@name='Orbit']/element", "name")[0], "Get Attribute", "PARAMETER");
	}
}
