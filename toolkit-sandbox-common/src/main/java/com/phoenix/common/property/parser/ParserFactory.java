package com.phoenix.common.property.parser;

import java.io.InputStream;

import com.phoenix.common.property.Property;

public abstract class ParserFactory {
    /** 唯一的ParserFactory对象. */
    private static ParserFactory factory = new com.phoenix.common.property.parser.helpers.ParserFactoryImpl();

    /**
     * 取得默认的ParserFactory.
     */
    public static ParserFactory getInstance() {
        return factory;
    }

    public abstract Parser getParser(InputStream is) throws ParseException;

    public abstract Property parse(InputStream is) throws ParseException;

    public abstract void recycle(Parser parser);
}
