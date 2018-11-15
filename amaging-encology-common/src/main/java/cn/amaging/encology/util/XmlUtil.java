package cn.amaging.encology.util;

import cn.amaging.encology.util.exception.XmlException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by DuQiyu on 2018/10/23 11:46.
 */
public class XmlUtil {

    private static final String XMLNS_XSI = "xmlns:xsi";
    private static final String XSI_SCHEMA_LOCATION = "xsi:schemaLocation";
    private static final String LOGIC_YES = "yes";
    private static final String DEFAULT_ENCODE = "UTF-8";
    private static final String REG_INVALID_CHARS = "&#\\d+;";

    public static Document newDocument() throws XmlException {
        Document doc = null;
        try {
            doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        } catch (ParserConfigurationException e) {
            throw new XmlException(e);
        }
        return doc;
    }

    public static Document getDocument(File file) throws XmlException {
        InputStream in = getInputStream(file);
        return getDocument(in);
    }

    public static Document getDocument(InputStream in) throws XmlException {
        Document doc = null;
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            doc = builder.parse(in);
        } catch (ParserConfigurationException e) {
            throw new XmlException(e);
        } catch (SAXException e) {
            throw new XmlException(XmlException.XML_PARSE_ERROR, e);
        } catch (IOException e) {
            throw new XmlException(XmlException.XML_READ_ERROR, e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return doc;
    }

    public static Element createRootElement(String tagName) throws XmlException {
        Document doc = newDocument();
        Element root = doc.createElement(tagName);
        doc.appendChild(root);
        return root;
    }

    public static Element getRootElementFromStream(InputStream in)
            throws XmlException {
        return getDocument(in).getDocumentElement();
    }

    public static Element getRootElementFromFile(File file) throws XmlException {
        return getDocument(file).getDocumentElement();
    }

    public static Element getRootElementFromString(String payload) throws XmlException {
        if (payload == null || payload.trim().length() < 1) {
            throw new XmlException(XmlException.XML_PAYLOAD_EMPTY);
        }
        byte[] bytes = null;
        try {
            bytes = payload.getBytes(DEFAULT_ENCODE);
        } catch (UnsupportedEncodingException e) {
            throw new XmlException(XmlException.XML_ENCODE_ERROR, e);
        }
        InputStream in = new ByteArrayInputStream(bytes);
        return getDocument(in).getDocumentElement();
    }

    public static List<Element> getElements(Element parent, String tagName) {
        NodeList nodes = parent.getElementsByTagName(tagName);
        List<Element> elements = new ArrayList<>();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (node instanceof Element) {
                elements.add((Element) node);
            }
        }
        return elements;
    }

    public static Element getElement(Element parent, String tagName) {
        List<Element> children = getElements(parent, tagName);
        if (children.isEmpty()) {
            return null;
        } else {
            return children.get(0);
        }
    }

