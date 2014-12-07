package com.phoenix.common.file;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
/**
 * 
 * @author 徐翔
 * <P>日期：2008-6-3</P>
 */
public class SolutionXml {

	private JAXBContext jaxbContext = null;
	private Unmarshaller unmarshaller = null;
	private Marshaller marshaller = null;
	private InputStream xmlInputStream = null;
	private Object returnObject=null;
	private File file = null;
	private FileOutputStream filewriter = null;
	/**
	 * 读取xml文件
	 * @param xmllfile
	 * @param solutionClazz
	 * @return
	 * @throws java.io.FileNotFoundException
	 * @throws javax.xml.bind.JAXBException
	 */
	@SuppressWarnings("unchecked")
	public Object readXml(String xmllfile,Class solutionClazz) throws FileNotFoundException, JAXBException{
		xmlInputStream = new FileInputStream(xmllfile);
		
		jaxbContext = JAXBContext.newInstance(solutionClazz);
		unmarshaller = jaxbContext.createUnmarshaller();
		returnObject = unmarshaller.unmarshal(xmlInputStream);
		return returnObject;
	}
	
	/**
	 * 生成Xml文件
	 * @param xmlfile
	 * @param solutionClazz
	 * @param object
	 * @param encoding
	 * @return
	 * @throws javax.xml.bind.JAXBException
	 * @throws java.io.IOException
	 */
	@SuppressWarnings("unchecked")
	public boolean exportXml(String xmlfile,Class solutionClazz,Object object,String encoding) throws JAXBException, IOException
	{
		boolean flag = false;
		StringWriter writer = new StringWriter();
		jaxbContext = JAXBContext.newInstance(solutionClazz);
		marshaller = jaxbContext.createMarshaller();
		
		marshaller.setProperty(Marshaller.JAXB_ENCODING, encoding);
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		marshaller.marshal(object, writer);
		
		file = new File(xmlfile);
		
		file.createNewFile();
		
		filewriter = new FileOutputStream(file);
		byte str[] = writer.getBuffer().toString().getBytes(encoding);
		filewriter.write(str);
		filewriter.flush();
		filewriter.close();
		flag = true;
		return flag;
	}
	/**
	 * 生成Xml文件
	 * @param xmlfile
	 * @param solutionClazz
	 * @param object
	 * @return
	 * @throws javax.xml.bind.JAXBException
	 * @throws java.io.IOException
	 */
	@SuppressWarnings("unchecked")
	public boolean exportXml(String xmlfile,Class solutionClazz,Object object) throws JAXBException, IOException{
		return exportXml(xmlfile, solutionClazz, object, "GBK");
	}
	
}
