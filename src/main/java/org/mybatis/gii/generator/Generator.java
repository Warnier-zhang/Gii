package org.mybatis.gii.generator;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.Getter;
import lombok.Setter;
import org.mybatis.gii.service.SchemaService;
import org.mybatis.gii.util.PropertiesLoader;
import org.mybatis.gii.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.*;

import static org.mybatis.gii.Config.*;

/**
 * 生成器基类。
 */
public abstract class Generator {
    /**
     * 数据字典服务；
     */
    @Autowired
    protected SchemaService schemaService;

    /**
     * 本身相关：唯一标识；
     */
    @Getter
    @Setter
    protected String id;

    /**
     * 本身相关：名称；
     */
    @Getter
    @Setter
    protected String name;

    /**
     * 本身相关：描述；
     */
    @Getter
    @Setter
    protected String hint;

    /**
     * 本身相关：页面；
     */
    @Getter
    @Setter
    protected String page;

    /**
     * 对象相关：包名；
     */
    @Getter
    @Setter
    protected String packageName;

    /**
     * 对象相关：依赖；
     */
    @Getter
    @Setter
    protected List<String> imports;

    /**
     * 对象相关：类名；
     */
    @Getter
    @Setter
    protected String className;

    /**
     * 对象相关：属性；
     */
    @Getter
    @Setter
    protected List<Map<String, String>> properties;

    /**
     * 数据相关：表名；
     */
    @Getter
    @Setter
    protected String tableName;

    /**
     * 数据相关：字段；
     */
    @Getter
    @Setter
    protected List<String> columns;

    /**
     * 文件相关：模板；
     */
    @Getter
    @Setter
    protected String template;

    /**
     * 文件相关：存储路径；
     */
    @Getter
    @Setter
    protected String absoluteURL;

    /**
     * 文件相关：过滤条件；
     */
    @Getter
    @Setter
    protected Map<String, String> options;

    /**
     * 文件相关：文件容器；
     */
    @Getter
    @Setter
    protected List<FileWrapper> fileWrappers;

    /**
     * 消息相关：生成结果；
     */
    @Getter
    @Setter
    protected List<Map<String, String>> results;

    /**
     * 配置相关：默认参数；
     */
    protected Properties cfgs;

    /**
     * 请求相关：动作标识；
     */
    @Getter
    @Setter
    protected String action;

    /**
     * 初始化。
     */
    @PostConstruct
    protected void init() {
        // 加载配置文件、预定义值；
        cfgs = PropertiesLoader.load(CFG_NAME);
    }

    /**
     * 生成源文件。
     */
    public void generate() {
        // 检查表名或者类名是否为空；
        if (StringUtils.isEmpty(tableName) || StringUtils.isEmpty(className)) {
            results = new ArrayList<Map<String, String>>();
            Map<String, String> result = new HashMap<String, String>();
            result.put("code", "0");
            if (StringUtils.isEmpty(tableName)) {
                result.put("info", "没有选择表，请重试！");
            } else {
                result.put("info", "类名为空值，请重试！");
            }
            results.add(result);
            return;
        }

        // 获取表中的字段；
        List<Map<String, Object>> columns = schemaService.getTableColumns(tableName);
        if (columns == null || columns.size() == 0) {
            results = new ArrayList<Map<String, String>>();
            Map<String, String> result = new HashMap<String, String>();
            result.put("code", "0");
            result.put("info", "表中没有字段，选择的表无效，请重试！");
            results.add(result);
            return;
        }

        this.columns = buildColumns(columns);
        this.properties = buildProperties(columns);
        this.imports = buildImports(this.options, this.properties);

        // 封装源文件；
        this.fileWrappers = wrapFiles();
    }

    /**
     * 封装源文件。Generator子类必须重写此方法！
     *
     * @return
     */
    protected abstract List<FileWrapper> wrapFiles();

    /**
     * 转换表中字段。
     *
     * @param columns 字段集合；
     * @return
     */
    protected List<String> buildColumns(List<Map<String, Object>> columns) {
        List<String> columnNames = new ArrayList<String>();
        for (Map<String, Object> column : columns) {
            columnNames.add(String.valueOf(column.get("columnName")));
        }
        return columnNames;
    }

