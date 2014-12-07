package com.phoenix.common.property.parser.helpers;

import java.io.BufferedInputStream;
import java.io.InputStream;

import com.phoenix.common.property.Property;
import com.phoenix.common.property.parser.ParseException;
import com.phoenix.common.property.parser.ParseHandler;
import com.phoenix.common.property.parser.Parser;
import com.phoenix.common.property.parser.ParserFactory;
import com.phoenix.common.property.parser.SimplePool;
import com.phoenix.common.property.parser.std.StandardParser;
import com.phoenix.common.property.parser.xml.XmlParser;

/**
 * 带缓冲的Parser工厂.
 * 
 * <p>
 * 使用方法:
 * <pre>
 *   ParserFactory factory  = ParserFactory.getInstance();
 *   Parser        parser   = factory.getParser(inputStream);
 *   Property      property = factory.parse(inputStream);
 * </pre>
 * </p>
 *
 * @author <a href="mailto:zyh@alibaba-inc.com">Michael Zhou</a>
 * @version $Id: ParserFactoryImpl.java 509 2004-02-16 05:42:07Z baobao $
 */
public class ParserFactoryImpl extends ParserFactory {
    /** 最多缓冲多少个parser. */
    private static final int MAX_PARSERS = 20;

    /** XMLParser缓冲池. */
    private SimplePool xmlParserPool = new SimplePool(MAX_PARSERS);

    /** StandardParser缓冲池. */
    private SimplePool standardParserPool = new SimplePool(MAX_PARSERS);

    public synchronized Parser getParser(InputStream is) throws ParseException {
        // 先试xml parser.
        Parser parser = (Parser) xmlParserPool.get();

        if (parser == null) {
            parser = new XmlParser();
        }

        if (parser.accept(is)) {
            return parser;
        }

        // 否则用标准的parser.
        parser = (Parser) standardParserPool.get();

        if (parser == null) {
            parser = new StandardParser();
        }

        parser.accept(is);

        return parser;
    }

    public Property parse(InputStream is) throws ParseException {
        if (!is.markSupported()) {
            is = new BufferedInputStream(is);
        }

        Parser       parser  = getParser(is);
        ParseHandler handler = new DefaultHandler();

        parser.setParseHandler(handler);
        parser.parse();
        recycle(parser);
        return handler.getRootProperty();
    }

    public synchronized void recycle(Parser parser) {
        if (parser instanceof XmlParser) {
            xmlParserPool.put(parser);
        } else if (parser instanceof StandardParser) {
            standardParserPool.put(parser);
        }
    }
}

