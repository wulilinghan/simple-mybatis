package top.b0x0.mybatis.binding;

import top.b0x0.mybatis.mapping.SqlType;
import top.b0x0.mybatis.session.SqlSession;

/**
 * @author tlh Created By 2022-07-30 23:05
 **/
public class MapperMethod {
    private String id;
    private SqlType sqlType;


    public Object execute(SqlSession sqlSession, Object[] args) {
        Object result = null;
        switch (sqlType) {
            case INSERT:
                break;
            case DELETE:
                break;
            case UPDATE:
                break;
            case SELECT:
                result = sqlSession.selectOne(id, args);
                break;
            default:
                throw new RuntimeException("Unknown execution method for: " + id);
        }
        return result;
    }
}
