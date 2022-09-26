package RegressionTest;

import RegressionTest.CSVContainer.RT_CSVContainer_addColumn;
import RegressionTest.CSVContainer.RT_CSVContainer_addRow;
import RegressionTest.CSVContainer.RT_CSVContainer_getColumns;
import RegressionTest.CSVContainer.RT_CSVContainer_getRows;
import RegressionTest.CSVContainer.RT_CSVContainer_getValues;
import RegressionTest.CSVContainer.RT_CSVContainer_mergeRows;
import RegressionTest.CSVContainer.RT_CSVContainer_putMetaData;
import RegressionTest.CSVContainer.RT_CSVContainer_setRow;
import RegressionTest.CSVContainer.RT_CSVContainer_setValues;
import RegressionTest.CommonUtility.RT_CommonUtil_get;
import RegressionTest.Container.RT_Container_checkHeader;
import RegressionTest.Container.RT_Container_construct;
import RegressionTest.Container.RT_Container_delete;
import RegressionTest.Container.RT_Container_exportContainer;
import RegressionTest.Container.RT_Container_getHeader;
import RegressionTest.Container.RT_Container_unsupportedMethods;
import RegressionTest.CryptoUtility.RT_CryptoUtil_encrypt;
import RegressionTest.DateUtility.RT_DateUtil_compare;
import RegressionTest.DateUtility.RT_DateUtil_diff;
import RegressionTest.DateUtility.RT_DateUtil_get;
import RegressionTest.DateUtility.RT_DateUtil_getFirstOf;
import RegressionTest.DateUtility.RT_DateUtil_getLastOf;
import RegressionTest.DateUtility.RT_DateUtil_getMillisecondsLength;
import RegressionTest.DateUtility.RT_DateUtil_getNumber;
import RegressionTest.Dispatcher.RT_File_Properties_values;
import RegressionTest.Dispatcher.RT_File_XML_values;
import RegressionTest.Dispatcher.RT_Settings_attributes;
import RegressionTest.Dispatcher.RT_Settings_exportContainer;
import RegressionTest.Dispatcher.RT_noFile_Default_values;
import RegressionTest.Dispatcher.RT_noFile_XML_values;
import RegressionTest.IO.RT_FileUtil;
import RegressionTest.IO.RT_XFileWiter;
import RegressionTest.JSONContainer.RT_JSONContainer_buildJson;
import RegressionTest.JSONContainer.RT_JSONContainer_dispatchJson;
import RegressionTest.JSONContainer.RT_JSONContainer_inputStream;
import RegressionTest.JSONContainer.RT_JSONContainer_readViaDc;
import RegressionTest.ListUtility.RT_ListUtility_asString;
import RegressionTest.Logging.RT_Logging_log;
import RegressionTest.Meter.RT_Counter;
import RegressionTest.Meter.RT_Transaction;
import RegressionTest.XMLContainer.RT_XMLContainer_getValues;
import RegressionTest.YAMLContainer.RT_YAMLContainer_dispatchYAML;

/**
 * Executes all regression test classes by calling their main method.
 * 
 * @author LK Test Solutions
 *
 */
public class RegressionTestRunner {
	// To see effects that occur when static fields get accessed more often during runtime
	private static final int loops = 1;

	public static void main(String[] args) {
		for (int i = 0; i < loops; i++) {
			// RegressionTest.CommonUtility
			RT_CommonUtil_get.main(args);

			// RegressionTest.Container
			RT_Container_checkHeader.main(args);
			RT_Container_construct.main(args);
			RT_Container_delete.main(args);
			RT_Container_exportContainer.main(args);
			RT_Container_getHeader.main(args);
			RT_Container_unsupportedMethods.main(args);

			// RegressionTest.CryptoUtility
			RT_CryptoUtil_encrypt.main(args);

			// RegressionTest.CSVContainer
			RT_CSVContainer_addColumn.main(args);
			RT_CSVContainer_addRow.main(args);
			RT_CSVContainer_getColumns.main(args);
			RT_CSVContainer_getRows.main(args);
			RT_CSVContainer_getValues.main(args);
			RT_CSVContainer_mergeRows.main(args);
			RT_CSVContainer_putMetaData.main(args);
			RT_CSVContainer_setRow.main(args);
			RT_CSVContainer_setValues.main(args);

			// RegressionTest.DateUtility
			RT_DateUtil_compare.main(args);
			RT_DateUtil_diff.main(args);
			RT_DateUtil_get.main(args);
			RT_DateUtil_getFirstOf.main(args);
			RT_DateUtil_getLastOf.main(args);
			RT_DateUtil_getMillisecondsLength.main(args);
			RT_DateUtil_getNumber.main(args);

			// RegressionTest.Dispatcher
			RT_File_Properties_values.main(args);
			RT_File_XML_values.main(args);
			RT_noFile_Default_values.main(args);
			RT_noFile_XML_values.main(args);
			RT_Settings_attributes.main(args);
			RT_Settings_exportContainer.main(args);

			// RegressionTest.IO
			RT_FileUtil.main(args);
			RT_XFileWiter.main(args);

			// RegressionTest.JSONContainer
			RT_JSONContainer_buildJson.main(args);
			RT_JSONContainer_dispatchJson.main(args);
			RT_JSONContainer_inputStream.main(args);
			RT_JSONContainer_readViaDc.main(args);

			// RegressionTest.ListUtility
			RT_ListUtility_asString.main(args);

			// RegressionTest.Logging
			RT_Logging_log.main(args);

			// RegressionTest.Meter
			RT_Counter.main(args);
			RT_Transaction.main(args);

			// RegressionTest.XMLContainer
			RT_XMLContainer_getValues.main(args);
			
			// RegressionTest.YAMLContainer
			RT_YAMLContainer_dispatchYAML.main(args);
		}

	}
}
