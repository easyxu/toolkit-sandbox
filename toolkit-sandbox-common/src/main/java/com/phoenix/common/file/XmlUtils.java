package com.phoenix.common.file;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.LoggerFactory;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.phoenix.common.lang.StringUtil;
@SuppressWarnings("unchecked")
public class XmlUtils {
	private static org.slf4j.Logger log = LoggerFactory.getLogger(XmlUtils.class);

	static class SampleNodeList extends ArrayList implements NodeList {

		/**
		 * 
		 */
		private static final long serialVersionUID = -9024685366513020592L;

		public Node item(int i) {
			return (Node) get(i);
		}

		public int getLength() {
			return size();
		}

		SampleNodeList() {
		}
	}

	public XmlUtils() {
	}

	protected ErrorHandler getErrorHandler() {
		return new ErrorHandler() {

			public void warning(SAXParseException saxparseexception)
					throws SAXException {
			}

			public void error(SAXParseException saxparseexception)
					throws SAXException {
				saxparseexception.printStackTrace();
			}

			public void fatalError(SAXParseException saxparseexception)
					throws SAXException {
				saxparseexception.printStackTrace();
			}

		};
	}

	public synchronized DocumentBuilderFactory getDocumentBuilderFactory(
			boolean flag, boolean flag1) {
		if (documentBuilderFactory == null) {
			documentBuilderFactory = DocumentBuilderFactory.newInstance();
			documentBuilderFactory.setValidating(flag);
			documentBuilderFactory.setNamespaceAware(flag1);
		}
		return documentBuilderFactory;
	}

	public static XmlUtils getInstance() {
		if (__Instance == null)
			__Instance = new XmlUtils();
		return __Instance;
	}

	private static DocumentBuilder getDocumentBuilder(boolean flag,
			boolean flag1) throws ParserConfigurationException {
		DocumentBuilder documentbuilder = getInstance()
				.getDocumentBuilderFactory(flag, flag1).newDocumentBuilder();
		documentbuilder.setErrorHandler(getInstance().getErrorHandler());
		return documentbuilder;
	}

	private static DocumentBuilder getDocumentBuilder()
			throws ParserConfigurationException {
		return getDocumentBuilder(false, false);
	}

	public static Document newDocument() throws ParserConfigurationException,
			SAXException, IOException {
		return getDocumentBuilder().newDocument();
	}

	public static Document parse(InputSource inputsource)
			throws ParserConfigurationException, SAXException, IOException {
		return getDocumentBuilder().parse(inputsource);
	}

	public static Document parse(InputStream inputstream)
			throws ParserConfigurationException, SAXException, IOException {
		byte abyte0[] = new byte[1];
		do {
			if (inputstream.available() <= 0)
				break;
			inputstream.read(abyte0);
		} while (abyte0[0] != 60);
		if (inputstream.available() > 0) {
			long l = 0L;
			try {
				l = inputstream.skip(-1L);
			} catch (Throwable throwable) {
				l = 0L;
			}
			if (l != -1L) {
				ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream(
						inputstream.available());
				abyte0[0] = 60;
				bytearrayoutputstream.write(abyte0);
				FileUtil.streamCopy(inputstream, bytearrayoutputstream);
				ByteArrayInputStream bytearrayinputstream = new ByteArrayInputStream(
						bytearrayoutputstream.toByteArray());
				inputstream = bytearrayinputstream;
			}
		}
		return getDocumentBuilder().parse(inputstream);
	}

	public static Document parseText(String s, String s1)
			throws ParserConfigurationException, SAXException, IOException {
		ByteArrayInputStream bytearrayinputstream = new ByteArrayInputStream(s
				.getBytes(s1));
		Document document = getDocumentBuilder().parse(bytearrayinputstream);
		bytearrayinputstream.close();
		return document;
	}

