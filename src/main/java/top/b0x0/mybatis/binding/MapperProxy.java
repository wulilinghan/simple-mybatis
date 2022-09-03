package top.b0x0.mybatis.binding;

import top.b0x0.mybatis.session.SqlSession;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

/**
 * @author tlh Created By 2022-07-23 12:04
 **/
public class MapperProxy<T> implements InvocationHandler {

    private SqlSession sqlSession;
    private final Class<T> mapper;
    private final Map<Method, MapperMethod> mapperMethodMap;

    public MapperProxy(Class<T> mapper, Map<Method, MapperMethod> mapperMethodMap, SqlSession sqlSession) {
        this.mapper = mapper;
        this.mapperMethodMap = mapperMethodMap;
        this.sqlSession = sqlSession;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 如果是 Object 提供的 toString、hashCode 等方法是不需要代理执行的，所以添加 Object.class.equals(method.getDeclaringClass()) 判断
        if (Object.class.equals(method.getDeclaringClass())) {
            return method.invoke(this, args);
        }
        if (method.getReturnType().isArray()) {
            return sqlSession.selectList(method.getName(), args);
        }
        return sqlSession.selectOne(method.getName(), args);
    }

}

