package com.github.dataflow.sender.database.handler;

import com.github.dataflow.common.utils.Closer;
import com.github.dataflow.common.utils.PropertyUtil;
import com.github.dataflow.dubbo.model.DataOutputMapping;
import com.github.dataflow.sender.core.DataSender;
import com.github.dataflow.sender.core.datasource.DataSourceHolder;
import com.github.dataflow.sender.core.exception.DataSenderException;
import com.github.dataflow.sender.database.HiveDataSender;
import com.github.dataflow.sender.database.config.DatabaseConfig;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/7/7
 */
public class HiveDataSenderHandler extends AbstractDatabaseDataSenderHandler {
    @Override
    protected DataSender createDataSender(DataOutputMapping dataOutputMapping) throws Exception {
        HiveDataSender dataSender = new HiveDataSender();
        Properties props = parseToProperties(dataOutputMapping.getDataSourceOutput().getOptions());
        dataSender.setDataSourceHolder(new DataSourceHolder(getHiveDataSource(props)));
        return dataSender;
    }

    private DataSource getHiveDataSource(Properties props) {
        try {
            HiveDataSource dataSource = new HiveDataSource(PropertyUtil.getString(props, DatabaseConfig.JDBC_URL),
                                                           PropertyUtil.getString(props, DatabaseConfig.USERNAME),
                                                           PropertyUtil.getString(props, DatabaseConfig.PASSWORD));
            return dataSource;
        } catch (SQLException e) {
            throw new DataSenderException(e);
        }
    }

    private static class HiveDataSource implements DataSource {
        static {
            String driverName = "org.apache.hadoop.hive.jdbc.HiveDriver";
            try {
                Class.forName(driverName);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        private String url;
        private String username;
        private String password;

        private HiveDataSource() {
        }

        public HiveDataSource(String url, String username, String password) throws SQLException {
            this.url = url;
            this.username = username;
            this.password = password;
            Connection connection = null;
            try {
                connection = getConnection(username, password);
            } finally {
                Closer.closeQuietly(connection);
            }
        }

        @Override
        public Connection getConnection() throws SQLException {
            return getConnection(username, password);
        }

        @Override
        public Connection getConnection(String username, String password) throws SQLException {
            return DriverManager.getConnection(url, username, password);
        }

        @Override
        public <T> T unwrap(Class<T> iface) throws SQLException {
            return null;
        }

        @Override
        public boolean isWrapperFor(Class<?> iface) throws SQLException {
            return false;
        }

        @Override
        public PrintWriter getLogWriter() throws SQLException {
            return null;
        }

        @Override
        public void setLogWriter(PrintWriter out) throws SQLException {

        }

        @Override
        public void setLoginTimeout(int seconds) throws SQLException {

        }

        @Override
        public int getLoginTimeout() throws SQLException {
            return 0;
        }

        @Override
        public Logger getParentLogger() throws SQLFeatureNotSupportedException {
            return null;
        }
    }
}
