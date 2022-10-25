<#import "../_layout.ftl" as layout>
${Html.s("pagetitle","用户列表")}
<@layout._layout>
    <style>
        .title{
            color: #8b4902;
        }
    </style>
<div class="head">
    <div class="title">
    ${pagetitle}
    </div>
</div>
<div class="orderlist m10 myorder">
    <form action="/user/index/" method="post" id="searchForm">
<#--        <div class="search">-->
<#--            <label>任务名</label><input type="text" class="text midtext" style="width:150px;" name="task" value="${task!}" />-->
<#--            <label>创建人</label><input type="text" class="text midtext" style="width:150px;" name="create_user" value="${create_user!}" />-->
<#--            <input type="submit" class="btn1" value="搜索" accesskey="S" />-->
<#--        </div>-->
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
    <a href="/user/edit/?id=0" class="btn1" target="_blank">新增</a>
</div>
<table class="mytable" width="50%">
    <tr>
        <th style="width:3%">Id</th>
        <th style="width:10%">用户名</th>
        <th style="width:3%">角色</th>
        <th style="width:8%">操作</th>
    </tr>
    <#list model as item>
        <tr data-id="${item.id}">
            <td>${item.id}</td>
            <td>${item.name!}</td>
            <td>${Html.w(item.role==1,"管理员","普通用户")!}</td>
            <td>
                <a href="/user/edit/?id=${item.id}" class="btn1" target="_blank">编辑</a>
                <a href="javascript:del(${item.id})" class="del">删除</a>
            </td>
        </tr>
    </#list>
</table>
<div class="total pt10">
    <@layout._pager/>
</div>
    <script type="text/javascript">
        function del(id) {
            if(!confirm("请确认用户不再使用,删除后不可恢复？"))
            {
                return;
            }
            $.ajax({
                url: '/user/del/',
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