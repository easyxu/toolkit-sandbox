package com.phoenix.common.property.parser;

import com.phoenix.common.property.Property;

public interface ParseHandler {
    Property getRootProperty();

    void setLocator(Locator locator);

    void start() throws ParseException;

    void end() throws ParseException;

    void setName(String name) throws ParseException;

    void include(String resource) throws ParseException;

    void startBase() throws ParseException;

    void endBase() throws ParseException;

    void startPropertyDefinition() throws ParseException;

    void endPropertyDefinition() throws ParseException;

    void startProperty() throws ParseException;

    void endProperty() throws ParseException;

    void propertyVar(String var) throws ParseException;

    void propertyKey(String key) throws ParseException;

    void propertyIndex(int index) throws ParseException;

    void value(String value) throws ParseException;

    void startReference(String valuePart) throws ParseException;

    void endReference() throws ParseException;
}
