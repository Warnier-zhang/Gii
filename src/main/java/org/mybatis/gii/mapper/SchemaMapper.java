package org.mybatis.gii.mapper;

import java.util.List;
import java.util.Map;

public interface SchemaMapper {
    /**
     * 查询当前用户下的所有表及其注释。
     *
     * @return
     */
    List<Map<String, Object>> getTables();

    /**
     * 查询某个表下的所有字段及其注释。
     * @param tableName
     * @return
     */
    List<Map<String, Object>> getTableColumns(String tableName);
}
