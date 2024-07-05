/* 
 * BSD 2-Clause License
 * 
 * Copyright (c) 2022, LK Test Solutions GmbH
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */
package org.opentdk.api.datastorage;

import org.opentdk.api.io.XMLEditor;
import org.opentdk.api.filter.Filter;
import org.opentdk.api.filter.FilterRule;
import org.apache.commons.lang3.StringUtils;
import org.opentdk.api.logger.MLogger;
import org.w3c.dom.Element;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * SubClass of {@link DataContainer} which provides all methods for reading and writing from or to
 * ASCII files in XML format, and store the data at runtime within the DataContainer. If the DataContainer
 * is linked to a file, then all write operations will immediately save the changes within the file.
 * In case that file changes are not wanted, then the DataContainer should be initialized with the content of the
 * XML file as InputStream.
 * <pre>
 * e.g.:
 * InputStream stream = new ByteArrayInputStream(xmlContent.getBytes(StandardCharsets.UTF_8));
 * DataContainer notationRulesDC = new DataContainer(stream);
 * </pre>
 * 
 * @author LK Test Solutions
 * @see org.opentdk.api.datastorage.DataContainer
 */
public class XMLDataContainer implements SpecificContainer {
	/**
	 * An instance of the class that handles XML files.
	 */
	private XMLEditor xEdit;
	
	private String rootNode; 
		
	public static XMLDataContainer newInstance() {		
		return new XMLDataContainer();
	}	

	private XMLDataContainer() {
		initXmlEditor(null);
	}
	
	public void initXmlEditor(String root) {
		xEdit = new XMLEditor(root);
		rootNode = xEdit.getRootNodeName();
	}

	@Override
	public String asString() {
		String ret = "";
		if (xEdit != null) {
			ret = xEdit.asString();
		}
		return ret;
	}

	/**
	 * See {@link #getContent(String, boolean)}
	 */
	public String getContent(String expr) {
		return getContent(expr, false);
	}

	/**
	 * @param expr XPath to the node that should be returned as string
	 * @param subContentOnly ignore the node that the expr refers to and only get the child content
	 * @return the whole content (with children) of the given XML node as string.
	 */
	public String getContent(String expr, boolean subContentOnly) {
		String ret = "";
		if (xEdit != null) {
			Element node = xEdit.getElement(expr);
			if(subContentOnly) {
				// Children only requires a loop to get their content
				StringBuilder sb = new StringBuilder();
				List<Element> children = xEdit.getChildren(node);
				for(Element child : children) {
					sb.append(xEdit.asString(child, true));
				}
				ret = sb.toString();
			} else {
				// Just get the node and its children as string
				ret = xEdit.asString(node, true);
			}
		}
		return ret;
	}
	
	@Override
	public void readData(File sourceFile) throws IOException {
		if(sourceFile.exists() && sourceFile.isFile() && Files.size(sourceFile.toPath()) > 0) {
			xEdit = new XMLEditor(sourceFile); 	
			rootNode = xEdit.getRootNodeName();
		}
	}

	@Override
	public void readData(InputStream stream) throws IOException {
		xEdit = new XMLEditor(stream);
		rootNode = xEdit.getRootNodeName();
	}

	@Override
	public void writeData(File outputFile) {
		xEdit.save(outputFile);
	}
	
	public String getRootNode() {
		return rootNode;
	}

	public void add(String name, String value) {
		add(name, value, new Filter());
	}

	public void add(String name, String value, Filter filter) {
		add(name, "", value, filter);
	}

	public void add(String name, String attr, String value, Filter filter) {
		add(name, attr, "", value, filter);
	}

	public void add(String name, String attr, String oldValue, String value, Filter filter) {
		for (FilterRule fltrRule : filter.getFilterRules()) {
			if (fltrRule.getHeaderName().equalsIgnoreCase("XPath")) {
				Element oldElement = xEdit.getElement(name, attr, oldValue);
				if (oldElement == null) {
					if(StringUtils.isBlank(attr)) {
						xEdit.addElement(fltrRule.getValue(), name, value); // Add tag
					} else {
						xEdit.addElement(fltrRule.getValue(), name, attr, value); // Add tag with attribute
					}
				} else {
					// Add in case of existing tag results to an attribute replacement
					if(StringUtils.isNotBlank(attr)) {
						Element newElement = oldElement;
						newElement.setAttribute(attr, value);
						xEdit.replaceElement(oldElement, newElement);
					}
				}
				break;
			}
		}
	}

