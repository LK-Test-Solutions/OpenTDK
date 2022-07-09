package org.opentdk.api.datastorage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

import org.opentdk.api.dispatcher.BaseDispatchComponent;
import org.opentdk.api.logger.MLogger;
import org.opentdk.api.util.ListUtil;

/**
 * This class is used to define one or more conditions for selecting data from a
 * datastorage. A datastorage can be one of the container classes like
 * DataContainer, CSVDataContainer, RSDataContainer or PropertiesDataContanier,
 * or any other object which has implemented getter methods that are using this
 * Filter.<br>
 * <br>
 * The following sample loads the content of <code>contacts.csv</code> into a
 * <code>CSVDataContainer</code>, and retrieves a filtered list with all
 * surnames that belong to the company LK Test Solutions:
 * 
 * <pre>
 * Filter fltr = new Filter();
 * fltr.addFilterRule("Company", "LK Test Solutions", Filter.Mode.STARTS_WITH);
 * dc.getValuesAsList("Surname", fltr);
 * </pre>
 * 
 * The next code sample shows an implementation of a check-method, using the
 * Filter class to select data from a data object.
 * 
 * <pre>
 * public boolean checkValuesFilter(String[] values, Filter fltr) {
 * 	boolean returnCode = false;
 * 	if (fltr.getFilterRules().size() == 0) {
 * 		returnCode = true;
 * 	}
 * 	for (FilterRule fr : fltr.getFilterRules()) {
 * 		returnCode = fr.checkValue(values[getColumnHeaderIndex(fr.getHeaderName())]);
 * 		if (!returnCode) {
 * 			break;
 * 		}
 * 	}
 * 	return returnCode;
 * }
 * </pre>
 *
 * @author LK Test Solutions GmbH
 *
 */
public class Filter {

	private List<FilterRule> rules = new ArrayList<FilterRule>();
	private List<String> plausibleHeaders = new ArrayList<String>();

	/**
	 * Default constructor that is called, when creating a new instance of the
	 * Filter Class without an argument.
	 */
	public Filter() {

	}

	public void addFilterRule(String rule) {
		rules.add(new FilterRule(rule));
	}

	public void addFilterRule(FilterRule rule) {
		rules.add(rule);
	}

	/**
	 * Adds a rule definition into the rules property, that will check a condition
	 * against a single value.<br>
	 * The following sample filters all DataSets where the company name starts with
	 * 'LK Test Solutions':
	 * 
	 * <pre>
	 * <code>
	 * Filter fltr = new Filter();
	 * fltr.addFilterRule("Company", "LK Test Solutions", EOperator.STARTS_WITH);
	 * </code>
	 * </pre>
	 * 
	 * @param headerName Header of the column or row within a tabular format, where
	 *                   to search for the defined value
	 * @param value      The value, which will be checked by the operation.
	 * @param mode       The operator that defines the filter operation for the
	 *                   value e.g. EOperator.EQUALS, EOperator.STARTS_WITH,
	 *                   EOperator.CONTAINS etc
	 */
	public void addFilterRule(String headerName, String value, BaseDispatchComponent mode) {
		addFilterRule(headerName, value, mode, false);
	}
	
	public void addFilterRule(String headerName, String value, BaseDispatchComponent mode, FilterRule.ERuleFormat ruleFormat) {
		addFilterRule(headerName, new String[] { value }, mode, ruleFormat);
	}

	@Deprecated
	public void addFilterRule(String headerName, String value, BaseDispatchComponent mode, boolean quoteRuleString) {
		addFilterRule(headerName, new String[] { value }, mode, quoteRuleString);
	}

	/**
	 * Adds a rule definition into the rules property, that will check a condition
	 * against multiple values. The following sample filters all DataSets where the
	 * city equals to Munich, Vienna or Lisboa:
	 * 
	 * <pre>
	 * <code>
	 * Filter fltr = new Filter();
	 * String[] cities = {"Munich", "Vienna", "Lisboa"};
	 * fltr.addFilterRule("City", cities, EOperator.EQUALS);
	 * </code>
	 * </pre>
	 * 
	 * @param headerName Header of the column or row within a tabular format, where
	 *                   to search for the defined values
	 * @param values     Array with multiple values, which will be checked by the
	 *                   operation.
	 * @param mode       The operator that defines the filter operation for the
	 *                   value e.g. EOperator.EQUALS, EOperator.STARTS_WITH,
	 *                   EOperator.CONTAINS etc
	 */
	public void addFilterRule(String headerName, String[] values, BaseDispatchComponent mode) {
		addFilterRule(headerName, values, mode, false);
	}

