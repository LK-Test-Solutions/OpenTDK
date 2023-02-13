/* 
 * BSD 2-Clause License
 * 
 * Copyright (c) 2022, LK Test Solutions GmbH
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */
package org.opentdk.api.application;

import org.opentdk.api.dispatcher.BaseDispatchComponent;

/**
 * Settings class with predefined set of common application settings of type {@link org.opentdk.api.dispatcher.BaseDispatchComponent}.
 * This class can be used as superclass to inherit the declared variables to application specific subclasses. <br><br>
 * 
 * Usage sample:
 * <pre>
 * public class EAppSettings extends EBaseSettings{
 * 	...
 * }
 * </pre>
 * The following pre-declared settings are inherited to the application specific subclass:
 * <pre>
 * EAppSettings.<b>LOGFILE</b>
 * EAppSettings.<b>LOGFILE_LIMIT</b>
 * EAppSettings.<b>LOGFILE_KEEP_AGE</b>
 * EAppSettings.<b>TRACE_LEVEL</b>
 * 
 * Sample:
 * EAppSettings.LOGFILE.setValue("/usr/logs/myApp.log");
 * </pre>
 * 
 * @author LK Test Solutions GmbH
 *
 */
public class EBaseSettings {
	/**
	 * The variable LOGFILE defines the full path and name of the file, where {@link org.opentdk.api.logger.MLogger} will log all messages and errors.
	 */
	public static final BaseDispatchComponent LOGFILE = new BaseDispatchComponent(EBaseSettings.class, "Logfile", "/AppSettings", "./logs/Application.log");	
	/**
	 * The variable LOGFILE_LIMIT defines the maximum size in kilobyte of the logfile. When the limit is exceeded, {@link org.opentdk.api.logger.MLogger} archives the file
	 * with a date stamp and a new file will be created.
	 */
	public static final BaseDispatchComponent LOGFILE_LIMIT = new BaseDispatchComponent(EBaseSettings.class, "LogFileLimit", "/AppSettings", "4000");	
	/**
	 * The variable LOGFILE_KEEP_AGE defines maximum age of logfile in days. {@link org.opentdk.api.logger.MLogger} automatically deletes the file in case the age is exceeded.
	 */
	public static final BaseDispatchComponent LOGFILE_KEEP_AGE = new BaseDispatchComponent(EBaseSettings.class, "LogKeepAge", "/AppSettings", "90");
	/**
	 * The variable TRACE_LEVEL defines the level off logging messages by {@link org.opentdk.api.logger.MLogger}.<br>
	 * <pre>
	 * 0 = disable logging
	 * 1 = log errors only
	 * 2 = log errors and warnings
	 * 3 = log all messages
	 * </pre>
	 */
	public static final BaseDispatchComponent TRACE_LEVEL = new BaseDispatchComponent(EBaseSettings.class, "TraceLevel", "/AppSettings", "1");

}
