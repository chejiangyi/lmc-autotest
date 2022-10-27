<#import "../_layout.ftl" as layout>
${Html.s("pagetitle","编辑定时计划")}
<@layout._layout>
    <style>
    </style>
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
                <input class="mtext" type="text" id="corn" name="corn" value="${model.corn!}" />
            </li>
            <li>
                ${Html.help("cron表达式在线工具")}<a target="_blank" href="https://cron.qqe2.com/">cron表达式在线工具</a>
                ${Html.help("脚本编写帮助文档")}<a target="_blank" href="/content/jobreadme.html">计划脚本不会编写?点击学习文档,成为编写高手！</a>
                ${Html.help("计划脚本速成篇")}<a target="_blank" href="https://gitee.com/chejiangyi/lmc-autotest/blob/master/README-Job.md">计划脚本速成篇</a>
            </li>
            <li>
                <textarea id="jscript" name="jscript" rows="20" cols="20" style="width: 90%;">${model.jscript!}</textarea>
            </li>
            <li>
               <#if model.state=="停止" && user.isAdminOrIsUser(model.create_user_id)  >
                    <input type="button" class="btn1" style="" value="保存" onclick="return save()" />
               </#if>
            </li>
        </ul>
    </div>
    <script type="text/javascript">
        function save()
        {
            $.post("/job/save",
                {
                    "id": ${id!"0"},
                    "title": $("#title").val(),
                    "jscript": $("#jscript").val(),
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