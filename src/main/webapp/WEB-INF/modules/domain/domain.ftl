<#include "../../../layouts/gnr-header.ftl"/>
<div class="default-view">
    <h1>${generator.name}生成器</h1>
    <p>${generator.hint}</p>
    <form id="domain-generator" action="base!renderGenerator?id=${generator.id!''}" method="post"
          onsubmit="return promptToPreview();">
        <div class="row">
            <div class="col-lg-8 col-md-10">
                <div class="form-group">
                    <label for="tableName">表名</label>
                    <input type="text" id="tableName" name="generator.tableName"
                           class="form-control typeahead domain-widget required" widgetName="表名" widgetType="3"
                           autocomplete="off" value="${generator.tableName!''}"
                           spellcheck="false" onchange="getClassName();"/>
                </div>
                <div class="form-group">
                    <label for="className">类名</label>
                    <input type="text" id="className" name="generator.className"
                           class="form-control domain-widget required"
                           widgetName="类名" widgetType="1" value="${generator.className!''}"/>
                </div>
                <div class="form-group">
                    <label for="packageName">包名</label>
                    <input type="text" id="packageName" class="form-control domain-widget required"
                           name="generator.packageName"
                           value="${generator.packageName!''}" widgetName="包名" widgetType="1">
                </div>
                <div class="form-group">
                    <label>
                        <input type="hidden" name="generator.options.addAnnotation"/>
                        <#if generator.options.addAnnotation == "1">
                            <input type="checkbox" class="domain-widget required"
                                   id="addAnnotation" name="addAnnotation"
                                   value="1" widgetType="2" widgetFlag="addAnnotation" checked="checked"/>是否使用注解（Getter
                            和 Setter）
                        <#else>
                            <input type="checkbox" class="domain-widget required"
                                   id="addAnnotation" name="addAnnotation"
                                   value="1" widgetType="2" widgetFlag="addAnnotation"/>是否使用注解（Getter 和 Setter）
                        </#if>
                    </label>
                </div>
                <div class="form-group">
                    <label>
                        <input type="hidden" name="generator.options.enableComment"/>
                        <#if generator.options.enableComment == "1">
                            <input type="checkbox" class="domain-widget required"
                                   id="enableComment" name="enableComment"
                                   value="1" widgetType="2" widgetFlag="enableComment" checked="checked"/>是否添加注释
                        <#else>
                            <input type="checkbox" class="domain-widget required"
                                   id="enableComment" name="enableComment"
                                   value="1" widgetType="2" widgetFlag="enableComment"/>是否添加注释
                        </#if>
                    </label>
                </div>
                <div class="form-group">
                    <button type="submit" class="btn btn-primary" name="generator.action" value="preview">
                        预览
                    </button>
                    <#if generator.fileWrappers??>
                    <button type="submit" class="btn btn-success" name="generator.action" value="generate">
                        生成
                    </button>
                    </#if>
                </div>
            </div>
        </div>
        <!-- 显示生成结果 -->
        <#if generator.results??>
            <div class="default-view-results">
                <#list generator.results as result>
                    <#if result.code == "1">
                        <div class="alert alert-success">生成成功！</div>
                    <#else>
                        <div class="alert alert-danger">生成失败！请查看详细的错误日志！</div>
                    </#if>
                    <pre>${result.info!""}</pre>
                </#list>
            </div>
        <!-- 显示预览结果 -->
        <#elseif generator.fileWrappers??>
            <div class="default-view-files">
                <p>单击上面的<code>生成</code>按钮来保存选中的文件：</p>
                <div class="row form-group">
                    <div class="col-xs-6">
                        <input id="filter-input" class="form-control" placeholder="过滤">
                    </div>
                    <div class="col-xs-6 text-right">
                        <div id="action-toggle" class="btn-group btn-group-xs"
                        ">
                        <label class="btn btn-success active">
                            <input type="checkbox" value="新建" checked> 新建
                        </label>
                        <label class="btn btn-default active">
                            <input type="checkbox" value="跳过" checked> 跳过
                        </label>
                        <label class="btn btn-warning active">
                            <input type="checkbox" value="替换" checked> 替换
                        </label>
                    </div>
                </div>
            </div>
                <table class="table table-bordered table-striped table-condensed">
                    <thead>
                    <tr>
                        <th><input type="checkbox" id="check-all"></th>
                        <th class="file">源文件</th>
                        <th class="action">操作</th>
                    </tr>
                    </thead>
                    <tbody id="files-body">
                    <#list generator.fileWrappers as fileWrapper>
                        <#if fileWrapper.fileFlag == "新建">
                        <tr class="success">
                        <#elseif fileWrapper.fileFlag == "替换">
                        <tr class="warning">
                        <#else>
                        <tr class="active">
                        </#if>
                        <td class="check">
                            <#if fileWrapper.fileFlag == "跳过">
                                &nbsp;
                            <#else>
                                <input type="checkbox" name="fileWrapper.id" value="${fileWrapper.id}"
                                       checked="checked"/>
                            </#if>
                        </td>
                        <td class="file">
                            <a id="${fileWrapper.id}" class="preview-code"
                               href="#">${fileWrapper.filePath}</a>
                        </td>
                        <td class="action">
                            ${fileWrapper.fileFlag}
                        </td>
                    </tr>
                    </#list>
                    </tbody>
                </table>
            </div>
        </#if>
    </form>
