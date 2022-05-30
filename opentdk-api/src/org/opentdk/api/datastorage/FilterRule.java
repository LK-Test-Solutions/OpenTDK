package org.opentdk.api.datastorage;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.logging.Level;

import org.opentdk.api.dispatcher.BaseDispatchComponent;
import org.opentdk.api.logger.MLogger;
import org.opentdk.api.util.DateUtil;
import org.opentdk.api.util.EFormat;

/**
 * This class is used to define rules that will be used for filtering DataSets
 * from tabular data formats like
 * {@link org.opentdk.api.datastorage.DataContainer} or in where and set
 * conditions of SQL queries.<br>
 * <br>
 * 
 * The following example defines the where condition of the SQL statement
 * <code>select * from customer where company = 'LK Test Solutions GmbH' and lastname = 'Meisinger'</code>
 * that can be used for the SQLStatement class to generate the statement:
 * 
 * <pre>
 * SQLStatement stmnt = new SQLStatement(ESQL.COMMAND_SELECT);
 * Filter fltr = new Filter()
 * fltr.addFilterRule("company", "LK Test Solutions GmbH", EOperator.EQUALS, EOperator.AND);
 * fltr.addFilterRule("lastname", "Meisinger", EOperator.EQUALS, EOperator.AND);
 * stmnt.setConditionsFilter(fltr);
 * </pre>
 * 
 * @author LK Test Solutions GmbH
 *
 */
public class FilterRule {

	/**
	 * headerName defines the name of the sequence, where the rules for specified
	 * value(s) will be checked. A sequence can be a column or row.
	 */
	private String headerName;

	/**
	 * Defines the value, used by check operation of the rule.
	 */
	private String value;

	/**
	 * An array with multiple values, used by check operation of the rule.
	 */
	private Object[] values;

	/**
	 * The operator used by check operation for the defined value of the rule.
	 */
//	private EOperator filterOperator;
	private BaseDispatchComponent filterOperator;

	/**
	 * The operator used to concatenate the rule with other rules of this filter
	 * instance.
	 */
//	private EOperator ruleConcatenationOperator;
	private BaseDispatchComponent ruleConcatenationOperator;

	/**
	 * The complete rule including ruleConcatenationOperator, headerName,
	 * filterOperator and value.<br>
	 * e.g. "and company = 'LK Test Solutions GmbH'"
	 */
	private String ruleString;

	/**
	 * Constructor that is called when creating an instance of FilterRule with the
	 * full rule definition as String. This string will be parsed into the single
	 * rule elements like headerName, value, filterOperator and
	 * ruleConcatenationOperator and the properties of these elements will
	 * automatically be set with the identified values.
	 */
	public FilterRule(String ruleStr) {
		setRuleString(ruleStr);
		if (ruleStr.trim().toUpperCase().startsWith("AND")) {
			ruleConcatenationOperator = EOperator.AND;
		} else if (ruleStr.trim().toUpperCase().startsWith("OR")) {
			ruleConcatenationOperator = EOperator.OR;
		} else if (ruleStr.trim().toUpperCase().startsWith("IN")) {
			ruleConcatenationOperator = EOperator.IN;
		} else if (ruleStr.trim().toUpperCase().startsWith("BETWEEN")) {
			ruleConcatenationOperator = EOperator.BETWEEN;
		}
	}

	/**
	 * Constructor that is called when creating an instance of FilterRule with the
	 * arguments for header name and value. In this case EOperator.EQUALS will be
	 * used as default operator for checking the rule against a single value.
	 * 
	 * @param hName String value with the Header name of the DataSet to which the
	 *              rule applies.
	 * @param value String value, used by the check operation of the FilterRule.
	 */
	public FilterRule(String hName, String value) {
		this(hName, value, false);
	}

	public FilterRule(String hName, String value, boolean quoteRuleString) {
		this(hName, new String[] { value }, quoteRuleString);
	}

	/**
	 * Constructor that is called when creating an instance of FilterRule with the
	 * arguments for header name and a values Array. In this case EOperator.EQUALS
	 * will be used as default operator for checking the rule against multiple
	 * values.
	 * 
	 * @param hName  String value with the Header name of the DataSet to which the
	 *               rule applies.
	 * @param values Array of Strings, used by the check operation of the
	 *               FilterRule.
	 */
	public FilterRule(String hName, String[] values) {
		this(hName, values, false);
	}

