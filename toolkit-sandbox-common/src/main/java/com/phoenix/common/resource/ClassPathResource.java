package com.phoenix.common.resource;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import com.phoenix.common.lang.StringUtil;
import com.phoenix.common.reflect.ClassUtils;
import com.phoenix.common.reflect.ObjectUtils;



@SuppressWarnings("unchecked")
public class ClassPathResource extends AbstractResource {

	private final String path;

	private ClassLoader classLoader;

	private Class clazz;

	public ClassPathResource(String path) {
		this(path, (ClassLoader) null);
	}
	
	public ClassPathResource(String path, ClassLoader classLoader) {
	
		if (path.startsWith("/")) {
			path = path.substring(1);
		}
		this.path = StringUtil.cleanPath(path);
		this.classLoader = (classLoader != null ? classLoader : ClassUtils.getDefaultClassLoader());
	}
	
	public ClassPathResource(String path, Class clazz) {
	
		this.path = StringUtil.cleanPath(path);
		this.clazz = clazz;
	}
	protected ClassPathResource(String path, ClassLoader classLoader, Class clazz) {
		this.path = path;
		this.classLoader = classLoader;
		this.clazz = clazz;
	}
	public final String getPath() {
		return this.path;
	}
	public InputStream getInputStream() throws IOException {
		InputStream is = null;
		if (this.clazz != null) {
			is = this.clazz.getResourceAsStream(this.path);
		}
		else {
			is = this.classLoader.getResourceAsStream(this.path);
		}
		if (is == null) {
			throw new FileNotFoundException(
					getDescription() + " cannot be opened because it does not exist");
		}
		return is;
	}
	public URL getURL() throws IOException {
		URL url = null;
		if (this.clazz != null) {
			url = this.clazz.getResource(this.path);
		}
		else {
			url = this.classLoader.getResource(this.path);
		}
		if (url == null) {
			throw new FileNotFoundException(
					getDescription() + " cannot be resolved to URL because it does not exist");
		}
		return url;
	}
	public File getFile() throws IOException {
		return ResourceUtils.getFile(getURL(), getDescription());
	}

	public Resource createRelative(String relativePath) {
		String pathToUse = StringUtil.applyRelativePath(this.path, relativePath);
		return new ClassPathResource(pathToUse, this.classLoader, this.clazz);
	}

	public String getFilename() {
		return StringUtil.getFilename(this.path);
	}
	public String getDescription() {
		return "class path resource [" + this.path + "]";
	}
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof ClassPathResource) {
			ClassPathResource otherRes = (ClassPathResource) obj;
			return (this.path.equals(otherRes.path) &&
					ObjectUtils.nullSafeEquals(this.classLoader, otherRes.classLoader) &&
					ObjectUtils.nullSafeEquals(this.clazz, otherRes.clazz));
		}
		return false;
	}
	public int hashCode() {
		return this.path.hashCode();
	}

}
