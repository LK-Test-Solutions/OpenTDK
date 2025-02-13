package org.opentdk.api.application;

import lombok.Getter;
import lombok.Setter;

/**
 * Storage object for the settings used by the {@link BaseApplication}. The user can extend it and add more settings.
 */
public class AppSettings {

    @Getter @Setter
    private String traceLevel = "INFO";
    @Getter @Setter
    private int logKeepAge = 10;
    @Getter @Setter
    private long logFileSize = 10 * 104 * 1024;
    @Getter @Setter
    private int logArchiveSize = 10;

}
