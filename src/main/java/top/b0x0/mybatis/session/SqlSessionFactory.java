package top.b0x0.mybatis.session;

/**
 * @author tlh Created By 2022-07-23 22:17
 **/
public interface SqlSessionFactory {

    SqlSession openSession();
}
