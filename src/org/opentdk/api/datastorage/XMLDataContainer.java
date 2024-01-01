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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.opentdk.api.filter.Filter;
import org.opentdk.api.filter.FilterRule;
import org.opentdk.api.io.FileUtil;
import org.opentdk.api.io.XMLEditor;
import org.w3c.dom.Element;

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
public class XMLDataContainer implements TreeContainer {
	/**
	 * An instance of the class that handles XML files.
	 */
	private XMLEditor xEdit;
	/**
	 * An instance of the DataContainer that should be filled with the data from the connected source
	 * file. -> Task of the specific data containers.
	 */
	private final DataContainer dc;

	/**
	 * This constructor is used to pass the DataContainer when the specific XML Container will be adapted
	 * from the base DataContainer class.
	 *
	 * @param dataContainer the <code>DataContainer</code> instance to use for read and write methods of
	 *              		this specific data container
	 */
	public XMLDataContainer(DataContainer dataContainer) {
		dc = dataContainer;
		dc.getImplicitHeaders().add("XPath");
	}

	/**
	 *
	 * @param name  Name of the element that will be added into the data source
	 * @param value Value that will be assigned to the name
	 */
	@Override
	public void add(String name, String value) {
		add(name, value, new Filter());
	}

	@Override
	public void add(String name, String value, Filter filter) {
		add(name, "", value, filter);
	}

	@Override
	public void add(String name, String attr, String value, Filter filter) {
		add(name, attr, "", value, filter);
	}

	@Override
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

	@Override
	public String asString() {
		String ret = "";
		if (xEdit != null) {
			ret = xEdit.asString();
		}
		return ret;
	}

	@Override
	public void createFile() throws IOException {
		StringBuilder sb = new StringBuilder();
		sb.append("<").append(dc.getRootNode()).append("/>");
		FileUtil.writeOutputFile(sb.toString(), dc.getInputFile().getPath());
		xEdit = new XMLEditor(dc.getInputFile());
		xEdit.save();
	}

	@Override
	public void delete(String name, String value) {
		delete(name, value, new Filter());
	}

	@Override
	public void delete(String name, String value, Filter filter) {
		delete(name, value, "", filter);
	}

	@Override
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
	@Override
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
	@Override
	public String[] get(String tagName, Filter filter) {
		return (String[]) get(tagName, filter, "values");
	}

	@Override
	public Object get(String tagName, String attributName, String attributValue){
		return xEdit.getElement(tagName, attributName, attributValue);
	}

	/**
	 * This method returns an array with all tag elements or values where headerName matches the tag
	 * name and the tag element matches with all defined filter rules. The method name getColumn is used
	 * also for tree formats like XML to allow standardized access to all supported DataContainer
	 * formats (table, tree etc.).
	 * 
	 * @param headerName The name of the tag that will be searched within an XML document.
	 * @param fltr       Filter rules that will be applied as additional search criteria for the
	 *                   returning tags.
	 * @param returnType values = String Array with all values of the matching tags; elements = Element
	 *                   Array with all matching tag objects
	 * @return Array of the type, specified by returnType argument
	 */
	@Override
	public Object[] get(String headerName, Filter fltr, String returnType) {
		List<FilterRule> implFilterRules = dc.getImplFilterRules(fltr);
		List<Element> filteredElements = new ArrayList<>();
		List<String> filteredValues = new ArrayList<>();

		/*
		 * Filter all tags and values that match an implicit XPath filter rule.
		 */
		if (implFilterRules.size() > 0) {
			for (FilterRule frImpl : implFilterRules) {
				if (frImpl.getHeaderName().equalsIgnoreCase("XPath")) {
					for (Element tagElement : xEdit.getElementsListByXPath(frImpl.getValue())) {
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
		 * Finally remove all tags that do not apply to any other filter rule defined by the fltr argument.
		 */
		List<Element> retElements = new ArrayList<>(filteredElements);
		List<String> retValues = new ArrayList<>(filteredValues);
		for (FilterRule fr : fltr.getFilterRules()) {
			if (dc.getImplFilterRules(fltr).contains(fr) == false) {
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
	@Override
	public String[] get(String expr, String attrName) throws NullPointerException{
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

	@Override
	public Object getRootElement(){
		return xEdit.getRoot();
	}

	/**
	 * This method is used to translate the XPath references to actual values from the XML File. Using
	 * the specified index, the according row (element Occurrence) will be read out and fed to a
	 * translator method. The output of this method is the return value.
	 *
	 * @param index The index of the returning row.
	 * @return an array with all translated references (values) of the row.
	 */
//	public String[] getDecoded(int index) {
//		String[] xPathArr = values.listIterator(index).next();
//		String[] ret = new String[xPathArr.length];
//		index++;
//		for (int i = 0; i < xPathArr.length; i++) {
//			ret[i] = xEdit.readXPath(xPathArr[i]);
//		}
//		return ret;
//	}

	/**
	 * This method is used to load data from an XML-File to the data container. As the structure of a
	 * XML is fundamentally different from the data container instance, only references of the values
	 * are stored in the container as "X-Paths". Every XML-Tag has its own column in the container. The
	 * references alone are useless, they need to be read out by <code>getValue</code>.
	 *
	 * @param filter Has no effect here.
	 * @throws IOException
	 */
	@Override
	public void readData(Filter filter) throws IOException {
		if (dc.getInputFile().exists()) {
			xEdit = new XMLEditor(dc.getInputFile());
			dc.setRootNode(xEdit.getRootNodeName());
		} else if (dc.getInputStream() != null) {
			xEdit = new XMLEditor(dc.getInputStream());
			dc.setRootNode(xEdit.getRootNodeName());
		} else {
			xEdit = new XMLEditor(dc.getRootNode());
		}
//		values = new ArrayList<String[]>() {
//			private static final long serialVersionUID = 1L;	
//			@Override
//			public String[] get(int index) {
//				return getDecoded(index);
//			}
//		};
//		dc.setHeaders(xEdit.getXmlTags());
//		for (String header : dc.getHeaders().keySet()) {
//			dc.setColumn(header, xEdit.getXPaths(header));
//		}
	}
	
	@Override
	public void set(String name, String value) {
		set(name, value, new Filter());
	}

	@Override
	public void set(String name, String value, Filter filter) {
		set(name, value, filter, false);
	}

	@Override
	public void set(String tagName, String tagValue, Filter filter, boolean allOccurences) {
		// Initialize occurrences array with 0 item, in case it is empty
//		if (occurences.length < 1) {
//			occurences = new int[] { 0 };
//		}
		for (FilterRule fltrRule : filter.getFilterRules()) {
			if (fltrRule.getHeaderName().equalsIgnoreCase("XPath")) {
//				if (newField) {
//					xEdit.addElement(fltrRule.getValue(), paramName, value);
//				} 
//				else {
					xEdit.checkXPath(fltrRule.getValue() + "/" + tagName, true); // Creates the hierarchy if not present
					Element[] elements = (Element[]) get(tagName, filter, "elements");								
					for (int i = 0; i < elements.length; i++) {
//						List<Integer> occList = Arrays.stream(occurences).boxed().collect(Collectors.toList());
//						if ((occList.contains(i)) || (occurences[0] == -1)) {
							xEdit.setElementValue(elements[i], tagValue);
//						}
					}
//				}
				break;
			}
		}
	}
	
	@Override
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

	@Override
	public void writeData(String srcFile) {
		xEdit.save(srcFile);
	}
}