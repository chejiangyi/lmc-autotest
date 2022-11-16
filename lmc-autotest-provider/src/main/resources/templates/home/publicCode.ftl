<#import "../_layout.ftl" as layout>
${Html.s("pagetitle","公共方法库")}
<@layout._layout>
    <script src="/content/ace-src-min-noconflict/ace.js"></script>
    <script src="/content/ace-src-min-noconflict/ext-language_tools.js"></script>
    <script src="/content/js/myAce.js"></script>
    <div class="head">
        <div class="title">
            公共方法库${Html.help("公共方法库,可以被定时和任务脚本使用")}
        </div>
    </div>
    <div>
        <ul class="mydetail">
            <li>
                <p style="color: red;margin: 5px;">注:公共方法库被定时和任务脚本使用,编译错误会影响所有脚本运行。</p>
                <p><a target="_blank" href="https://jsrun.net/new">在线测试js工具</a> <a target="_blank" href="https://gitee.com/chejiangyi/lmc-autotest/blob/master/README-PublicCode.md">案例代码</a></p>
            </li>
            <li>
                <textarea id="jscript" name="jscript" rows="50" cols="20" style="width: 90%;"></textarea>
                <input id="hd_jscript" type="hidden" value="${model.dic_value!?html}">
            </li>
            <li>
                <#if user.isAdminOrIsUser(model.create_user_id)  >
                    <input type="button" class="btn1" style="" value="保存" onclick="return save()" />
                </#if>
            </li>
        </ul>
    </div>
    <script type="text/javascript">
        var tx_jscript;
        $( function() {
            tx_jscript=bindAce("jscript",$("#hd_jscript").val());
            bindJobAutocompletion(tx_jscript);
        } );
        function save()
        {
            $.post("/publicCodeSave",
                {
                    "jscript": tx_jscript.getValue(),
                },
                function (data) {
                    if (data.code < 0) {
                        alert(data.message);
                    } else {
                        window.location = '/publicCode/';
                    }
                }, "json");
        }

    </script>
</@layout._layout>