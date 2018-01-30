package org.warnier.zhang.gii.action;

import com.opensymphony.xwork2.ActionSupport;
import lombok.Getter;
import lombok.Setter;
import org.warnier.zhang.gii.service.SchemaService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

public class SchemaAction extends ActionSupport {
    @Autowired
    private SchemaService schemaService;

    @Getter
    @Setter
    private List<Map<String, String>> allowedTableNames;

    public String getTableNames() {
        // 查询出可选的表名；
        allowedTableNames = schemaService.getTableNames();
        return SUCCESS;
    }
}
