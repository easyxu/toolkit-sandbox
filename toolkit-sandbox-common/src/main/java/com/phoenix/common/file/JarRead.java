package com.phoenix.common.file;

import java.io.*;

import java.util.jar.*;


/**
 *读取jar文件中的对象
 * @author	徐翔 
 *
 */
public class JarRead {

	
	public   String read(String jarFileName,String className) throws IOException
	{
	
		JarFile jarFile = new JarFile(jarFileName);
		StringBuffer str = transFileName(className);
		JarEntry entry = jarFile.getJarEntry(str.toString());
		InputStream input = jarFile.getInputStream(entry);
		
		String value =process(input);
		jarFile.close();
		return value;
		
	}

	private  String process(InputStream input)throws IOException 
	{

		InputStreamReader isr = new InputStreamReader(input);

		BufferedReader reader = new BufferedReader(isr);
		int ptr=0;
		StringBuffer buffer = new StringBuffer();
		while ((ptr = reader.read()) != -1 ) {
			buffer.append((char)ptr);
		}
		reader.close();
	
		return buffer.toString();
	}
	/**
 	 *	替换"."为"/"增加".class"后缀
 	 * @param className
 	 * @return
 	 */
 	private   StringBuffer transFileName(String className){
 		StringBuffer str = new StringBuffer();
 		str.append(className.replace(".", "/"));
 		str.append(".class");
 		return str;
 	}
	
	public static void main(String[] a){
		try {
//			System.out.println(read("e:/jar/sunrise-core.jar", "org/sunrise/util/file/Files.class"));
//			
//			Runtime t = Runtime.getRuntime();
			JarRead j = new JarRead();
			System.out.println(j.read("e:/jar/sunrise-core.jar", "test.XMLTest"));
//			FileWriter f = new FileWriter("e:/jar/Names.class");
//			f.write(j.read("e:/jar/sunrise-core.jar", "test.Names"));
//			f.close();
//			
//			System.out.println(((Names)f).getName());
//			ClassLoader l = ClassLoader.getSystemClassLoader();
		
			
		
//			t.load();
//			System.out.println(System.getProperty("java.library.path"));
//			t.loadLibrary("jdwp");
//			System.out.println( System.getProperty("os.name" ));
//			Process p =  t.exec("calc");
//			InputStreamReader ir=new InputStreamReader(p.getInputStream());
//
//			LineNumberReader input = new LineNumberReader (ir);
//
//			String line;
//
//			while ((line = input.readLine ()) != null)
//
//			System.out.println(line); 
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
