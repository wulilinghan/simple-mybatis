package top.b0x0.mybatis.builder;


import top.b0x0.mybatis.mapping.ResultMap;
import top.b0x0.mybatis.mapping.ResultMapping;

import java.util.List;

/**
 * 结果映射解析器
 *
 * @author tlh Created By 2022-07-31 19:00
 **/
public class ResultMapResolver {

    private final MapperBuilderHelper assistant;
    private String id;
    private Class<?> type;
    private List<ResultMapping> resultMappings;

    public ResultMapResolver(MapperBuilderHelper assistant, String id, Class<?> type, List<ResultMapping> resultMappings) {
        this.assistant = assistant;
        this.id = id;
        this.type = type;
        this.resultMappings = resultMappings;
    }

    public ResultMap resolve() {
        return assistant.addResultMap(this.id, this.type, this.resultMappings);
    }

}