	public void addFilterRule(String headerName, String[] values, BaseDispatchComponent mode, FilterRule.ERuleFormat ruleFormat) {
		if (plausibleHeaders.isEmpty() || checkHeader(headerName)) {
			rules.add(new FilterRule(headerName, values, mode, ruleFormat));
		}
	}
	
	@Deprecated
	public void addFilterRule(String headerName, String[] values, BaseDispatchComponent mode, boolean quoteRuleString) {
		if (plausibleHeaders.isEmpty() || checkHeader(headerName)) {
			rules.add(new FilterRule(headerName, values, mode, quoteRuleString));
		}
	}

	public void addFilterRule(String headerName, String value, BaseDispatchComponent mode, BaseDispatchComponent concat) {
		addFilterRule(headerName, value, mode, concat, false);
	}

	public void addFilterRule(String headerName, String value, BaseDispatchComponent mode, BaseDispatchComponent concat, FilterRule.ERuleFormat ruleFormat) {
		addFilterRule(headerName, new String[] { value }, mode, concat, ruleFormat);
	}
	
	@Deprecated
	public void addFilterRule(String headerName, String value, BaseDispatchComponent mode, BaseDispatchComponent concat, boolean quoteRuleString) {
		addFilterRule(headerName, new String[] { value }, mode, concat, quoteRuleString);
	}

	public void addFilterRule(String headerName, String[] values, BaseDispatchComponent mode, BaseDispatchComponent concat) {
		addFilterRule(headerName, values, mode, concat, false);
	}

	public void addFilterRule(String headerName, String[] values, BaseDispatchComponent mode, BaseDispatchComponent concat, FilterRule.ERuleFormat ruleFormat) {
		if (plausibleHeaders.isEmpty() || checkHeader(headerName)) {
			rules.add(new FilterRule(headerName, values, mode, concat, ruleFormat));
		}
	}
	
	@Deprecated
	public void addFilterRule(String headerName, String[] values, BaseDispatchComponent mode, BaseDispatchComponent concat, boolean quoteRuleString) {
		if (plausibleHeaders.isEmpty() || checkHeader(headerName)) {
			rules.add(new FilterRule(headerName, values, mode, concat, quoteRuleString));
		}
	}

	public void addFilterRules(List<FilterRule> r) {
		rules.addAll(r);
	}

	/**
	 * Sets a Set of plausible headers to the Filter. As soon as this method has
	 * been called, only headers equal to the ones in the plausibleHeaders List can
	 * be applied to the filter.
	 * 
	 * @param headers Set of applicable headers (e.g. column names of a table) - NOT
	 *                case-sensitive
	 */
	public void setPlausibleHeaders(Set<String> headers) {
		plausibleHeaders = new ArrayList<String>(headers);
	}

	/**
	 * Adds a Set of plausible headers to the filter. As soon as this method has
	 * been called, only headers equal to the ones in the plausibleHeaders List can
	 * be applied to the filter.
	 * 
	 * @param headers Set of applicable headers (e.g. column names of a table) - NOT
	 *                case-sensitive
	 */
	public void addPlausibleHeaders(Set<String> headers) {
		plausibleHeaders.addAll(headers);
	}

	/**
	 * Clears all plausible headers from the filter. After the call of this method,
	 * any header can be applied again to the filter.
	 */
	public void clearPlausibleHeaders() {
		plausibleHeaders = new ArrayList<String>();
	}

