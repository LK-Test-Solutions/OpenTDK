package org.opentdk.api.io;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.opentdk.api.logger.MLogger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * This class is used for read and write access to data which is stored in XML format.
 * 
 */
public class XMLEditor {

	private File xmlFile;
	private InputStream xmlStream;
	private Document doc;
	private String rootNodeName = "rootTag";
	private Element rootElement;

	/**
	 * Constructor that is used to create a new instance of this object with a given file-object,
	 * defined by the argument <code>xml_src</code>. After creating an instance with this constructor,
	 * immediate read and write access can be performed to the xml file, using the methods provided by
	 * this class.
	 * 
	 * @param xml_src Object of type {@link java.io.File}
	 * @throws IOException if any I/O error occurred
	 */
	public XMLEditor(File xml_src) throws IOException {
		this(xml_src, "");
	}

	public XMLEditor(String fullPath, String rootNode) throws IOException {
		this(new File(fullPath), rootNode);
	}

	/**
	 * Constructor that is used to create a new instance of this object with a given file, defined by
	 * the argument <code>fullPath</code>. After creating an instance with this constructor, immediate
	 * read and write access can be performed to the xml file, using the methods provided by this class.
	 * 
	 * @param fullPath String with the relative or absolute path and filename of the xml file
	 * @throws IOException if any I/O error occurred
	 */
	public XMLEditor(String fullPath) throws IOException {
		this(new File(fullPath), "");
	}

	public XMLEditor(File xml_src, String rootNode) throws IOException {
		xmlFile = xml_src;
		if (!rootNode.isBlank()) {
			rootNodeName = rootNode;
		}
//		try {
		FileUtil.checkDir(xmlFile.getParentFile(), true);
		createXMLEditor();
//		} catch (IOException e) {
//			MLogger.getInstance().log(Level.SEVERE, e);
//		}
	}

	public XMLEditor(InputStream inStream) {
		this(inStream, "");
	}

	public XMLEditor(InputStream inStream, String rootNode) {
		xmlStream = inStream;
		if (!rootNode.isBlank()) {
			rootNodeName = rootNode;
		}
		createXMLEditor();
	}

	public Element addChildElement(Element parent, Element child) {
		Element newE = (Element) parent.appendChild(child);
		save();
		return newE;
	}

	public Element addElement(String xPath, String elementName, String elementValue) {
		return addElement(xPath, elementName, elementValue, "", "");
	}

	public Element addElement(String xPath, String elementName, String attributeName, String attributeValue) {
		return addElement(xPath, elementName, "", attributeName, attributeValue);
	}

	public Element addElement(String xPath, String elementName, String elementValue, String attributeName, String attributeValue) {
		// check if all xpath nodes exist and create missing nodes
		Element pathE = checkXPath(xPath, true);
		// create the element to add
		Element newChild = null;
		if (attributeName.isBlank()) {
			newChild = createElement(elementName);
		} else {
			newChild = this.createElement(elementName, attributeName, attributeValue);
		}
		newChild.setTextContent(elementValue);
		pathE.appendChild(newChild);
		save();
		return newChild;
	}

	public Element addRootElement(Element rootE) {
		rootNodeName = rootE.getNodeName();
		Element outRoot = doc.createElement(rootNodeName);
		save();
		return outRoot;
	}

	/**
	 * Adds a child tag to the root tag of the XML document which is represented by the current instance
	 * of the {@link #doc} property. This method will immediately write the tag into the physical XML
	 * file which is represented by the {@link #doc} property, using the {@link #save()} Method.
	 * 
	 * @param entry The XML tag as type {@link org.w3c.dom.Element} that will be added to the root
	 *              element of the current XML Document
	 */
	public void addTag(Element entry) {
		Element parent = null;
		if (parent == null) {
			// get the root node of the XML document
			parent = doc.getDocumentElement();
		}
		parent.appendChild(entry);
		save();
	}

	/**
	 * Add an element to the root element by using the newElement() without attribute.
	 * 
	 * @param tagName the tag name
	 */
	public void addTag(String tagName) {
		addTag(createElement(tagName));
	}

	/**
	 * Adds a child tag with one or more attributes to the root tag of the XML document which is
	 * represented by the current instance of the {@link #doc} property. This method will immediately
	 * write the tag into the physical XML file which is represented by the {@link #doc} property, using
	 * the {@link #save()} Method.
	 * 
	 * @param tagName    Name of the XML tag
	 * @param attributes HashMap containing attributes with their names as keys
	 */
	public void addTag(String tagName, HashMap<String, String> attributes) {
		addTag(createElement(tagName, attributes));
	}

