package top.b0x0.mybatis.binding;

import top.b0x0.mybatis.session.SqlSession;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author tlh Created By 2022-07-23 13:32
 **/
public class MapperProxyFactory<T> {

    private final Class<T> mapper;

    private final Map<Method, MapperMethod> mapperMethodMap = new ConcurrentHashMap<Method, MapperMethod>();

    public MapperProxyFactory(Class<T> mapper) {
        this.mapper = mapper;
    }

    @SuppressWarnings("unchecked")
    public T newInstance(SqlSession sqlSession) {
        MapperProxy<T> mapperProxy = new MapperProxy<>(mapper, mapperMethodMap, sqlSession);
        return (T) Proxy.newProxyInstance(mapper.getClassLoader(), new Class[]{mapper}, mapperProxy);
    }


}
