package RegressionSets.Api;

import org.junit.Test;

import RegressionTests.CSVContainer.RT_CSVContainer_addColumn;
import RegressionTests.CSVContainer.RT_CSVContainer_addRow;
import RegressionTests.CSVContainer.RT_CSVContainer_getColumns;
import RegressionTests.CSVContainer.RT_CSVContainer_getRows;
import RegressionTests.CSVContainer.RT_CSVContainer_getValues;
import RegressionTests.CSVContainer.RT_CSVContainer_mergeRows;
import RegressionTests.CSVContainer.RT_CSVContainer_putMetaData;
import RegressionTests.CSVContainer.RT_CSVContainer_setRow;
import RegressionTests.CSVContainer.RT_CSVContainer_setValues;
import RegressionTests.Container.RT_Container_checkHeader;
import RegressionTests.Container.RT_Container_construct;
import RegressionTests.Container.RT_Container_getHeader;
import RegressionTests.DateUtility.RT_DateUtil_compare;
import RegressionTests.DateUtility.RT_DateUtil_get;
import RegressionTests.DateUtility.RT_DateUtil_getMillisecondsLength;
import RegressionTests.Dispatcher.RT_Settings_attributes;
import RegressionTests.Dispatcher.RT_File_XML_values;
import RegressionTests.IO.RT_FileUtil;
import RegressionTests.ListUtility.RT_ListUtility_asString;
import RegressionTests.Logging.RT_Logging_log;
//import RegressionTests.Parser.RT_Parser_withoutSettingsFile;
import RegressionTests.XMLContainer.RT_XMLContainer_getValues;

public class RegressionSet {

	@Test
	public void runRegressionTest() {
		// List Utility
		Class<?>[] listClasses = { RT_ListUtility_asString.class };
		BaseRegressionSet.runRegressionTest(listClasses);

		// Logging
		Class<?>[] logClasses = { RT_Logging_log.class };
		BaseRegressionSet.runRegressionTest(logClasses);

		// Parsing
//		Class<?>[] parseClasses = { RT_Parser_withoutSettingsFile.class };
//		Class<?>[] parseClasses = { TST_ParserWithoutSettingsFile.class, TST_ParserWithPreparedSettingsFile.class };
//		BaseRegressionSet.runRegressionTest(parseClasses);

		// Container general
		Class<?>[] dcClasses = { RT_Container_construct.class, RT_Container_checkHeader.class, RT_Container_getHeader.class };
		BaseRegressionSet.runRegressionTest(dcClasses);

		// CSVContainer
		Class<?>[] csvDcClasses = { RT_CSVContainer_addColumn.class, RT_CSVContainer_addRow.class, RT_CSVContainer_getColumns.class, RT_CSVContainer_getRows.class, RT_CSVContainer_getValues.class, RT_CSVContainer_mergeRows.class, RT_CSVContainer_putMetaData.class, RT_CSVContainer_setRow.class, RT_CSVContainer_setValues.class};

		BaseRegressionSet.runRegressionTest(csvDcClasses);

		// XML Container
		Class<?>[] xmlDcClasses = { RT_XMLContainer_getValues.class };
		BaseRegressionSet.runRegressionTest(xmlDcClasses);

		// Settings
		Class<?>[] settingsClasses = { RT_File_XML_values.class, RT_Settings_attributes.class };
		BaseRegressionSet.runRegressionTest(settingsClasses);

		// HTTP ==> This will upload an image to the Confluence WIKI so is is commented out by default
//		Class<?>[] httpClasses = { RT_Http_getAndChange.class };
//		BaseRegressionSet.runRegressionTest(httpClasses);

		// IO
		Class<?>[] ioClasses = { RT_FileUtil.class };
		BaseRegressionSet.runRegressionTest(ioClasses);

		// DATE
		Class<?>[] dateClasses = { RT_DateUtil_compare.class, RT_DateUtil_get.class, RT_DateUtil_getMillisecondsLength.class };
		BaseRegressionSet.runRegressionTest(dateClasses);		
		
	}
}
