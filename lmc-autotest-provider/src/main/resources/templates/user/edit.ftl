<#import "../_layout.ftl" as layout>
${Html.s("pagetitle","编辑用户")}
<@layout._layout>
    <style>
    </style>
    <div class="head">
        <div class="title">
            编辑用户${Html.help("编辑用户")}
        </div>
    </div>
    <div>
        <ul class="mydetail">
            <li>
                <label>用户名</label>
                <input class="mylongtext" type="text" id="name" name="name" value="${model.name!}" />(用户名必须为字母,且保证唯一)
            </li>
            <li>
                <label>角色</label>
                <select id="role" name="role">
                    <option value="0" ${Html.w(model.role==0,"selected='selected'","")}>普通用户</option>
                    <option value="1" ${Html.w(model.role==1,"selected='selected'","")}>管理员</option>
                </select>
            </li>
            <li>
                <label>密码</label>
                <input class="mylongtext" type="text" id="pwd" name="pwd" value="${model.pwd!}" />
            </li>
            <li>
                <label>限制最大节点数</label>
                <input class="mylongtext" type="text" id="limit_node_count" name="limit_node_count" value="${model.limit_node_count!}" />
                ${Html.help("限制用户在压测的时候最多使用的节点数量")}
            </li>
            <li>
                <label>api访问token</label>
                <span id="token"></span>
                ${Html.help("访问api的token,跟用户名和密码有关,改动后token就会失效!")}
            </li>
            <li>
               <#if user.isAdmin()  >
                   <input type="button" class="btn1" style="" value="保存" onclick="return save()" />
               </#if>
            </li>
        </ul>
    </div>
    <script type="text/javascript">

        function save()
        {
            $.post("/user/save",
                {
                    "id": ${id!"0"},
                    "name": $("#name").val(),
                    "pwd": $("#pwd").val(),
                    "role": $("#role").val(),
                    "limit_node_count": $("#limit_node_count").val()
                },
                function (data) {
                    if (data.code < 0) {
                        alert(data.message);
                    } else {
                        window.location = '/user/index';
                    }
                }, "json");
        }

        function getToken()
        {
            $.post("/user/token",
                {
                    "id": ${id!"0"}
                },
                function (data) {
                    if (data.code < 0) {
                        alert(data.message);
                    } else {
                        $('#token').text(data.data);
                    }
                }, "json");
        }
        $(function (){
            getToken();
        });

    </script>
</@layout._layout>