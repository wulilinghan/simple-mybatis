package top.b0x0.mybatis.session.defaults;

import top.b0x0.mybatis.binding.MapperRegistry;
import top.b0x0.mybatis.session.Configuration;
import top.b0x0.mybatis.session.SqlSession;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tlh Created By 2022-07-23 23:28
 **/
@SuppressWarnings("unchecked")
public class DefaultSqlSession implements SqlSession {

    private final Configuration configuration;

    public DefaultSqlSession(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public <T> T selectOne(String statement) {
        return (T) ("selectOne 方法：" + statement);
    }

    @Override
    public <T> T selectOne(String statement, Object parameter) {
        return (T) ("selectOne 方法：" + statement + " 入参：" + parameter);
    }

    @Override
    public <T> List<T> selectList(String statement, Object parameter) {
        List<T> list = new ArrayList<>();
        list.add((T) ("selectList 方法：" + statement + " 入参：" + parameter));
        return list;
    }

    @Override
    public <T> List<T> selectList(String statement) {
        List<T> list = new ArrayList<>();
        list.add((T) ("selectList 方法：" + statement));
        return list;
    }

    @Override
    public <T> T getMapper(Class<T> mapperClass) {
        return configuration.getMapper(mapperClass, this);
    }
}
