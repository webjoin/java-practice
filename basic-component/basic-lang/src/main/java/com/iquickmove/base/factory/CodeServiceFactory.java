package com.iquickmove.base.factory;

public interface CodeServiceFactory<T extends CodeService> {
    T getService(String code);
}