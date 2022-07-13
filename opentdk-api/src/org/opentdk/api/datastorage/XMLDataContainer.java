package org.opentdk.api.datastorage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

import org.opentdk.api.datastorage.BaseContainer.EContainerFormat;
import org.opentdk.api.io.FileUtil;
import org.opentdk.api.io.XMLEditor;
import org.opentdk.api.logger.MLogger;
import org.w3c.dom.Element;

/**
 * SubClass of {@link DataContainer} which provides all methods for reading and writing
 * from or to Ascii files in XML format, and store the data at runtime within the DataContainer.
 * 
 * @author LK Test Solutions
 * @see org.opentdk.api.datastorage.DataContainer
 */
public class XMLDataContainer implements CustomContainer {

	public enum EImplicitHeaders {
		XPath;
	}

	/**
	 * An instance of the class that handles XML files.
	 */
	private XMLEditor xEdit;
	
	/**
	 * An instance of the DataContainer that should be filled with the data from the
	 * connected source file. -> Task of the specific data containers.
	 */
	private final DataContainer dc;

	/**
	 * Construct a new specific <code>DataContainer</code> for XML files.
	 *
	 * @param dCont the <code>DataContainer</code> instance to use it in the read
	 *              and write methods of this specific data container
	 */
	public XMLDataContainer(DataContainer dCont) {
		dc = dCont;
		dc.containerFormat = EContainerFormat.XML;
		dc.getImplicitHeaders().add("XPath");
	}

	/**
	 * This method is used to load data from an XML-File to the datacontainer. As
	 * the structure of a XML is fundamentally different from the datacontainer
	 * instance, only references of the values are stored in the container as
	 * "X-Paths". Every XML-Tag has its own column in the container. The references
	 * alone are useless, they need to be read out by <code>getValue</code>.
	 *
	 * @param filter Has no effect here.
	 */
	@Override
	public void readData(Filter filter) {
		if(!dc.getFileName().isEmpty()) {
			try {
//				if(!FileUtil.checkFile(dc.getFileName())) {
//					FileUtil.createFile(dc.getFileName(), true);
//				}
				xEdit = new XMLEditor(new File(dc.getFileName()));
			} catch (IOException e) {
				MLogger.getInstance().log(Level.SEVERE, e, "readData");
				throw new RuntimeException(e);
			}
		} else if(dc.getInputStream() != null) {
			xEdit = new XMLEditor(dc.getInputStream());
		}
		dc.setHeaders(xEdit.getXmlTags());
		for (String header : dc.getHeaders().keySet()) {
			dc.setColumn(header, xEdit.getXPaths(header));
		}
	}

	@Override
	public void writeData(String srcFile) {
		xEdit.save(srcFile);
	}
	
	/**
	 * This method is used to translate the XPath references to actual values from
	 * the XML File. Using the specified index, the according row (element
	 * Occurrence) will be read out and fed to a translator method. The output of
	 * this method is the return value.
	 *
	 * @param index The index of the returning row.
	 * @return an array with all translated references (values) of the row.
	 */
	public String[] getDecoded(int index) {
		String[] xPathArr = dc.values.listIterator(index).next();
		String[] ret = new String[xPathArr.length];
		index++;
		for (int i = 0; i < xPathArr.length; i++) {
			ret[i] = xEdit.readXPath(xPathArr[i]);
		}
		return ret;
	}