	public FilterRule(String hName, String[] values, boolean quoteRuleString) {
		this.headerName = hName;
		if (values.length == 1) {
			this.value = values[0];
		}
		this.values = values;
		this.filterOperator = EOperator.EQUALS;
		this.ruleConcatenationOperator = EOperator.AND;
		assignRuleString(quoteRuleString);
	}

	/**
	 * Constructor that is called when creating an instance of FilterRule with the
	 * arguments for header name, value and operator. In this case any operator from
	 * the EOperator ENUM can be chosen for checking the rule against a single
	 * value.
	 * 
	 * @param hName String value with the Header name of the DataSet to which the
	 *              rule applies.
	 * @param value String value, used by the check operation of the FilterRule.
	 * @param m     Value of type EOperator, used for the check operation.
	 * @throws Exception If any error occurs, it should be logged with the MLogger.
	 */
	public FilterRule(String hName, String value, BaseDispatchComponent m) throws Exception {
		this(hName, value, m, false);
	}

	public FilterRule(String hName, String value, BaseDispatchComponent m, boolean quoteRuleString) throws Exception {
		this(hName, new String[] { value }, m, quoteRuleString);
	}

	/**
	 * Constructor that is called when creating an instance of FilterRule with the
	 * arguments for header name, values Array and operator. In this case any
	 * operator from the EOperator ENUM can be chosen for checking the rule against
	 * multiple values.
	 * 
	 * @param hName  String value with the Header name of the DataSet to which the
	 *               rule applies.
	 * @param values Array of Strings, used by the check operation of the
	 *               FilterRule.
	 * @param m      Value of type EOperator, used for the check operation.
	 * @throws Exception If any error occurs, it should be logged with the MLogger.
	 */
	public FilterRule(String hName, String[] values, BaseDispatchComponent m) throws Exception {
		this(hName, values, m, false);
	}

	public FilterRule(String hName, String[] values, BaseDispatchComponent m, boolean quoteRuleString) throws Exception {
		if (isValidOperator(values, m)) {
			this.headerName = hName;
			if (values.length == 1) {
				this.value = values[0];
			}
			this.values = values;
			this.filterOperator = m;
			this.ruleConcatenationOperator = EOperator.AND;
			assignRuleString(quoteRuleString);
		} else {
			throw new Exception("Operator doesn't comply to values!");
		}
	}

	/**
	 * Constructor that is called when creating an instance of FilterRule with the
	 * arguments for header name, value and operator. In this case any operator from
	 * the EOperator ENUM can be chosen for checking the rule against a single
	 * value.
	 * 
	 * @param hName  String value with the Header name of the DataSet to which the
	 *               rule applies.
	 * @param value  String value, used by the check operation of the FilterRule.
	 * @param m      Value of type EOperator, used for the check operation.
	 * @param concat Value of type EOperator, used for concatenating multiple filter
	 *               conditions (e.g. EOperator.CONCATENATE_AND,
	 *               EOperator.CONCATENATE_OR)
	 * @throws Exception If any error occurs, it should be logged with the MLogger.
	 */
	public FilterRule(String hName, String value, BaseDispatchComponent m, BaseDispatchComponent concat) throws Exception {
		this(hName, value, m, concat, false);
	}

	public FilterRule(String hName, String value, BaseDispatchComponent m, BaseDispatchComponent concat, boolean quoteRuleString) throws Exception {
		this(hName, new String[] { value }, m, concat, quoteRuleString);
	}

	/**
	 * Constructor that is called when creating an instance of FilterRule with the
	 * arguments for header name, values Array and operator. In this case any
	 * operator from the EOperator ENUM can be chosen for checking the rule against
	 * multiple values.
	 * 
	 * @param hName  String value with the Header name of the DataSet to which the
	 *               rule applies.
	 * @param values Array of Strings, used by the check operation of the
	 *               FilterRule.
	 * @param m      Value of type EOperator, used for the check operation.
	 * @param concat The operator that gets used to add the rule string to the one
	 *               before.
	 * @throws Exception If any error occurs, it should be logged with the MLogger.
	 */
	public FilterRule(String hName, String[] values, BaseDispatchComponent m, BaseDispatchComponent concat) throws Exception {
		this(hName, values, m, concat, false);
	}

	public FilterRule(String hName, String[] values, BaseDispatchComponent m, BaseDispatchComponent concat, boolean quoteRuleString) throws Exception {
		if (isValidOperator(values, m)) {
			this.headerName = hName;
			if (values.length == 1) {
				this.value = values[0];
			}
			this.values = values;
			this.filterOperator = m;
			this.ruleConcatenationOperator = concat;
			assignRuleString(quoteRuleString);
		} else {
			throw new Exception("Operator doesn't comply to values!");
		}
	}

