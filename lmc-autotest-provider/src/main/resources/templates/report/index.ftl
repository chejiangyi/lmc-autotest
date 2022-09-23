<#import "../_layout.ftl" as layout>
${Html.s("pagetitle","报表查询")}
<@layout._layout>
<div class="head">
    <div class="title">
    ${pagetitle}
    </div>
</div>
<div class="orderlist m10 myorder">
    <form action="/report/index/" method="post" id="searchForm">
        <div class="search">
            <label>报告名</label>
            <input type="text" class="text mtext" name="report_name" value="${report_name!}" />
            <label>任务名</label>
            <input type="text" class="text mtext" name="task_name" value="${task_name!}" />
            <label>创建时间</label>
            <input type="text" class="text midtext" style="width:150px;" name="create_time_from" value="${create_time_from!}" /> 至 <input type="text" class="text midtext" style="width:150px;" name="create_time_to" value="${create_time_to!}" />
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
<table class="mytable" width="50%">
    <tr>
        <th style="width:3%">Id</th>
        <th style="width:20%">报表名</th>
        <th style="width:20%">关联任务</th>
        <th style="width:15%">创建时间</th>
        <th style="width:10%">操作</th>
    </tr>
    <#list model as item>
        <tr data-id="${item.id}">
            <td>${item.id}</td>
            <td>${item.report_name}</td>
            <td>${item.task_name}</td>
            <td>${Html.p(item.create_time)}</td>
            <td>
                <a href="/report/view/?id=${item.id}" class="del">查看</a>  <a href="javascript:del(${item.id})" class="del">删除</a>
            </td>
        </tr>
    </#list>
</table>
<div class="total pt10">
    <@layout._pager/>
</div>
    <script type="text/javascript">
        function del(id) {
            if(!confirm("请确认报表不再使用,删除后不可恢复？"))
            {
                return;
            }
            $.ajax({
                url: '/report/del/',
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