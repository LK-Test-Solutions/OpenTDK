# OpenTDK

The **Open Tool Development Kit** provides packages and classes for easy implementation of java tools or applications. Developers don't need to take care about implementing code for the handling of data souces. Once a data source is required within an application it can be connected to the application by one of the following concepts:

## Base Application
The package **org.opentdk.api.application** provides base classes for the implementation of new non-GUI java applications. In the current stage there is only a dispatcher class **EBaseSettings** that shows how to setup application settings that will be connected to a XML structure.

## DataContainer
The package **org.opentdk.api.datastorage** implements a concept to access data from different source using unified methods for several tabular and tree formats. The methods for accessing data are defined within the class **org.opentdk.api.datastorage.DataContainer** which adapts container classes for specific formats. Currently available are the following container classes:<br>
* CSVDataContainer: Adapts the functionality to access data in tabular format that can be read from and written to column separated files
* PropertiesDataContainer: Adapts funktionality to access data of properties files with key/value pairs per row, separated by = symbol
* RSDataContainer: Adapts functionality to acces data from tabular result sets recieved by SQL requests
* XMLDataContainer: Adapts funktionality to acces data in XML format<br>
Due to the adaption technique, the package can be extended for nearly any tabular or tree format by implementing format specific DataContainers. There are nearly no code changes required in case that the data source of a DataContainer object will change.<br> 

### Sample usage:
A java application is using a DataContainer to load settings from a **.properties** file at runtime. The **.properties** file includes key/value pairs like **Language=german**. Now it is planned to change from the current **.properties** format to a simple **.xml** format where the same information is stored in tags like **<Language>german</Language>**. In case the key **Language** is unique in both files, then no change is required except assign the new file name **AppSettings.xml** to the String variable **settingsFile**. The following is a complete code sample for loading the content of a file into a DataContainer and getting a value of a defined field (column or node):<br>
`String settingsFile = "AppSettings.properties";`<br>
`DataContainer dc = new DataContainer(settingsFile);`<br>
`dc.getValue("Language");`<br>

## Dispatcher
The package **org.opentdk.api.dispatcher** is designed to implement classes with constants that have attributes and methods, similar to the standard Java Enum type. The constants will be declared as objects of type **org.opentdk.api.dispatcher.BaseDispatcherComponent** and each object represents a field within a **org.opentdk.api.datastorage.DataContainer**. This allows to pre-define the data model of a **org.opentdk.api.datastorage.DataContainer** within a dispatcher class and to perform all getter, setter and other methods on the fields within the **org.opentdk.api.datastorage.DataContainer**.<br>
### Sample usage:
A dispatcher class is defined to setup the logger settings for an application and the values assigned to the constants are read within the application by calling their getValue method. <br>
- Dispatcher Class<br>
`public class EBaseSettings extends BaseDispatcher {`<br>
&emsp;`public static final BaseDispatchComponent APP_LOGFILE = new BaseDispatchComponent(EBaseSettings.class, "Logfile", "/AppSettings", "./logs/Application.log");`<br>
&emsp;`public static final BaseDispatchComponent APP_LOGFILE_LIMIT = new BaseDispatchComponent(EBaseSettings.class, "LogFileLimit", "/AppSettings", "4000");`<br>
&emsp;`public static final BaseDispatchComponent APP_LOGFILE_KEEP_AGE = new BaseDispatchComponent(EBaseSettings.class, "LogKeepAge", "/AppSettings", "90");`<br>
&emsp;`public static final BaseDispatchComponent APP_TRACE_LEVEL = new BaseDispatchComponent(EBaseSettings.class, "TraceLevel", "/AppSettings", "1");`<br>
`}`<br><br>
- Usage within application<br>
`/* Assign XML file to the dispatcher */`<br>
`EBaseSettings.setDataContainer(EBaseSettings.class, "config/myAppSettings.xml");`<br>
`/* Get value from a constant that corresponds with a XML tag within the file */`<br>
`EBaseSettings.APP_LOGFILE.getValue(); // returns "./logs/Application.log" (the defautl value defined in declaration of the constant APP_LOGFILE)`<br>
`EBaseSettings.APP_TRACE_LEVEL.getValue(); // returns 3 (the value defined in myAppSettings.xml)`<br><br>
- Content of myAppSettings.xml<br>
`<?xml version="1.0" encoding="UTF-8" standalone="no"?>`<br>
`<AppSettings>`<br>
&emsp;`<TraceLevel>3</TraceLevel>`<br>
`</AppSettings>`<br>

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