	/**
	 * Add an element to the root element by using the newElement() method with one attribute.
	 * 
	 * @param tagName        the tag name
	 * @param attributeName  name of the attribute as String
	 * @param attributeValue value of the attribute as String
	 */
	public void addTag(String tagName, String attributeName, String attributeValue) {
		HashMap<String, String> hm = new HashMap<String, String>();
		hm.put(attributeName, attributeValue);
		addTag(tagName, hm);
	}

	public Element checkXPath(String XPath, boolean createMissingNodes) {
		List<Element> eList = getElementsListFromXPath(XPath);
		Element resolvedE = null;
		for (Element searchE : eList) {
			boolean eFound = false;
			if (resolvedE == null) {
				if (compareElements(searchE, doc.getDocumentElement())) {
					resolvedE = doc.getDocumentElement();
					eFound = true;
//				} else {
//					break;
				}
			} else {
				NodeList cnl = resolvedE.getChildNodes();
				for (int i = 0; i < cnl.getLength(); i++) {
					if (cnl.item(i).getNodeType() == 1) {
						if (compareElements(searchE, (Element) cnl.item(i))) {
							resolvedE = (Element) cnl.item(i);
							eFound = true;
							break;
						}
					}
				}
			}
			if ((!eFound) && (createMissingNodes)) {
				if (resolvedE != null) {
					resolvedE = addChildElement(resolvedE, searchE);
					eFound = compareElements(searchE, resolvedE);
				} else {
					addRootElement(searchE);
				}
			}
			if (!eFound) {
				return null;
			}

		}
		return resolvedE;
	}

//	public Element checkXPath(String XPath, boolean createMissingNodes) {
//		List<Element> eList = getElementsListFromXPath(XPath);
//		Element resolvedE = null;
//		for (Element searchE : eList) {
//			boolean eFound = false;
//			if (resolvedE == null) {
//				if (compareElements(searchE, doc.getDocumentElement())) {
//					resolvedE = doc.getDocumentElement();
//					eFound = true;
//				} else {
//					break;
//				}
//			} else {
//				NodeList cnl = resolvedE.getChildNodes();
//				for (int i = 0; i < cnl.getLength(); i++) {
//					if (cnl.item(i).getNodeType() == 1) {
//						if (compareElements(searchE, (Element) cnl.item(i))) {
//							resolvedE = (Element) cnl.item(i);
//							eFound = true;
//							break;
//						}
//					}
//				}
//			}
//			if ((!eFound) && (createMissingNodes)) {
//				resolvedE = addChildElement(resolvedE, searchE);
//				eFound = compareElements(searchE, resolvedE);
//			}
//			if (!eFound) {
//				return null;
//			}
//
//		}
//		return resolvedE;
//	}

	public boolean compareElements(Element eSearch, Element eCompareWith) {
		if (eSearch.getNodeName().equals(eCompareWith.getNodeName())) {
			if (eSearch.getNodeValue() != null) {
				if (eCompareWith.getNodeValue() != null) {
					if (!eSearch.getNodeValue().equals(eCompareWith.getNodeValue())) {
						return false;
					}
				} else {
					return false;
				}
			} else if (eCompareWith.getNodeValue() != null) {
				return false;
			}
			if (eSearch.hasAttributes()) {
				NamedNodeMap attributes = eSearch.getAttributes();
				for (int i = 0; i < attributes.getLength(); i++) {
					if (eCompareWith.hasAttribute(attributes.item(i).getNodeName())) {
						String searchAttrValue = attributes.getNamedItem(attributes.item(i).getNodeName()).getNodeValue();
						String compareAttrValue = eCompareWith.getAttribute(attributes.item(i).getNodeName());
						if (!searchAttrValue.equals(compareAttrValue)) {
							return false;
						}
					} else {
						return false;
					}
				}
			}
		} else {
			return false;
		}
		return true;
	}