	public static Document parse(File file)
        throws ParserConfigurationException, SAXException, IOException
    {
		 Document document=null;
		try{
        FileInputStream fileinputstream = null;
       
        fileinputstream = new FileInputStream(file.toString());
        document = getDocumentBuilder().parse(fileinputstream);
        if(fileinputstream != null)
            fileinputstream.close();
    
		}catch(Exception ex){
			log.debug(ex.getMessage(),ex);
		}
	    return document;
    }

	public static Document parse(String s) throws ParserConfigurationException,
	SAXException, IOException {
		Document document=null;
		try{
			FileInputStream fileinputstream = new FileInputStream(s);
			document = getDocumentBuilder().parse(fileinputstream);
			if(fileinputstream != null)
	            fileinputstream.close();
		}catch(Exception ex){
			log.debug(ex.getMessage(),ex);
		}
		return document;
	}
//	FindBugs:
//	public static Document parse(String s) throws ParserConfigurationException,
//			SAXException, IOException {
//		return parse(s);
//	}

	public static String getAttribute(Node node, String s) {
		return getAttribute(node, s, null);
	}

	public static String getAttribute(Node node, String s, String s1) {
		NamedNodeMap namednodemap = node.getAttributes();
		if (namednodemap != null) {
			Attr attr = (Attr) namednodemap.getNamedItem(s);
			if (attr != null)
				return attr.getValue();
		}
		return s1;
	}

	public static void setAttribute(Node node, String s, String s1) {
		if ((Element.class).isInstance(node))
			((Element) node).setAttribute(s, s1);
	}

	public static void removeAttribute(Node node, String s) {
		Attr attr = (Attr) node.getAttributes().getNamedItem(s);
		if (attr != null)
			node.getAttributes().removeNamedItem(s);
	}

	public static Node createChildNode(Node node, String s) {
		Document document;
		if ((Document.class).isInstance(node))
			document = (Document) node;
		else
			document = node.getOwnerDocument();
		Element element = document.createElement(s);
		node.appendChild(element);
		return element;
	}

	public static Node createTextNode(Node node, String s) {
		Document document;
		if ((Document.class).isInstance(node))
			document = (Document) node;
		else
			document = node.getOwnerDocument();
		Text text = document.createTextNode(s);
		node.appendChild(text);
		return text;
	}

	
	public static NodeList getChildListByName(Node node, String s, int i) {
		SampleNodeList samplenodelist = new SampleNodeList();
		for (node = node.getFirstChild(); node != null; node = node
				.getNextSibling()) {
			if (!node.getNodeName().equals(s))
				continue;
			if (i != -1) {
				if (--i == 0)
					samplenodelist.add(node);
			} else {
				samplenodelist.add(node);
			}
		}

		return samplenodelist;
	}

	public static Node getChildByName(Node node, String s) {
		return getChildByName(node, s, 1);
	}

	public static Node getChildByName(Node node, String s, int i) {
		Node node1 = null;
		NodeList nodelist = getChildListByName(node, s, i);
		if (nodelist.getLength() > 0) {
			node1 = nodelist.item(0);
			((SampleNodeList) nodelist).clear();
		}
		return node1;
	}

	public static String getXPath(Node node) {
		String s = new String(node.getNodeName());
		if (node.getParentNode() != null) {
			int i = 0;
			for (Node node1 = node; node1 != null; node1 = node1
					.getPreviousSibling())
				if (node1.getNodeName().toString().equals(
						node.getNodeName().toString()))
					i++;

			if (!(node.getParentNode() instanceof Document)) {
				s = getXPath(node.getParentNode()) + "/" + s;
				if (i > 1)
					s = s + "[" + String.valueOf(i) + "]";
			}
		}
		return s;
	}

	public static String XPathEvaluate(Node node, String s) {
		String s2 = null;
		int i = s.indexOf('@');
		String s1;
		if (i > 0) {
			s1 = s.substring(0, i);
			s2 = s.substring(i + 1);
		} else {
			s1 = s;
		}
		Node node1 = getNodebyXPath(node, s1);
		if (node1 == null)
			return null;
		if (s2 == null)
			return node1.getFirstChild().getNodeValue();
		else
			return getAttribute(node1, s2);
	}

