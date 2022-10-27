﻿<#import "../_layout.ftl" as layout>
${Html.s("pagetitle","定时计划列表")}
<@layout._layout>
    <style>
        .title{
            color: #8b4902;
        }
        .run{
            color: #0ABD0A;
        }
        .stop{
            color: red;
        }
    </style>
<div class="head">
    <div class="title">
    ${pagetitle}
    </div>
</div>
<div class="orderlist m10 myorder">
    <form action="/job/index/" method="post" id="searchForm">
        <div class="search">
            <label>计划名</label><input type="text" class="text midtext" style="width:150px;" name="title" value="${title!}" />
            <label>创建人</label><input type="text" class="text midtext" style="width:150px;" name="create_user" value="${create_user!}" />
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
<div>
    <a href="/job/edit/?id=0" class="btn1" target="_blank">新增</a>
</div>
<table class="mytable" width="100%">
    <tr>
        <th style="width:3%">Id</th>
        <th style="width:10%">计划名</th>
        <th style="width:5%">cron表达式</th>
        <th style="width:3%">运行状态</th>
        <th style="width:7%">创建人</th>
        <th style="width:12%">操作</th>
    </tr>
    <#list model as item>
        <tr data-id="${item.id}">
            <td>${item.id}</td>
            <td>${item.title!}</td>
            <td>${item.corn!}</td>
            <td>${Html.w(item.state=="运行","<b class='run'>运行</b>","<b class='stop'>停止")!}</td>
            <td>${item.create_user}<br/>${Html.p(item.create_time)}</td>
            <td>
                 <#if user.isAdminOrIsUser(item.create_user_id)>
                    <a href="javascript:setRunState(${item.id},'${Html.w(item.state=="停止","运行","停止")}')" class="btn1"> ${Html.w(item.state=="停止","运行","停止")}</a>
                 </#if>
                <a href="/job/edit/?id=${item.id}" class="btn1" target="_blank">${Html.w(user.isAdminOrIsUser(item.create_user_id),"编辑","查看")}</a>
                <a href="/log/index/?message=计划任务id:${item.id}" class="btn1">执行日志</a>
                <#if item.state=="停止" && user.isAdminOrIsUser(item.create_user_id)>
                    <a href="javascript:del(${item.id})" class="del">删除</a>
                </#if>
                <a href="javascript:copy(${item.id})" class="del">复制</a>
            </td>
        </tr>
    </#list>
</table>
<div class="total pt10">
    <@layout._pager/>
</div>
    <script type="text/javascript">
        function setRunState(id,todo){
            $.ajax({
                url: '/job/setState/',
                type: "post",
                data: {
                    id: id,
                    todo:todo
                },
                success: function (data) {
                    if (data.code > 0) {
                        console.log(todo);
                        window.location.reload();
                    }
                    else {
                        alert(data.message);
                        window.location.reload();
                    }
                }
            });
        }
        function del(id) {
            if(!confirm("请确认定时计划不再使用,删除后不可恢复？"))
            {
                return;
            }
            $.ajax({
                url: '/job/del/',
                type: "post",
                data: {
                    id: id
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
        function copy(id) {
            if(!confirm("请确认复制定时计划？"))
            {
                return;
            }
            $.ajax({
                url: '/job/copy/',
                type: "post",
                data: {
                    id: id
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
</#macro>