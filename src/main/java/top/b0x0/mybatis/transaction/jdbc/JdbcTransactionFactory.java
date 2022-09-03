package top.b0x0.mybatis.transaction.jdbc;

import top.b0x0.mybatis.transaction.TransactionIsolationLevel;
import top.b0x0.mybatis.transaction.Transaction;
import top.b0x0.mybatis.transaction.TransactionFactory;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * JdbcTransaction 工厂
 *
 * @author tlh Created By 2022-07-31 19:00
 **/
public class JdbcTransactionFactory implements TransactionFactory {

    @Override
    public Transaction newTransaction(Connection conn) {
        return new JdbcTransaction(conn);
    }

    @Override
    public Transaction newTransaction(DataSource dataSource, TransactionIsolationLevel level, boolean autoCommit) {
        return new JdbcTransaction(dataSource, level, autoCommit);
    }

}
