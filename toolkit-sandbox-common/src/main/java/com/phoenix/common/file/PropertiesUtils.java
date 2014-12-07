package com.phoenix.common.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.LogManager;
public class PropertiesUtils {
	private Properties props = null;
	private File configFile = null;
	private long fileLastModified = 0L;
	private static Logger log =LogManager.getLogger(PropertiesUtils.class);
	private void init(String fileName) {
		URL url = PropertiesUtils.class.getClassLoader().getResource(fileName);
		configFile = new File(url.getFile());
		fileLastModified = configFile.lastModified();
		props = new Properties();
		load();
	}

	private void load() {
		FileInputStream in = null;
		try {
			in = new FileInputStream(configFile);
			props.load(in);
			fileLastModified = configFile.lastModified();
		} catch (IOException e) {
			log.error(e);
			throw new RuntimeException(e);
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				log.error(e);
			}
		}
	}

	public Properties getConfig(String fileName, String key) {
		if ((configFile == null) || (props == null))
			init(fileName);
		if (configFile.lastModified() > fileLastModified)
			load();
		return props;
	}

	/**
	 *从文件中读取properties
	 */
	/***
	 * <summary></summary>
	 * <param name=""></param>
	 */
	public static final Properties getConnectProperties(final String file)
			throws IOException {
		final Properties props = new Properties();
		if (file != null) {
			log.info("Reading properties from: "
					+ file);
			final File f = new File(file);
			log.info("file:" + f.getAbsolutePath());
			if (f.exists()) {
				final FileInputStream is = new FileInputStream(f);
				try {
					props.load(is);
				} finally {
					is.close();
				}
			} else {
				log.info("File \"" + file
						+ " does not exist， using defaults.");
			}
		}
		return (props);
	}
}
