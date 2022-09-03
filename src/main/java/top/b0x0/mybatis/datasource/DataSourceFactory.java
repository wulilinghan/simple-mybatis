package top.b0x0.mybatis.datasource;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * @author tlh Created By 2022-07-31 11:58
 **/
public interface DataSourceFactory {
    void setProperties(Properties props);

    DataSource getDataSource();
}