	public void delete(String name, String value) {
		delete(name, value, new Filter());
	}

	public void delete(String name, String value, Filter filter) {
		delete(name, value, "", filter);
	}

	public void delete(String headerName, String attributeName, String attributeValue, Filter filter) {
		for (FilterRule fltrRule : filter.getFilterRules()) {
			if (fltrRule.getHeaderName().equalsIgnoreCase("XPath")) {
				xEdit.delElement(fltrRule.getValue(), headerName, attributeName, attributeValue);
				break;
			}
		}
	}

	/**
	 * Retrieve a string array with the text content of all tags specified by the tag name.
	 * This method will search on all levels of the XML tree hierarchy.
	 *
	 * @param tagName Name of the tag(s) to search for
	 * @return String-Array with the text-content of all found tags
	 */
	public String[] get(String tagName) {
		return get(tagName, new Filter());
	}

	/**
	 * Retrieve a string array with the text content of all tags specified by the tag name.
	 * This method will search only within the XPath, defined by the Filter.<br>
	 * Sample usage:
	 * <pre>
	 *     Filter fltr = new Filter();
	 *     fltr.addFilterRule("XPath", "/Contacts/Business/Management", EOperator.EQUALS);
	 *     dataContainer.treeInstance.get("firstname", fltr);
	 * </pre>
	 *
	 * @param tagName Name of the tag(s) to search for
	 * @param filter Filter condition for more precise localization of the element within the data structure
	 * @return String array with the text-content of all found tags
	 */
	public String[] get(String tagName, Filter filter) {
		return (String[]) get(tagName, filter, "values");
	}

	public Element get(String tagName, String attributName, String attributValue){
		return xEdit.getElement(tagName, attributName, attributValue);
	}

	public Element getRootElement(){
		return xEdit.getRoot();
	}

	/**
	 * This method returns an array with all tag elements or values where headerName matches the tag
	 * name and the tag element matches with all defined filter rules. The method name getColumn is used
	 * also for tree formats like XML to allow standardized access to all supported DataContainer
	 * formats (table, tree etc.).
	 * 
	 * @param headerName The name of the tag that will be searched within an XML document.
	 * @param filter       Filter rules that will be applied as additional search criteria for the
	 *                   returning tags.
	 * @param returnType values = String Array with all values of the matching tags; elements = Element
	 *                   Array with all matching tag objects
	 * @return Array of the type, specified by returnType argument
	 */
	public Object[] get(String headerName, Filter filter, String returnType) {

		List<Element> filteredElements = new ArrayList<>();
		List<String> filteredValues = new ArrayList<>();

		/*
		 * Filter all tags and values that match an implicit XPath filter rule.
		 */
		if (filter.getFilterRules().size() > 0) {
			for (FilterRule rule : filter.getFilterRules()) {
				if (rule.getHeaderName().equalsIgnoreCase("XPath")) {
					for (Element tagElement : xEdit.getElementsListByXPath(rule.getValue())) {
						for (Element childE : xEdit.getChildren(tagElement)) {
							if (childE.getTagName().equals(headerName)) {
								filteredElements.add(childE);
								filteredValues.add(childE.getTextContent());
							}
						}
					}
				}
			}
		} else {
			/*
			 * If no implicit XPath filter rule exists, then all tags and values are added to the
			 * filteredElements and filteredValues list.
			 */
			for (Element e : xEdit.getElementsList(headerName)) {
				filteredElements.add(e);
				filteredValues.add(e.getTextContent());
			}
		}
		/*
		 * Finally remove all tags that do not apply to any other filter rule defined by the filter argument.
		 */
		List<Element> retElements = new ArrayList<>(filteredElements);
		List<String> retValues = new ArrayList<>(filteredValues);
		for (FilterRule fr : filter.getFilterRules()) {
			if (!fr.getHeaderName().equalsIgnoreCase("XPath")) {
				for (Element fltrE : filteredElements) {
					if (fr.isValidValue(fltrE.getTextContent(), fr.getValue()) == false) {
						retValues.remove(fltrE.getTextContent());
						retElements.remove(fltrE);
					}

				}
			}
		}

		filteredElements = null;
		filteredValues = null;

		if (returnType.equalsIgnoreCase("elements")) {
			filteredValues = null;
			return retElements.toArray(new Element[retElements.size()]);
		} else {
			filteredElements = null;
			return retValues.toArray(new String[retValues.size()]);
		}
	}

