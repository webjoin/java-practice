package com.iquickmove.ds.core;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class MasterSlaveRoutingDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {

        return DbContextHolder.getDbType();
    }
}