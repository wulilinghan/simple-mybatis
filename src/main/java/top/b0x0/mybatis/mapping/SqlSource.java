package top.b0x0.mybatis.mapping;

/**
 * SQL源码
 *
 * @author tlh Created By 2022-07-31 18:45
 **/
public interface SqlSource {

    BoundSql getBoundSql(Object parameterObject);

}