    /**
     * 转换依赖。
     *
     * @param options    默认参数；
     * @param properties 对象属性；
     * @return
     */
    protected List<String> buildImports(Map<String, String> options, List<Map<String, String>> properties) {
        List<String> imports = new ArrayList<String>();

        // 检查是否使用注解；
        if (options.containsKey("addAnnotation") && options.get("addAnnotation").equals("1")) {
            if (!imports.contains("lombok.Getter")) {
                imports.add("lombok.Getter");
            }
            if (!imports.contains("lombok.Setter")) {
                imports.add("lombok.Setter");
            }
        }
        // 遍历对象属性类型；
        for (Map<String, String> property : properties) {
            if (property.get("type").equals("Date")) {
                if (!imports.contains("java.util.Date")) {
                    imports.add("java.util.Date");
                }
            }
        }
        return imports;
    }

    /**
     * 转换对象属性。
     *
     * @param columns 字段集合；
     * @return
     */
    protected List<Map<String, String>> buildProperties(List<Map<String, Object>> columns) {
        List<Map<String, String>> properties = new ArrayList<Map<String, String>>();

        // 表名
        String tableName;

        // 字段名称；
        String columnName;

        // 字段类型；
        String columnType;

        // 字段精度；
        String columnScale;

        // 字段注释；
        String columnComment;
        for (Map<String, Object> column : columns) {
            tableName = String.valueOf(column.get("tableName"));
            columnName = String.valueOf(column.get("columnName"));
            columnType = String.valueOf(column.get("dataType"));
            columnScale = String.valueOf(column.get("dataScale"));
            columnComment = String.valueOf(column.get("comments"));

            // 构建属性；
            Map<String, String> property = new HashMap<String, String>();
            property.put("tableName", tableName);
            property.put("name", StringUtils.toCamelCase(columnName, "_"));
            property.put("type", parseJavaType(columnType, columnScale));
            property.put("jdbcType", parseJdbcType(columnType));
            property.put("getter", "get" + StringUtils.toPascalCase(columnName, "_"));
            property.put("setter", "set" + StringUtils.toPascalCase(columnName, "_"));
            property.put("comment", columnComment);
            properties.add(property);
        }
        return properties;
    }

    /**
     * 转换默认参数。
     *
     * @param keys 参数名称；
     * @return
     */
    protected Map<String, String> buildOptions(String[] keys) {
        Map<String, String> options = new HashMap<String, String>();
        for (String key : keys) {
            String field = key.substring(key.lastIndexOf(".") + 1);
            String value = cfgs.getProperty(key);
            options.put(field, value);
        }
        return options;
    }

    /**
     * 解析Java属性类型。
     *
     * @param columnType 字段类型；
     * @param scale      字段精度；
     * @return
     */
    protected String parseJavaType(String columnType, String scale) {
        if (columnType.equals("NUMBER")) {
            if (StringUtils.isEmpty(scale) || scale.equals("0")) {
                scale = "0";
            } else {
                scale = "1";
            }
        } else {
            scale = "0";
        }
        return JAVATYPES.containsKey(columnType + "-" + scale) ? JAVATYPES.get(columnType + "-" + scale) : "String";
    }

    /**
     * 解析MyBatis 3 JDBC类型。
     *
     * @param columnType 字段类型；
     * @return
     */
    protected String parseJdbcType(String columnType) {
        return JDBCTYPES.containsKey(columnType) ? JDBCTYPES.get(columnType) : "VARCHAR";
    }

    /**
     * 渲染模板文件，返回文本内容。
     *
     * @param templateName 模板名称；
     * @param root         数据模型；
     * @return
     */
    protected String renderText(String templateName, Map<String, Object> root) {
        String text;
        Configuration cfg = new Configuration();
        try {
            cfg.setClassForTemplateLoading(getClass(), TPL_PATH);
            Template tpl = cfg.getTemplate(templateName);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            tpl.process(root, new OutputStreamWriter(out));
            text = new String(out.toByteArray());
            out.flush();
            out.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TemplateException e) {
            throw new RuntimeException(e);
        }
        return text;
    }

    /**
     * 清空用户输入。
     */
    public void clear() {
        if (this.action != null) {
            if (this.action.equals("generate")) {
                this.imports = null;
                this.className = null;
                this.properties = null;
                this.tableName = null;
                this.columns = null;
                this.fileWrappers = null;
                this.action = null;
            } else if (this.action.equals("preview")) {
                this.results = null;
            }
        }
    }
}
