# OpenTDK

The **Open Tool Development Kit** provides packages and classes for easy implementation of java tools or applications. Developers don't need to take care about implementing code for the handling of data souces. Once a data source is required within an application it can be connected to the application by one of the following concepts:

## Base Application
The package **org.opentdk.api.application** provides base classes for the implementation of new non-GUI java applications. In the current stage there is only a dispatcher class **EBaseSettings** that shows how to setup application settings that will be connected to a XML structure.

## DataContainer
The package **org.opentdk.api.datastorage** implements a concept to access data from different source formats in a unified way. The methods for accessing the data are defined within the class **org.opentdk.api.datastorage.DataContainer** which adapts container classes for specific formats. Currently available are the following container classes:<br>
* CSVDataContainer: Adapts the functionality to access data in tabular format that can be read from and written to column separated files
* PropertiesDataContainer: Adapts funktionality to access data of properties files with key/value pairs per row, separated by = symbol
* RSDataContainer: Adapts functionality to acces data from tabular result sets recieved by SQL requests
* XMLDataContainer: Adapts funktionality to acces data in XML format

## Dispatcher
The package **org.opentdk.api.dispatcher** is designed to define ENUM like objects that represent a field within a DataContainer. A field can be cells of a column or row (tabular formats) or nodes (tree formats).

## Logger
The class **org.opentdk.api.logger.MLogger** can be used in a static way without any further setup or initialization, to log messages at runtime of an application into a log file. <br>
With its implemented default behaviour the **MLogger** will write all messages of level **SEVERE** into the file **Application.log** within a folder **logs** relative to the current working directory. The logfile will be automatically cleared, once a size of 10 MB has been reached and its content will be moved into an archive file with a date prefix in the file name. When an archived files has reached an age of 30 days, it will be deleted next time when the MLogger is called, in case the log file setting will not change.<br>
### Sample usage:
`/* Change the behaviour */`<br>
`MLogger.getInstance().setLogFile("c:/temp/logs/myJavaApp.log");`<br>
`MLogger.getInstance().setTraceLevel(3); // Log all messages (Error, Warning and Information)`<br>
`MLogger.getInstance().setLogKeepAge(100); // Keep archived log files for 100 days`<br><br>
`/* Log an error message */`<br>
`MLogger.getInstance().log(Level.SEVERE, "The mesh trace string has the wrong format! ", FileUtil.class.getSimpleName(), "createResultsFileName");`<br><br>




