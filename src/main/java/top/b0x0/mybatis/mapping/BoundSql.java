package top.b0x0.mybatis.mapping;

import java.util.Map;

/**
 * @author tlh Created By 2022-07-30 23:18
 **/
public class BoundSql {
    private String sql;
    private String parameterType;
    private String resultType;

    private Map<Integer, String> parameters;

    public BoundSql() {
    }

    public BoundSql(String sql, String parameterType, String resultType, Map<Integer, String> parameters) {
        this.sql = sql;
        this.parameterType = parameterType;
        this.resultType = resultType;
        this.parameters = parameters;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getParameterType() {
        return parameterType;
    }

    public void setParameterType(String parameterType) {
        this.parameterType = parameterType;
    }

    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    public Map<Integer, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<Integer, String> parameters) {
        this.parameters = parameters;
    }
}
