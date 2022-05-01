# OpenTDK

The **Open Tool Development Kit** provides packages and classes for easy implementation of java tools or applications. Developers don't need to take care about implementing code for the handling of data souces. Once a data source is required within an application it can be connected to the application by one of the following concepts:

## Logging
The class **org.opentdk.api.logger.MLogger** can be used in a static way without any further setup or initialization, to log messages at runtime of an application into a log file. <br>
With its implemented default behaviour the **MLogger** will write all messages of level **SEVERE** into the file **Application.log** within a folder **logs** relative to the current working directory. The logfile will be archived automatically with a date prefix, once a size of 10 MB has been reached and the archive files will be deleted after 30 days.<br>
### Sample usage:
`/* Change the behaviour */`<br>
`MLogger.getInstance().setLogFile("c:/temp/logs/myJavaApp.log");`<br>
`MLogger.getInstance().setTraceLevel(3); // Log all messages (Error, Warning and Information)`<br>
`MLogger.getInstance().setLogKeepAge(100); // Keep archived log files for 100 days`<br><br>
`/* Log an error message */`<br>
`MLogger.getInstance().log(Level.SEVERE, "The mesh trace string has the wrong format! ", FileUtil.class.getSimpleName(), "createResultsFileName");`<br><br>

## DataContainer