	/**
	 * Searches for all tags defined by parameter <code>expr</code> and retrieves the values of their
	 * attributes defined by <code>attrName</code>.<br>
	 * If expr includes the tag with full xPath, then the scope of the search will be the parent xPath
	 * of the tag (e.g. /Rules/rule/regExpr only searches in /Rules/rule).<br>
	 * If expr only includes a tag name, the scope of the search will be the whole document. In this
	 * case all tags that match to <code>expr</code> will be searched.
	 * 
	 * @param expr     Name or full XPath of XML tags
	 * @param attrName Name of the XML tags attribute from which the values will be returned
	 */
	public String[] get(String expr, String attrName) {
		List<String> lst = new ArrayList<String>();
		// leading "/" indicates a tag name with full xPath
		if (expr.startsWith("/")) {
			// split all elements of full xPath
			String[] splittedTags = expr.split("/");
			// last element of the split xPath is the tag name
			String targetTag = splittedTags[splittedTags.length - 1];
			// delete tag name from the string to get the parent xPath
			String parentExpr = expr.substring(0, expr.length() - targetTag.length() - 1);
			// get DOM element of the tags parent
			Element e = xEdit.getElement(parentExpr);
			// search all child elements that match to the target tag name
			for (Element eCh : xEdit.getChildren(e)) {
				// get all attributes that matching to attrName and add the attributes value to
				// the result list
				if (eCh.getTagName().equals(targetTag)) {
					lst.add(eCh.getAttribute(attrName));
				}
			}
		} else {
			// if no xPath included in expr, search for all tags that match to expr
			for (Element e : xEdit.getElementsList(expr)) {
				lst.add(e.getAttribute(attrName));
			}
		}
		return lst.toArray(new String[lst.size()]);
	}
	
	public void set(String name, String value) {
		set(name, value, new Filter(), false);
	}

	public void set(String tagName, String tagValue, Filter filter, boolean allOccurrences) {
		// Creates the missing nodes if not present
		for (FilterRule fltrRule : filter.getFilterRules()) {
			if (fltrRule.getHeaderName().equalsIgnoreCase("XPath")) {
				xEdit.checkXPath(fltrRule.getValue() + "/" + tagName, true); 
				break;
			}
		}

		Element[] elements = (Element[]) get(tagName, filter, "elements");								
		for (int i = 0; i < elements.length; i++) {
			xEdit.setElementValue(elements[i], tagValue);
			if(!allOccurrences) {
				break;
			}
		}
	}
	
	public void set(String tagName, String attributeName, String oldAttributeValue, String attributeValue, Filter filter) {
		for (FilterRule fltrRule : filter.getFilterRules()) {
			if (fltrRule.getHeaderName().equalsIgnoreCase("XPath")) {
				Element oldEl = xEdit.getElement(tagName, attributeName, oldAttributeValue);
				if (oldEl == null) {
					Element newEl = xEdit.addElement(fltrRule.getValue(), tagName, attributeName, attributeValue);
					newEl.getAttribute(attributeName);
				} else {
					Element newEl = oldEl;
					newEl.setAttribute(attributeName, attributeValue);
					xEdit.replaceElement(oldEl, newEl);
					newEl.getAttribute(attributeName);
				}
				break;
			}
		}
	}
}