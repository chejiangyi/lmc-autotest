<#import "../_layout.ftl" as layout>
${Html.s("pagetitle","日志列表")}
<@layout._layout>
    <script type="text/javascript">
        function clear() {
            if(!confirm("请确认清理日志,删除后不可恢复？"))
            {
                return;
            }
            $.ajax({
                url: '/log/clear/',
                type: "post",
                data: {
                },
                success: function (data) {
                    if (data.code > 0) {
                        window.location.reload();
                    }
                    else {
                        alert(data.message);
                    }
                }
            });
        }
    </script>
<div class="head">
    <div class="title">
    ${pagetitle}
    </div>
</div>
<div class="orderlist m10 myorder">
    <form action="/log/index/" method="post" id="searchForm">
        <div class="search">
            <label>节点</label><input type="text" class="text midtext" style="width:150px;" name="node" value="${node!}" />
            <label>级别</label>
            <select id="type" name="type">
                <option value="" ${Html.w(""==type,"selected='selected'","")}></option>
                <#list ["错误","普通"] as item>
                    <option value="${item}" ${Html.w(item==type,"selected='selected'","")}>${item}</option>
                </#list>
            </select>
            <label>创建时间</label><input type="text" class="text midtext" style="width:150px;" name="create_time_from" value="${create_time_from!}" /> 至 <input type="text" class="text midtext" style="width:150px;" name="create_time_to" value="${create_time_to!}" />
            <input type="submit" class="btn1" value="搜索" accesskey="S" />
            <input type="button" class="btn1" value="清空" onclick="clear()" />
        </div>
    </form>
    <div class="tab_cont">
        <div class="List">
            <@_list/>
        </div>
    </div>
</div>
</@layout._layout>
<#macro _list >
<#--<div>-->
<#--    <a href="/task/edit/?id=0" class="btn1" target="_blank">新增</a>-->
<#--</div>-->
<table class="mytable" width="100%">
    <tr>
        <th style="width:3%">Id</th>
        <th style="width:10%">节点</th>
        <th style="width:40%">日志</th>
        <th style="width:7%">级别</th>
        <th style="width:7%">创建时间</th>
    </tr>
    <#list model as item>
        <tr data-id="${item.id}">
            <td>${item.id}</td>
            <td>${item.node}</td>
            <td title="${item.message}">${Html.cutstring(item.message,300)}</td>
            <td>${item.type}</td>
            <td>${Html.p(item.create_time)}</td>
        </tr>
    </#list>
</table>
<div class="total pt10">
    <@layout._pager/>
</div>
</#macro>