package ${packageName};

<#if options.extendsMapper == "0">
import java.util.List;
</#if>
import ${domainPackageName}.${domainClassName};

/**
 * 表“${tableName}” 的MAPPER接口。
 */
public interface ${className} <#if options.extendsMapper == "1">extends BaseMapper<${domainClassName}></#if> {
<#if options.extendsMapper == "0">
    /**
     * 查询一条记录。
     *
     * @param ${domainInstance}
     * @return
     */
    ${domainClassName} queryOne(${domainClassName} ${domainInstance});

    /**
     * 查询所有记录。
     *
     * @return
     */
    List<${domainClassName}> queryAll();

    /**
     * 增加。
     *
     * @param ${domainInstance}
     */
    void insert(${domainClassName} ${domainInstance});

    /**
     * 删除。
     *
     * @param ${domainInstance}
     */
    void delete(${domainClassName} ${domainInstance});

    /**
     * 更新。
     *
     * @param ${domainInstance}
     */
    void update(${domainClassName} ${domainInstance});
</#if>
}
