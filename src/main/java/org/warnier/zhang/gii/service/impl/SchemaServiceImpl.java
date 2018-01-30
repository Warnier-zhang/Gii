package org.warnier.zhang.gii.service.impl;

import org.warnier.zhang.gii.mapper.SchemaMapper;
import org.warnier.zhang.gii.service.SchemaService;
import org.warnier.zhang.gii.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SchemaServiceImpl implements SchemaService {
    @Autowired
    private SchemaMapper schemaMapper;

    /**
     * 获得可选的表名。
     *
     * @return
     */
    public List<Map<String, String>> getTableNames() {
        List<Map<String, String>> tableNames = null;

        List<Map<String, Object>> tables = StringUtils.toCamelCase(schemaMapper.getTables());
        if (tables != null && tables.size() > 0) {
            tableNames = new ArrayList<Map<String, String>>();

            for (Map<String, Object> table : tables) {
                Map<String, String> tableName = new HashMap<String, String>();
                tableName.put("tableName", String.valueOf(table.get("tableName")));
                tableName.put("className", StringUtils.toPascalCase(String.valueOf(table.get("tableName")), "_"));
                tableNames.add(tableName);
            }
        }
        return tableNames;
    }

    /**
     * 查询表中的字段。
     */
    public List<Map<String, Object>> getTableColumns(String tableName) {
        return StringUtils.toCamelCase(schemaMapper.getTableColumns(tableName));
    }
}
