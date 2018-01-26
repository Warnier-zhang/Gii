package org.mybatis.gii.generator;

import org.mybatis.gii.util.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Scope(BeanDefinition.SCOPE_SINGLETON)
@Component
public class MapperGenerator extends Generator {
    /**
     * ID；
     */
    public static final String GNR_MAPPER_PKID = "MAPPER";

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

        // 模板；
        this.template = "mapper.ftl";

        // 存储路径；
        this.absoluteURL = cfgs.getProperty("gii.src.mapper.absoluteURL");

        // 解析预定义参数；
        this.options = buildOptions(new String[]{
                "gii.cfg.mapper.generateQuery",
                "gii.cfg.mapper.extendsMapper"
        });

        // 获得IoC容器中的MapperGenerator实例；
        generator = this;
    }

    @Override
    protected List<FileWrapper> wrapFiles() {
        return null;
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
