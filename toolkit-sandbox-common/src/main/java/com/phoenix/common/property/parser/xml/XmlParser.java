package com.phoenix.common.property.parser.xml;


import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import com.phoenix.common.property.parser.ParseException;
import com.phoenix.common.property.parser.ParseHandler;
import com.phoenix.common.property.parser.Parser;

@SuppressWarnings("unchecked")
public class XmlParser implements Parser {

    @SuppressWarnings("unused")
	private XMLReader    saxParser;
    private ParseHandler handler;
    private InputStream  stream;

    public XMLReader getSAXParser() throws SAXException {
        XMLReader parser = XMLReaderFactory.createXMLReader("org.apache.xerces.parsers.SAXParser");

        try {
            // parser.setFeature("http://xml.org/sax/features/validation", true);
            parser.setFeature("http://apache.org/xml/features/validation/dynamic", true);
        } catch (SAXNotRecognizedException snre) {
        } catch (SAXNotSupportedException snse) {
        }

        parser.setFeature("http://xml.org/sax/features/namespaces", true);
        return parser;
    }

    public void setParseHandler(ParseHandler handler) {
        this.handler = handler;
    }

    public boolean accept(InputStream is) throws ParseException {
        try {
            byte[] lookAhead = new byte[200];
            boolean accepted = false;

            // 预读输入流.
            is.mark(lookAhead.length);
            @SuppressWarnings("unused")
			int bytesRead = is.read(lookAhead);
            is.reset();

            // 找到第一个非空字符.
            int i = 0;
            while (i < lookAhead.length && lookAhead[i] <= ' ') {
                i++;
            }

            // 如果第一个非空字符为'<', 则可能为xml文件.
            if (i < lookAhead.length && lookAhead[i] == '<') {
                accepted = true;
            }

            // 保存输入流, 以备使用.
            stream = is;
            return accepted;

        } catch (IOException e) {
            throw new ParseException(e);
        }
    }

    public void parse() throws ParseException {
        try {
            XMLReader reader = getSAXParser();
            reader.setContentHandler(new SaxHandler());
            reader.parse(new InputSource(stream));
        } catch(IOException ioe) {
            throw new ParseException(ioe);
        } catch(SAXException saxe) {
            if (saxe.getException() instanceof ParseException) {
                throw (ParseException) saxe.getException();
            }
            throw new ParseException(saxe);
        }
    }
    @SuppressWarnings("unchecked")
    private class SaxHandler extends DefaultHandler {

        @SuppressWarnings("unused")
		private Locator locator;

      
		private List stack = new ArrayList();

        private void push() {
            stack.add(new Element());
        }

        private Element pop() {
            return (Element) stack.remove(stack.size() - 1);
        }

        private int getLevel() {
            return stack.size();
        }

        private void increaseCount(int top, String name) {
            Element elem = (Element) stack.get(stack.size() - top - 1);
            if (elem.count.containsKey(name)) {
                elem.count.put(name, new Integer(((Integer) elem.count.get(name)).intValue() + 1));
            } else {
                elem.count.put(name, new Integer(1));
            }
        }

        private int getCount(int top, String name) {
            Element elem = (Element) stack.get(stack.size() - top - 1);
            Integer count = (Integer) elem.count.get(name);
            return count == null ? 0 : count.intValue();
        }

        private void append(char[] ch, int start, int len) {
            if (stack.size() > 0) {
                ((Element) stack.get(stack.size() - 1)).buffer.append(ch, start, len);
            }
        }

        public void setDocumentLocator(Locator locator) {
            this.locator = locator;
            handler.setLocator(new SAXLocator(locator));
        }

        public void startElement(String namespaceURI, String localName,
                                 String qName, Attributes atts) throws SAXException {
            try {
                push();

                if (getLevel() == 1) {
                    handler.start();
                    handler.setName(localName);
                } else {
                    handler.startBase();
                    handler.startProperty();
                    handler.propertyVar("base");
                    handler.propertyKey(localName);

                    int count = getCount(1, localName);
                    if (count > 0) {
                        handler.propertyIndex(count);
                    }
                    increaseCount(1, localName);

                    handler.endProperty();
                    handler.endBase();
                }

                for (int i = 0; i < atts.getLength(); i++) {
                    String name = atts.getLocalName(i);
                    handler.startPropertyDefinition();
                    handler.startProperty();
                    handler.propertyKey(name);

                    int count = getCount(0, name);
                    if (count > 0) {
                        handler.propertyIndex(count);
                    }
                    increaseCount(0, name);

                    handler.endProperty();
                    handler.value(atts.getValue(i));
                    handler.endPropertyDefinition();
                }
            } catch (ParseException e) {
                throw new SAXException(e);
            }
        }

        public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
            try {
                if (getLevel() == 1) {
                    handler.end();
                    return;
                }

                String value = pop().buffer.toString().trim();

                handler.startBase();
                handler.startProperty();
                handler.propertyVar("parent");
                handler.endProperty();
                handler.endBase();

                if (!"".equals(value)) {
                    handler.startPropertyDefinition();
                    handler.startProperty();
                    handler.propertyKey(localName);

                    int count = getCount(0, localName);
                    if (count > 1) {
                        handler.propertyIndex(count - 1);
                    }

                    handler.endProperty();
                    handler.value(value);
                    handler.endPropertyDefinition();
                }
            } catch (ParseException e) {
                throw new SAXException(e);
            }
        }

        public void processingInstruction(String target, String data) throws SAXException {
            /** @todo include instruction */
        }

        public void characters(char[] ch, int start, int length) throws SAXException {
            append(ch, start, length);
        }
    }

    private static class SAXLocator implements com.phoenix.common.property.parser.Locator {
        private Locator locator;

        SAXLocator(Locator locator) {
            this.locator = locator;
        }

        public int getLineNumber() {
            return locator.getLineNumber();
        }

        public int getColumnNumber() {
            return locator.getColumnNumber();
        }

        public String getResourceId() {
            return locator.getSystemId();
        }
    }

    private static class Element {
        StringBuffer buffer = new StringBuffer();
        Map          count  = new HashMap();
    }
}
