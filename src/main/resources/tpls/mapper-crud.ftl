<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${packageName}.${className}">
    <!-- 查询一条记录 -->
    <select id="getOne" resultType="${domainClassName}">
        SELECT * FROM ${tableName} WHERE ${tableName}.${columns[0]} = ${r"#{"}id, jdbcType=${properties[0].jdbcType}}
    </select>

    <!-- 查询所有记录 -->
    <select id="getAll" resultType="${domainClassName}">
        SELECT * FROM ${tableName}
    </select>

    <!-- 增加 -->
    <insert id="insert">
        INSERT INTO ${tableName}
        (
<#list columns as column>
    <#if column_has_next>
        ${column},
    <#else>
        ${column}
    </#if>
</#list>
        )
        VALUES
        (
<#list properties as property>
    <#if property_has_next>
        ${r"#{"}${property.name}, jdbcType=${property.jdbcType}},
    <#else>
        ${r"#{"}${property.name}, jdbcType=${property.jdbcType}}
    </#if>
</#list>
        )
    </insert>

    <!-- 删除 -->
    <delete id="delete">
        DELETE FROM ${tableName} WHERE ${tableName}.${columns[0]} = ${r"#{"}id, jdbcType=${properties[0].jdbcType}}
    </delete>

    <!-- 更新 -->
    <update id="update">
        UPDATE ${tableName}
        <set>
    <#list columns as column>
        <#if column_has_next>
            ${column}=${r"#{"}${properties[column_index].name}, jdbcType=${properties[column_index].jdbcType}},
        <#else>
            ${column}=${r"#{"}${properties[column_index].name}, jdbcType=${properties[column_index].jdbcType}}
        </#if>
    </#list>
        </set>
        WHERE ${tableName}.${columns[0]} =${r"#{"}${properties[0].name}, jdbcType=${properties[0].jdbcType}}
    </update>
</mapper>