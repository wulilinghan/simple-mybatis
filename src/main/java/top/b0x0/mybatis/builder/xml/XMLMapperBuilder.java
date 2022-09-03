package top.b0x0.mybatis.builder.xml;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import top.b0x0.mybatis.builder.BaseBuilder;
import top.b0x0.mybatis.builder.MapperBuilderHelper;
import top.b0x0.mybatis.builder.ResultMapResolver;
import top.b0x0.mybatis.io.Resources;
import top.b0x0.mybatis.mapping.*;
import top.b0x0.mybatis.session.Configuration;
import top.b0x0.mybatis.type.ResultFlag;
import top.b0x0.mybatis.utils.StringUtils;

import java.io.InputStream;
import java.io.Reader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author tlh Created By 2022-07-24 21:51
 **/
public class XMLMapperBuilder extends BaseBuilder {
    /**
     * 匹配SQL中的 #{} 占位符
     */
    private static final Pattern PATTERN_POUND_KEY = Pattern.compile("(#\\{(.*?)})");


    private Element element;

    private String resource;
    private String namespace;

    private MapperBuilderHelper builderAssistant;


    public XMLMapperBuilder(InputStream inputStream, Configuration configuration, String resource) throws DocumentException {
        this(new SAXReader().read(inputStream), configuration, resource);
    }

    private XMLMapperBuilder(Document document, Configuration configuration, String resource) {
        super(configuration);
        this.builderAssistant = new MapperBuilderHelper(configuration, resource);
        this.element = document.getRootElement();
        this.resource = resource;
    }

    public void parse() throws Exception {
        // 如果当前资源没有加载过再加载，防止重复加载
        if (!configuration.isResourceLoaded(resource)) {
            // 解析mapper xml文件
            parseMapperXml(element);

            // 标记一下，已经加载过了
            configuration.addLoadedResource(resource);
            // 绑定映射器到namespace Mybatis 源码方法名 -> bindMapperForNamespace
            configuration.addMapper(Resources.classForName(namespace));
        }
    }


    private void parseMapperXml(Element rootElement) {
        // mapper xml文件命名空间
        String namespace = rootElement.attribute(XMLTagAttribute.namespace.name()).getValue();
        if (StringUtils.isBlank(namespace)) {
            throw new RuntimeException(resource + " Invalid namespace is empty ");
        }
        this.namespace = namespace;
        builderAssistant.setNamespace(namespace);

        // xml 中resultMap标签解析
        resultMapTagElements(element.elements(XMLTag.resultMap.name()));

        // 一个xml文件 所有SQL语句节点
        List<Element> elements = rootElement.elements();
        for (Element element : elements) {
            // sql 类型
            String sqlTypeStr = element.getName();

            Attribute idAttr = element.attribute(XMLTagAttribute.id.name());
            if (idAttr == null) {
                throw new RuntimeException(namespace + " Invalid id is empty ");
            }
            // sql方法名
            String id = idAttr.getValue();

            Attribute parameterTypeAttr = element.attribute(XMLTagAttribute.parameterType.name());
            String parameterType = parameterTypeAttr == null ? null : parameterTypeAttr.getValue();
            Attribute resultTypeAttr = element.attribute(XMLTagAttribute.resultType.name());
            String resultType = resultTypeAttr == null ? null : resultTypeAttr.getValue();

            String sql = element.getText();
            // 匹配SQL中的 #{} 占位符
            Map<Integer, String> parameters = new HashMap<>(4);
            Matcher matcher = PATTERN_POUND_KEY.matcher(sql);
            for (int i = 1; matcher.find(); i++) {
                // #{username,jdbcType=VARCHAR}
                String g1 = matcher.group(1);
                // username,jdbcType=VARCHAR
                String g2 = matcher.group(2);
                parameters.put(i, g2);
                sql = sql.replace(g1, "?");
            }
            // SQL
            BoundSql boundSql = new BoundSql(sql, parameterType, resultType, parameters);

            SqlType sqlType = SqlType.valueOf(sqlTypeStr.toUpperCase(Locale.ENGLISH));
            String msId = namespace + "." + id;

            MapperStatement mapperStatement = new MapperStatement.Builder(configuration, msId, sqlType, boundSql).build();
            // 添加解析SQL
            configuration.addMappedStatement(mapperStatement);
        }
    }

    private void resultMapTagElements(List<Element> list) {
        for (Element element : list) {
            try {
                resultMapElement(element, Collections.emptyList());
            } catch (Exception ignore) {
            }
        }
    }

    /**
     * <pre>
     *
     * <resultMap id="activityMap" type="top.b0x0.mybatis.test.po.Activity">
     *      <id column="id" property="id"/>
     *      <result column="activity_id" property="activityId"/>
     *      <result column="activity_name" property="activityName"/>
     *      <result column="activity_desc" property="activityDesc"/>
     *      <result column="create_time" property="createTime"/>
     *      <result column="update_time" property="updateTime"/>
     * </resultMap>
     *
     * </pre>
     */
    private ResultMap resultMapElement(Element resultMapNode, List<ResultMapping> additionalResultMappings) throws Exception {
        String id = resultMapNode.attributeValue("id");
        String type = resultMapNode.attributeValue("type");
        Class<?> typeClass = resolveClass(type);

        List<ResultMapping> resultMappings = new ArrayList<>(additionalResultMappings);

        List<Element> resultMapElementChildren = resultMapNode.elements();
        for (Element resultChild : resultMapElementChildren) {
            List<ResultFlag> flags = new ArrayList<>();
            if ("id".equals(resultChild.getName())) {
                flags.add(ResultFlag.ID);
            }
            // 构建 ResultMapping
            resultMappings.add(buildResultMappingFromContext(resultChild, typeClass, flags));
        }

        // 创建结果映射解析器
        ResultMapResolver resultMapResolver = new ResultMapResolver(builderAssistant, id, typeClass, resultMappings);
        return resultMapResolver.resolve();
    }

    /**
     * <id column="id" property="id"/>
     * <result column="activity_id" property="activityId"/>
     */
    private ResultMapping buildResultMappingFromContext(Element context, Class<?> resultType, List<ResultFlag> flags) throws Exception {
        String property = context.attributeValue("property");
        String column = context.attributeValue("column");
        return builderAssistant.buildResultMapping(resultType, property, column, flags);
    }

}
