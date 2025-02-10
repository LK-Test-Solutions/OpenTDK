package org.opentdk.api.application;

import org.testng.annotations.Test;

import java.io.IOException;
import java.util.logging.Level;

public class BaseApplicationTest {
    @Test
    public void testApp() {
        String[] args = { "-logFile=logs/app.log", "-settings=conf/settings.json", "-traceLevel=INFO", "logEnabled=true", "-logArchiveSize=5" };
        InheritedApp app = new InheritedApp(args);
        app.initLogger();
        try {
            app.initSettings();
        } catch (IOException e) {
            app.getLogger().log(Level.SEVERE, e.getMessage());
        }
        assert app.getProperties() != null;
        assert app.getLogger() != null;
        assert app.getSettingsFile() != null;
        assert app.isLogEnabled();
        assert app.getSettings().getLogKeepAge() == 10;
        assert app.getSettings().getLogArchiveSize() == 5;
        app.getLogger().log(Level.INFO, "App is started");
        app.saveSettings();
    }
}