	/**
	 * Creates and returns a new object instance of type Element without attributes for further
	 * operations like adding the element to the root tag of an XML document, shown in the following
	 * sample where a new tag with the name <code>newTag</code> will be added to the root tag of XML
	 * file <code>c:\\temp\\sample.xml</code>:
	 * 
	 * <pre>
	 * XMLEditor xmlEdit = new XMLEditor(new File("c:\\temp\\sample.xml"));
	 * Element newE = xmlEdit.createElement("newTag");
	 * xmlEdit.addElement(newE);
	 * </pre>
	 * 
	 * The result can look like this:
	 * 
	 * <pre>
	 * &lt;?xml version="1.0" encoding="UTF-8" standalone="no"?&gt; &lt;rootTag&gt; &lt;newTag/&gt;
	 * &lt;/rootTag&gt;
	 * 
	 * </pre>
	 * 
	 * @param tagName Name of a new XML tag that will be represented by the created Element
	 * @return new object of type {@link org.w3c.dom.Element}
	 */
	public Element createElement(String tagName) {
		return doc.createElement(tagName);
	}

	/**
	 * Creates and returns a new object instance of type Element one or multiple attributes for further
	 * operations, like adding the element to the root tag of an XML document, shown in the following
	 * sample where a new tag with the name <code>newTag</code> will be added to the root tag of XML
	 * file <code>c:\\temp\\sample.xml</code>:
	 * 
	 * <pre>
	 * XMLEditor xmlEdit = new XMLEditor(new File("c:\\temp\\sample.xml"));
	 * HashMap&lt;String, String&gt; attrMap = new HashMap&lt;&gt;();
	 * Element newE = xmlEdit.createElement("newTag", attrMap);
	 * xmlEdit.addElement(newE);
	 * </pre>
	 * 
	 * The result can look like this:
	 * 
	 * <pre>
	 * &lt;?xml version="1.0" encoding="UTF-8" standalone="no"?&gt; &lt;rootTag&gt; &lt;newTag
	 * name="SampleText"&gt;/ &lt;/rootTag&gt;
	 * 
	 * </pre>
	 * 
	 * @param tagName    Name of a new XML tag that will be represented by the created Element
	 * @param attributes HashMap containing attributes with their names as keys
	 * @return new object of type {@link org.w3c.dom.Element}
	 */
	public Element createElement(String tagName, HashMap<String, String> attributes) {
		Element ret = doc.createElement(tagName);
		if (attributes != null && !attributes.isEmpty())
			for (String attribute : attributes.keySet()) {
				ret.setAttribute(attribute, attributes.get(attribute));
			}
		return ret;
	}

	/**
	 * Creates and returns a new object instance of type Element one attribute for further operations,
	 * like adding the element to the root tag of an XML document, shown in the following sample where a
	 * new tag with the name <code>newTag</code> will be added to the root tag of XML file
	 * <code>c:\\temp\\sample.xml</code>:
	 * 
	 * <pre>
	 * XMLEditor xmlEdit = new XMLEditor(new File("c:\\temp\\sample.xml"));
	 * Element newE = xmlEdit.createElement("newTag", "name", "SampleText");
	 * xmlEdit.addElement(newE);
	 * </pre>
	 * 
	 * The result can look like this:
	 * 
	 * <pre>
	 * &lt;?xml version="1.0" encoding="UTF-8" standalone="no"?&gt; &lt;rootTag&gt; &lt;newTag
	 * name="SampleText"&gt;/ &lt;/rootTag&gt;
	 * 
	 * </pre>
	 * 
	 * @param tagName        Name of a new XML tag that will be represented by the created Element
	 * @param attributeName  name of the attribute as String
	 * @param attributeValue value of the attribute as String
	 * @return new object of type {@link org.w3c.dom.Element}
	 */
	public Element createElement(String tagName, String attributeName, String attributeValue) {
		HashMap<String, String> hm = new HashMap<String, String>();
		hm.put(attributeName, attributeValue);
		return createElement(tagName, hm);
	}

