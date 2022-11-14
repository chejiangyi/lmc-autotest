<#import "../_layout.ftl" as layout>
${Html.s("pagetitle","节点列表")}
<@layout._layout>
    <style>
        .used{
            color: red;
        }
        .noused{
            color: blue;
        }
        .cpu{
            color: #6f42c1;
        }
        .memory{
            color: #8b4902;
        }
        .thread{
            color: #08575B;
        }
        table b{
            font-family: inherit;
             font-weight: bold;
            margin:  0 1px;
        }
    </style>
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
            <td>${Utils.printRunState(item.heatbeat_time)}:${Html.w(item.used,"<i class='used'>使用中</i>","<i class='noused'>空闲</i>")!}${Html.help("运行状态/使用状态;运行状态表示节点是否健康运行,使用状态表示节点是否被任务占用")}</td>
            <td style="text-align: left">cpu:<b class="cpu">${Html.p(item.cpu*100)}</b>%/${Html.p(item.local_cpu)}核${Html.help("cpu占用率%/cpu核心数")}<br/>内存:<b class="memory">${Html.p(item.memory)!}</b>M/${Html.p(item.local_memory)}M${Html.help("节点占用内存大小(M)/系统内存大小(M);节点占用内存大小=JvmTotal内存-JvmFree内存;")}<br/>
                活动线程:<b class="thread">${Html.p(item.threads)!}</b>${Html.help("压测时压测任务的正在进行的活跃线程数")}<br/>IP(端口):${Html.p(item.ip)!}(${Html.p(item.port)!})${Html.help("节点所在服务器的ip(节点打开的端口)")}<br/>
            </td>
            <td>
                <a href="/log/index/?node=${Html.urlEncode(item.node)}" class="del">日志</a>
                <#if Utils.showRunState(item.heatbeat_time) == "停止">
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