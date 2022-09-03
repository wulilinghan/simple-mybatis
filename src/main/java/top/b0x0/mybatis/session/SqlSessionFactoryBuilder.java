package top.b0x0.mybatis.session;

import top.b0x0.mybatis.builder.xml.XMLConfigBuilder;
import top.b0x0.mybatis.builder.xml.XMLMapperBuilder;
import top.b0x0.mybatis.session.defaults.DefaultSqlSessionFactory;

import java.io.Reader;

/**
 * @author tlh Created By 2022-07-24 21:37
 **/
public class SqlSessionFactoryBuilder {

    public SqlSessionFactory build(Reader reader) {
        XMLConfigBuilder xmlMapperBuilder = new XMLConfigBuilder(reader);
        return build(xmlMapperBuilder.getConfiguration());
    }

    public SqlSessionFactory build(Configuration config) {
        return new DefaultSqlSessionFactory(config);
    }
}
