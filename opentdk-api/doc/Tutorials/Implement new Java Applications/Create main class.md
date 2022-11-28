# Implementation steps
1. Create a new class that extends the class org.opentdk.api.application.BaseApplication
2. Implement a main method which creates a new instance of the application class
3. Implement a constructor with an args argument
4. Call the methods `parseArgs`and `initRuntimeSettings` to force an initial setup of the settings, used at runtime of the application

# Code sample
```
    import org.opentdk.api.application.BaseApplication;
    public class Application extends BaseApplication {

	public static void main(String[] args) throws Exception {
		new Application(args);
	}
	
	public Application(String[] args) throws Exception {
		parseArgs(ERuntimeProperties.class, args);
		initRuntimeProperties(ERuntimeProperties.class, EAppSettings.class);
	}
    }
```

