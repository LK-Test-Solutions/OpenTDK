/*
 * BSD 2-Clause License
 *
 * Copyright (c) 2025, LK Test Solutions GmbH
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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import lombok.Setter;
import org.opentdk.api.logger.LogFactory;
import org.opentdk.api.logger.LogFormatter;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.logging.*;

public abstract class BaseApplication {
    /**
     * The one and only instance of this class to get access from other classes.
     */
    @Getter
    private static BaseApplication instance;
    /**
     * Logger for the whole application.
     */
    @Getter
    private Logger logger;
    /**
     * True: Log file gets written, false: No logging at all.
     */
    @Getter @Setter
    private boolean logEnabled = true;
    /**
     * Storage object for all specific application settings.
     */
    @Getter
    private AppSettings settings;
    /**
     * File for the {@link #settings}.
     */
    @Getter @Setter
    private Path configFile = Paths.get("conf" + File.separator + getClass().getSimpleName() + ".json");
    /**
     * Storage object for all main application settings.
     */
    @Getter
    private Properties properties;

    /**
     * TODO
     * @param args
     */
    public BaseApplication(String[] args) {
        if (instance == null) {
            instance = this;
        }
        properties = new Properties();
        for (String arg : args) {
            if(arg.contains("=")) {
                String argKey = arg.split("=")[0];
                String value = arg.split("=")[1];
                if (argKey.startsWith("-")) {
                    argKey = argKey.replace("-", "");
//                    value = arg.substring(argKey.length() + 1);
                }
                properties.put(argKey, value);
            }
        }
    }

    public void initLogger() {
        String logPath = "logs" + File.separator + getClass().getSimpleName() + ".log";
        if(properties.containsKey("logFile")) {
            logPath = properties.getProperty("logFile");
        }
        Path logFile = Paths.get(logPath);
        logger = LogFactory.buildLogger(logFile, properties.getProperty("traceLevel"), true);
    }

    public void initSettings() throws IOException {
        if(!configFile.toFile().exists()) {
            Files.createFile(configFile);
            Files.writeString(configFile, "{}");
        }
        try (FileReader reader = new FileReader(configFile.toFile())) {
            settings = new GsonBuilder().setPrettyPrinting().create().fromJson(reader, AppSettings.class);
        }
    }

    public void saveSettings() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonContent = gson.toJson(settings);
        try {
            Files.writeString(configFile, jsonContent);
        } catch (IOException e) {
            logger.severe(e.getMessage());
        }
    }
}
