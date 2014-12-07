package com.phoenix.common.logging.spi.log4j;

/**
 * 修正log4j的<code>ConsoleAppender</code>的bug,
 * 根据<code>ConsoleAppender.setEncoding</code>方法所指定的字符集编码输出。
 * 避免关闭stdout和stderr。
 *
 * @author xiang.xu
 * 
 */
public class ConsoleAppender extends org.apache.log4j.ConsoleAppender {
    /**
     * 根据指定的参数选项, 初始化appender.
     */
    public void activateOptions() {
        if (target.equals(SYSTEM_OUT)) {
            setWriter(createWriter(System.out));
        } else {
            setWriter(createWriter(System.err));
        }
    }

    protected void reset() {
        this.qw = null;
    }
}