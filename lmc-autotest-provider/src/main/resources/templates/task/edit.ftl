<#import "../_layout.ftl" as layout>
${Html.s("pagetitle","编辑任务")}
<@layout._layout>
    <style>
    </style>
    <div class="head">
        <div class="title">
            编辑任务 ${Html.help("编辑自动化测试任务")}
        </div>
    </div>
    <div>
        <ul class="mydetail">
            <li>
                <label>任务名</label>
                <input class="mylongtext" type="text" id="task" name="task" value="${model?task!}" />
            </li>
            <li>
                <label>采集样本存储引擎</label>
                <select id="filter_store" name="filter_store">
                    <#list ["mysql"] as item>
                        <option value="${item}" ${Html.w(model?filter_store==item,"selected='selected'","")}>${item}</option>
                    </#list>
                </select>
            </li>
            <li>
                <label>采集样本筛选脚本</label>
                <textarea id="filter_script" rows="50" cols="20" style="width: 80%;display: none">${model?filter_script!}</textarea>
            </li>
            <li>
                <label>采样表文件</label>
                <input class="mtext" type="text" id="filter_table" name="filter_table" value="${model?filter_table!}" />
            </li>
            <li>
                <label>第一次执行过滤错误采样样本</label> ${Html.help("先过滤错误样本后,再执行压测任务")}
               <input type="checkbox" id="clear_data_first" name="clear_data_first" value="${model?clear_data_first!}" >
            </li>
            <li>
                <label>执行节点</label>
                <select id="filter_store" name="filter_store" multiple>
                    <#list Utils.getOnlineNodes() as item>
                        <option value="${item}" ${Html.w(Html.inArray(model?nodes.spilt(','),item),"selected='selected'","")}>${item}</option>
                    </#list>
                </select>
            </li>
            <li>
                <label>执行时间</label>
                <input class="mtext" type="text" id="corn" name="corn" value="${model?corn!}" />
            </li>
            <li>
                <label>节点并行线程数量</label>
                <input class="stext" type="text" id="run_threads_count" name="run_threads_count" value="${model?run_threads_count!}" />
            </li>
            <li>
                <label>样式执行前脚本</label> ${Html.help("无")}
                <textarea id="http_begin_script" rows="50" cols="20" style="width: 80%;display: none">${model?http_begin_script!}</textarea>
            </li>
            <li>
                <label>样式执行后脚本</label> ${Html.help("无")}
                <textarea id="http_end_script" rows="50" cols="20" style="width: 80%;display: none">${model?http_end_script!}</textarea>
            </li>
            <li>
                <label>任务终止判断脚本</label> ${Html.help("无")}
                <textarea id="check_stop_script" rows="50" cols="20" style="width: 80%;display: none">${model?check_stop_script!}</textarea>
            </li>
            <li>
               <#if Utils.showRunState(item.run_heart_time)=="停止" >
                    <input type="button" class="btn1" style="" value="保存" onclick="return save()" />
               </#if>
            </li>
        </ul>
    </div>
    <script type="text/javascript">
        function save()
        {
            $.post("/task/save",
                {
                    "id": ${id!"0"},
                    "task": $("#task").val(),
                    "filter_store": $("#filter_store").val(),
                    "filter_script": $("#filter_script").val(),
                    "filter_table":$("#filter_table").val(),
                    "clear_data_first":$("#clear_data_first").val(),
                    "nodes":$("#nodes").val(),
                    "corn":$("#corn").val(),
                    "run_threads_count":$("#run_threads_count").val(),
                    "http_begin_script":$("#http_begin_script").val(),
                    "http_end_script":$("#http_end_script").val(),
                    "check_stop_script":$("#check_stop_script").val(),
                },
                function (data) {
                    if (data.code < 0) {
                        alert(data.message);
                    } else {
                        window.location = '/task/index';
                    }
                }, "json");
        }

    </script>
</@layout._layout>