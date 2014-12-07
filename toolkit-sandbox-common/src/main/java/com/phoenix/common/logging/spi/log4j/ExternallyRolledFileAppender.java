package com.phoenix.common.logging.spi.log4j;

import java.io.File;
import java.io.IOException;

/**
 * 自动创建文件目录的appender。
 *
 * @author Michael Zhou
 * @version $Id: ExternallyRolledFileAppender.java 1011 2004-05-13 12:41:00Z baobao $
 */
public class ExternallyRolledFileAppender
        extends org.apache.log4j.varia.ExternallyRolledFileAppender {
    public synchronized void setFile(String fileName, boolean append, boolean bufferedIO,
        int bufferSize) throws IOException {
        File logfile = new File(fileName);

        logfile.getParentFile().mkdirs();

        super.setFile(fileName, append, bufferedIO, bufferSize);
    }
}