	private void createXMLEditor() {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		String feature = null;
		try {
			// Security settings: If document type definitions (DTD) are disallowed, almost all XML entity
			// attacks are prevented
			feature = "http://apache.org/xml/features/disallow-doctype-decl";
			dbf.setFeature(feature, true);
		} catch (ParserConfigurationException e) {
			MLogger.getInstance().log(Level.WARNING, "The feature '" + feature + "' is probably not supported the XML processor. XML will not be read.", getClass().getSimpleName(), "createXMLEditor");
		}
		// Security settings: This prevents the parser to expand the entity reference node
		dbf.setExpandEntityReferences(false);
		// Security settings: Set an empty string to deny all access to external references
		dbf.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
		dbf.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");

		try {
			DocumentBuilder docBuilder = dbf.newDocumentBuilder();
			if ((xmlFile != null) && (xmlFile.exists())) {
				doc = docBuilder.parse(xmlFile);
				doc.getDocumentElement().normalize();
				rootElement = doc.getDocumentElement();
			} else if (xmlStream != null) {
				xmlStream.reset();
				doc = docBuilder.parse(xmlStream);
				doc.getDocumentElement().normalize();
				rootElement = doc.getDocumentElement();
			} else {
				doc = docBuilder.newDocument();
				rootElement = doc.createElement(rootNodeName);
				doc.appendChild(rootElement);
			}
		} catch (ParserConfigurationException e) {
			MLogger.getInstance().log(Level.SEVERE, "The feature '" + feature + "' is probably not supported the XML processor.", getClass().getSimpleName(), "createXMLEditor");
		} catch (SAXException e) {
			MLogger.getInstance().log(Level.SEVERE, "A DOCTYPE was passed into the XML document.", getClass().getSimpleName(), "createXMLEditor");
		} catch (IOException e) {
			MLogger.getInstance().log(Level.SEVERE, "Exception occured: XXE may still possible ==> XML will not be read.", getClass().getSimpleName(), "createXMLEditor");
		}
	}

	/**
	 * Removes the specified element from the XML file.
	 * 
	 * @param target the {@link org.w3c.dom.Element} that should be removed. Retrieve via getElement().
	 */
	public void delElement(Element target) {
		getElement(target).getParentNode().removeChild(getElement(target));
		save();
	}

	/**
	 * Delete a element by tag name and attribute.
	 * 
	 * @param tagName    the tag name
	 * @param attributes the target element's attributes
	 */
	public void delElement(String tagName, HashMap<String, String> attributes) {
		this.delElement(this.getElement(this.createElement(tagName, attributes)));
	}

	/**
	 * Delete a element by tag name and attribute.
	 * 
	 * @param tagName        the tag name
	 * @param attributeName  the target's attributeName for identification
	 * @param attributeValue the target's attributeValue for identification
	 */
	public void delElement(String tagName, String attributeName, String attributeValue) {
		HashMap<String, String> hm = new HashMap<String, String>();
		hm.put(attributeName, attributeValue);
		delElement(tagName, hm);
	}

	public void delElement(String xPath, String elementName) {
		delElement(xPath, elementName, "", "");
	}

	public void delElement(String xPath, String elementName, String attributeName, String attributeValue) {
		Element oldChild = null;
		Element pathE = checkXPath(xPath, false);
		if (pathE != null) {
			if (attributeName.isBlank()) {
				oldChild = getElement(xPath + "/" + elementName);
			} else {
				oldChild = getElement(elementName, attributeName, attributeValue);
			}
			pathE.removeChild(oldChild);
			save();
		}
	}

	/**
	 * Find the XPath of the referenced element.
	 * 
	 * @param target element which XPath shall be returned
	 * @return XPath of the element
	 */
	public String findXPath(Element target) {
		StringBuilder sb = new StringBuilder();
		while (target.getParentNode().getParentNode() != null) {
			String xPart = target.getNodeName();
			if (getIndex(target) > 0)
				xPart = xPart + "[" + getIndex(target) + "]";
			sb.insert(0, "/" + xPart);
			target = this.getParent(target);
		}
		sb.insert(0, "/" + target.getNodeName());
		return sb.toString();
	}

	/**
	 * Retrieve a child element by it's parent, applicable to the specified tag name. If there can be
	 * more than one child use getChildren instead.
	 * 
	 * @param parent  the parent element
	 * @param tagName the child's tag name
	 * @return The found element or null if nothing could be found
	 */
	public Element getChild(Element parent, String tagName) {
		Element ret = null;
		ArrayList<Element> eChildList = this.getChildren(parent);
		for (Element eChild : eChildList) {
			if (eChild.getNodeName().equals(tagName))
				ret = eChild;
		}
		return ret;
	}

	/**
	 * Retrieve a child element by it's parent, applicable to the specified tag name and attribute
	 * value.
	 * 
	 * @param parent    the parent element
	 * @param tagName   the child's tag name
	 * @param attrName  the child's attribute name
	 * @param attrValue the child's attribute value
	 * @return The found element or null if nothing could be found
	 */
	public Element getChild(Element parent, String tagName, String attrName, String attrValue) {
		Element ret = null;
		ArrayList<Element> eChildList = this.getChildren(parent);
		for (Element eChild : eChildList) {
			if (eChild.getNodeName().equals(tagName) && eChild.getAttribute(attrName).contentEquals(attrValue))
				ret = eChild;
		}
		return ret;
	}

