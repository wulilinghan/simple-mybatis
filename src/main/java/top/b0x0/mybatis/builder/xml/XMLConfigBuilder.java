package top.b0x0.mybatis.builder.xml;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.xml.sax.InputSource;
import top.b0x0.mybatis.builder.BaseBuilder;
import top.b0x0.mybatis.datasource.DataSourceFactory;
import top.b0x0.mybatis.io.Resources;
import top.b0x0.mybatis.mapping.Environment;
import top.b0x0.mybatis.mapping.XMLTag;
import top.b0x0.mybatis.mapping.XMLTagAttribute;
import top.b0x0.mybatis.session.Configuration;
import top.b0x0.mybatis.session.LocalCacheScope;
import top.b0x0.mybatis.transaction.TransactionFactory;

import javax.sql.DataSource;
import java.io.InputStream;
import java.io.Reader;
import java.util.List;
import java.util.Properties;

/**
 * @author tlh Created By 2022-07-31 16:58
 **/
public class XMLConfigBuilder extends BaseBuilder {

    private Element mybatisConfigElement;

    public XMLConfigBuilder(Reader reader) {
        // 1. 调用父类初始化Configuration
        super(new Configuration());
        // 2. dom4j 处理 xml
        SAXReader saxReader = new SAXReader();
        try {
            Document document = saxReader.read(new InputSource(reader));
            mybatisConfigElement = document.getRootElement();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }


    /**
     * 解析配置；类型别名、插件、对象工厂、对象包装工厂、设置、环境、类型转换、映射器
     *
     * @return Configuration
     */
    public Configuration parse() {
        try {
            // 插件
            pluginElement(mybatisConfigElement.element(XMLTag.plugins.name()));
            // 设置
            settingsElement(mybatisConfigElement.element(XMLTag.settings.name()));
            // 环境
            environmentsElement(mybatisConfigElement.element(XMLTag.environments.name()));
            // 解析映射器
            mapperElement(mybatisConfigElement.element(XMLTag.mappers.name()));
        } catch (Exception e) {
            throw new RuntimeException("Error parsing SQL Mapper Configuration. Cause: " + e, e);
        }
        return configuration;
    }

    /**
     * Mybatis 允许你在某一点切入映射语句执行的调度
     * <plugins>
     * <plugin interceptor="top.b0x0.mybatis.test.plugin.TestPlugin">
     * <property name="test00" value="100"/>
     * <property name="test01" value="100"/>
     * </plugin>
     * </plugins>
     */
    private void pluginElement(Element parent) throws Exception {
        if (parent == null) return;
        List<Element> elements = parent.elements();
        for (Element element : elements) {
            String interceptor = element.attributeValue(XMLTagAttribute.interceptor.name());
            // 参数配置
            Properties properties = new Properties();
            List<Element> propertyElementList = element.elements("property");
            for (Element property : propertyElementList) {
                properties.setProperty(property.attributeValue("name"), property.attributeValue("value"));
            }
            // 获取插件实现类并实例化：top.b0x0.mybatis.test.plugin.TestPlugin
//            Interceptor interceptorInstance = (Interceptor) resolveClass(interceptor).newInstance();
//            interceptorInstance.setProperties(properties);
//            configuration.addInterceptor(interceptorInstance);
        }
    }

    /**
     * <pre>
     *
     * <settings>
     *      <!-- 全局缓存：true/false -->
     *      <setting name="cacheEnabled" value="false"/>
     *      <!--缓存级别：SESSION/STATEMENT-->
     *      <setting name="localCacheScope" value="SESSION"/>
     * </settings>
     *
     * </pre>
     */
    private void settingsElement(Element context) {
        if (context == null) {
            return;
        }
        List<Element> elements = context.elements();
        Properties props = new Properties();
        for (Element element : elements) {
            props.setProperty(element.attributeValue("name"), element.attributeValue("value"));
        }
        configuration.setCacheEnabled(booleanValueOf(props.getProperty("cacheEnabled"), true));
        configuration.setLocalCacheScope(LocalCacheScope.valueOf(props.getProperty("localCacheScope")));
    }

    /**
     * <pre>
     *
     * <environments default="development">
     *      <environment id="development">
     *          <environment id="production">transactionManager type="JDBC">
     *              <property name="..." value="..."/>
     *              </transactionManager>
     *                  <dataSource type="POOLED">
     *                      <property name="driver" value="${driver}"/>
     *                      <property name="url" value="${url}"/>
     *                      <property name="username" value="${username}"/>
     *                      <property name="password" value="${password}"/>
     *                  </dataSource>
     *      </environment>
     * </environments>
     *
     * </pre>
     */
    private void environmentsElement(Element context) throws Exception {
        String environment = context.attributeValue("default");

        List<Element> environmentList = context.elements("environment");
        for (Element e : environmentList) {
            String id = e.attributeValue("id");
            if (environment.equals(id)) {
                // 事务管理器
                TransactionFactory txFactory = (TransactionFactory) typeAliasRegistry.resolveAlias(e.element("transactionManager").attributeValue("type")).newInstance();

                // 数据源
                Element dataSourceElement = e.element("dataSource");
                DataSourceFactory dataSourceFactory = (DataSourceFactory) typeAliasRegistry.resolveAlias(dataSourceElement.attributeValue("type")).newInstance();
                List<Element> propertyList = dataSourceElement.elements("property");
                Properties props = new Properties();
                for (Element property : propertyList) {
                    props.setProperty(property.attributeValue("name"), property.attributeValue("value"));
                }
                dataSourceFactory.setProperties(props);
                DataSource dataSource = dataSourceFactory.getDataSource();

                // 构建环境
                Environment.Builder environmentBuilder = new Environment.Builder(id)
                        .transactionFactory(txFactory)
                        .dataSource(dataSource);

                configuration.setEnvironment(environmentBuilder.build());
            }
        }
    }

    /**
     * <pre>
     *
     * <mappers>
     * 	 <mapper resource="org/mybatis/builder/AuthorMapper.xml"/>
     * 	 <mapper resource="org/mybatis/builder/BlogMapper.xml"/>
     * 	 <mapper resource="org/mybatis/builder/PostMapper.xml"/>
     *
     *   <mapper class="top.b0x0.mybatis.test.dao.IUserDao"/>
     * </mappers>
     *
     * </pre>
     */
    private void mapperElement(Element mappers) throws Exception {
        List<Element> mapperList = mappers.elements("mapper");
        for (Element e : mapperList) {
            String resource = e.attributeValue("resource");
            String mapperClass = e.attributeValue("class");
            // XML 解析
            if (resource != null && mapperClass == null) {
                InputStream inputStream = Resources.getResourceAsStream(resource);
                // 在for循环里每个mapper都重新new一个XMLMapperBuilder，来解析
                XMLMapperBuilder mapperParser = new XMLMapperBuilder(inputStream, configuration, resource);
            }
            // 说明使用注解方式写SQL
            // Annotation 注解解析
            else if (resource == null && mapperClass != null) {
                Class<?> mapperInterface = Resources.classForName(mapperClass);
                configuration.addMapper(mapperInterface);
            }

        }
    }

}
