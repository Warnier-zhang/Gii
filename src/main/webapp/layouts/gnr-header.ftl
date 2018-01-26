<#include "header.ftl"/>
<div class="row">
    <div class="col-md-3 col-sm-4">
        <div class="list-group">
            <#list generators as generator>
            <#if id = generator.id>
            <a class="list-group-item active" href="gii!renderGenerator?id=${generator.id}">
            <#else>
            <a class="list-group-item" href="gii!renderGenerator?id=${generator.id}">
            </#if>
                <i class="glyphicon glyphicon-chevron-right"></i>${generator.name}生成器
            </a>
            </#list>
        </div>
    </div>
    <div class="col-md-9 col-sm-8">