	/**
	 * Retrieve a list of all the parent's child elements.
	 * 
	 * @param parent the parent element
	 * @return The found elements or an empty list if nothing could be found.
	 */
	public ArrayList<Element> getChildren(Element parent) {
		ArrayList<Element> children = new ArrayList<Element>();
		NodeList nlChildren = parent.getChildNodes();
		for (int i = 0; i < nlChildren.getLength(); i++) {
			if (nlChildren.item(i).getNodeType() == 1) {
				children.add((Element) nlChildren.item(i));
			}
		}
		return children;
	}

	public Document getDocument() {
		return doc;
	}

	/**
	 * Get Element by XPath expression. In case of multiple occurrences of the XPath within the XML
	 * file, the method will retrieve the element of the first occurrence.
	 * 
	 * @param exp XPath referencing to the searched element
	 * @return the found element
	 */
	public Element getElement(String exp) {
		Element ret = null;
		if (isXPath(exp)) {
			XPath xPath = XPathFactory.newInstance().newXPath();
			try {
				// Security: This evaluation is fine because the document object is already checked when the XML is
				// read.
				ret = (Element) xPath.compile(exp).evaluate(doc, XPathConstants.NODE);
			} catch (XPathExpressionException e) {
				MLogger.getInstance().log(Level.SEVERE, e);
			}
		}
		return ret;
	}

	/**
	 * Get Element by using a newly created target element. Use getElementByParent instead if the
	 * element is not unique.
	 * 
	 * @param target the element to search for
	 * @return the referring element in the xml-file
	 */
	public Element getElement(Element target) {
		HashMap<String, String> attributes = new HashMap<String, String>();
		for (int i = 0; i < target.getAttributes().getLength(); i++) {
			attributes.put(target.getAttributes().item(i).getNodeName(), target.getAttributes().item(i).getNodeValue());
		}
		return getElement(target.getNodeName(), attributes);
	}

	public Element getElement(Element parent, Element searchChild) {
		boolean retVal = false;
		ArrayList<Element> eChildList = this.getChildren(parent);
		for (Element eChild : eChildList) {
			if (eChild.getNodeName().equals(searchChild.getNodeName())) {
				retVal = true;
				NamedNodeMap nMap = eChild.getAttributes();
				for (int i = 0; i < nMap.getLength(); i++) {
					nMap.item(i).getNodeName();
					nMap.item(i).getNodeValue();
					if (!searchChild.hasAttribute(nMap.item(i).getNodeName())) {
						retVal = false;
					} else if (!searchChild.getAttribute(nMap.item(i).getNodeName()).equals(nMap.item(i).getNodeValue())) {
						retVal = false;
					}
				}
			}
			if (retVal) {
				return eChild;
			}
		}
		return null;
	}

	/**
	 * Get Element by using tagName and attribute (only if it is unique).
	 * 
	 * @param tagName        the tagName of the target element
	 * @param attributeName  the attributeName of the searched element
	 * @param attributeValue the attributeValue of the searched element
	 * @return the found element
	 */
	public Element getElement(String tagName, String attributeName, String attributeValue) {
		HashMap<String, String> hm = new HashMap<String, String>();
		hm.put(attributeName, attributeValue);
		return getElement(tagName, hm);
	}

	/**
	 * Get Element by using tagName and attribute (only if it is unique).
	 * 
	 * @param tagName    the tagName of the target element
	 * @param attributes the attributes of the searched element
	 * @return the found element
	 */
	public Element getElement(String tagName, HashMap<String, String> attributes) {
		Element ret = null;
		int i = 0;

		NodeList matchElements = rootElement.getElementsByTagName(tagName);

		while (ret == null && i < matchElements.getLength()) {

			if (matchElements.item(i).getAttributes().getLength() == attributes.size()) {
				int matches = 0;
				for (String attr : attributes.keySet()) {
					Node attrNode = matchElements.item(i).getAttributes().getNamedItem(attr);
					if (attrNode != null && attrNode.getNodeValue().equals(attributes.get(attr)))
						matches++;
				}
				if (matches == attributes.size())
					ret = (Element) matchElements.item(i);
			}
			i++;
		}
		return ret;
	}

