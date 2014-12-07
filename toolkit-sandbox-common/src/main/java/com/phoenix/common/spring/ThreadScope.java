package com.phoenix.common.spring;

import org.springframework.context.support.SimpleThreadScope;

public class ThreadScope extends SimpleThreadScope {
    @Override
    public void registerDestructionCallback(String name, Runnable callback) {
    }
}