	/**
	 * Searches for all tags defined by parameter <code>expr</code> and retrieves
	 * the values of their attributes defined by <code>attrName</code>.<br>
	 * If expr includes the tag with full xPath, then the scope of the search will
	 * be the parent xPath of the tag (e.g. /Rules/rule/regExpr only searches in
	 * /Rules/rule).<br>
	 * If expr only includes a tag name, the scope of the search will be the whole
	 * document. In this case all tags that match to <code>expr</code> will be
	 * searched.
	 * 
	 * @param expr 		Name or full XPath of XML tags
	 * @param attrName 	Name of the XML tags attribute from which the values will be returned
	 */
	@Override
	public String[] getAttributes(String expr, String attrName) {
		List<String> lst = new ArrayList<String>();
		// leading "/" indicates a tagname with full xPath
		if (expr.startsWith("/")) {
			// split all elements of full xPath
			String[] splittedTags = expr.split("/");
			// last element of the split xPath is the tagname
			String targetTag = splittedTags[splittedTags.length - 1];
			// delete tagname from the string to get the parent xPath
			String parentExpr = expr.substring(0, expr.length() - targetTag.length() - 1);
			// get DOM element of the tags parent
			Element e = xEdit.getElement(parentExpr);
			// search all child elements that match to the target tagname
			for (Element eCh : xEdit.getChildren(e)) {
				// get all attributes that matching to attrName and add the attributes value to
				// the resultlist
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
	public Object[] getColumn(String headerName, Filter fltr) {
		return getColumn(headerName, fltr, "values");
	}

	/**
	 * This method returns an array with all tag elements or values where headerName matches the tag name and 
	 * the tag element matches with all defined filter rules. The method name getColumn is used also for tree
	 * formats like XML to allow standardized access to all supported DataContainer formats (table, tree etc.).  
	 * 
	 * @param headerName	The name of the tag that will be searched within an XML document.
	 * @param fltr			Filter rules that will be applied as additional search criteria for the returning tags.
	 * @param returnType	values = String Array with all values of the matching tags; elements = Element Array with all matching tag objects
	 * @return				Array of the type, specified by returnType argument
	 */
	private Object[] getColumn(String headerName, Filter fltr, String returnType) {
		List<FilterRule> implFilterRules = dc.getImplFilterRules(fltr);
		List<Element> filteredElements = new ArrayList<>();
		List<String> filteredValues = new ArrayList<>();

		/*
		 *  Filter all tags and values that match an implicit XPath filter rule.
		 */
		if (implFilterRules.size() > 0) {
			for (FilterRule frImpl : implFilterRules) {
				if (frImpl.getHeaderName().equalsIgnoreCase("XPath")) {
					for(Element tagElement:xEdit.getElementsListByXPath(frImpl.getValue())) {
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
			 *  If no implicit XPath filter rule exists, then all tags and values are added to the filteredElements and filteredValues list.
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
	
//	private Object[] getColumn(String headerName, Filter fltr, String returnType) {
//		List<FilterRule> implFilterRules = dc.getImplFilterRules(fltr);
//		List<Element> filteredElements = new ArrayList<Element>();
//		List<String> filteredValues = new ArrayList<String>();
//
//		// filter all values that match an implicit filter rule
//		if (implFilterRules.size() > 0) {
//			for (FilterRule frImpl : implFilterRules) {
//				if (frImpl.getHeaderName().equalsIgnoreCase("XPath")) {
//					Element tagElement = xEdit.getElement(frImpl.getValue());
//					if (tagElement != null) {
//						for (Element childE : xEdit.getChildren(tagElement)) {
//							if (childE.getTagName().equals(headerName)) {
//								filteredElements.add(childE);
//								filteredValues.add(childE.getTextContent());
//							}
//						}
//					}
//				}
//			}
//		} else {
//			// if no implicit filter rule exists, add all values to the filteredValues list
//			for (Element e : xEdit.getElementList(headerName)) {
//				filteredElements.add(e);
//				filteredValues.add(e.getTextContent());
//			}
//		}
//		
//		List<Element> retElements = new ArrayList<>(filteredElements);
//		List<String> retValues = new ArrayList<>(filteredValues);
//		// apply standard filter
//		for (FilterRule fr : fltr.getFilterRules()) {
//			if (dc.getImplFilterRules(fltr).contains(fr) == false) {
//				for (Element fltrE : filteredElements) {
//					if (fr.isValidValue(fltrE.getTextContent(), fr.getValue()) == true) {
//						retValues.remove(fltrE.getTextContent());
//						retElements.remove(fltrE);
//					}
//					
//				}
//			} 
//		}
//		
//		filteredElements = null;
//		filteredValues = null;
//		
//		if (returnType.equals("elements")) {	
//			filteredValues = null;
//			return retElements.toArray(new Element[retElements.size()]);
//		} else {
//			filteredElements = null;
//			return retValues.toArray(new String[retValues.size()]);
//		}
//	}

	@Override
	public void addField(String headerName, String value, Filter fltr) {
		setFieldValues(headerName, new int[0], value, fltr, true);
	}

	@Override
	public void addField(String headerName, String attributeName, String attributeValue, Filter fltr) {
		addField(headerName, attributeName, "", attributeValue, fltr);
	}
	
	@Override
	public void addField(String headerName, String attributeName, String oldAttributeValue, String attributeValue, Filter fltr) {
		for (FilterRule fltrRule : fltr.getFilterRules()) {
			if (fltrRule.getHeaderName().equalsIgnoreCase("XPath")) {
				Element oldEl = xEdit.getElement(headerName, attributeName, oldAttributeValue);
				if(oldEl == null) {
					Element newEl = xEdit.addElement(fltrRule.getValue(), headerName, attributeName, attributeValue);
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
	public void deleteField(String headerName, String attributeName, String attributeValue, Filter fltr) {
		for (FilterRule fltrRule : fltr.getFilterRules()) {
			if (fltrRule.getHeaderName().equalsIgnoreCase("XPath")) {
				xEdit.delElement(fltrRule.getValue(), headerName, attributeName, attributeValue);
				break;
			}
		}
	}

	@Override
	public void setFieldValues(String headerName, int[] occurences, String value, Filter fltr) {
		setFieldValues(headerName, occurences, value, fltr, false);
	}

	public void setFieldValues(String headerName, int[] occurences, String value, Filter fltr, boolean newField) {
		// Initialize occurences array with 0 item, in case it is empty
		if(occurences.length < 1) {
			occurences = new int[] {0};
		}
		for (FilterRule fltrRule : fltr.getFilterRules()) {
			if (fltrRule.getHeaderName().equalsIgnoreCase("XPath")) {
				if (newField) {
					xEdit.addElement(fltrRule.getValue(), headerName, value);
				} else {
					xEdit.checkXPath(fltrRule.getValue() + "/" + headerName, true);
					Element[] elements = (Element[]) getColumn(headerName, fltr, "elements");
					for(int i=0; i<elements.length; i++) {
						List<Integer> occList = Arrays.stream(occurences).boxed().collect(Collectors.toList());
						if((occList.contains(i)) || (occurences[0] == -1)) {
							xEdit.setElementValue(elements[i], value);
						}
					}
				}
				break;
			}
		}
	}
	
//	public String[] setFieldValues(String headerName, int[] occurences, String value, Filter fltr, boolean newField) {
//		List<String> outLst = new ArrayList<String>();
//		for (FilterRule fltrRule : fltr.getFilterRules()) {
//			if (fltrRule.getHeaderName().equalsIgnoreCase("XPath")) {
//				if (newField) {
////    				Element pathE = xEdit.checkXPath(fltrRule.getValue(), true);
//					Element addedE = xEdit.addElement(fltrRule.getValue(), headerName, value);
//					outLst.add(addedE.getTextContent());
//				} else {
//					xEdit.checkXPath(fltrRule.getValue() + "/" + headerName, true);
//					Element[] elements = (Element[]) getColumn(headerName, fltr, "elements");
//					for (Element e : elements) {
//						Element changedE = xEdit.setElementValue(e, value);
//						outLst.add(changedE.getTextContent());
//					}
//				}
//				break;
//			}
//		}
//		return outLst.toArray(new String[outLst.size()]);
//	}
}