package com.phoenix.common.file;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
@SuppressWarnings("unchecked")
public class XmlWriter {

	private org.dom4j.io.XMLWriter writer;

	private List list = new ArrayList();
	private String fileName;
	private String encoding="GBK";
	private OutputFormat format = null;
	private Document document = null;

	public XmlWriter() {
	}

	public XmlWriter(String fileName, String encoding) {
		this.fileName = fileName;
		this.encoding = encoding;
	}

	public  Element getRootElement(InputStream stream)
			throws DocumentException {
		SAXReader saxReader = new SAXReader();

		saxReader.setMergeAdjacentText(true);
		return saxReader.read(stream).getRootElement();
	}

	/**
	 * 读xml文件
	 * 
	 * @param fileName
	 *            fileName=path+name
	 * @return
	 * @throws DocumentException
	 * @throws java.io.FileNotFoundException
	 */
	public Document read(String fileName) throws DocumentException,
			FileNotFoundException {

		SAXReader reader = new SAXReader();

		return reader.read(new File(fileName));
	}

	public Document read() throws DocumentException, FileNotFoundException {

		SAXReader reader = new SAXReader();

		return reader.read(new File(fileName));
	}

	/**
	 * 创建Docuemnt
	 * 
	 * @param rootElement
	 * @return
	 */
	public Document createDocument() {
		document = DocumentHelper.createDocument();
		return document;
	}

	/**
	 * 写xml文件
	 * 
	 * @param document
	 * @param file
	 * @throws java.io.IOException
	 */
	public void writer(Document document, String file, String encoding)
			throws IOException {
		this.encoding = encoding;
		format = OutputFormat.createPrettyPrint();
		format.setTrimText(true);
		format.setEncoding(this.encoding);
		format.setNewlines(true);

		writer = new XMLWriter(new FileWriter(file), format);
		writer.write(document);

	}

	public void writer(Document document, String file)
			throws IOException {
		
		format = OutputFormat.createPrettyPrint();
		format.setTrimText(true);
		format.setEncoding(this.encoding);
		format.setNewlines(true);

		writer = new XMLWriter(new FileWriter(file), format);
		writer.write(document);

	}

	public void close() throws IOException {
		writer.close();
	}

	/**
	 * 枚举所有子节点
	 * 
	 * @param root
	 * @return
	 */
	public List getAllChildElement(Element root) {

		list.clear();
		// 枚举所有子节点
		for (Iterator i = root.elementIterator(); i.hasNext();) {
			Element element = (Element) i.next();
			list.add(element);
		}
		return list;
	}

	/**
	 * 枚举名为“childName”的子节点
	 * 
	 * @param root
	 * @param childName
	 * @return
	 */
	public List getChildElement(Element root, String childName) {
		list.clear();
		// 枚举名为“childName”的子节点
		for (Iterator i = root.elementIterator(childName); i.hasNext();) {
			Element element = (Element) i.next();
			// do something

			list.add(element);
		}
		return list;
	}

	/**
	 * XML转字符串
	 * 
	 * @param doc
	 * @return
	 */
	public String xmlToString(Document doc) {

		return doc.asXML();
	}

	/**
	 * 字符串转XML
	 * 
	 * @param text
	 * @return
	 * @throws DocumentException
	 */
	public Document stringToXml(String text) throws DocumentException {
		return DocumentHelper.parseText(text);
	}

	/**
	 * Parses an XML document safely, as to not resolve any external DTDs
	 */
	public Element getRootElementSafely(InputStream stream)
			throws DocumentException {
		SAXReader saxReader = new SAXReader();
		saxReader.setEntityResolver(new NullEntityResolver());
		saxReader.setMergeAdjacentText(true);
		return saxReader.read(stream).getRootElement();
	}

	public static class NullEntityResolver implements EntityResolver {
		private static final byte[] empty = new byte[0];

		public InputSource resolveEntity(String systemId, String publicId)
				throws SAXException, IOException {
			return new InputSource(new ByteArrayInputStream(empty));
		}

	}

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}

}
