package com.phoenix.common.property.parser;

public interface Locator {
    int getLineNumber();

    int getColumnNumber();

    String getResourceId();
}

