<#import "../_layout.ftl" as layout>
${Html.s("pagetitle","压测报告")}
<@layout._layout>
    <style>
    </style>
    <div class="head">
        <div class="title">
            压测报告
        </div>
    </div>
    <div>
        <ul class="mydetail">
            <li>
                <label>报告名</label>
                <p>${model?report_name}</p>
            </li>
            <li>
                <label>任务</label>
                <p><a href="/task/edit?id=${model?task_id}">${model?task_name}</a></p>
            </li>
            <li>
                <label>参与节点</label>
                <p>${model?nodes}</p>
                <table class="mytable" width="500">
                    <tr>
                        <th style="width:3%">节点</th>
                        <th style="width:15%">cpu核心数</th>
                        <th style="width:20%">内存</th>
                        <th style="width:15%">压测线程数</th>
                    </tr>
                    <#list nodeinfos as item>
                        <tr data-id="${item.node}">
                            <td>${item.node}</td>
                            <td>${item.cpu}</td>
                            <td>${item.memory}</td>
                            <td>${item.threads}</td>
                        </tr>
                    </#list>
                </table>
            </li>
            <li>
<#--                <label>采样数据</label>-->
<#--                <input class="mtext" type="text" id="filter_table" name="filter_table" value="${model?filter_table!}" />-->
            </li>
        </ul>
    </div>
</@layout._layout>