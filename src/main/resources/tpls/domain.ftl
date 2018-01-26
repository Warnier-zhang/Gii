package ${packageName};

<#list imports as import>
import ${import};
</#list>
<#if imports?? && (imports?size > 0)>

</#if>
<#if options.enableComment = "1">
/**
 * 表“${tableName}” 的DOMAIN类。
 */
</#if>
public class ${className} {
<#list properties as property>
    <#if options.enableComment = "1">
    /**
     * ${property.comment}
     */
    </#if>
    <#if options.addAnnotation = "1">
    @Getter
    @Setter
    </#if>
    private ${property.type} ${property.name};
    <#if property_has_next>

    </#if>
</#list>
<#if options.addAnnotation = "0">

    /**
     * Getters 和 Setters。
     */
    <#list properties as property>
    public ${property.type} ${property.getter}(){
        return ${property.name};
    }

    public void ${property.setter}(${property.type} ${property.name}){
        this.${property.name} = ${property.name};
    }
    <#if property_has_next>

    </#if>
    </#list>
</#if>
}