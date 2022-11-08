package Tests.XML;

import org.apache.commons.io.FileUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import RegressionTest.BaseRegression;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.nio.file.Paths;

public class TST_exportHTML extends BaseRegression {

	private final String outputFilePath = "output";
	private final String HTML_REPORT_FILE_NAME = "SQSReport.html";
	private final String xslFilePath = "testdata/transformer.xsl";
	private Document document;
	private Element rootElement;
	
	public static void main(String[] args) {
		new TST_exportHTML();
	}
	
	@Override
	protected void runTest() {
		buildXML();
		exportAsHTML();	
	}
	
	private void buildXML(){
        setDocument();
        createRootElement();
    }

    private void setDocument(){
        try{
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

            document = documentBuilder.newDocument();

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    private void createRootElement(){
        rootElement = document.createElement("test");
        rootElement.setAttribute("testName", "TestName");
        document.appendChild(rootElement);
    }

	public void exportAsHTML() {
		try {	
			Source domSource = new DOMSource(document);
			Source xslDocument = new StreamSource(xslFilePath);
			
			File filePath = Paths.get(outputFilePath, HTML_REPORT_FILE_NAME).toFile();
			OutputStream htmlFile = FileUtils.openOutputStream(filePath);
			Result htmlResult = new StreamResult(htmlFile);

			Transformer xslTransformer = TransformerFactory.newInstance().newTransformer(xslDocument);
			xslTransformer.transform(domSource, htmlResult);

		} catch (TransformerFactoryConfigurationError | TransformerException | IOException e) {
			e.printStackTrace();
		}
	}


}
