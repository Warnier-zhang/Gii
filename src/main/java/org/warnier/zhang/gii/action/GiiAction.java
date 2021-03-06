package org.warnier.zhang.gii.action;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;
import lombok.Getter;
import lombok.Setter;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.*;
import org.warnier.zhang.gii.generator.DomainGenerator;
import org.warnier.zhang.gii.generator.FileWrapper;
import org.warnier.zhang.gii.generator.Generator;
import org.warnier.zhang.gii.generator.MapperGenerator;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ParentPackage("struts-default")
@Namespace("/")
@Action("gii")
@ResultPath("/static")
@Results({
        @Result(name = "indexPage", type = "freemarker", location = "index.ftl"),
        @Result(name = "domainPage", type = "freemarker", location = "modules/domain/domain.ftl"),
        @Result(name = "mapperPage", type = "freemarker", location = "modules/mapper/mapper.ftl"),
})
public class GiiAction extends ActionSupport implements Preparable {
    @Getter
    @Setter
    private String id;

    @Getter
    @Setter
    private Generator generator;

    @Getter
    @Setter
    private List<Generator> generators;

    /**
     * 初始化Action类。
     *
     * @throws Exception
     */
    public void prepare() throws Exception {
        // 添加系统支持生成器；
        generators = new ArrayList<Generator>();
        generators.add(DomainGenerator.getInstance());
        generators.add(MapperGenerator.getInstance());

        // 无法支持ModelDriven策略！手动解析“id”请求参数，实例化Generator类；
        String id = getParameter("id");
        generator = build(id);
    }

    /**
     * 实例化生成器。
     *
     * @param id 唯一标识；
     * @return
     */
    private Generator build(String id) {
        Generator generator = null;
        if (DomainGenerator.GNR_DOMAIN_PKID.equals(id)) {
            generator = DomainGenerator.getInstance();
        } else if (MapperGenerator.GNR_MAPPER_PKID.equals(id)) {
            generator = MapperGenerator.getInstance();
        }
        return generator;
    }

    /**
     * 解析HTTP请求请求参数。
     *
     * @param name 参数名称；
     * @return
     */
    private String getParameter(String name) {
        HttpServletRequest request = ServletActionContext.getRequest();
        return request.getParameter(name);
    }

    /**
     * 显示首页。
     *
     * @return
     */
    public String renderIndexPage() {
        return "indexPage";
    }

    /**
     * 显示生成器页面。
     *
     * @return
     */
    public String renderGenerator() {
        // 检查是否是“预览”或者“生成”；
        if (generator.getAction() != null) {

            // 生成源文件；
            generator.generate();

            // 检查是否是“生成”；
            if (generator.getAction().equals("generate")) {

                // 保存源文件；
                List<Map<String, String>> results = new ArrayList<Map<String, String>>();
                for (FileWrapper fileWrapper : generator.getFileWrappers()) {
                    results.add(fileWrapper.renderFile());
                }
                generator.setResults(results);
            }
        }

        // 清空用户输入；
        generator.clear();
        return generator.getPage();
    }

    /**
     * 预览源码文件。
     *
     * @return
     */
    public String viewFile() {
        return null;
    }

    /**
     * 比较源码文件。
     *
     * @return
     */
    public String showDiff() {
        return null;
    }

    /**
     * 生成源码文件。
     *
     * @return
     */
    public String generate() {
        return null;
    }
}
