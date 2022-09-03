package top.b0x0.mybatis.session;

import top.b0x0.mybatis.binding.MapperRegistry;
import top.b0x0.mybatis.mapping.Environment;
import top.b0x0.mybatis.mapping.MapperStatement;
import top.b0x0.mybatis.mapping.ResultMap;
import top.b0x0.mybatis.type.TypeAliasRegistry;
import top.b0x0.mybatis.type.TypeHandlerRegistry;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author tlh Created By 2022-07-24 21:39
 **/
public class Configuration {
    protected Environment environment;

    /**
     * 默认启用缓存，cacheEnabled = true/false
     */
    protected boolean cacheEnabled = true;
    /**
     * 缓存机制，默认不配置的情况是 SESSION
     */
    protected LocalCacheScope localCacheScope = LocalCacheScope.SESSION;

    /**
     * 映射注册机
     */
    protected MapperRegistry mapperRegistry = new MapperRegistry(this);

    /**
     * 映射的语句，存在Map里
     * key：mapper方法全限定名 例如: top.b0x0.mybatis.test.mapper.UserMapper.getUsernameById
     * value: 值是解析xml的值对象
     */
    protected final Map<String, MapperStatement> mappedStatements = new HashMap<>();

    /**
     * 已加载xml资源集合
     */
    protected final Set<String> loadedResources = new HashSet<>();

    /**
     * 类型别名注册机
     */
    protected final TypeAliasRegistry typeAliasRegistry = new TypeAliasRegistry();

    /**
     * 类型处理器注册机
     */
    protected final TypeHandlerRegistry typeHandlerRegistry = new TypeHandlerRegistry();

    protected final Map<String, ResultMap> resultMaps = new HashMap<>();

    public Environment getEnvironment() {
        return environment;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public ResultMap getResultMap(String id) {
        return resultMaps.get(id);
    }

    public void addResultMap(ResultMap resultMap) {
        resultMaps.put(resultMap.getId(), resultMap);
    }

    public TypeAliasRegistry getTypeAliasRegistry() {
        return typeAliasRegistry;
    }

    public TypeHandlerRegistry getTypeHandlerRegistry() {
        return typeHandlerRegistry;
    }

    public boolean isResourceLoaded(String resource) {
        return loadedResources.contains(resource);
    }

    public void addLoadedResource(String resource) {
        loadedResources.add(resource);
    }

    public void addMappers(String packageName) {
        mapperRegistry.scanMapperPackage(packageName);
    }

    public <T> void addMapper(Class<T> type) {
        mapperRegistry.addMapper(type);
    }

    public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
        return mapperRegistry.getMapper(type, sqlSession);
    }

    public boolean hasMapper(Class<?> type) {
        return mapperRegistry.hasMapper(type);
    }

    public void addMappedStatement(MapperStatement ms) {
        mappedStatements.put(ms.getId(), ms);
    }

    public MapperStatement getMappedStatement(String id) {
        return mappedStatements.get(id);
    }

    public boolean isCacheEnabled() {
        return cacheEnabled;
    }

    public void setCacheEnabled(boolean cacheEnabled) {
        this.cacheEnabled = cacheEnabled;
    }

    public LocalCacheScope getLocalCacheScope() {
        return localCacheScope;
    }

    public void setLocalCacheScope(LocalCacheScope localCacheScope) {
        this.localCacheScope = localCacheScope;
    }
}
