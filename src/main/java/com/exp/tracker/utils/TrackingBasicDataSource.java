package com.exp.tracker.utils;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TrackingBasicDataSource extends BasicDataSource
{
    private static final Log logger = LogFactory
            .getLog(TrackingBasicDataSource.class);
    
    public Connection getConnection() throws SQLException {
        if (logger.isDebugEnabled()) {
            logger.debug("Active database connections count = " + getNumActive());
        }
        return super.getConnection();
    }
}
