package top.b0x0.mybatis.test.mapper;

import java.util.Map;

/**
 * @author tlh Created By 2022-07-23 13:06
 **/
public interface UserDetailMapper {

    Map<String, Object> getUserInfo(String id, String username, String password);

}
