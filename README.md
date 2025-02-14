# OpenTDK

The `Open Tool Development Kit` provides packages and classes for easy implementation of Java tools or applications. Developers don't need to take care about implementing code for the handling of data souces. Once a data source is required within an application it can be connected to the application by one of the concepts, provided by OpenTDK. These data sources can be configuration files, result sets of DB requests, Webservice responses (JSON, XML, YAML) and several more.
There are two Java projects includes in this repository, which are:

| Project name | Project purpose |
|--------------|-----------------|
| opentdk_api | Implements the functional concepts of OpenTDK to create non-GUI applications |
| opentdk_gui | Implements concepts and base classes to create JavaFX GUI applications and charts |

The opentdk_api project includes the following packages:

## Base Application

Base class for applications providing essential initialization and utility functionalities such as logging, application settings management, and runtime configuration parsing. It implements support for:
- Singleton application instance management.
- Initializing and configuring a file-based logger.
- Loading, merging, and saving application-specific settings via a JSON file.
- Parsing command-line arguments into key-value pairs for dynamic runtime configuration.
Classes extending this base should utilize its functionality to ensure structured application setup and maintenance.

## DataContainer

The DataContainer class provides a standardized data container interface and supports multiple data formats including CSV, XML, JSON, and YAML. It acts as a data structure primarily for interacting with data from various sources like files, streams, or external inputs. The class offers factory methods for creation and utility methods for data processing and manipulation.

...
