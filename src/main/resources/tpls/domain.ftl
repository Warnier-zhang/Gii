package ${packageName};

<#list imports as import>
import ${import};
</#list>
<#if imports?? && (imports?size > 0)>

</#if>
<#if options.enableComment == "1">
/**
 * 表“${tableName}” 的DOMAIN类。
 */
</#if>
public class ${className} {
<#list properties as property>
    <#if options.enableComment == "1">
    /**
     * ${property.comment}
     */
    </#if>
    <#if options.addAnnotation == "1">
    @Getter
    @Setter
    </#if>
    private ${property.type} ${property.name};
    <#if property.type == "byte[]" && options.convertBinary == "1">

    <#if options.enableComment == "1">
    /**
     * 把${property.name}的BLOB值转换成字符串
     */
    </#if>
    private String string${property.getter?substring(3)};
    </#if>
    <#if property_has_next>

    </#if>
</#list>
    <#if options.addAnnotation == "0">

    /**
     * Getters 和 Setters。
     */
    </#if>
    <#list properties as property>
    <#if options.addAnnotation == "0">
    <#if options.convertBinary == "1" && property.type == "byte[]">
    @JSON(serialize = false)
    </#if>
    public ${property.type} ${property.getter}(){
        return ${property.name};
    }

    public void ${property.setter}(${property.type} ${property.name}){
        this.${property.name} = ${property.name};
    }
    </#if>
    <#if property.type == "byte[]" && options.convertBinary == "1">

    public String getString${property.getter?substring(3)}() {
        String ${property.name} = null;
        try {
        ${property.name} = string${property.getter?substring(3)} != null ? string${property.getter?substring(3)} : (this.${property.name} != null ? new String(this.${property.name}, "UTF-8") : null);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return ${property.name};
    }

    public void setString${property.setter?substring(3)}(String string${property.setter?substring(3)}) {
        try {
            this.${property.name} = string${property.setter?substring(3)}.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        this.string${property.setter?substring(3)} = string${property.setter?substring(3)};
    }
    </#if>
    <#if options.addAnnotation == "0">
    <#if property_has_next>

    </#if>
    </#if>
    </#list>
}