    public static List<Element> getChildElements(Element parent, String tagName) {
        NodeList nodes = parent.getElementsByTagName(tagName);
        List<Element> elements = new ArrayList<>();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (node instanceof Element && node.getParentNode() == parent) {
                elements.add((Element) node);
            }
        }
        return elements;
    }

    public static Element getChildElement(Element parent, String tagName) {
        List<Element> children = getChildElements(parent, tagName);
        if (children.isEmpty()) {
            return null;
        } else {
            return children.get(0);
        }
    }

    public static String getElementValue(Element parent, String tagName) {
        String value = null;

        Element element = getElement(parent, tagName);
        if (element != null) {
            value = element.getTextContent();
        }

        return value;
    }

    public static Element appendElement(Element parent, String tagName) {
        Element child = parent.getOwnerDocument().createElement(tagName);
        parent.appendChild(child);
        return child;
    }

    public static Element appendElement(Element parent, String tagName,
                                        String value) {
        Element child = appendElement(parent, tagName);
        child.setTextContent(value);
        return child;
    }

    public static void appendElement(Element parent, Element child) {
        Node tmp = parent.getOwnerDocument().importNode(child, true);
        parent.appendChild(tmp);
    }

    public static Element appendCDATAElement(Element parent, String tagName, String value) {
        Element child = appendElement(parent, tagName);
        if (value == null) { // avoid "null" word in the XML payload
            value = "";
        }
        Node cdata = child.getOwnerDocument().createCDATASection(value);
        child.appendChild(cdata);
        return child;
    }

    public static String childNodeToString(Node node) throws XmlException {
        String payload = null;
        try {
            Transformer tf = TransformerFactory.newInstance().newTransformer();
            Properties props = tf.getOutputProperties();
            props.setProperty(OutputKeys.OMIT_XML_DECLARATION, LOGIC_YES);
            tf.setOutputProperties(props);
            StringWriter writer = new StringWriter();
            tf.transform(new DOMSource(node), new StreamResult(writer));
            payload = writer.toString();
            payload = payload.replaceAll(REG_INVALID_CHARS, " ");
        } catch (TransformerException e) {
            throw new XmlException(XmlException.XML_TRANSFORM_ERROR, e);
        }
        return payload;
    }

    public static String nodeToString(Node node) throws XmlException {
        String payload = null;
        try {
            Transformer tf = TransformerFactory.newInstance().newTransformer();
            Properties props = tf.getOutputProperties();
            props.setProperty(OutputKeys.INDENT, LOGIC_YES);
            props.setProperty(OutputKeys.ENCODING, DEFAULT_ENCODE);
            tf.setOutputProperties(props);
            StringWriter writer = new StringWriter();
            tf.transform(new DOMSource(node), new StreamResult(writer));
            payload = writer.toString();
            payload = payload.replaceAll(REG_INVALID_CHARS, " ");
        } catch (TransformerException e) {
            throw new XmlException(XmlException.XML_TRANSFORM_ERROR, e);
        }
        return payload;
    }

    public static String xmlToString(File file) throws XmlException {
        Element root = getRootElementFromFile(file);
        return nodeToString(root);
    }

    public static String xmlToString(InputStream in) throws XmlException {
        Element root = getRootElementFromStream(in);
        return nodeToString(root);
    }

    public static void saveToXml(Node doc, File file) throws XmlException {
        OutputStream out = null;
        try {
            Transformer tf = TransformerFactory.newInstance().newTransformer();
            Properties props = tf.getOutputProperties();
            props.setProperty(OutputKeys.METHOD, XMLConstants.XML_NS_PREFIX);
            props.setProperty(OutputKeys.INDENT, LOGIC_YES);
            tf.setOutputProperties(props);
            DOMSource dom = new DOMSource(doc);
            out = getOutputStream(file);
            Result result = new StreamResult(out);
            tf.transform(dom, result);
        } catch (TransformerException e) {
            throw new XmlException(XmlException.XML_TRANSFORM_ERROR, e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void validateXml(Node doc, File schemaFile)
            throws XmlException {
        validateXml(doc, getInputStream(schemaFile));
    }

    public static void validateXml(Node doc, InputStream schemaStream)
            throws XmlException {
        try {
            Source source = new StreamSource(schemaStream);
            Schema schema = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(source);
            Validator validator = schema.newValidator();
            validator.validate(new DOMSource(doc));
        } catch (SAXException e) {
            throw new XmlException(XmlException.XML_VALIDATE_ERROR, e);
        } catch (IOException e) {
            throw new XmlException(XmlException.XML_READ_ERROR, e);
        } finally {
            if (schemaStream != null) {
                try {
                    schemaStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String xmlToHtml(String payload, File xsltFile)
            throws XmlException {
        String result = null;
        try {
            Source template = new StreamSource(xsltFile);
            Transformer transformer = TransformerFactory.newInstance().newTransformer(template);
            Properties props = transformer.getOutputProperties();
            props.setProperty(OutputKeys.OMIT_XML_DECLARATION, LOGIC_YES);
            transformer.setOutputProperties(props);
            StreamSource source = new StreamSource(new StringReader(payload));
            StreamResult sr = new StreamResult(new StringWriter());
            transformer.transform(source, sr);
            result = sr.getWriter().toString();
        } catch (TransformerException e) {
            throw new XmlException(XmlException.XML_TRANSFORM_ERROR, e);
        }
        return result;
    }

    public static void setNamespace(Element element, String namespace, String schemaLocation) {
        element.setAttributeNS(XMLConstants.XMLNS_ATTRIBUTE_NS_URI, XMLConstants.XMLNS_ATTRIBUTE, namespace);
        element.setAttributeNS(XMLConstants.XMLNS_ATTRIBUTE_NS_URI, XMLNS_XSI, XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI);
        element.setAttributeNS(XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI, XSI_SCHEMA_LOCATION, schemaLocation);
    }

    public static String encodeXml(String payload) throws XmlException {
        Element root = createRootElement(XMLConstants.XML_NS_PREFIX);
        root.setTextContent(payload);
        return childNodeToString(root.getFirstChild());
    }

    private static InputStream getInputStream(File file) throws XmlException {
        InputStream in = null;
        try {
            in = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new XmlException(XmlException.FILE_NOT_FOUND, e);
        }
        return in;
    }

    private static OutputStream getOutputStream(File file) throws XmlException {
        OutputStream in = null;
        try {
            in = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            throw new XmlException(XmlException.FILE_NOT_FOUND, e);
        }
        return in;
    }
}
