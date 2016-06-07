package com.meetu.console.domain;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

public class EasyUIResult {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private Long total;

    private List<?> rows;

    public EasyUIResult() {
        super();
    }

    public EasyUIResult(Long total, List<?> rows) {
        super();
        this.total = total;
        this.rows = rows;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<?> getRows() {
        return rows;
    }

    public void setRows(List<?> rows) {
        this.rows = rows;
    }

    /**
     * 把json格式的数据反序列化为EasyUIResult对象
     * 
     * @param jsonData json格式的数据
     * @param clazz 结果集中的元素的类
     * @return
     */
    public static EasyUIResult formatJsonToEasyUIResult(String jsonData, Class<?> clazz) {
        // 判断jsonData不为空
        if (StringUtils.isNotBlank(jsonData)) {
            try {
                // 转换jsonNode，方便解析
                JsonNode jsonNode = MAPPER.readTree(jsonData);
                // 获取数据总条数
                Long total = jsonNode.get("total").asLong();

                // 解析rows
                // 获取arrayNode，强转
                ArrayNode arrayNode = (ArrayNode) jsonNode.get("rows");

                // 获取rows
                List<?> list = null;
                if (arrayNode.isArray()) {
                    // 使用反序列化方法获取集合
                    list = MAPPER.readValue(arrayNode.traverse(), MAPPER.getTypeFactory()
                            .constructCollectionType(List.class, clazz));
                }

                // 创建EasyUIResult对象
                EasyUIResult easyUIResult = new EasyUIResult(total, list);

                return easyUIResult;
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        // 如果有问题就返回null
        return null;
    }

}
