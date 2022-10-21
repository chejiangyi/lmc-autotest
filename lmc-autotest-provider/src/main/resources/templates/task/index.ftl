<#import "../_layout.ftl" as layout>
${Html.s("pagetitle","任务列表")}
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
    <form action="/task/index/" method="post" id="searchForm">
        <div class="search">
            <label>任务名</label><input type="text" class="text midtext" style="width:150px;" name="task" value="${task!}" />
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
    <a href="/task/edit/?id=0" class="btn1" target="_blank">新增</a>
</div>
<table class="mytable" width="100%">
    <tr>
        <th style="width:3%">Id</th>
        <th style="width:10%">任务名</th>
        <th style="width:3%">存储引擎</th>
        <th style="width:3%">运行状态</th>
        <th style="width:7%">创建人</th>
        <th style="width:7%">更新人</th>
        <th style="width:20%">执行结果</th>
        <th style="width:8%">操作</th>
    </tr>
    <#list model as item>
        <tr data-id="${item.id}">
            <td>${item.id}</td>
            <td>${item.task!}</td>
            <td>${item.filter_store!}</td>
            <td>${Utils.printRunState(item.run_heart_time)}</td>
            <td>${item.create_user}<br/>${Html.p(item.create_time)}</td>
            <td>${item.update_user}<br/>${Html.p(item.update_time)}</td>
            <td style="text-align: left">
                <p>${Utils.titleContent("分配节点",item.run_nodes)!}</p>
                <p>${Utils.titleContent("执行结果",Utils.replaceChar(item.exec_result,"\n","<br/>"))!}</p>
            </td>
            <td>
                 <#if user.isAdminOrIsUser(item.create_user_id)>
                    <a href="javascript:setRunState(${item.id},'${Html.w(Utils.showRunState(item.run_heart_time)=="停止","运行","停止")}')" class="btn1"> ${Html.w(Utils.showRunState(item.run_heart_time)=="停止","运行","停止")}</a>
                 </#if>
                <a href="/task/edit/?id=${item.id}" class="btn1" target="_blank">${Html.w(user.isAdminOrIsUser(item.create_user_id),"编辑","查看")}</a>
                <#if Utils.showRunState(item.run_heart_time)=="停止" && user.isAdminOrIsUser(item.create_user_id)>
                    <a href="javascript:del(${item.id})" class="del">删除</a>
                </#if>
                <a href="javascript:copy(${item.id})" class="del">复制</a>
                <br/>
                <a href="/report/index/?report_name=${item.task}" class="btn1">压测报告</a>
                <a href="/log/index/?taskid=${item.id}" class="btn1">日志</a>
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
                url: '/task/setRunState/',
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
            if(!confirm("请确认任务不再使用,删除后不可恢复？"))
            {
                return;
            }
            $.ajax({
                url: '/task/del/',
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
            if(!confirm("请确认复制压测任务？"))
            {
                return;
            }
            $.ajax({
                url: '/task/copy/',
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