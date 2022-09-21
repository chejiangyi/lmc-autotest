<#import "../_layout.ftl" as layout>
${Html.s("pagetitle","节点列表")}
<@layout._layout>
<div class="head">
    <div class="title">
    ${pagetitle}
    </div>
</div>
<div class="orderlist m10 myorder">
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
        <th style="width:10%">节点</th>
        <th style="width:5%">运行状态</th>
        <th style="width:20%">信息</th>
        <th style="width:10%">操作</th>
    </tr>
    <#list model as item>
        <tr data-id="${item.id}">
            <td>${item.id}</td>
            <td>${item.node!}</td>
            <td>${Utils.showRunState(item.heart_time)}</td>
            <td>cpu:${Html.p(item.cpu)}/${Html.p(item.local_cpu)}<br/>内存:${Html.p(item.memory)!}/${Html.p(item.local_memory)}<br/>
                线程:${Html.p(item.threads)!}<br/>IP(端口):${Html.p(item.ip)!}(${Html.p(item.port)!})<br/>
            </td>
            <td>
                <a href="/log/index/" class="del">日志</a>
                <#if Utils.showRunState(item.heart_time)=="停止" >
                    <a href="javascript:del(${item.id})" class="del">删除</a>
                </#if>
            </td>
        </tr>
    </#list>
</table>
<#--<div class="total pt10">-->
<#--    <@layout._pager/>-->
<#--</div>-->
    <script type="text/javascript">
        function del(id) {
            if(!confirm("请确认节点不再使用,删除后不可恢复？"))
            {
                return;
            }
            $.ajax({
                url: '/node/del/',
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