	/**
	 * Retrieve a list of all applicable elements.
	 * 
	 * @param tagName the element's tag name
	 * @return A list with all the suitable elements.
	 */
	public ArrayList<Element> getElementsList(String tagName) {
		return getElementsList(tagName, false);
	}

	/**
	 * Retrieve a list of all applicable elements. If ignoreChildless is true, child-less elements will
	 * be ignored.
	 * 
	 * @param tagName         the element's tag name
	 * @param ignoreChildless child-less elements will be ignored, if true
	 * @return A list with all the suitable elements.
	 */
	public ArrayList<Element> getElementsList(String tagName, boolean ignoreChildless) {
		ArrayList<Element> ret = new ArrayList<Element>();
		NodeList matchElements = rootElement.getElementsByTagName(tagName);
		for (int i = 0; i < matchElements.getLength(); i++) {
			Node toAdd = matchElements.item(i);
			if (ignoreChildless) {
				if (toAdd.hasChildNodes()) {
					ret.add((Element) toAdd);
				}
			} else {
				ret.add((Element) toAdd);
			}
		}
		return ret;
	}

	public ArrayList<Element> getElementsListByXPath(String exp) {
		NodeList nl = null;
		ArrayList<Element> ret = new ArrayList<Element>();
		if (isXPath(exp)) {
			XPath xPath = XPathFactory.newInstance().newXPath();
			try {
				// Security: This evaluation is fine because the document object is already checked when the XML is
				// read.
				nl = (NodeList) xPath.compile(exp).evaluate(doc, XPathConstants.NODESET);
				for (int i = 0; i < nl.getLength(); i++) {
					ret.add((Element) nl.item(i));
				}
			} catch (XPathExpressionException e) {
				MLogger.getInstance().log(Level.SEVERE, e);
			}
		}
		return ret;
	}

	public List<Element> getElementsListFromXPath(String xPath) {
		List<Element> eList = new ArrayList<Element>();
		Element e = null;
		String[] pathArray = xPath.split("/");
		for (int i = 1; i < pathArray.length; i++) {
			String[] nodeArray = pathArray[i].split("\\[@");
			for (int j = 0; j < nodeArray.length; j++) {
				if (j == 0) {
					e = doc.createElement(nodeArray[j].trim());
				} else {
					if (e != null) {
						String[] attrArray = nodeArray[j].split("=");
						e.setAttribute(attrArray[0], attrArray[1].substring(1, attrArray[1].length() - 2));
					}
				}
			}
			eList.add(e);
		}
		return eList;
	}

	/**
	 * Get the index of the element, corresponding to the number of elements with the same tagName.
	 * 
	 * @param e the element which index shall be returned
	 * @return index of the element
	 */
	public int getIndex(Element e) {
		int index = 0;
		ArrayList<Element> siblings = this.getChildren(this.getParent(e));
		ArrayList<Element> twins = new ArrayList<Element>();
		if (siblings.size() > 1) {
			for (Element sibling : siblings) {
				if (sibling.getNodeName().equals(e.getNodeName())) {
					twins.add(sibling);
				}
			}
			for (index = 0; index < twins.size(); index++) {
				if (e.isSameNode(twins.get(index))) {
					index++;
					break;
				}
			}
		}
		return index;
	}

	public Node getLastNode_fail(String xPath) {
		Node targetNode = (Node) getDocument().getDocumentElement();
		NodeList nl = null;
		String[] pathArray = xPath.split("/");
		for (int i = 1; i < pathArray.length; i++) {
			String[] nodeArray = pathArray[i].split("\\[@");
			if (i == 1) {
				if (targetNode.getNodeName().equalsIgnoreCase(nodeArray[0])) {
					nl = targetNode.getChildNodes();
				}
			} else {
				targetNode = null;
				for (int nIndex = 0; nIndex < nl.getLength(); nIndex++) {
					if ((nl.item(nIndex).getNodeType() == 1) && (nl.item(nIndex).getNodeName().equalsIgnoreCase(nodeArray[0].trim()))) {
						targetNode = nl.item(nIndex);
						nl = targetNode.getChildNodes();
						break;
					}
				}
			}
			if (targetNode == null) {
				System.out.println("Expected Node '" + nodeArray[0] + "' not found");
			}
		}
		return targetNode;
	}

	/**
	 * Get the parent of the referenced element.
	 * 
	 * @param target the child element
	 * @return The parent element
	 */
	public Element getParent(Element target) {
		return (Element) target.getParentNode();
	}

