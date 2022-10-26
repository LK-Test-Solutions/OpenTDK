package org.opentdk.api.filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

import org.opentdk.api.dispatcher.BaseDispatchComponent;
import org.opentdk.api.filter.FilterRule.ERuleFormat;
import org.opentdk.api.logger.MLogger;
import org.opentdk.api.mapping.EOperator;
import org.opentdk.api.util.ListUtil;

/**
 * This class gets used to define one or more conditions to select data from a data source. <br>
 * <br>
 * The following sample loads the content of <code>contacts.csv</code> into a
 * <code>DataContainer</code> and retrieves a filtered list with all surnames that belong to the
 * company LK Test Solutions:
 * 
 * <pre>
 * DataContainer container = new DataContainer("contacts.csv");
 * Filter filter = new Filter();
 * fltr.addFilterRule("Company", "LK Test Solutions", EOperator.STARTS_WITH);
 * List{@literal <String>} result = container.getValuesAsList("Surname", filter);
 * </pre>
 *
 * @author LK Test Solutions
 *
 */
public class Filter {
	/**
	 * Storage object for the filter rules that should be used for comparison.
	 */
	private List<FilterRule> rules;
	/**
	 * This object can be used to only allow specific headers as filter criteria. They are not case
	 * sensitive. So if this list has elements and any <code>addFilterRule</code> method gets called
	 * with an invalid header the rule will not be added to the {@link #rules} list.
	 */
	private List<String> plausibleHeaders;

	/**
	 * Create a new instance without arguments. The filter rules get added later.
	 */
	public Filter() {
		rules = new ArrayList<FilterRule>();
		plausibleHeaders = new ArrayList<String>();
	}

	/**
	 * Add a filter to the List property {@link #rules} by passing the whole condition as string.<br>
	 * e.g.: 
	 * <pre> 
	 * Filter fltr = new Filter();
	 * fltr.addFilterRule("AND Company = LK Test Solutions");
	 * </pre>
	 * 
	 * @param rule The rule definition as String
	 */
	public void addFilterRule(String rule) {
		rules.add(new FilterRule(rule));
	}

	/**
	 * Adds an instance of type {@link org.opentdk.api.filter.FilterRule} to the List property {@link #rules}.
	 * 
	 * @param rule instance of type {@link org.opentdk.api.filter.FilterRule}
	 */
	public void addFilterRule(FilterRule rule) {
		rules.add(rule);
	}

	/**
	 * Directly adds several {@link FilterRule} instances to the {@link #rules} list.
	 * 
	 * @param filterRules A {@link java.util.List} object with items of type {@link FilterRule}
	 */
	public void addFilterRules(List<FilterRule> filterRules) {
		rules.addAll(filterRules);
	}

	/**
	 * Creates a new instance of type {@link FilterRule} and adds the instance to the {@link #rules} list.
	 * 
	 * @param headerName	Name of the field or column header, where the rule 
	 * @param value			
	 * @param mode			An instance of type BaseDispatchComponent within the class {@link org.opentdk.api.mapping.EOperator} e.g. <code>EOperator.CONTAINS</code>
	 */
	public void addFilterRule(String headerName, String value, BaseDispatchComponent mode) {
		addFilterRule(headerName, new String[] { value }, mode);
	}

//	/**
//	 * See {@link #addFilterRule(String, String[], BaseDispatchComponent, FilterRule.ERuleFormat)} with
//	 * {@link ERuleFormat.STRING}.
//	 */
	public void addFilterRule(String headerName, String[] values, BaseDispatchComponent mode) {
		addFilterRule(headerName, values, mode, ERuleFormat.STRING);
	}

//	/**
//	 * See {@link #addFilterRule(String, String[], BaseDispatchComponent, FilterRule.ERuleFormat)} for
//	 * single value comparison.
//	 */
	public void addFilterRule(String headerName, String value, BaseDispatchComponent mode, FilterRule.ERuleFormat ruleFormat) {
		addFilterRule(headerName, new String[] { value }, mode, ruleFormat);
	}

