package com.iquickmove.base.factory;

import com.iquickmove.util.exception.ErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractCodeServiceFactory<T extends CodeService> implements CodeServiceFactory<T> {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    private Map<String, T> providerMap = new HashMap<>();

    public AbstractCodeServiceFactory(List<T> providers) {
        this.initializeProviderMap(providers);
    }

    private void initializeProviderMap(List<T> providers) {
        this.logger.info("初始化工厂服务: {}", this.getFactoryName());
        if (providers != null) {

            for (T provider : providers) {
                String serviceCode = provider.getServiceCode();
                if (serviceCode == null) {
                    throw new ErrorException(String.format("注册服务编码不可为空:%s", provider.getClass()));
                }

                if (this.providerMap.containsKey(serviceCode)) {
                    throw new ErrorException(String.format("注册服务重复: %s, %s, %s", serviceCode,
                        this.providerMap.get(serviceCode).getClass(), provider.getClass()));
                }

                this.providerMap.put(serviceCode, provider);
                this.logger.info("已注册服务: {}, {}", serviceCode, provider.getClass());
            }

        }
    }

    protected abstract String getFactoryName();

    @Override
    public T getService(String code) {
        return this.providerMap.get(code);
    }
}