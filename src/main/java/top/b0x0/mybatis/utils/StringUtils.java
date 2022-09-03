package top.b0x0.mybatis.utils;

/**
 * @author tlh Created By 2022-07-31 18:29
 **/
public class StringUtils {

    public static boolean isBlank(String str) {
        return str == null || str.length() == 0;
    }
}
