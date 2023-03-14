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

	requires transitive java.scripting;
	requires transitive java.sql;
	requires transitive java.desktop; 
	requires transitive java.xml;
	requires transitive java.logging;
	
	requires transitive org.apache.commons.io;
	requires transitive commons.math3;
	requires transitive org.json;
	requires transitive org.apache.commons.lang3;
	requires transitive org.apache.commons.codec;
//	requires transitive org.apache.tika.core;
	requires transitive org.yaml.snakeyaml;
}