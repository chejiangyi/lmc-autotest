<#import "../_layout.ftl" as layout>
${Html.s("pagetitle","采样查询")}
<@layout._layout>
<div class="head">
    <div class="title">
    ${pagetitle}
    </div>
</div>
<div class="orderlist m10 myorder">
    <form action="/sample/index/" method="post" id="searchForm">
        <div class="search">
            <label>采样表</label>
            <select id="type" name="table">
                <#list tables as item>
                    <option value="${item}" ${Html.w(item==table,"selected='selected'","")}>${item}</option>
                </#list>
            </select>
            <label>Where SQl</label>
            <input type="text" class="text longtext" style="width: 450px" name="sql" value="${sql!}" />${Html.help("普通的sql查询模式,如: url like '%192.168%' and operator_type!='未知'")}
            <input type="submit" class="btn1" value="搜索" accesskey="S" />
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
<table class="mytable" width="100%">
    <tr>
        <th style="width:3%">Id</th>
        <th style="width:80px">app_name<br/>服务名</th>
        <th style="width:100px">formip<br/>来源IP</th>
        <th style="width:10%">url</th>
        <th style="width:50px">method</th>
        <th style="width:40%">header</th>
        <th style="width:30%">body</th>
        <th style="width:100px">create_time<br/>创建时间</th>
        <th style="width:100px">traceid${Html.help("调用链id,涉及到AutoTest的调用链传递")}</th>
        <th style="width:100px">trace_top${Html.help("堆栈顶部,是否是AutoTest的调用链第一个")}</th>
        <th style="width:50px">operator_type${Html.help("操作类型,自动检测程序是否涉及到update,insert,delete之类的操作")}</th>
    </tr>
    <#list model as item>
        <tr data-id="${item.id}">
            <td>${item.id}</td>
            <td>${item.app_name}</td>
            <td>${item.fromip}</td>
            <td  style="word-break: break-all">${item.url}</td>
            <td>${item.method}</td>
            <td style="word-break: break-all" title="${item.header?html}">${Html.cutstring(item.header,300)}</td>
            <td style="word-break: break-all" title="${item.body?html}">${Html.cutstring(item.body,300)}</td>
            <td>${Html.p(item.create_time)}</td>
            <td style="word-break: break-all">${Html.p(item.traceid)}</td>
            <td>${Html.p(item.trace_top)}</td>
            <td>${Html.p(item.operator_type)}</td>
        </tr>
    </#list>
</table>
<div class="total pt10">
    <@layout._pager/>
</div>
</#macro>
<script type="text/javascript">

</script>