</div>
<script type="text/javascript">
    $(function () {
        // 查询可选的表名
        queryTableNames();
    });

    /**
     * 查询可选的表名
     */
    function queryTableNames() {
        $.post("schema!getTableNames", {}, function (data, status) {
            engine = new Bloodhound({
                name: "tableNames",
                limit: 8,
                identify: function (e) {
                    return e.tableName;
                },
                datumTokenizer: function (e) {
                    return Bloodhound.tokenizers.whitespace(e.tableName);
                },
                queryTokenizer: Bloodhound.tokenizers.whitespace,
                local: data
            });

            engine.initialize();

            $("#tableName").typeahead({
                hint: true,
                highlight: true,
                minLength: 1
            }, {
                name: "tableNames",
                displayKey: "tableName",
                source: engine.ttAdapter(),
                limit: 8,
                templates: {
                    empty: "没有相关的匹配项"
                }
            });
        }, "json");
    }

    /**
     * 填充相应的类名
     */
    function getClassName() {
        var tableName = $("#tableName").typeahead("val");
        var className = engine.get([tableName])[0].className;
        $("#className").val(className);
    }

    /**
     * 弹窗提示是否预览文件
     */
    function promptToPreview() {
        var widgets = $(".domain-widget[id][name]");
        var widget;
        for (var i = 0; i < widgets.length; i++) {
            widget = $(widgets[i]);
            var widgetType = widget.attr("widgetType");
            var widgetData;
            var widgetName;
            var widgetFlag;
            if (widgetType == 1) {
                // 文本框
                widgetName = widget.attr("widgetName");
                widgetData = widget.val();
                if (widget.hasClass("required")) {
                    if (!widgetData) {
                        alert("【" + widgetName + "】是必填项，请填写！");
                        return false;
                    }
                }
            } else if (widgetType == 2) {
                // 多选框
                if (widget.is(":checked")) {
                    widgetData = 1;
                } else {
                    widgetData = 0;
                }
                widgetFlag = widget.attr("widgetFlag");
                $("input[type='hidden'][name='generator.options." + widgetFlag + "']").val(widgetData);
            } else if (widgetType == 3) {
                // 自动补全控件
                widgetName = widget.attr("widgetName");
                widgetData = widget.typeahead("val");
                if (widget.hasClass("required")) {
                    if (!widgetData) {
                        alert("【" + widgetName + "】是必填项，请填写！");
                        return false;
                    }
                }
            }
        }
        return true;
    }
</script>
<#include "../../../layouts/gnr-footer.ftl"/>