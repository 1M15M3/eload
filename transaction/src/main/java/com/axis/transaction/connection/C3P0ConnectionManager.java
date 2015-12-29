//(C) Copyright 2012 FEITIAN.LTD
/**
* c3p0连接池
* @author STEVEN
* @version V1
*/
package com.axis.transaction.connection;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;

import com.axis.common.Log;
import com.axis.common.log.LogWrapper;
import com.axis.transaction.config.DBConfig;
import com.mchange.v2.c3p0.ComboPooledDataSource;

public final class C3P0ConnectionManager {
    
	private static final LogWrapper log = Log.get();
	
    private static C3P0ConnectionManager instance;  
    private static ComboPooledDataSource dataSource;  
  
    private C3P0ConnectionManager() throws SQLException, PropertyVetoException {  
        dataSource = new ComboPooledDataSource();  
        dataSource.setDriverClass(DBConfig.JDBC_DRIVER);
        dataSource.setJdbcUrl(DBConfig.JDBC_URL);
        dataSource.setUser(DBConfig.JDBC_USER);  
        dataSource.setPassword(DBConfig.JDBC_PASSWORD);  
        dataSource.setInitialPoolSize(DBConfig.JDBC_INITIALSIZE);  
        dataSource.setMinPoolSize(DBConfig.JDBC_MINSIZE);
        dataSource.setMaxPoolSize(DBConfig.JDBC_MAXSIZE);
        dataSource.setAcquireIncrement(DBConfig.JDBC_ACQUIREINCREMENT);
        dataSource.setCheckoutTimeout(DBConfig.JDBC_CHECKOUTTIMEOUT);
        dataSource.setIdleConnectionTestPeriod(DBConfig.JDBC_IDLECONNECTIONTESTPERIOD);
        dataSource.setMaxIdleTime(DBConfig.JDBC_MAXIDLETIME);
    }  
  
    public static final C3P0ConnectionManager getInstance() {  
        if (instance == null) {  
            try {  
                instance = new C3P0ConnectionManager();  
            } catch (Exception e) {  
            	log.error(e.getCause().getMessage());
            }  
        }  
        return instance;  
    }  
  
    public synchronized final Connection getConnection() {  
        Connection conn = null;  
        try {  
            conn = dataSource.getConnection();  
        } catch (SQLException e) {  
        	log.error(e.getCause().getMessage());
        }  
        return conn;  
    }  
    
}
