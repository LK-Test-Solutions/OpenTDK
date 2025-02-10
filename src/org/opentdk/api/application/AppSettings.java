package org.opentdk.api.application;

import lombok.Getter;
import lombok.Setter;

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
