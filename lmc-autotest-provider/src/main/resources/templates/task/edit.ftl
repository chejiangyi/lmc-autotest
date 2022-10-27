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
                <input class="mylongtext" type="text" id="task" name="task" value="${model.task!}" />
            </li>
            <li>
                <label>采集样本存储引擎</label>
                <select id="filter_store" name="filter_store">
                    <#list ["mysql"] as item>
                        <option value="${item}" ${Html.w2("==",model.filter_store,item,"selected='selected'","")}>${item}</option>
                    </#list>
                </select>
            </li>
            <li>
                <label>执行节点数量</label>
                <input class="stext" type="text" id="node_count" name="node_count" value="${model.node_count!}" />${Html.help("系统会根据当前[空闲]的节点,智能调度!")}
                <label>复用HTTP链接</label>
                <input type="checkbox" id="use_http_keepalive" name="use_http_keepalive" ${Html.w2("==",model.use_http_keepalive,true,"checked='checked'","")}>${Html.help("使用keep-alive头,底层采用http连接池尽量复用链接!开启后会有额外少量连接池维护和切换的cpu损耗,cpu不够的情况下反而会降低吞吐量!")}
            </li>
            <li>
                <label>节点并行线程数量</label>
                <input class="stext" type="text" id="run_threads_count" name="run_threads_count" value="${model.run_threads_count!}" />${Html.help("在cpu允许的情况下,建议线程数2000-3000之间,会更好发挥并发性能!")}
                <label>每个线程启动间隔</label>
                <input class="stext" type="text" id="sleep_time_every_thread" name="sleep_time_every_thread" value="${model.sleep_time_every_thread!}" />毫秒${Html.help("压测时每个线程之间启动的时间间隔,逐步启动,避免突然间并发导致程序无法响应,为0则表示采用系统自动算法均衡(一般为200-700ms)")}
            </li>
            <li>
                ${Html.help("脚本编写帮助文档")}<a target="_blank" href="/content/readme.html">脚本不会编写?点击学习文档,成为编写高手！</a>
                ${Html.help("基础案例篇文档")}<a target="_blank" href="https://gitee.com/chejiangyi/lmc-autotest/blob/master/README-Demo.md">基础案例篇</a>
                ${Html.help("高级案例篇文档")}<a target="_blank" href="https://gitee.com/chejiangyi/lmc-autotest/blob/master/README-Demo2.md">高手进阶篇</a>
            </li>
            <li>
                <div id="tabs">
                    <ul>
                        <li><a href="#tabs-1">采集样本筛选脚本${Html.help("从样本库中筛选出需要压测的样本")}</a></li>
                        <li><a href="#tabs-2">首次执行过滤错误采样样本${Html.help("首次执行(压测前)进行过滤部分错误的样本数据,减少不必要的错误")}</a></li>
                        <li><a href="#tabs-3">样本执行前脚本${Html.help("单个样本进行回访前执行的脚本")}</a></li>
                        <li><a href="#tabs-4">样本执行后脚本${Html.help("单个样本进行回放后执行的脚本,一般用于错误判断")}</a></li>
                        <li><a href="#tabs-5">任务终止判断脚本${Html.help("任务终止的定时(一般单个心跳时间,默认5s)判断脚本,达到条件则终止压测任务")}</a></li>
                    </ul>
                    <div id="tabs-1">
                        <textarea id="filter_script" rows="20" cols="20" style="width: 90%;">${model.filter_script!}</textarea>
                    </div>
                    <div id="tabs-2">
                        <textarea id="first_filter_error_script" rows="20" cols="20" style="width: 90%;">${model.first_filter_error_script!}</textarea>
                    </div>
                    <div id="tabs-3">
                        <textarea id="http_begin_script" rows="20" cols="20" style="width: 90%;">${model.http_begin_script!}</textarea>
                    </div>
                    <div id="tabs-4">
                        <textarea id="http_end_script" rows="20" cols="20" style="width: 90%;">${model.http_end_script!}</textarea>
                    </div>
                    <div id="tabs-5">
                        <textarea id="check_stop_script" rows="20" cols="20" style="width: 90%;">${model.check_stop_script!}</textarea>
                    </div>
                </div>

            </li>

            <li>
               <#if Utils.showRunState(model.run_heart_time)=="停止" && user.isAdminOrIsUser(model.create_user_id)  >
                    <input type="button" class="btn1" style="" value="保存" onclick="return save()" />
               </#if>
            </li>
        </ul>
    </div>
    <script type="text/javascript">
        $( function() {
            $( "#tabs").tabs();
        } );

        function save()
        {
            $.post("/task/save",
                {
                    "id": ${id!"0"},
                    "task": $("#task").val(),
                    "filter_store": $("#filter_store").val(),
                    "filter_script": $("#filter_script").val(),
                    "filter_table":$("#filter_table").val(),
                    "first_filter_error_script":$("#first_filter_error_script").val(),
                    "node_count":$("#node_count").val(),
                    "corn":$("#corn").val(),
                    "run_threads_count":$("#run_threads_count").val(),
                    "http_begin_script":$("#http_begin_script").val(),
                    "http_end_script":$("#http_end_script").val(),
                    "check_stop_script":$("#check_stop_script").val(),
                    "sleep_time_every_thread":$("#sleep_time_every_thread").val(),
                    "use_http_keepalive":$("#use_http_keepalive").is(':checked')
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