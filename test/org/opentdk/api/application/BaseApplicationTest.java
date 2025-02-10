package org.opentdk.api.application;

import org.testng.annotations.Test;

import java.util.logging.Level;

public class BaseApplicationTest {
    @Test
    public void testApp() {
        String[] args = { "-logFile=logs/app.log", "-settings=conf/settings.json", "-traceLevel=INFO" };
        InheritedApp app = new InheritedApp(args);
        app.initLogger();
        assert app.getProperties() != null;
        assert app.getLogger() != null;
        assert app.getConfigFile() != null;
        assert app.isLogEnabled();
        app.getLogger().log(Level.INFO, "App is started");

    }
}
