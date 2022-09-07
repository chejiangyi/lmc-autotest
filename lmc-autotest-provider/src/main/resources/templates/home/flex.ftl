<#import "../_layout.ftl" as layout>
${Html.s("pagetitle","demo示范")}
<@layout._layout>
    <link href="/content/form/css/flex.css" rel="stylesheet"/>
    <style>

    </style>
<div class="head">
    <div class="title draggable">
        表单引擎-相对定位-flex布局 ${Html.help("表单引擎-相对定位-flex布局")}
    </div>
</div>
<div>
    <div id="测试表单flex" class="lkform" style="width: 500px;height: 800px;background-color: #FFF5EE">
    </div>
</div>
    <script src="/content/form/lkform.js"></script>
    <script>
        $(function() {
            LkFormObj.form_design_type=1;
            LkFormObj.load("测试表单flex");
        });
    </script>
</@layout._layout>