	/**
	 * Adds a rule definition into the rules property, that will check a condition against multiple
	 * values. The following sample filters all data set where the city equals to Munich, Vienna or
	 * Lisboa. If regular expression or quoted strings should be used the ruleFormat parameter can be
	 * set.
	 * 
	 * <pre>
	 * Filter filter = new Filter();
	 * String[] cities = { "Munich", "Vienna", "Lisboa" };
	 * filter.addFilterRule("City", cities, EOperator.EQUALS);
	 * </pre>
	 * 
	 * @param headerName Header or key to search for the defined values
	 * @param values     Array with multiple values that will be checked by the operation.
	 * @param mode       The operator that defines the filter operation for the value e.g.
	 *                   EOperator.EQUALS, EOperator.STARTS_WITH, EOperator.CONTAINS etc
	 * @param ruleFormat See {@link FilterRule.ERuleFormat}
	 */
	public void addFilterRule(String headerName, String[] values, BaseDispatchComponent mode, FilterRule.ERuleFormat ruleFormat) {
		if (plausibleHeaders.isEmpty() || checkHeader(headerName)) {
			rules.add(new FilterRule(headerName, values, mode, ruleFormat));
		}
	}

	public void addFilterRule(String headerName, String value, BaseDispatchComponent mode, BaseDispatchComponent concat) {
		addFilterRule(headerName, new String[] { value }, mode, concat);
	}

	public void addFilterRule(String headerName, String[] values, BaseDispatchComponent mode, BaseDispatchComponent concat) {
		addFilterRule(headerName, values, mode, concat, ERuleFormat.STRING);
	}

	public void addFilterRule(String headerName, String value, BaseDispatchComponent mode, BaseDispatchComponent concat, FilterRule.ERuleFormat ruleFormat) {
		addFilterRule(headerName, new String[] { value }, mode, concat, ruleFormat);
	}

	public void addFilterRule(String headerName, String[] values, BaseDispatchComponent mode, BaseDispatchComponent concat, FilterRule.ERuleFormat ruleFormat) {
		if (plausibleHeaders.isEmpty() || checkHeader(headerName)) {
			rules.add(new FilterRule(headerName, values, mode, concat, ruleFormat));
		}
	}

	/**
	 * Sets a Set of plausible headers to the Filter. As soon as this method has been called, only
	 * headers equal to the ones in the plausibleHeaders List can be applied to the filter.
	 * 
	 * @param headers Set of applicable headers (e.g. column names of a table) - NOT case-sensitive
	 */
	public void setPlausibleHeaders(Set<String> headers) {
		plausibleHeaders = new ArrayList<String>(headers);
	}

	/**
	 * Adds a Set of plausible headers to the filter. As soon as this method has been called, only
	 * headers equal to the ones in the plausibleHeaders List can be applied to the filter.
	 * 
	 * @param headers Set of applicable headers (e.g. column names of a table) - NOT case-sensitive
	 */
	public void addPlausibleHeaders(Set<String> headers) {
		plausibleHeaders.addAll(headers);
	}

	/**
	 * Clears all plausible headers from the filter. After the call of this method, any header can be
	 * applied again to the filter.
	 */
	public void clearPlausibleHeaders() {
		plausibleHeaders = new ArrayList<String>();
	}

	/**
	 * Checks if a defined headerName matches to a value defined in the plausibleHeaders list of current
	 * the Filter instance.
	 * 
	 * @param headerName The header name to search for in the plausibleHeaders list
	 * @return true = <code>headerName</code> exists in <code>plausibleHeaders</code>; false =
	 *         <code>headerName</code> doesn't exist in <code>plausibleHeaders</code>
	 */
	private boolean checkHeader(String headerName) {
		boolean ret = false;
		headerName = headerName.trim().toLowerCase();
		if (!plausibleHeaders.isEmpty()) {
			for (String h : plausibleHeaders) {
				if (headerName.equals(h.trim().toLowerCase()))
					ret = true;
			}
			if (!ret) {
				MLogger.getInstance().log(Level.SEVERE, "Header of new rule doesn't match to the headers of the current DataContainer instance. Rule will not be applied!", this.getClass().getSimpleName(), this.getClass().getName(), "checkHeader");
			}
		}
		return ret;
	}

	/**
	 * Returns the List property <code>rules</code> with elements of type {@link FilterRule}
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
			if ((rule.getFilterOperator().equals(EOperator.EQUALS)) || (rule.getFilterOperator().equals(EOperator.EQUALS_IGNORE_CASE))) {
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
	 * Deletes a rule from the filter instance. For example the rule can be created with new
	 * FilterRule(), but has to have the same headerName, value and mode as the rule that has to be
	 * deleted in the filter.
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
	 * Deletes a rule from the filter instance. The rule has to have the same headerName, value and mode
	 * as the rule that has to be deleted in the filter.
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
	 * Deletes a rule from the filter instance. The rule has to have the same headerName, values and
	 * mode as the rule that has to be deleted in the filter.
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
