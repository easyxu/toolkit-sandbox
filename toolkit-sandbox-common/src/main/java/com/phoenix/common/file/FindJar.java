package com.phoenix.common.file;

import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
/**
 * 查询jar文件
 * 
 * @author xiang.leau
 * @time  2008-02-25
 */
@SuppressWarnings("unchecked")
public class FindJar {
	public String className;

  
	public List jarFiles = new ArrayList();

    public FindJar() {
    }

    public FindJar(String className) {
        this.className = className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List findClass(String className,String dir, boolean recurse) {
    	this.className = className;
        searchDir(dir, recurse);
        return this.jarFiles;
    }
    public List findClass(String dir, boolean recurse) {
    	
        searchDir(dir, recurse);
        return this.jarFiles;
    }
    protected void searchDir(String dir, boolean recurse) {
        try {
            File d = new File(dir);
            if (!d.isDirectory()) {
                return;
            }
            File[] files = d.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (recurse && files[i].isDirectory()) {
                    searchDir(files[i].getAbsolutePath(), true);
                } else {
                    String filename = files[i].getAbsolutePath();
                    if (filename.endsWith(".jar")||filename.endsWith(".zip")) {
                        ZipFile zip = new ZipFile(filename);
                        Enumeration entries = zip.entries();
                        while (entries.hasMoreElements()) {
                            ZipEntry entry = (ZipEntry) entries.nextElement();
                            String thisClassName = getClassName(entry);
                            if (thisClassName.equals(this.className) || thisClassName.equals(this.className + ".class")) {
                                this.jarFiles.add(filename);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List getFilenames() {
        return this.jarFiles;
    }

    protected String getClassName(ZipEntry entry) {
        StringBuffer className = new StringBuffer(entry.getName().replace('/', '.' ));
        return className.toString();
    }

    public static void main(String args[]) {
        FindJar findInJar = new FindJar("org.sunrise.Model");
        List jarFiles = findInJar.findClass("E:/jar", true);
        
        if (jarFiles.size() == 0) {
            System.out.println("Not Found");
        } else {
            for (int i = 0; i < jarFiles.size(); i++) {
                System.out.println(jarFiles.get(i));
            }
        }
    }
}
