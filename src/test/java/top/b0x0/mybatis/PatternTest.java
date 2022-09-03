package top.b0x0.mybatis;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author tlh Created By 2022-07-24 23:11
 **/
public class PatternTest {

    @Test
    public void test() {

        String sql = "        SELECT count(1)\n" +
                "        FROM user\n" +
                "        where username = #{username,jdbcType=VARCHAR}\n" +
                "          and password = #{password,jdbcType=VARCHAR}";
        // 匹配SQL中的 #{} 占位符
        Pattern pattern = Pattern.compile("(#\\{(.*?)})");
        Matcher matcher = pattern.matcher(sql);
        for (int i = 1; matcher.find(); i++) {
            String g1 = matcher.group(1); // #{username,jdbcType=VARCHAR}
            String g2 = matcher.group(2);   // username,jdbcType=VARCHAR
            System.out.println("g1 = " + g1);
            System.out.println("g2 = " + g2);
        }
    }
}
