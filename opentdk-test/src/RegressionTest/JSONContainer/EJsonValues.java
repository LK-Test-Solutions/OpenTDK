package RegressionTest.JSONContainer;

import org.opentdk.api.dispatcher.BaseDispatchComponent;
import org.opentdk.api.dispatcher.BaseDispatcher;


public class EJsonValues extends BaseDispatcher {
	
	public static final BaseDispatchComponent ID = new BaseDispatchComponent(EJsonValues.class, "id", "", "");
	public static final BaseDispatchComponent SIR = new BaseDispatchComponent(EJsonValues.class, "Sir", "properties/titles", "");
	public static final BaseDispatchComponent ADDRESS = new BaseDispatchComponent(EJsonValues.class, "address");
	public static final BaseDispatchComponent PHONE_NUMBERS = new BaseDispatchComponent(EJsonValues.class, "phoneNumbers", "", "");
	public static final BaseDispatchComponent PEOPLE = new BaseDispatchComponent(EJsonValues.class, "people");
	public static final BaseDispatchComponent PEOPLE_AGE = new BaseDispatchComponent(EJsonValues.class, "age", "people/2", "");
	public static final BaseDispatchComponent EMPLOYEE_AGE = new BaseDispatchComponent(EJsonValues.class, "age", "people/1", "");
	public static final BaseDispatchComponent BOSS_SALARY = new BaseDispatchComponent(EJsonValues.class, "salary", "people/0/special", "");
	public static final BaseDispatchComponent ROLE = new BaseDispatchComponent(EJsonValues.class, "role");
	public static final BaseDispatchComponent CITIES = new BaseDispatchComponent(EJsonValues.class, "cities", "", "");

	public static final BaseDispatchComponent NEW = new BaseDispatchComponent(EJsonValues.class, "new", "");
	public static final BaseDispatchComponent INVALID = new BaseDispatchComponent(EJsonValues.class, "invalid", "", "");



}
