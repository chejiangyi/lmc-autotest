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
                <input class="mylongtext" type="text" id="name" name="name" value="${model.name!}" />(用户名必须为字母)
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
                },
                function (data) {
                    if (data.code < 0) {
                        alert(data.message);
                    } else {
                        window.location = '/user/index';
                    }
                }, "json");
        }

    </script>
</@layout._layout>