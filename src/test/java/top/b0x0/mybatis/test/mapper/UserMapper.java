package top.b0x0.mybatis.test.mapper;

import top.b0x0.mybatis.test.po.User;

/**
 * @author tlh Created By 2022-07-23 13:06
 **/
public interface UserMapper {
    String getUsernameById(String id);

    User getUserInfoById(String email);

    int existUser(String username, String password);
}
