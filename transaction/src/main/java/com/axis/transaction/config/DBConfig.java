package com.axis.transaction.config;

import java.util.Properties;

import com.axis.common.ConfigTool;

public class DBConfig {
    //数据库驱动
    public static final String JDBC_DRIVER;
    //连接URL
    public static final String JDBC_URL;
    //数据库用户
    public static final String JDBC_USER;  
    //密码
    public static final String JDBC_PASSWORD;
    //最小连接池
    public static final int JDBC_MINSIZE;
    //最大连接池
    public static final int JDBC_MAXSIZE;
    //初始化连接池
    public static final int JDBC_INITIALSIZE;
    //超时时间
    public static final int JDBC_CHECKOUTTIMEOUT;
    //取连接数
    public static final int JDBC_ACQUIREINCREMENT;
    //检查空闲连接
    public static final int JDBC_IDLECONNECTIONTESTPERIOD;
    //最大空闲时间
    public static final int JDBC_MAXIDLETIME;
    //配置文件相对路径
    public static final String JDBC_PROPERTIES = "jdbc.properties";

    static {
        Properties env = ConfigTool.getConfigProperties(JDBC_PROPERTIES);
        JDBC_DRIVER = (String) env.get("jdbc.driver");
        JDBC_URL = (String) env.get("jdbc.url");
        JDBC_USER = (String) env.get("jdbc.user");
        JDBC_PASSWORD = (String) env.get("jdbc.password");
        JDBC_MINSIZE = Integer.parseInt((String) env.get("jdbc.minPoolSize"));
        JDBC_MAXSIZE = Integer.parseInt((String) env.get("jdbc.maxPoolSize"));
        JDBC_INITIALSIZE = Integer.parseInt((String) env.get("jdbc.initialPoolSize"));
        JDBC_CHECKOUTTIMEOUT = Integer.parseInt((String) env.get("jdbc.checkouttimeout"));
        JDBC_ACQUIREINCREMENT = Integer.parseInt((String) env.get("jdbc.acquireincrement"));
        JDBC_IDLECONNECTIONTESTPERIOD = Integer.parseInt((String) env.get("jdbc.idleConnectionTestPeriod"));
        JDBC_MAXIDLETIME = Integer.parseInt((String) env.get("jdbc.maxidletime"));
    }
}
