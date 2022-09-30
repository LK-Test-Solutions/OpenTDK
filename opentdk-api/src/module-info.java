module opentdk.api {
	
	exports org.opentdk.api.application;		
	exports org.opentdk.api.datastorage;
	exports org.opentdk.api.dispatcher;	
	exports org.opentdk.api.filter;	
	exports org.opentdk.api.io;
	exports org.opentdk.api.logger;
	exports org.opentdk.api.mapping;
	exports org.opentdk.api.meter;
	exports org.opentdk.api.util;							

	requires java.scripting;
	requires transitive java.sql;
	requires java.desktop; 
	requires java.xml;
	requires java.logging;
	
	requires org.apache.commons.io;
	requires commons.math3;
	requires org.json;
	requires org.apache.commons.lang3;
	requires snakeyaml;
}