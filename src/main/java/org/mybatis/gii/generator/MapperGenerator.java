package org.mybatis.gii.generator;

import lombok.Getter;
import lombok.Setter;
import org.mybatis.gii.util.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mybatis.gii.generator.DomainGenerator.PKG_DOMAIN_NAME;

@Scope(BeanDefinition.SCOPE_SINGLETON)
@Component
public class MapperGenerator extends Generator {
    /**
     * ID；
     */
    public static final String GNR_MAPPER_PKID = "mapper";

    /**
     * 名称；
     */
    public static final String GNR_MAPPER_NAME = "MAPPER类";

    /**
     * 描述；
     */
    public static final String GNR_MAPPER_HINT = "选择当前用户下的某张表，根据表中的字段及其类型、注释来生成含增、删、改、查等CURD操作的MAPPER类。";

    /**
     * 默认的包名；
     */
    public static final String PKG_MAPPER_NAME = "org.mybatis.gii.mapper";

    /**
     * MAPPER类生成器实例；
     */
    private static MapperGenerator generator;

    /**
     * Mapper接口基类模板；
     */
    private String baseTemplate;

    /**
     * Mapper映射文件模板；
     */
    private String crudTemplate;

    /**
     * 对应的DOMAIN类包名；
     */
    @Getter
    @Setter
    private String domainPackageName;

    /**
     * 对应的DOMAIN类类名；
     */
    @Getter
    @Setter
    private String domainClassName;

    /**
     * 对应的DOMAIN类实例；
     */
    @Getter
    @Setter
    private String domainInstance;

    @Override
    protected void init() {
        super.init();
        // 唯一标识；
        this.id = GNR_MAPPER_PKID;

        // 名称；
        this.name = GNR_MAPPER_NAME;

        // 描述；
        this.hint = GNR_MAPPER_HINT;

        // 页面；
        this.page = "mapperPage";

        // 包名；
        this.packageName = cfgs.getProperty("gii.pkg.mapper.packageName");
        if (StringUtils.isEmpty(packageName)) {
            this.packageName = PKG_MAPPER_NAME;
        }
        this.domainPackageName = cfgs.getProperty("gii.pkg.domain.packageName");
        if (StringUtils.isEmpty(domainPackageName)) {
            this.packageName = PKG_DOMAIN_NAME;
        }

        // 模板；
        this.template = "mapper.ftl";
        this.baseTemplate = "mapper-base.ftl";
        this.crudTemplate = "mapper-crud.ftl";

        // 存储路径；
        this.absoluteURL = cfgs.getProperty("gii.src.mapper.absoluteURL");

        // 解析预定义参数；
        this.options = buildOptions(new String[]{
                // 同时生成Mapper映射；
                "gii.cfg.mapper.generateQuery",
                // 扩展Mapper接口基类；
                "gii.cfg.mapper.extendsMapper"
        });

        // 获得IoC容器中的MapperGenerator实例；
        generator = this;
    }

    @Override
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
        // 解析对应的DOMAIN类类名；
        this.domainClassName = className.substring(0, className.indexOf("Mapper"));
        // 解析对应的DOMAIN类实例；
        this.domainInstance = StringUtils.lowerCase(this.domainClassName);

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

        // 构建数据模型；
        Map<String, Object> root = new HashMap<String, Object>();
        root.put("tableName", tableName);
        root.put("columns", this.columns);
        root.put("packageName", packageName);
        root.put("imports", imports);
        root.put("className", className);
        root.put("properties", properties);
        root.put("options", options);
        root.put("domainPackageName", domainPackageName);
        root.put("domainClassName", domainClassName);
        root.put("domainInstance", domainInstance);

        // 构建代码文件；
        fileWrappers = new ArrayList<FileWrapper>();

        String fileName = className + ".java";
        String filePath = absoluteURL + File.separator + fileName;
        String fileBody = renderText(template, root);
        fileWrappers.add(new FileWrapper(fileName, filePath, fileBody));

        // 检查是否扩展Mapper接口基类；
        if (options.containsKey("extendsMapper") && options.get("extendsMapper").equals("1")) {
            fileName = "Mapper.java";
            filePath = absoluteURL + File.separator + fileName;
            fileBody = renderText(baseTemplate, root);
            fileWrappers.add(new FileWrapper(fileName, filePath, fileBody));
        }

        // 检查是否同时生成Mapper映射；
        if(options.containsKey("generateQuery") && options.get("generateQuery").equals("1")){
            fileName = className + ".xml";
            filePath = absoluteURL + File.separator + fileName;
            fileBody = renderText(crudTemplate, root);
            fileWrappers.add(new FileWrapper(fileName, filePath, fileBody));
        }
    }

    /**
     * 返回MapperGenerator实例。
     *
     * @return
     */
    public static Generator getInstance() {
        if (generator != null) {
            return generator;
        }
        throw new RuntimeException("MapperGenerator类实例化失败！");
    }
}