	/**
	 * Checks if a defined headerName matches to a value defined in the
	 * plausibleHeaders list of current the Filter instance.
	 * 
	 * @param headerName The header name to search for in the plausibleHeaders list
	 * @return true = <code>headerName</code> exists in
	 *         <code>plausibleHeaders</code>; false = <code>headerName</code>
	 *         doesn't exist in <code>plausibleHeaders</code>
	 */
	private boolean checkHeader(String headerName) {
		boolean ret = false;
		headerName = headerName.trim().toLowerCase();
		if (!plausibleHeaders.isEmpty()) {
			for (String h : plausibleHeaders) {
				if (headerName.equals(h.trim().toLowerCase()))
					ret = true;
			}
			if (!ret)
				MLogger.getInstance().log(Level.SEVERE, "Header of new rule doesn't match to the headers of the current DataContainer instance. Rule will not be applied!",
						this.getClass().getSimpleName(), this.getClass().getName(), "checkHeader");
		}
		return ret;
	}

	/**
	 * Returns the List property <code>rules</code> with elements of type
	 * {@link FilterRule}
	 * 
	 * @return The property <code>rules</code> of type List.
	 */
	public List<FilterRule> getFilterRules() {
		return rules;
	}

	public List<String> getFilterRulesHeaders() {
		List<String> headersList = new ArrayList<String>();
		for (FilterRule rule : rules) {
			headersList.add(rule.getHeaderName());
		}
		List<String> outHeaders = new ArrayList<String>();
		outHeaders.addAll(new HashSet<String>(headersList));
		return outHeaders;
	}

	public List<String> getFilterRulesValues(String headerName) {
		List<String> valuesList = new ArrayList<>();
		for (FilterRule rule : rules) {
			if((rule.getFilterOperator().equals(EOperator.EQUALS)) || (rule.getFilterOperator().equals(EOperator.EQUALS_IGNORE_CASE))){
				if (rule.getHeaderName().equalsIgnoreCase(headerName)) {
					valuesList.addAll(ListUtil.asList(Arrays.asList(rule.getValues())));
				}
			}
		}
		return valuesList;
	}

	/**
	 * Removes all entries from the <code>rules</code> List.
	 */
	public void clear() {
		rules.clear();
	}

	/**
	 * Deletes a rule from the filter instance. For example the rule can be created
	 * with new FilterRule(), but has to have the same headerName, value and mode as
	 * the rule that has to be deleted in the filter.
	 * 
	 * @param rule An equal rule to the one that has to be deleted.
	 * @return True, if the rule has been deleted, else false.
	 */
	public boolean deleteRule(FilterRule rule) {
		boolean ret = false;
		for (FilterRule r : rules) {
			if (r.getHeaderName().equals(rule.getHeaderName()))
				ret = rules.remove(r);
		}
		return ret;
	}

	/**
	 * Deletes a rule from the filter instance. The rule has to have the same
	 * headerName, value and mode as the rule that has to be deleted in the filter.
	 * 
	 * @param headerName Header of the column or row within a tabular format.
	 * @param value      The value.
	 * @param mode       The operator, used to the the values e.g. EOperator.EQUALS,
	 *                   EOperator.STARTS_WITH, EOperator.CONTAINS etc
	 * @return True, if the rule has been deleted, else false.
	 */
	public boolean deleteRule(String headerName, String value, BaseDispatchComponent mode) {
		boolean ret = false;
		ret = this.deleteRule(new FilterRule(headerName, value, mode));
		return ret;
	}

	/**
	 * Deletes a rule from the filter instance. The rule has to have the same
	 * headerName, values and mode as the rule that has to be deleted in the filter.
	 * 
	 * @param headerName Header of the column or row within a tabular format.
	 * @param values     The values array.
	 * @param mode       The operator, used to the the values e.g. EOperator.EQUALS,
	 *                   EOperator.STARTS_WITH, EOperator.CONTAINS etc
	 * @return True, if the rule has been deleted, else false.
	 */
	public boolean deleteRule(String headerName, String[] values, BaseDispatchComponent mode) {
		boolean ret = false;
		ret = this.deleteRule(new FilterRule(headerName, values, mode));
		return ret;
	}

	/**
	 * Deletes a rule from the filter instance, specified through an index.
	 * 
	 * @param index Index of the rule in the rules property of the filter instance.
	 * @return True, if the rule has been deleted, else false.
	 */
	public boolean deleteRule(int index) {
		boolean ret = false;
		if (rules.remove(index) != null)
			ret = true;
		return ret;
	}
}