	/**
	 * Get the value of the <code>headerName</code> property,
	 * 
	 * @return Value of type string
	 */
	public String getHeaderName() {
		return headerName;
	}

	/**
	 * Get the value, used by check operation of the rule.
	 * 
	 * @return Value of type string
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @return the values Array, used by check operation of the rule.
	 */
	public Object[] getValues() {
		return values;
	}

	/**
	 * Get the filterOperator attribute which is used for validation of the filtered
	 * values.
	 * 
	 * @return filterOperator of type EOperator.
	 */
	public BaseDispatchComponent getFilterOperator() {
		return filterOperator;
	}

	/**
	 * Get the ruleConcatenationOperator attribute which is used to concatenate
	 * multiple filter rules. Valid operators can be <code>EOperator.AND</code> and
	 * <code>EOperator.OR</code>.
	 * 
	 * @return The Operator of type EOperator.
	 */
	public BaseDispatchComponent getRuleConcatenationOperator() {
		return ruleConcatenationOperator;
	}

	/**
	 * Get the complete rule including ruleConcatenationOperator, headerName,
	 * filterOperator and value as String.<br>
	 * e.g. <code>"and company = 'LK Test Solutions GmbH'"</code>
	 * 
	 * @return complete rule as String
	 */
	public String getRuleString() {
		return ruleString;
	}

	/**
	 * Set the ruleString property with the complete rule including
	 * ruleConcatenationOperator, headerName, filterOperator and value.<br>
	 * e.g. <code>"and company = 'LK Test Solutions GmbH'"</code>
	 * 
	 * @param ruleStr Complete filter rule with all rule elements as String
	 */
	public void setRuleString(String ruleStr) {
		ruleString = ruleStr;
	}

	public void assignRuleString() {
		assignRuleString(true);
	}

	public void assignRuleString(boolean quoteRuleString) {
		StringBuffer sb = new StringBuffer();
		sb.append(ruleConcatenationOperator.getValue());
		sb.append(" ");
		sb.append(headerName);
		sb.append(" ");
		sb.append(filterOperator.getValue());
		sb.append(" ");

		String quote = "";
		if (quoteRuleString) {
			quote = "'";
		}

		if (filterOperator.equals(EOperator.IN)) {
			sb.append("(");
		}

		sb.append(quote);
		
		if (values.length > 1) {
			for (int i = 0; i < values.length; i++) {
				Object val = values[i];
				if (i > 0 && i < values.length) {
					sb.append(",");
				}
				if (val instanceof String || val instanceof Timestamp || val instanceof Time) {
					sb.append("'" + val + "'");
				} else {
					sb.append(val);
				}
			}
		} else {
			sb.append(values[0]);
		}
		
		sb.append(quote);

		if (filterOperator.equals(EOperator.IN)) {
			sb.append(")");
		}
		ruleString = sb.toString();
	}

	/**
	 * Checks, if the defined rule matches to a given value.
	 * 
	 * @param val Value to check
	 * @return boolean value; true = rule matches to the defined value; false = rule
	 *         doesn't match to the defined value.
	 */
	public boolean checkValue(String val) {
		boolean returnCode = false;
		if (val != null) {
			for (Object filterValue : values) {
				returnCode = isValidValue(val, String.valueOf(filterValue));
				if (returnCode) {
					break;
				}
			}
		}
		return returnCode;
	}

