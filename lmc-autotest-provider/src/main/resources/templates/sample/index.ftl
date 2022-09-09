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
            <input type="text" class="text longtext" name="task" value="${sql!}" />
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
        <th style="width:10%">app_name(服务名)</th>
        <th style="width:10%">formip(来源IP)</th>
        <th style="width:10%">url</th>
        <th style="width:5%">method</th>
        <th style="width:30%">header</th>
        <th style="width:40%">body</th>
        <th style="width:7%">创建时间</th>
    </tr>
    <#list model as item>
        <tr data-id="${item.id}">
            <td>${item.id}</td>
            <td>${item.app_name}</td>
            <td>${item.fromip}</td>
            <td>${item.url}</td>
            <td>${item.method}</td>
            <td title="${item.header}">${Html.cutstring(item.header,300)}</td>
            <td title="${item.body}">${Html.cutstring(item.body,300)}</td>
            <td>${Html.p(item.create_time)}</td>
        </tr>
    </#list>
</table>
<div class="total pt10">
    <@layout._pager/>
</div>
</#macro>