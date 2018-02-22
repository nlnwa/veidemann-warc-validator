package no.nb.warcvalidator;

import edu.harvard.hul.ois.jhove.JhoveException;
import no.nb.warcvalidator.validator.JhoveWarcFileValidator;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.net.URL;

import static org.junit.Assert.*;

public class DemoApplicationTests {


	@Test
	public void testWarc() throws JhoveException, ParserConfigurationException, IOException, SAXException, XPathExpressionException {

		URL resource = getClass().getClassLoader().getResource("IAH-20080430204825-00000-blackbook.warc.gz");

		String xmlOutFile = "/tmp/warcfile/outfile.xml";

		JhoveWarcFileValidator validator = new JhoveWarcFileValidator(resource.getFile(), xmlOutFile);

		boolean success = true;
		try {
			validator.validateFile();

			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document xmlDocument = documentBuilder.parse(xmlOutFile);

			XPath xpath = XPathFactory.newInstance().newXPath();
			String expression = "/jhove/repInfo/status";
			Node widgetNode = (Node) xpath.evaluate(expression, xmlDocument, XPathConstants.NODE);

			assertNotNull(widgetNode);
			assertEquals("Well-Formed and valid",widgetNode.getTextContent());

			System.out.println("Status er: " + widgetNode.getTextContent());
		} catch (Exception e) {
			success = false;
		}

		assertTrue(success);

	}

	@Test
	public void testWarcThatFails() throws JhoveException, ParserConfigurationException, IOException, SAXException, XPathExpressionException {

		URL resource = getClass().getClassLoader().getResource("test.warc.gz");

		String xmlOutFile = "/tmp/warcfile/failing_warc_outfile.xml";

		JhoveWarcFileValidator validator = new JhoveWarcFileValidator(resource.getFile(), xmlOutFile);

		boolean success = true;
		try {
			validator.validateFile();

			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document xmlDocument = documentBuilder.parse(xmlOutFile);

			XPath xpath = XPathFactory.newInstance().newXPath();
			String expression = "/jhove/repInfo/status";
			Node widgetNode = (Node) xpath.evaluate(expression, xmlDocument, XPathConstants.NODE);

			assertNotNull(widgetNode);
			assertNotEquals("Well-Formed and valid",widgetNode.getTextContent());

			System.out.println("Status er: " + widgetNode.getTextContent());
		} catch (Exception e) {
			success = false;
		}

		assertTrue(success);

	}


}
