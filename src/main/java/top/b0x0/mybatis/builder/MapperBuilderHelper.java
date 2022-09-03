package top.b0x0.mybatis.builder;

import top.b0x0.mybatis.mapping.*;
import top.b0x0.mybatis.session.Configuration;
import top.b0x0.mybatis.type.ResultFlag;
import top.b0x0.mybatis.type.TypeHandler;

import javax.crypto.KeyGenerator;
import java.util.ArrayList;
import java.util.List;

/**
 * 映射构建器助手
 *
 * @author tlh Created By 2022-07-31 18:01
 **/
public class MapperBuilderHelper extends BaseBuilder {

    private String namespace;
    private String resource;

    public MapperBuilderHelper(Configuration configuration, String resource) {
        super(configuration);
        this.resource = resource;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public ResultMapping buildResultMapping(Class<?> resultType, String property, String column, List<ResultFlag> flags) {

        Class<?> javaTypeClass = resolveResultJavaType(resultType, property, null);
        TypeHandler<?> typeHandlerInstance = resolveTypeHandler(javaTypeClass, null);

        ResultMapping.Builder builder = new ResultMapping.Builder(configuration, property, column, javaTypeClass);
        builder.typeHandler(typeHandlerInstance);
        builder.flags(flags);

        return builder.build();

    }

    private Class<?> resolveResultJavaType(Class<?> resultType, String property, Class<?> javaType) {
        if (javaType == null && property != null) {
            try {
                // TODO: 2022/7/31 resolveResultJavaType
//                MetaClass metaResultType = MetaClass.forClass(resultType);
//                javaType = metaResultType.getSetterType(property);
                return javaType;
            } catch (Exception ignore) {
            }
        }
        if (javaType == null) {
            javaType = Object.class;
        }
        return javaType;
    }


    public String applyCurrentNamespace(String base, boolean isReference) {
        if (base == null) {
            return null;
        }
        if (isReference) {
            if (base.contains(".")) {
                return base;
            }
        } else {
            if (base.startsWith(namespace + ".")) {
                return base;
            }
            if (base.contains(".")) {
                throw new RuntimeException("Dots are not allowed in element names, please remove it from " + base);
            }
        }
        return namespace + "." + base;
    }

    public ResultMap addResultMap(String id, Class<?> type, List<ResultMapping> resultMappings) {
        // 补全ID全路径，如：top.b0x0.mybatis.test.dao.UserMapper + userMap
        id = applyCurrentNamespace(id, false);

        ResultMap resultMap = new ResultMap.Builder(configuration, id, type, resultMappings).build();
        configuration.addResultMap(resultMap);
        return resultMap;
    }

    private <T> T valueOrDefault(T value, T defaultValue) {
        return value == null ? defaultValue : value;
    }

}
