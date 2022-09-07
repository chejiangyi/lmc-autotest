<#import "../_layout.ftl" as layout>
${Html.s("pagetitle","demo示范")}
<@layout._layout>
    <link href="/content/form/css/absolute.css" rel="stylesheet"/>
    <style>
    </style>
<div class="head">
    <div class="title draggable">
        表单引擎-绝对定位-旧表单兼容 ${Html.help("表单引擎-绝对定位")}
    </div>
</div>
<div>
    <div id="测试表单3" class="lkform" style="width: 800px;height: 500px; background-color: #FFF5EE">
            <ul class="mydetail">
                <li>
                    <div key="模型名称" class="lkControl" type="placeholder" >
                        <label>模型名称</label><input class="mylongtext" type="text" id="name" name="name" value="" />
                    </div>
                </li>
                <li>
                    <label>模型标题</label><input class="mylongtext" type="text" id="title" name="title" value=""/>
                </li>
                <li>
                    <label>模型类型</label>
                    <select id="model_type" name="model_type">
                    </select>
                </li>
                <li>
                    <input type="button" class="btn1" style="" value="提交" onclick="return save('new')" />
                </li>
                <li>
                    <div key="删除表单" class="lkControl" type="placeholder" >
                        <input type="button" class="btn1" style="" value="删除表单" onclick="return deleteForm()" />
                    </div>
                </li>
            </ul>
    </div>
</div>
    <script src="/content/form/lkform.js"></script>
    <script>
        $(function() {
            LkFormObj.load("测试表单3");
        });
        function deleteForm(){
            LKApiObj.deleteForm({form_key:"测试表单3"},function (){
                alert("删除表单成功");
            });
        }
    </script>
</@layout._layout>