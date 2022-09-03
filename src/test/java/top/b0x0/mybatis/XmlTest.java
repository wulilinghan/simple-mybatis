package top.b0x0.mybatis;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.junit.Test;
import top.b0x0.mybatis.io.Resources;
import top.b0x0.mybatis.mapping.MapperStatement;
import top.b0x0.mybatis.mapping.SqlType;
import top.b0x0.mybatis.mapping.XMLTag;
import top.b0x0.mybatis.mapping.XMLTagAttribute;

import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author tlh Created By 2022-07-24 22:28
 **/
public class XmlTest {

    @Test
    public void test_resources_getResourceAsReaders() throws IOException {
        List<Reader> resourceAsReaders = Resources.getResourceAsReaders("mybatis/mapper");
        System.out.println("resourceAsReaders = " + resourceAsReaders);
    }

    @Test
    public void test_parse_xml() throws Exception {
        Reader reader = Resources.getResourceAsReader("mybatis/mapper/User_Mapper.xml");

        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(reader);
        Element root = document.getRootElement();

        // mapper xml文件命名空间
        String namespace = root.attribute(XMLTagAttribute.namespace.name()).getValue();

        // 获取所有select标签对象
//        List<Element> selectElements = root.elements(SqlType.SELECT.name().toLowerCase(Locale.ENGLISH));

        List<Element> elements = root.elements();
        for (Element element : elements) {

            String sqlTypeStr = element.getName();

            String id = element.attribute(XMLTagAttribute.id.name()).getValue();
            String parameterType = element.attribute(XMLTagAttribute.parameterType.name()).getValue();
            String resultType = element.attribute(XMLTagAttribute.resultType.name()).getValue();

            String sql = element.getText();
            // ? 匹配
            Map<Integer, String> parameter = new HashMap<>();
            // 匹配SQL中的 #{} 占位符
            Pattern pattern = Pattern.compile("(#\\{(.*?)})");
            Matcher matcher = pattern.matcher(sql);
            for (int i = 1; matcher.find(); i++) {
                String g1 = matcher.group(1);
                String g2 = matcher.group(2);
                parameter.put(i, g2);
                sql = sql.replace(g1, "?");
            }
            MapperStatement ms = new MapperStatement();
            // sql类型
            ms.setSqlType(SqlType.valueOf(sqlTypeStr.toUpperCase().toUpperCase(Locale.ENGLISH)));
            // sql全限定名
            String msId = namespace + "." + id;
            ms.setId(msId);
        }

    }
}