	/**
	 * Retrieves the root element of the xml file for this instance of XMLEditor. The rootElement
	 * property will be assigned by the method {@link #createXMLEditor} with the root element of the
	 * assigned XML-file. If the assigned XML-file does not exist, a new root element will be created
	 * with a default name.
	 * 
	 * @return an object of type Element, representing the root element of the assigned XML-file
	 * 
	 * @see org.w3c.dom.Element
	 */
	public Element getRoot() {
		return doc.getDocumentElement();
	}

	public Element getTargetElement(String xPath) {
		List<Element> eList = getElementsListFromXPath(xPath);
		return eList.get(eList.size() - 1);
	}

	/**
	 * Get element value by XPath expression.
	 * 
	 * @param exp XPath referencing to the searched element
	 * @return the value of the found element
	 */
	public String getText(String exp) {
		String ret = null;
		if (isXPath(exp)) {
			XPath xPath = XPathFactory.newInstance().newXPath();
			try {
				// Security: This evaluation is fine because the document object is already checked when the XML is
				// read.
				ret = (String) xPath.compile(exp).evaluate(doc, XPathConstants.STRING);
			} catch (XPathExpressionException e) {
				MLogger.getInstance().log(Level.SEVERE, e);
			}
		}
		return ret;
	}

	public String getValue(Element parent) {
		String ret = null;
		if (parent.getChildNodes().getLength() == 1) {
			if (parent.getFirstChild().getNodeType() == 3)
				ret = parent.getFirstChild().getNodeValue();
		}
		return ret;
	}

	/**
	 * Returns the current value of property xmlFile as object of type {@link java.io.File}.
	 * 
	 * @return The current value of property xmlFile.
	 */
	public File getXMLFile() {
		return xmlFile;
	}

	/**
	 * This method returns all XML tags used in the XML file by calling another instance of this method
	 * with a list of all elements on the top level and an empty one, used for recursive calls.
	 * 
	 * @return List containing all XML tags used in the XML file
	 */
	public List<String> getXmlTags() {
		List<String> tags = new ArrayList<String>();
		List<Element> eList = new ArrayList<Element>();
		eList.addAll(this.getChildren(rootElement));
		return getXmlTags(eList, tags);
	}

	/**
	 * This method returns all XML tags used in the XML file by checking the passed list for new tags
	 * and calling a new instance of itself on child element lists. The results are stored in the tags
	 * list.
	 * 
	 * @param eList List with all top level elements
	 * @param tags  List with all new XML tags
	 * 
	 * @return List containing all XML tags used in the eList level and beyond
	 */
	private List<String> getXmlTags(List<Element> eList, List<String> tags) {
		for (Element e : eList) {
			if (!tags.contains(e.getNodeName()))
				tags.add(e.getNodeName());
			if (e.hasChildNodes())
				getXmlTags(this.getChildren(e), tags);
		}
		return tags;
	}

	/**
	 * Returns a list of all XPaths, referencing to a element with the passed tag.
	 * 
	 * @param tag String with the tag reference
	 * 
	 * @return List containing all referencing XPaths
	 */
	public List<String> getXPaths(String tag) {
		List<String> xPaths = new ArrayList<String>();
		for (Element e : this.getElementsList(tag)) {
			xPaths.add(findXPath(e));
		}
		return xPaths;
	}

	/**
	 * Check if the String expression is a XPath (currently only checks for a slash character at the
	 * beginning)
	 * 
	 * @param exp XPath expression
	 * @return true if the expression is a XPath (starts with a slash character)
	 */
	public boolean isXPath(String exp) {
		boolean ret = false;
		if (exp != null) {
			if (exp.startsWith("/")) {
				ret = true;
			}
		}
		return ret;
	}

	/**
	 * If the passed element, referenced by an XPath, has attributes, this method returns those by
	 * combining attribute name and value. Otherwise if the element has a text value, this method
	 * returns the value.
	 * 
	 * @param exp String with the XPath reference
	 * 
	 * @return String containing the attributes or value
	 */
	public String readXPath(String exp) {
		StringBuilder sb = new StringBuilder();
		Element el = getElement(exp);
		if (el != null) {
			if (el.hasChildNodes() && el.getFirstChild().getNodeType() == Node.TEXT_NODE && !el.getFirstChild().getNodeValue().trim().equals("")) {
				sb.append(el.getFirstChild().getNodeValue());

			} else if (el.hasAttributes()) {
				NamedNodeMap m = el.getAttributes();
				for (int i = 0; i < m.getLength(); i++) {
					sb.append(m.item(i).getNodeName() + "=" + m.item(i).getNodeValue()).append(";");
				}
				sb.deleteCharAt(sb.length() - 1);
			}
		}
		return sb.toString();
	}