	/**
	 * Calls the comparison operation for the rule and returns true (rule matches)
	 * or false (rule doesn't match).
	 * 
	 * @param val         Value of DataSet, where the rule will apply to
	 * @param filterValue Value to compare with: defined in the filter rule
	 * @return boolean value; true = rule matches to the defined value; false = rule
	 *         doesn't match to the defined value.
	 */
	public boolean isValidValue(String val, String filterValue) {
		if(filterOperator.equals(EOperator.CONTAINS)) {
			return val.trim().contains(filterValue);
		} else if(filterOperator.equals(EOperator.CONTAINS_DATE)) {
			if (DateUtil.compare(DateUtil.parse(val, EFormat.getDateEFormat(filterValue).getFormatPattern()), filterValue) == 0) {
				return true;
			}
		} else if(filterOperator.equals(EOperator.CONTAINS_DATE_AFTER)) {
			if (DateUtil.compare(DateUtil.parse(val, EFormat.getDateEFormat(filterValue).getFormatPattern()), filterValue) > 0) {
				return true;
			}
		} else if(filterOperator.equals(EOperator.CONTAINS_DATE_BEFORE)) {
			if (DateUtil.compare(DateUtil.parse(val, EFormat.getDateEFormat(filterValue).getFormatPattern()), filterValue) < 0) {
				return true;
			}
		} else if(filterOperator.equals(EOperator.CONTAINS_IGNORE_CASE)) {
			return val.trim().toUpperCase().contains(filterValue.toUpperCase());
		} else if(filterOperator.equals(EOperator.DATE_AFTER)) {
			if (DateUtil.compare(val, filterValue) > 0) {
				return true;
			}
		} else if(filterOperator.equals(EOperator.DATE_BEFORE)) {
			if (DateUtil.compare(val, filterValue) < 0) {
				return true;
			}
		} else if(filterOperator.equals(EOperator.DATE_EQUALS)) {
			if (DateUtil.compare(val, filterValue) == 0) {
				return true;
			}
		} else if(filterOperator.equals(EOperator.ENDS_WITH)) {
			return val.trim().endsWith(filterValue);
		} else if(filterOperator.equals(EOperator.ENDS_WITH_IGNORE_CASE)) {
			return val.trim().toUpperCase().endsWith(filterValue.toUpperCase());
		} else if(filterOperator.equals(EOperator.EQUALS)) {
			return val.trim().equals(filterValue);
		} else if(filterOperator.equals(EOperator.EQUALS_IGNORE_CASE)) {
			return val.trim().equalsIgnoreCase(filterValue);
		} else if(filterOperator.equals(EOperator.GREATER_THAN)) {
			if (Integer.valueOf(val) > Integer.valueOf(filterValue)) {
				return true;
			}
		} else if(filterOperator.equals(EOperator.LESS_THAN)) {
			if (Integer.valueOf(val) < Integer.valueOf(filterValue)) {
				return true;
			}
		} else if(filterOperator.equals(EOperator.NOT_EQUALS)) {
			if (!val.trim().equals(filterValue)) {
				return true;
			}
		} else if(filterOperator.equals(EOperator.NOT_EQUALS_IGNORE_CASE)) {
			if (!val.trim().equalsIgnoreCase(filterValue)) {
				return true;
			}
		} else if(filterOperator.equals(EOperator.STARTS_WITH)) {
			return val.trim().startsWith(filterValue);
		} else if(filterOperator.equals(EOperator.STARTS_WITH_IGNORE_CASE)) {
			return val.trim().toUpperCase().startsWith(filterValue.toUpperCase());
		} else {
			return false;
		}
		
		return false;
	}
	
	/**
	 * Checks if the operator is applicable to the value.
	 * 
	 * @param value Value which the operator should be applied to.
	 * @param m     Operator for validation
	 * @return boolean value True if operator is applicable to value.
	 */
	private boolean isValidOperator(String value, BaseDispatchComponent m) {
		boolean ret = false;
		if((m.equals(EOperator.GREATER_THAN)) || (m.equals(EOperator.LESS_THAN))) {
			try {
				Double.parseDouble(value);
				ret = true;
			} catch (NumberFormatException e) {
				MLogger.getInstance().log(Level.SEVERE, "Value is not a number. Rule can not be applied to filter!", this.getClass().getSimpleName(), this.getClass().getName(), "isValidOperator");
			}
		} else {
			ret = true;
		}
		return ret;		
	}

	/**
	 * Checks if the operator is applicable to the values.
	 * 
	 * @param values Value array which the operator should be applied to.
	 * @param m      Operator for validation
	 * @return boolean value True if operator is applicable to all values.
	 */
	private boolean isValidOperator(String[] values, BaseDispatchComponent m) {
		boolean ret = true;
		for (String v : values) {
			if (!isValidOperator(v, m)) {
				ret = false;
				break;
			}
		}
		return ret;
	}

	@Override
	public boolean equals(Object o) {
		boolean ret = false;
		if (o != null) {
			if (o.getClass().equals(this.getClass())) {
				FilterRule rule = (FilterRule) o;
				if (this.getValue() != null) {
					if (this.getHeaderName().equals(rule.getHeaderName()) && this.getValue().equals(rule.getValue()) && this.getFilterOperator().equals(rule.getFilterOperator()))
						ret = true;
				} else if (this.getValues() != null) {
					if (this.getHeaderName().equals(rule.getHeaderName()) && this.getValues().equals(rule.getValues()) && this.getFilterOperator().equals(rule.getFilterOperator()))
						ret = true;
				}
			}
		}
		return ret;
	}

}