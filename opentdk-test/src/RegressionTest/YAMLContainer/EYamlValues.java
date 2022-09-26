package RegressionTest.YAMLContainer;

import org.opentdk.api.dispatcher.BaseDispatchComponent;
import org.opentdk.api.dispatcher.BaseDispatcher;


public class EYamlValues extends BaseDispatcher {
	
	public static final BaseDispatchComponent ID = new BaseDispatchComponent(EYamlValues.class, "id", "", "");
	public static final BaseDispatchComponent NAME = new BaseDispatchComponent(EYamlValues.class, "name", "", "");
	public static final BaseDispatchComponent SIR = new BaseDispatchComponent(EYamlValues.class, "Sir", "properties/titles", "");
	public static final BaseDispatchComponent ADDRESS = new BaseDispatchComponent(EYamlValues.class, "address");
	public static final BaseDispatchComponent PHONE_NUMBERS = new BaseDispatchComponent(EYamlValues.class, "phoneNumbers", "", "");
	public static final BaseDispatchComponent PEOPLE = new BaseDispatchComponent(EYamlValues.class, "people");
	public static final BaseDispatchComponent PEOPLE_AGE = new BaseDispatchComponent(EYamlValues.class, "age", "people/2", "");
	public static final BaseDispatchComponent EMPLOYEE_AGE = new BaseDispatchComponent(EYamlValues.class, "age", "people/1", "");
	public static final BaseDispatchComponent BOSS_SALARY = new BaseDispatchComponent(EYamlValues.class, "salary", "people/0/special", "");
	public static final BaseDispatchComponent BOSS_NAME = new BaseDispatchComponent(EYamlValues.class, "name", "people/0", "");
	public static final BaseDispatchComponent ROLE = new BaseDispatchComponent(EYamlValues.class, "role");
	public static final BaseDispatchComponent CITIES = new BaseDispatchComponent(EYamlValues.class, "cities", "", "");

	public static final BaseDispatchComponent NEW = new BaseDispatchComponent(EYamlValues.class, "new", "");
	public static final BaseDispatchComponent INVALID = new BaseDispatchComponent(EYamlValues.class, "invalid", "", "");



}