	private void removeEmptySpace(Node parent) {
		int i = 0;
		while (i != parent.getChildNodes().getLength()) {
			if (parent.getChildNodes().item(i).hasChildNodes()) {
				removeEmptySpace(parent.getChildNodes().item(i));
			}
			if (parent.getChildNodes().item(i).getNodeType() == 3 && parent.getChildNodes().item(i).getNodeValue().trim().equals("")) {
				parent.removeChild(parent.getChildNodes().item(i));
			} else {
				i++;
			}
		}
	}

	/**
	 * Replaces the old Element with a new Element at the same hierarchical position.
	 * 
	 * @param oldEl the old Element
	 * @param newEl the new Element
	 */
	public void replaceElement(Element oldEl, Element newEl) {
		Element parent = this.getParent(oldEl);
		this.delElement(oldEl);
		this.addChildElement(parent, newEl);
	}

	/**
	 * Write out results of the XMLEditor to the related file.
	 */
	public void save() {
		if (doc.getDocumentURI() != null) {
			if (!doc.getDocumentURI().isBlank()) {
				save(xmlFile);
			}
		}
	}

	public void save(String fileName) {
		save(new File(fileName));
	}

	public void save(File xmlOut) {
		try {
			StreamResult result = new StreamResult(xmlOut);
			TransformerFactory tf = TransformerFactory.newInstance();
			// Security settings: Set an empty string to deny all access to external references
			tf.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
			tf.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");

			Transformer transformer = tf.newTransformer();

			removeEmptySpace(rootElement);

			DOMSource source = new DOMSource(doc);
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.transform(source, result);

			doc.getDocumentElement().normalize();

		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	}

	public String asString() {
		String ret = "";
		TransformerFactory tf = TransformerFactory.newInstance();
		// Security settings: Set an empty string to deny all access to external references
		tf.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
		tf.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");
		try {
			Transformer transformer = tf.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			
			this.removeEmptySpace(rootElement);
			
			StringWriter writer = new StringWriter();
			transformer.transform(new DOMSource(doc), new StreamResult(writer));
			ret = writer.getBuffer().toString();
		} catch (TransformerException e) {
			e.printStackTrace();
		} 
		return ret;
	}

	/**
	 * Set the TextValue of the element. Use only for elements without children, if it has any they will
	 * be deleted.
	 * 
	 * @param el  the element reference to the element that should be changed
	 * @param val new TextValue
	 * @return the changed element
	 */
	public Element setElementValue(Element el, String val) {
		while (el.hasChildNodes()) {
			el.removeChild(el.getFirstChild());
		}
		el.appendChild(doc.createTextNode(val));
		save();
		return el;
	}

	public static Boolean validateXMLFile(File inFile) {
		try {
			SAXParserFactory.newInstance().newSAXParser().parse(inFile, new DefaultHandler());
		} catch (SAXException e) {
			MLogger.getInstance().log(Level.WARNING, "A DOCTYPE was passed into the XML document.", "XMLEditor", "validateXMLString");
			return false;
		} catch (IOException e) {
			MLogger.getInstance().log(Level.WARNING, "Invalid input used for XML parser", "XMLEditor", "validateXMLString");
			return false;
		} catch (ParserConfigurationException e) {
			MLogger.getInstance().log(Level.WARNING, "Probably non supported feature used for the XML processor.", "XMLEditor", "validateXMLString");
			return false;
		}
		return true;
	}

	public static Boolean validateXMLString(InputStream inStream) {
		try {
			SAXParserFactory.newInstance().newSAXParser().getXMLReader().parse(new InputSource(inStream));
		} catch (IOException e) {
			MLogger.getInstance().log(Level.WARNING, "Invalid input used for XML parser", "XMLEditor", "validateXMLString");
			return false;
		} catch (ParserConfigurationException e) {
			MLogger.getInstance().log(Level.WARNING, "Probably non supported feature used for the XML processor.", "XMLEditor", "validateXMLString");
			return false;
		} catch (SAXException e) {
			MLogger.getInstance().log(Level.WARNING, "A DOCTYPE was passed into the XML document.", "XMLEditor", "validateXMLString");
			return false;
		}
		return true;
	}
}
