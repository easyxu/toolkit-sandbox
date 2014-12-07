package com.phoenix.common.property.parser;

import java.io.InputStream;

public interface Parser {
    void setParseHandler(ParseHandler handler);

    boolean accept(InputStream is) throws ParseException;

    void parse() throws ParseException;
}
