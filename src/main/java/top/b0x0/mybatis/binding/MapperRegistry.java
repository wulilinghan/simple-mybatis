package top.b0x0.mybatis.binding;

import cn.hutool.core.lang.ClassScanner;
import top.b0x0.mybatis.session.Configuration;
import top.b0x0.mybatis.session.SqlSession;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author tlh Created By 2022-07-23 22:44
 **/
public class MapperRegistry {

    private final Map<Class<?>, MapperProxyFactory<?>> mapperProxyFactoryMap = new HashMap<>();

    private Configuration config;

    public MapperRegistry(Configuration config) {
        this.config = config;
    }

    public MapperRegistry(String packageName) {
        scanMapperPackage(packageName);
    }

    @SuppressWarnings("unchecked")
    public <T> T getMapper(Class<T> clazz, SqlSession sqlSession) {
        MapperProxyFactory<T> mapperProxyFactory = (MapperProxyFactory<T>) mapperProxyFactoryMap.get(clazz);
        if (mapperProxyFactory == null) {
            throw new IllegalStateException("mapper " + clazz + " is not registry.");
        }
        try {
            return mapperProxyFactory.newInstance(sqlSession);
        } catch (Exception e) {
            throw new RuntimeException("Error getting mapper instance. Cause:{}" + e, e);
        }
    }

    public void addMapper(Class<?> mapperClass) {
        if (mapperClass.isInterface()) {
            if (hasMapper(mapperClass)) {
                throw new RuntimeException("mapper " + mapperClass + " is already exist.");
            }
            mapperProxyFactoryMap.put(mapperClass, new MapperProxyFactory<>(mapperClass));
        }
    }


    public <T> boolean hasMapper(Class<T> type) {
        return mapperProxyFactoryMap.containsKey(type);
    }

    /**
     * @param packageName ex: "top.b0x0.mybatis.test"
     */
    public void scanMapperPackage(String packageName) {
        Set<Class<?>> classes = ClassScanner.scanPackage(packageName);
        for (Class<?> mapperClass : classes) {
            addMapper(mapperClass);
        }
    }
}