	public static String[] XPathEvaluateA(Node node, String s) {
		String s2 = null;
		int i = s.indexOf('@');
		String s1;
		if (i > 0) {
			s1 = s.substring(0, i);
			s2 = s.substring(i + 1);
		} else {
			s1 = s;
		}
		NodeList nodelist = getNodeListbyXPath(node, s1);
		if (nodelist == null)
			return null;
		int j = nodelist.getLength();
		String as[] = new String[j];
		for (int k = 0; k < j; k++) {
			Node node1 = nodelist.item(k);
			if (s2 == null)
				as[k] = node1.getFirstChild().getNodeValue();
			as[k] = getAttribute(node1, s2);
		}

		return as;
	}

	public static Node getNodebyXPath(Node node, String s) {
		NodeList nodelist = getNodeListbyXPath(node, s);
		if (nodelist.getLength() > 0)
			return nodelist.item(0);
		else
			return null;
	}

	public static NodeList getNodeListbyXPath(Node node, String s) {
		String as[] = s.split("/");
		SampleNodeList samplenodelist = new SampleNodeList();
		Node node1 = node.getFirstChild();
	
		getNodeListbyXPath(node1, samplenodelist, as, 0);
		return samplenodelist;
	}

	public static NodeList newNodeList() {
		return new SampleNodeList();
	}

	private static int getNodeListbyXPath(Node node,
			SampleNodeList samplenodelist, String as[], int i) {
		if (i > as.length - 1)
			return 0;
		String s = as[i];
		if (s.trim().length() == 0)
			return getNodeListbyXPath(node, samplenodelist, as, i + 1);
		int j = 1;
		int k = s.indexOf('[');
		int l = s.indexOf(']');
		if (k > 0 && l > 0) {
			String s1 = s.substring(k + 1, l);
			if (s1.charAt(0) == '*')
				j = -1;
			else
				j = StringUtil.parseInt(s1, 1);
			s = s.substring(0, k);
		}
		for (; node != null; node = node.getNextSibling()) {
			if (!node.getNodeName().equals(s))
				continue;
			if (j != -1) {
				i++;
				if (--j != 0)
					continue;
				if (i != as.length)
					return getNodeListbyXPath(node.getFirstChild(),
							samplenodelist, as, i);
				samplenodelist.add(node);
				continue;
			}
			if (i + 1 == as.length)
				samplenodelist.add(node);
			else
				getNodeListbyXPath(node.getFirstChild(), samplenodelist, as,
						i + 1);
		}

		return i;
	}

	public static String getNodeText(Node node) {
		if ((Text.class).isInstance(node))
			return ((Text) node).getNodeValue();
		StringBuffer stringbuffer = new StringBuffer();
		NodeList nodelist = node.getChildNodes();
		int i = nodelist.getLength();
		for (int j = 0; j < i; j++) {
			node = nodelist.item(j);
			if ((Text.class).isInstance(node))
				stringbuffer.append(((Text) node).getNodeValue());
		}

		return stringbuffer.toString();
	}

	public static Node findNodeByAttr(Node node, String s, String s1) {
		return findNodeByAttr(node, s, s1, true);
	}

	public static Node findNodeByAttr(Node node, String s, String s1,
			boolean flag) {
		if (node == null || s == null || s.length() == 0 || s1 == null
				|| s1.length() == 0)
			return null;
	
		NodeList nodelist = node.getChildNodes();
		int i = nodelist.getLength();
		for (int j = 0; j < i; j++) {
			Node node1 = nodelist.item(j);
			String s2 = getAttribute(node1, s);
			if (s1.equals(s2))
				return node1;
		}

		if (flag) {
			for (int k = 0; k < i; k++) {
				Node node2 = findNodeByAttr(nodelist.item(k), s, s1, flag);
				if (node2 != null)
					return node2;
			}

		}
		return null;
	}

	private DocumentBuilderFactory documentBuilderFactory;
	private static XmlUtils __Instance;
}
