<#import "../_layout.ftl" as layout>
${Html.s("pagetitle","编辑定时计划")}
<@layout._layout>
    <script src="/content/ace-src-min-noconflict/ace.js"></script>
    <script src="/content/ace-src-min-noconflict/ext-language_tools.js"></script>
    <script src="/content/js/myAce.js"></script>
    <div class="head">
        <div class="title">
            编辑定时计划 ${Html.help("编辑定时执行计划")}
        </div>
    </div>
    <div>
        <ul class="mydetail">
            <li>
                <label>计划名</label>
                <input class="mylongtext" type="text" id="title" name="title" value="${model.title!}" />
            </li>
            <li>
                <label>描述</label>
                <input class="mylongtext" type="text" id="remark" name="remark" value="${model.remark!}" />
            </li>
            <li>
                <label>cron表达式</label>
                <input class="mtext" type="text" id="corn" name="corn" value="${model.corn!}" />${Html.help("同一个任务同时触发,仅保留一个在运行,不会重复运行,其他会跳过执行!")}
            </li>
            <li>
                ${Html.help("cron表达式在线工具")}<a target="_blank" href="https://cron.qqe2.com/">cron表达式在线工具</a>
                ${Html.help("脚本编写帮助文档")}<a target="_blank" href="/content/jobreadme.html">计划脚本不会编写?点击学习文档,成为编写高手！</a>
                ${Html.help("计划脚本速成篇")}<a target="_blank" href="https://gitee.com/chejiangyi/lmc-autotest/blob/master/README-Job.md">计划脚本速成篇</a>
            </li>
            <li>
                <div id="tabs">
                    <ul>
                        <li><a href="#tabs-1">计划脚本${Html.help("定时计划需要执行的脚本,请看文档进行编写!")}</a></li>
                    </ul>
                    <div id="tabs-1">
                        <textarea id="jscript" name="jscript" rows="20" cols="20" style="width: 90%;"></textarea>
                        <input id="hd_jscript" type="hidden" value="${model.jscript!?html}">
                    </div>
                </div>
            </li>
            <li>
               <#if model.state=="停止" && user.isAdminOrIsUser(model.create_user_id)  >
                    <input type="button" class="btn1" style="" value="保存" onclick="return save()" />
               </#if>
            </li>
        </ul>
    </div>
    <script type="text/javascript">
        var tx_jscript;
        $( function() {
            $( "#tabs").tabs();
            tx_jscript=bindAce("jscript",$("#hd_jscript").val());
            bindJobAutocompletion(tx_jscript);
        } );
        function save()
        {
            $.post("/job/save",
                {
                    "id": ${id!"0"},
                    "title": $("#title").val(),
                    "jscript": tx_jscript.getValue(),
                    "corn": $("#corn").val(),
                    "remark":$("#remark").val()
                },
                function (data) {
                    if (data.code < 0) {
                        alert(data.message);
                    } else {
                        window.location = '/job/index';
                    }
                }, "json");
        }

    </script>
</@layout._layout>