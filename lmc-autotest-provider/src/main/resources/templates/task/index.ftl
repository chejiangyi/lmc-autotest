<#import "../_layout.ftl" as layout>
${Html.s("pagetitle","任务列表")}
<@layout._layout>
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
        <th style="width:5%">存储引擎</th>
        <th style="width:5%">运行状态</th>
        <th style="width:20%">执行时间</th>
        <th style="width:5%">任务状态</th>
        <th style="width:5%">创建人</th>
        <th style="width:20%">执行结果</th>
        <th style="width:20%">操作</th>
    </tr>
    <#list model as item>
        <tr data-id="${item.id}">
            <td>${item.id}</td>
            <td>${item.filter_store!}</td>
            <td>${Utils.showRunState(item.run_heart_time)}</td>
            <td>下次执行时间:${Html.p(item.next_time)}<br/>执行周期:${item.corn!}</td>
            <td>${item.use_state!}</td>
            <td>${item.create_user}<br/>${Html.p(item.create_time)}<br/>更新:${item.update_user}(${Html.p(item.update_time)})</td>
            <td>${item.task_result!}</td>
            <td>
                <a href="javascript:setUseState(${item.id})" class="usestate" title="">${item.use_state}</a>
                <a href="javascript:setRunState(${item.id})" class="del"> ${Html.w(Utils.showRunState(item.run_heart_time)=="停止","运行","停止")}</a>
                <a href="/task/edit/?id=${item.id}" class="btn1" target="_blank">编辑</a>
                <#if Utils.showRunState(item.run_heart_time)=="停止" >
                    <a href="javascript:del(${item.id})" class="del">删除</a>
                </#if>
<#--                <a href="javascript:del(${item.id})" class="del">最新压测报告</a>-->
            </td>
        </tr>
    </#list>
</table>
<div class="total pt10">
    <@layout._pager/>
</div>
    <script type="text/javascript">
        function setUseState(id){
            $.ajax({
                url: '/task/setUseState/',
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
        function setRunState(id){
            $.ajax({
                url: '/task/setRunState/',
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
    </script>
</#macro>