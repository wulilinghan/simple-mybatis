package top.b0x0.mybatis.mapping;

import top.b0x0.mybatis.session.Configuration;

/**
 * @author tlh Created By 2022-07-24 21:44
 **/
public class MapperStatement {

    private Configuration configuration;

    /**
     * sql全限定名 top.b0x0.mybatis.test.mapper.UserDetailMapper.getUserInfo
     */
    private String id;
    private SqlType sqlType;
    private BoundSql boundSql;

    public MapperStatement() {
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public SqlType getSqlType() {
        return sqlType;
    }

    public void setSqlType(SqlType sqlType) {
        this.sqlType = sqlType;
    }

    public BoundSql getBoundSql() {
        return boundSql;
    }

    public void setBoundSql(BoundSql boundSql) {
        this.boundSql = boundSql;
    }

    public static class Builder {
        private final MapperStatement mapperStatement = new MapperStatement();

        public Builder(Configuration configuration, String id, SqlType sqlType, BoundSql boundSql) {
            mapperStatement.configuration = configuration;
            mapperStatement.id = id;
            mapperStatement.sqlType = sqlType;
            mapperStatement.boundSql = boundSql;
        }

        public MapperStatement build() {
            assert mapperStatement.configuration != null;
            assert mapperStatement.id != null;
            return mapperStatement;
        }
    }
}
