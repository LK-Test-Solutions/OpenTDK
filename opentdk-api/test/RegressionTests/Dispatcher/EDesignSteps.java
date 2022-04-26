package RegressionTests.Dispatcher;

import org.opentdk.api.application.EBaseSettings;
import org.opentdk.api.dispatcher.BaseDispatchComponent;
 
public class EDesignSteps extends EBaseSettings {
    public static final BaseDispatchComponent STEP_NAME = new BaseDispatchComponent(EDesignSteps.class,"Value", "/Entities/Entity[@Type='design-step']/Fields/Field[@Name='name']","");
    public static final BaseDispatchComponent STEP_DESCRIPTION = new BaseDispatchComponent(EDesignSteps.class,"Value", "/Entities/Entity[@Type='design-step']/Fields/Field[@Name='description']","");
    public static final BaseDispatchComponent STEP_EXPECTED = new BaseDispatchComponent(EDesignSteps.class,"Value", "/Entities/Entity[@Type='design-step']/Fields/Field[@Name='expected']","");
 
}
