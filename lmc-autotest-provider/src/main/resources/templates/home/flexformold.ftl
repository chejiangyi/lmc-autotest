<#import "../_layout.ftl" as layout>
${Html.s("pagetitle","demo示范")}
<@layout._layout>
    <link href="/content/form/css/flex.css" rel="stylesheet"/>
    <style>

    </style>
<div class="head">
    <div class="title draggable">
        表单引擎-相对定位-flex布局-旧表单兼容 ${Html.help("表单引擎-相对定位-flex布局")}
    </div>
</div>
<div>
    <div id="测试表单flex3" class="lkform mydetail" style="width: 800px;height: 800px;background-color: #FFF5EE">
            <div key="模型名称" class="lkControl" type="placeholder" >
                <label>模型名称</label><input class="mylongtext" type="text" id="name" name="name" value="" />
            </div>
            <div key="模型标题" class="lkControl" type="placeholder" >
                <label>模型标题</label><input class="mylongtext" type="text" id="title" name="title" value=""/>
            </div>
            <div>
                <label>模型类型</label>
                <select id="model_type" name="model_type">
                </select>
            </div>
            <div>
                <input type="button" class="btn1" style="" value="提交" onclick="" />
            </div>
            <li>
                <div key="删除表单" class="lkControl" type="placeholder" >
                    <input type="button" class="btn1" style="" value="删除表单" onclick="return deleteForm()" />
                </div>
            </li>
    </div>
</div>
    <script src="/content/form/lkform.js"></script>
    <script>
        $(function() {
            LkFormObj.form_design_type=1;
            LkFormObj.load("测试表单flex3");
        });
        function deleteForm(){
            LKApiObj.deleteForm({form_key:"测试表单flex3"},function (){
                alert("删除表单成功");
            });
        }
    </script>
</@layout._layout>