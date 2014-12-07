package com.phoenix.common;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phoenix.common.lang.StringUtil;
import com.thoughtworks.xstream.XStream;


/**
 * 
 * @author xiang
 *
 */
public class AsyncJobUtil {
	private static Logger log = LoggerFactory.getLogger(AsyncJobUtil.class);

	public static enum UniqueKey {
		operator, operatorOrgId, templateId, mailSubject, toAddress, replyToAddress, receiverId, receiverCompName, planType, maillist, smsContent, needSms, mobileNo, faxNo, contactName, site, isGroup, division, senderAlitalkId, htmlCode, memberIds, onlineCheck, operationType, taskId, ownerId
	}

	private static final String DEFAULT_ENCODING = "UTF-8";

	private static XStream stream;
	static {
		stream = new XStream();
		stream.alias("UniqueKey", UniqueKey.class);
	}

	/**
	 * 将异步任务相关信息存放在文件中（将MAP序列化成XML存放到文件中）
	 * 
	 * @param map
	 *            存放信息的MAP
	 * @param root
	 *            文件存放根目录
	 * @param encoding
	 *            文件编码，默认为UTF-8，(encoding == null) ? "UTF-8" : encoding
	 * @return
	 * @throws java.io.IOException
	 */
	public static String createJobFile(Map map, String root, String encoding)
			throws IOException {
		if (map == null || map.isEmpty()) {
			return null;
		}

		String xml = stream.toXML(map);
		// Save XML
		StringBuffer sb = new StringBuffer(root);
		sb.append(File.separatorChar);
		String uuid = getUUID();
		sb.append(uuid);
		String fileName = sb.toString();

		File file = new File(fileName);
		try {
			FileUtils.forceMkdir(new File(root));
			FileUtils.writeStringToFile(file, xml,
					(encoding == null) ? DEFAULT_ENCODING : encoding);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
			throw e;
		}
		return uuid;
	}

	/**
	 * 读取任务信息并将XML转换成MAP
	 * 
	 * @param fileName
	 *            文件全路径
	 * @param encoding
	 *            文件编码，默认为UTF-8，(encoding == null) ? "UTF-8" : encoding
	 * @return
	 * @throws java.io.IOException
	 */
	public static Map readJobFile(String fileName, String encoding)
			throws IOException {
		Map map = new HashMap();
		File file = new File(fileName);
		String xml = null;
		try {
			xml = FileUtils.readFileToString(file,
					(encoding == null) ? DEFAULT_ENCODING : encoding);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
			throw e;
		}
		// XStream stream = new XStream();
		if (StringUtil.isNotBlank(xml)) {
			map = (Map) stream.fromXML(xml);
			return map;
		}

		return null;
	}

	public static void removeJobFile(String fullName) {
		File file = new File(fullName);
		file.delete();
	}

	private static String getUUID() {
		return UUID.randomUUID().toString();
	}

	// public static void main(String[] args) {
	// Map map = new HashMap();
	// for (int j = 0; j < 50; j++) {
	// map.put(Integer.toString(j), "hello你好中国点点滴滴单独的");
	// }
	// map.put("date", new Date());
	// map.put(AsyncJobUtil.UniqueKey.operator, "hhhhjjjjj");
	// map.put(AsyncJobUtil.UniqueKey.templateId,new Integer(1234));
	// long start = System.currentTimeMillis();
	// for (int i = 0; i < 1000; i++) {
	// try {
	// createJobFile(map, "d:/temp", null);
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }
	// long end = System.currentTimeMillis();
	// System.out.println("create file time:" + (end - start));
	//
	// Iterator iter = FileUtils.iterateFiles(new File("d:/temp"), null, true);
	// long s = System.currentTimeMillis();
	// while (iter.hasNext()) {
	// File f = (File) iter.next();
	// String name = f.getPath();
	// try {
	// Map ret = readJobFile(name, null);
	// // System.out.println(ret.get(AsyncJobUtil.UniqueKey.operator));
	// // System.out.println(ret.get(AsyncJobUtil.UniqueKey.templateId));
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }
	// long e = System.currentTimeMillis();
	// System.out.println("read file time:" + (e - s));
	// }

}
