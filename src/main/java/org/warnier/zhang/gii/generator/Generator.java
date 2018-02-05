package org.warnier.zhang.gii.generator;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.warnier.zhang.gii.Config;
import org.warnier.zhang.gii.service.SchemaService;
import org.warnier.zhang.gii.util.PropertiesLoader;
import org.warnier.zhang.gii.util.StringUtils;

import javax.annotation.PostConstruct;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.*;

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
        cfgs = PropertiesLoader.load(Config.CFG_NAME);
    }

    /**
     * 生成源文件。Generator子类必须重写该方法！
     */
    public abstract void generate();

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
            } else if (property.get("type").equals("byte[]")) {
                if (options.containsKey("convertBinary") && options.get("convertBinary").equals("1")) {
                    if (!imports.contains("org.apache.struts2.json.annotations.JSON")) {
                        imports.add("org.apache.struts2.json.annotations.JSON");
                    }
                    if (!imports.contains("java.io.UnsupportedEncodingException")) {
                        imports.add("java.io.UnsupportedEncodingException");
                    }
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

        // 表名；
        String tableName;

        // 字段名称；
        String columnName;

        // 字段类型；
        String columnType;

        // 小数位数；
        int columnScale;

        // 字段注释；
        String columnComment;

        // 数据库提供商；
        String databaseId;

        // 类型转换器；
        TypeResolver typeResolver = TypeResolver.getTypeResolver();
        for (Map<String, Object> column : columns) {
            tableName = String.valueOf(column.get("tableName"));
            columnName = String.valueOf(column.get("columnName"));
            columnType = String.valueOf(column.get("columnType"));
            columnScale = 0;
            if (column.get("columnScale") != null && !StringUtils.isEmpty(String.valueOf(column.get("columnScale")))) {
                columnScale = Integer.parseInt(String.valueOf(column.get("columnScale")));
            } else {
                // 可能存在NUMBER类型数据没有设置小数位！
                columnScale = -1;
            }
            columnComment = String.valueOf(column.get("columnComment"));
            databaseId = String.valueOf(column.get("databaseId"));

            // 构建属性；
            Map<String, String> property = new HashMap<String, String>();
            property.put("tableName", tableName);
            property.put("name", StringUtils.toCamelCase(columnName, "_"));
            property.put("type", typeResolver.parseJavaType(databaseId, columnType, columnScale));
            property.put("jdbcType", typeResolver.parseJdbcType(databaseId, columnType, columnScale));
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
            cfg.setClassForTemplateLoading(getClass(), Config.TPL_PATH);
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
        } else {
            this.imports = null;
            this.className = null;
            this.properties = null;
            this.tableName = null;
            this.columns = null;
            this.fileWrappers = null;
            this.action = null;
            this.results = null;
        }
    }
}
