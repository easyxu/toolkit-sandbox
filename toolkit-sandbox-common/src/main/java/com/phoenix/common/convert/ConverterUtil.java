package com.phoenix.common.convert;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Iterator;
import java.util.Map;

import com.phoenix.common.lang.io.ByteArrayInputStream;


public class ConverterUtil {
	public static String object2XML(Object obj) {
		String beanXml = null;
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		XMLEncoder encoder = new XMLEncoder(outputStream);

		// 对象序列化输出到XML文件
		encoder.writeObject(obj);
		encoder.flush();
		// 关闭序列化工具
		encoder.close();
		// 关闭输出流
		try {
			outputStream.flush();
			beanXml = new String(outputStream.toByteArray(), "utf-8");
			outputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

		}

		return beanXml;
	}

	public static Object XML2Object(String xml) {

		Object object = null;
		ByteArrayInputStream input = null;
		XMLDecoder decoder = null;
		try {
			input = new ByteArrayInputStream(xml.getBytes("utf-8"));
			decoder = new XMLDecoder(input);
			object = decoder.readObject();

		} catch (Exception e) {

		} finally {
			if (decoder != null) {
				decoder.close();
			}
			if (input != null) {
				input.close();
			}
		}
		return object;
	}

	@SuppressWarnings("unchecked")
	public static String map2Content(Map map,String delim) {
		if(delim==null || "".equals(delim)){
			delim="&";
		}
		Iterator keys = map.keySet().iterator();
		StringBuffer buffer = new StringBuffer();
		while (keys.hasNext()) {
			Object key = keys.next();
			Object value = map.get(key);

			buffer.append(key.toString() + "=" + value != null ? value
					.toString().replaceAll(delim, "\\"+delim) : "" + delim);
		}
		if (buffer.length() > 0) {
			return buffer.substring(0, buffer.length() - 1);
		}
		return "";
	}

	public static Object bytesToObject(byte[] data) {
		Object obj = null;

		ObjectInputStream objectInputStream = null;
		try {

			objectInputStream = new ObjectInputStream(new ByteArrayInputStream(
					data));
			obj = objectInputStream.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		} finally {
			try {
				objectInputStream.close();

			} catch (Exception e) {

				e.printStackTrace();
			}

		}
		return obj;
	}

	public static byte[] objectToBytes(Object obj) {

		ByteArrayOutputStream byteArrayOutputStream = null;
		ObjectOutputStream outputStream = null;
		try {
			byteArrayOutputStream = new ByteArrayOutputStream();
			outputStream = new ObjectOutputStream(byteArrayOutputStream);

			outputStream.writeObject(obj);

		} catch (IOException e) {

			e.printStackTrace();
		} finally {
			try {
				outputStream.close();
				byteArrayOutputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		return byteArrayOutputStream.toByteArray();
	}

	public static String objectToString(Object obj) {
		String ret = "";
		ByteArrayOutputStream byteArrayOutputStream = null;
		ObjectOutputStream outputStream = null;
		try {
			byteArrayOutputStream = new ByteArrayOutputStream();
			outputStream = new ObjectOutputStream(byteArrayOutputStream);

			outputStream.writeObject(obj);
			ret = byteArrayOutputStream.toString();

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				outputStream.close();
				byteArrayOutputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return ret;
	}

	public static Object stringToObject(String source) {
		Object obj = null;
		ByteArrayInputStream arrayInputStream = null;
		ObjectInputStream objectInputStream = null;
		try {
			arrayInputStream = new ByteArrayInputStream(source.getBytes());
			objectInputStream = new ObjectInputStream(arrayInputStream);
			obj = objectInputStream.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				objectInputStream.close();
				arrayInputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return obj;
	}

	/**
	 * 转换全大写_字符为驼峰式命名字符
	 * @param binding
	 * @return
	 */
	public static String changeBindingName(String binding){
		String back = "";
		binding = binding.trim(); //去掉前后空格
		if(binding.indexOf("_")>=0){
			String start = binding.substring(0,binding.indexOf("_"));
			String end = binding.substring(binding.indexOf("_")+1);
			if(end.equals("")){
				back= start.toLowerCase();
			}else if(start.equals("")){
				back = end.toLowerCase();
			}else{
				back= start.toLowerCase()+end.substring(0,1).toUpperCase()
				+end.substring(1,end.length()).toLowerCase();
			}
		}else{
			back = binding.toLowerCase();
		}
		return back;
	}
}
