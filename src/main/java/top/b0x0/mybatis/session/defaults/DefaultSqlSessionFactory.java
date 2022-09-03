package top.b0x0.mybatis.session.defaults;

import top.b0x0.mybatis.binding.MapperRegistry;
import top.b0x0.mybatis.session.Configuration;
import top.b0x0.mybatis.session.SqlSession;
import top.b0x0.mybatis.session.SqlSessionFactory;

/**
 * @author tlh Created By 2022-07-23 23:54
 **/
public class DefaultSqlSessionFactory implements SqlSessionFactory {

    private final Configuration configuration;

    public DefaultSqlSessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public SqlSession openSession() {
        return new DefaultSqlSession(configuration);
    }
}
