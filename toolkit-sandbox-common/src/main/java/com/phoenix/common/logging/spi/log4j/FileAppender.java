package com.phoenix.common.logging.spi.log4j;

import java.io.File;
import java.io.IOException;

/**
 * 自动创建文件目录的appender。
 *
 * @author xiang.xu
 * 
 */
public class FileAppender extends org.apache.log4j.FileAppender {
    public synchronized void setFile(String fileName, boolean append, boolean bufferedIO,
        int bufferSize) throws IOException {
        File logfile = new File(fileName);

        logfile.getParentFile().mkdirs();

        super.setFile(fileName, append, bufferedIO, bufferSize);
    }
}
