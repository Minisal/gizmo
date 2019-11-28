package xmlcheck;

import java.io.IOException;
import java.io.FileInputStream;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Contains utilities used by various readers in this package.
 */
public class XMLChecker {

    // Constants used for JAXP 1.2
    protected static final String JAXP_SCHEMA_LANGUAGE =
            "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
    protected static final String W3C_XML_SCHEMA =
            "http://www.w3.org/2001/XMLSchema";
    protected static final String JAXP_SCHEMA_SOURCE =
            "http://java.sun.com/xml/jaxp/properties/schemaSource";

    protected XMLChecker() {}

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: XMLChecker xml-file schema-file");
            System.exit(1);
        }
        try {
            XMLChecker reader = new XMLChecker();
            InputSource xml = new InputSource(new FileInputStream(args[0]));
            InputSource schema = new InputSource(new FileInputStream(args[1]));
            reader.parseXml(xml, schema);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected Document parseXml(InputSource xml, InputSource xsdSchema)
            throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        dbf.setValidating(true);
        dbf.setIgnoringElementContentWhitespace(true);
        dbf.setIgnoringComments(true);
        dbf.setAttribute(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA);
        dbf.setAttribute(JAXP_SCHEMA_SOURCE, xsdSchema);
        DocumentBuilder db = dbf.newDocumentBuilder();
        ParseErrorHandler errHandler = new ParseErrorHandler();
        db.setErrorHandler(errHandler);
        Document doc = db.parse(xml);
        if (errHandler.isErrorFree()) {
            return doc;
        } else {
            printException((SAXParseException) errHandler.getErrors().get(0));
            throw (SAXException) errHandler.getErrors().get(0);
        }
    }

    /** parses an integer from an attirbute */
    protected static int getIntFromAttribute(
            String attributeName,
            NamedNodeMap attribute) {
        return Integer.parseInt(
                attribute.getNamedItem(attributeName).getNodeValue());
    }

    /** parses a double from an attirbute */
    protected static double getDoubleFromAttribute(
            String attributeName,
            NamedNodeMap attribute) {
        return Double.parseDouble(
                attribute.getNamedItem(attributeName).getNodeValue());
    }

    /** parses a String from an attirbute */
    protected static String getStringFromAttribute(
            String attributeName,
            NamedNodeMap attribute) {
        return attribute.getNamedItem(attributeName).getNodeValue();
    }

    /** class used to catch errors from parse */
    protected class ParseErrorHandler implements ErrorHandler {

        private List<SAXParseException> errors =
                new LinkedList<SAXParseException>();

        public boolean isErrorFree() {
            return errors.size() == 0;
        }

        public List getErrors() {
            return Collections.unmodifiableList(errors);
        }

        /**
         * @see org.xml.sax.ErrorHandler#error(org.xml.sax.SAXParseException)
         */
        public void error(SAXParseException exception) throws SAXException {
            printException(exception);
            errors.add(exception);
        }

        /**
         * @see org.xml.sax.ErrorHandler#fatalError(org.xml.sax.SAXParseException)
         */
        public void fatalError(SAXParseException exception)
                throws SAXException {
            printException(exception);
            errors.add(exception);
        }

        /**
         * @see org.xml.sax.ErrorHandler#warning(org.xml.sax.SAXParseException)
         */
        public void warning(SAXParseException exception) throws SAXException {
            printException(exception);
            errors.add(exception);
        }

    }

    protected static String prettyPrintIterator(Iterator iter) {
        StringBuffer buffer = new StringBuffer();
        while (iter.hasNext()) {
            buffer.append(iter.next());
        }
        return buffer.toString();
    }

    protected void printException(SAXParseException exception) {
        System.out.println(exception.toString() + " on line " +
                exception.getLineNumber() + " in " + exception.getPublicId());
    }

}
