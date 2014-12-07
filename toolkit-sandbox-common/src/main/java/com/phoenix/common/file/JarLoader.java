package com.phoenix.common.file;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@SuppressWarnings("unchecked")
public class JarLoader extends URLClassLoader{

	private  JarLoader loader = null;
	private static Logger log = LoggerFactory.getLogger(JarLoader.class);
	public JarLoader() {
		super(new URL[0], JarLoader.class.getClassLoader());
	}
	public  static JarLoader getInstance(){
		
        return new JarLoader();
    }
    public  void addURL(String url) throws MalformedURLException {

        this.addURL(new URL("file:///"+url));
        
    }
    /**
     * 执行方法
     * @param url			要调用类的包路径  E:/jar/sunrise-core.jar			
     * @param className 	要执行的类  带包名 org.sunrise.util.file.JarLoader
     * @param methodName	执行的方法
     * @param args			方法参数
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws ClassNotFoundException
     * @throws SecurityException
     * @throws IllegalArgumentException
     * @throws NoSuchMethodException
     * @throws java.lang.reflect.InvocationTargetException
     * @throws java.net.MalformedURLException
     */
    public  Object callMethod(String url,String className,String methodName,Object[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SecurityException, IllegalArgumentException, NoSuchMethodException, InvocationTargetException, MalformedURLException{
    	this.addURL(url);
    	 //����
        Thread.currentThread().setContextClassLoader(loader);
      
    	Object o =Class.forName(className,true,loader).newInstance();
    	return invokeMethod(o, methodName, args);
    }
    /**
     *
     * @param className		要执行的类  带包名 org.sunrise.util.file.JarLoader
     * @param methodName	
     * @param args			方法参数
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws ClassNotFoundException
     * @throws SecurityException
     * @throws IllegalArgumentException
     * @throws NoSuchMethodException
     * @throws java.lang.reflect.InvocationTargetException
     * @throws java.net.MalformedURLException
     */
    public  Object callMethod(String className,String methodName,Object[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SecurityException, IllegalArgumentException, NoSuchMethodException, InvocationTargetException, MalformedURLException{
    	
   
     Thread.currentThread().setContextClassLoader(loader);
     Object o =Class.forName(className,true,loader).newInstance();
     
   	return invokeMethod(o, methodName, args);
   }
    /**
     *
     * @param url E:/jar/sunrise-core.jar
     * @param className org.sunrise.util.file.JarLoader
     * @return
     * @throws java.net.MalformedURLException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws ClassNotFoundException
     */
    public  Object callObject(String url,String className) throws MalformedURLException, InstantiationException, IllegalAccessException, ClassNotFoundException{
    	this.addURL(url);
   	 //����
       Thread.currentThread().setContextClassLoader(loader);
       
       return Class.forName(className,true,loader).newInstance();
    }
    /**
     *
     * @param className org.sunrise.util.file.JarLoader
     * @return
     * @throws java.net.MalformedURLException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws ClassNotFoundException
     */
    public  Object callObject(String className) throws MalformedURLException, InstantiationException, IllegalAccessException, ClassNotFoundException{

       Thread.currentThread().setContextClassLoader(loader);
       
       return Class.forName(className,true,loader).newInstance();
    }
    
    /**
     *
     * @param owner
     * @param methodName
     * @param args
     * @return
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws java.lang.reflect.InvocationTargetException
     */
   
	private final  Object invokeMethod(Object owner, String methodName,
			Object[] args) throws SecurityException, NoSuchMethodException,
			IllegalArgumentException, IllegalAccessException,
			InvocationTargetException
	{
		Method thisConMethod = null;
		Class ownerClass = owner.getClass();
		Object returnObject = null;
		if(args==null){
			thisConMethod = ownerClass.getDeclaredMethod(methodName, null);
		
			 returnObject = thisConMethod.invoke(owner, null);
			return returnObject;
		}else{
		Class[] argsClass = new Class[args.length];

		for (int i = 0, j = args.length; i < j; i++)
		{
			argsClass[i] = args[i].getClass();
		}
		thisConMethod = ownerClass.getDeclaredMethod(methodName, argsClass);
		 returnObject = thisConMethod.invoke(owner, args);
		return returnObject;
		}
	}

    
    /** *//**
     *
     * @param name
     * @return
     */
    final static  public byte[] getDataSource(String name) {
        FileInputStream fileInput;
        try {
            fileInput = new FileInputStream(new File(name));
        } catch (FileNotFoundException e) {
        	e.printStackTrace();
        	log.error(e.getMessage(),e);
            fileInput = null;
        }
        BufferedInputStream bufferedInput = new BufferedInputStream(fileInput);
        return getDataSource(bufferedInput);
    }
    
    /** *//**
     *
     * @param name
     * @return
     */
    final static  public byte[] getDataSource(InputStream is) {
        if (is == null) {
            return null;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] arrayByte = null;
        try {
//            byte[] bytes = new byte[8192];
        	 byte[] bytes = null;
            bytes = new byte[is.available()];
            int read;
            while ((read = is.read(bytes)) >= 0) {
                byteArrayOutputStream.write(bytes, 0, read);
            }
            arrayByte = byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            return null;
        } finally {
            try {
                if (byteArrayOutputStream != null) {
                    byteArrayOutputStream.close();
                    byteArrayOutputStream = null;
                }
                if (is != null) {
                    is.close();
                    is = null;
                }

            } catch (IOException e) {
            	e.printStackTrace();
            	log.error(e.getMessage(),e);
            }
        }
        return arrayByte;
    }

    /**
     *
     * @param jar
     * @return
     * @throws java.io.IOException
     */
     final static  public byte[] getResourceData(JarInputStream jar)
            throws IOException {
        ByteArrayOutputStream data = new ByteArrayOutputStream();
        byte[] buffer = new byte[8192];
        int size;
        while (jar.available() > 0) {
            size = jar.read(buffer);
            if (size > 0) {
                data.write(buffer, 0, size);
            }
        }
        return data.toByteArray();
    }

     /**
      *	从jar文件中创建对象
      * @param jarFileName
      * @param className
      * @return
      */
     public  Object createObjectFromJar(String jarFileName,String className)
	 {
//	    	
    	 Object o = null;
    	 try{
    		JarFile jarFile = new JarFile(jarFileName);
    		StringBuffer str = transFileName(className);
    		
 			JarEntry entry = jarFile.getJarEntry(str.toString());
 			
 			InputStream input = jarFile.getInputStream(entry);
 			
 			o=newInstance(input);
    	 }catch(Exception ex)
    	 {
    		 ex.printStackTrace();
    		 log.error(ex.getMessage(),ex);
    	 }
	    return o;
	 }
     private  Object newInstance(InputStream input,String name){
    	 return null;
     }
     /**
      *从虚拟机从创建对象
      * @param fileName
      * @return
      */
     public  Object  createObjectFromJvm(String className){
    		StringBuffer str = new StringBuffer();
    		str.append("/");
    		str.append(transFileName(className));
    	 InputStream input = JarLoader.class.getClass().getResourceAsStream(str.toString());
    	 return newInstance(input);
     }
     /**
      *从文件中创建对象
      * @param fileName
      * @return
      */
     public  Object createObjectFromFile(String fileName){
    	 FileInputStream fileInput;
         try {
             fileInput = new FileInputStream(new File(fileName));
         } catch (FileNotFoundException e) {
         	e.printStackTrace();
         	log.error(e.getMessage(),e);
             fileInput = null;
         }
         BufferedInputStream bufferedInput = new BufferedInputStream(fileInput);
         
    	 return newInstance(bufferedInput);
     }
 	@SuppressWarnings({ "unchecked", "finally" })
	private  Object newInstance(InputStream input){
 		
		byte[] b;
        Object o=null;
		try {
			
			b = new byte[input.available()];
			
			input.read(b);
			
			
		    Class clz = defineClass(null,b, 0, b.length);
		    
		    o=Class.forName(clz.getName(),true,this.getClass().getClassLoader()).newInstance();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			return o;
		}
		
	}
 	/**
 	 *
 	 * @param className
 	 * @return
 	 */
 	private StringBuffer transFileName(String className){
 		StringBuffer str = new StringBuffer();
 		str.append(className.replace(".", "/"));
 		str.append(".class");
 		return str;
 	}
    public static void main(String[] args) {
    	
    	try {
//    		JarLoader jar = JarLoader.getInstance();
    		
//    		XMLTest t = (XMLTest)jar.createObjectFromJar("e:\\jar\\sunrise-core.jar", "test.XMLTest");
//    		XMLTest t = (XMLTest)jar.createObjectFromJvm("test.XMLTest");
//    		XMLTest t = (XMLTest)jar.createObjectFromFile("E:\\jar\\XMLTest.class");
//    		jar.addURL("e:/jar/sunrise-core.jar");
//    		
//    		System.out.println(jar.callMethod("e:/jar/sunrise-core.jar","test.XMLTest", "say", new Object[]{"aa","bb"}));
//    		System.out.println(t);
//    		System.out.println(t.say("Xu", "aa"));
    		
    		

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
