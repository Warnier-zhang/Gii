package org.mybatis.gii.generator;

import org.mybatis.gii.util.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Scope(BeanDefinition.SCOPE_SINGLETON)
@Component
public class DomainGenerator extends Generator {
    /**
     * ID；
     */
    public static final String GNR_DOMAIN_PKID = "domain";

    /**
     * 名称；
     */
    public static final String GNR_DOMAIN_NAME = "DOMAIN类";

    /**
     * 描述；
     */
    public static final String GNR_DOMAIN_HINT = "选择当前用户下的某张表，根据表中的字段及其类型、注释来生成DOMAIN类。";

    /**
     * 默认的包名；
     */
    public static final String PKG_DOMAIN_NAME = "org.mybatis.gii.domain";

    /**
     * DOMAIN类生成器实例；
     */
    private static DomainGenerator generator;

    @Override
    protected void init() {
        super.init();

        // 唯一标识；
        this.id = GNR_DOMAIN_PKID;

        // 名称；
        this.name = GNR_DOMAIN_NAME;

        // 描述；
        this.hint = GNR_DOMAIN_HINT;

        // 页面；
        this.page = "domainPage";

        // 包名；
        this.packageName = cfgs.getProperty("gii.pkg.domain.packageName");
        if (StringUtils.isEmpty(packageName)) {
            this.packageName = PKG_DOMAIN_NAME;
        }

        // 模板；
        this.template = "domain.ftl";

        // 存储路径；
        this.absoluteURL = cfgs.getProperty("gii.src.domain.absoluteURL");

        // 解析预定义参数；
        this.options = buildOptions(new String[]{
                // 启用注解；
                "gii.cfg.domain.addAnnotation",
                // 添加注释；
                "gii.cfg.domain.enableComment",
                // 转换BOLB；
                "gii.cfg.domain.convertBinary"
        });

        // 获得IoC容器中的DomainGenerator实例；
        generator = this;
    }

    @Override
    protected List<FileWrapper> wrapFiles() {
        List<FileWrapper> fileWrappers = new ArrayList<FileWrapper>();

        // 构建数据模型；
        Map<String, Object> root = new HashMap<String, Object>();
        root.put("tableName", tableName);
        root.put("columns", columns);
        root.put("packageName", packageName);
        root.put("imports", imports);
        root.put("className", className);
        root.put("properties", properties);
        root.put("options", options);

        // 构建文件参数；
        String fileName = className + ".java";
        String filePath = absoluteURL + File.separator + fileName;
        String fileBody = renderText(template, root);
        fileWrappers.add(new FileWrapper(fileName, filePath, fileBody));
        return fileWrappers;
    }

    /**
     * 返回DomainGenerator实例。
     *
     * @return
     */
    public static Generator getInstance() {
        if (generator != null) {
            return generator;
        }
        throw new RuntimeException("DomainGenerator类实例化失败！");
    }
}
