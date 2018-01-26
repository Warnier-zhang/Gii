package org.mybatis.gii.service;

import java.util.List;
import java.util.Map;

/**
 * 数据字典服务。
 */
public interface SchemaService {
    /**
     * 获得可选的表名。
     *
     * @return
     */
    List<Map<String, String>> getTableNames();

    /**
     * 查询表中的字段。
     */
    List<Map<String, Object>> getTableColumns(String tableName);
}
