/**
 * @Project :  스마트톨링정보시스템 구축
 * @Class : XmlUtil.java
 * @Description : 
 *
 * @Author : LGCNS
 * @Since : 2017. 4. 20.
 *
 * @Copyright (c) 2018 EX All rights reserved.
 *-------------------------------------------------------------
 *              Modification Information
 *-------------------------------------------------------------
 * 날짜            수정자             변경사유 
 *-------------------------------------------------------------
 * 2018. 7. 17.        LGCNS             최초작성
 *-------------------------------------------------------------
 */

package cmn.deploy.util;

import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XmlUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(XmlUtil.class);

	public static XMLStreamWriter getWriter(ByteArrayOutputStream bos) throws XMLStreamException {
		
		XMLStreamWriter writer = XMLOutputFactory.newInstance().createXMLStreamWriter(bos);
		
		return writer;
	}
	
	public static void startDocument(XMLStreamWriter writer) throws XMLStreamException {
		writer.writeStartDocument();
	}
	
	public static void startDocument(XMLStreamWriter writer, String encoding, String version) throws XMLStreamException {
		writer.writeStartDocument();
	}
	
	public static void endDocument(XMLStreamWriter writer) throws XMLStreamException {
		writer.writeEndDocument();
	}
	
	public static void addNamespace(XMLStreamWriter writer, String namespace, String  value) throws XMLStreamException {
		writer.writeNamespace(namespace, value);
	}
	
	public static void startElement(XMLStreamWriter writer, String element) throws XMLStreamException {
		writer.writeStartElement(element);
	}
	public static void startElement(XMLStreamWriter writer, String element, String value) throws XMLStreamException {
		writer.writeStartElement(element, value);
	}
	
	public static void endElement(XMLStreamWriter writer) throws XMLStreamException {
		writer.writeEndElement();
	}
	
	public static void writeAttribute(XMLStreamWriter writer, String name, String value) throws XMLStreamException {
		writer.writeAttribute(name, value);
	}
	
	
	public static XMLStreamReader getReader(String fileName) throws XMLStreamException, IOException{
		
		XMLStreamReader reader = XMLInputFactory.newInstance().createXMLStreamReader(new FileReader(fileName));
		
		return reader;
	}
	
	public static XMLStreamReader getReader(StringReader strReader) throws XMLStreamException, IOException{
		
		XMLStreamReader reader = XMLInputFactory.newInstance().createXMLStreamReader(strReader);
		
		return reader;
	}
	
	public static void parseXML(XMLStreamReader reader) throws XMLStreamException {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> eleMap = null;
		while(reader.hasNext()) {
			int event = reader.next();
			if (event == XMLStreamReader.START_ELEMENT) {
				eleMap = new HashMap<String, Object>();
			}
			else if (event == XMLStreamReader.ATTRIBUTE) {
				LOGGER.debug("{}:{}", reader.getAttributeName(event), reader.getAttributeValue(event));
			}
			else if (event == XMLStreamReader.END_ELEMENT) {
			}
			else if (event == XMLStreamReader.END_DOCUMENT) {
				reader.close();
				break;
			}
			
		}
	}
	
	
	public static String prettyPrint(String xml) throws ParserConfigurationException, SAXException, TransformerException, IOException {
		
		StringBuilder sb = new StringBuilder();
		sb.append("<?xml version=\"1.0\" ?>");

		if (xml == null) {
			return sb.toString();
		}
		
		Document document = toXmlDocument(xml);
		
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		
		DOMSource source = new DOMSource(document);
		StringWriter strWriter = new StringWriter();
		StreamResult result = new StreamResult(strWriter);

		transformer.transform(source, result);

		sb.append("\n");
		sb.append(strWriter.getBuffer().toString());
		return sb.toString();

	}

	public static Document toXmlDocument(String str) throws ParserConfigurationException, SAXException, IOException {

		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
		Document document = docBuilder.parse(new InputSource(new StringReader(str)));

		return document;
	}
}
