<#include "layouts/header.ftl"/>
<div class="default-index">
    <div class="page-header">
        <h1>欢迎使用Gii <small>一个自动生成代码的魔法工具</small></h1>
    </div>
    <p class="lead">开始一段愉快的旅程：</p>
    <div class="row">
        <#list generators as generator>
            <div class="generator col-lg-4">
                <h3>${generator.name}生成器</h3>
                <p>${generator.hint}</p>
                <p><a class="btn btn-default" href="gii!renderGenerator?id=${generator.id}">生成${generator.name}</a></p>
            </div>
        </#list>
    </div>
    <p><a class="btn btn-success" href="#">查看更多的生成器</a></p>
</div>
<#include "layouts/footer.ftl"/>