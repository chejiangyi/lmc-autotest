<#--头部样式填充-->
<#macro _style_head>
</#macro>
<#--<#setting boolean_format="true,false" >-->
<#macro _leftmenu>
<div class="leftmenu fl">
    <ul class="level1">
<#--        <li>-->
<#--            <p>流程中心<i></i></p>-->
<#--            <ul class="level2">-->
<#--                <li><a href="/user/flow/mode_list">发起流程</a></li>-->
<#--            </ul>-->
<#--            <ul class="level2">-->
<#--                <li><a href="/user/flow/lunched_list">我的流程</a></li>-->
<#--            </ul>-->
<#--            <ul class="level2">-->
<#--                <li><a href="/user/flow/undo_list">待办任务</a></li>-->
<#--            </ul>-->
<#--            <ul class="level2">-->
<#--                <li><a href="/user/flow/done_list">已办任务</a></li>-->
<#--            </ul>-->
<#--        </li>-->
<#--       <#if user.isAdmin()==true>-->
            <li>
                <p>用户指标统计<i></i></p>
                <ul class="level2">
                    <li><a href="/allrank">统计列表</a></li>
                </ul>
             <#--   <ul class="level2">
                    <li><a href="/flex/">相对定位-flex布局</a></li>
                </ul>
                <ul class="level2">
                    <li><a href="/formold">绝对定位-对旧表单的兼容</a></li>
                </ul>
                <ul class="level2">
                    <li><a href="/flexformold">相对定位-对旧表单的兼容</a></li>
                </ul>
                <ul class="level2">
                    <li><a href="/sort/">flex盒子布局</a></li>
                </ul>
                <ul class="level2">
                    <li><a href="/ligai/">ligai模拟</a></li>
                </ul>-->
            </li>
     	<#--
      	<li>
	        <p>系统维护<i></i></p>
	        <ul class="level2">
	        <li><a href="@Url.Action(" ConfigExport", "Operation")">项目配置导出</a></li>
	        <li><a href="@Url.Action(" Configimport", "Operation")">项目配置导入</a></li>
	       </ul>
       </li>
       -->
<#--        </#if>-->
    </ul>
</div>
</#macro>

<#macro _footer>
by 车江毅
</#macro>
<#--分页控件-->
<#macro _pager formid="searchForm">
    ${pagehtml!}
    <script type="text/javascript">
    function pagerfunction(pageindex) {
        $("#${formid}").prepend("<input type='hidden' name='pageindex' value='"+pageindex+"'/>");
        $("#${formid}").prepend("<input type='hidden' name='pagesize' value='"+'${pagesize!}'+"'/>");
        $("#${formid}").submit();
    }
    </script>
</#macro>

<#--${Html.s("_html",html!)}-->
<#macro _layout stylehead=_style_head foot=_footer leftmenu=_leftmenu >
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width"/>
    <title> ${Html.g("pagetitle")!}</title>
    <link href="/content/css/css.css" rel="stylesheet"/>
    <#--<link href="/content/HighChartPackCss.css" rel="stylesheet"/>-->
    <link href="/content/pager.css" rel="stylesheet"/>
    <link href="/content/form/css/absolute.css" rel="stylesheet"/>
    <link href="/content/themes/base/jquery.ui.all.css" rel="stylesheet"/>
    <link href="/content/datepicker/skin/WdatePicker.css" rel="stylesheet"/>
    <script src="/scripts/jquery-1.8.2.min.js" type="text/javascript"></script>
    <script src="/scripts/jquery-ui-1.8.24.js" type="text/javascript"></script>
    <script src="/content/datepicker/WdatePicker.js"></script>
    <script src="/content/js/common.js"></script>

    <#--<script src="/scripts/jquery.unobtrusive-ajax.min.js"></script>-->
    <#--<script src="/scripts/jquery.validate.min.js"></script>-->
    <#--<script src="/scripts/jquery.validate.unobtrusive.min.js"></script>-->
    <@stylehead/>
    <link href="/content/menu/jquery.contextMenu.min.css" rel="stylesheet"/>
    <script src="/content/menu/jquery.contextMenu.min.js"></script>
    <script src="/content/menu/jquery.ui.position.js"></script>
    <style>
        .mydetail{margin-bottom: 20px;}
    </style>
</head>
<body>
<div class="header">
    <span style="color: white; font-size: 25px; margin-left: 25px; font-weight: bold; font-family: 微软雅黑;">管理端</span>
    <a href="/index/" style="text-decoration: blue;color: blue; font-size: 16px; margin-left: 10px; font-weight: bold; font-family: 微软雅黑;">返回用户端</a>
    <div class="fr mr10">

<#--        <#if user.getCurrent()??>-->
<#--            <span id="toptips" class="remind fl"><span id="toptips1">${user.username!}</span></span>-->
<#--           <a href="/logout" class="fl">退出</a>-->
<#--        <#else>-->
<#--           <a href="/" class="fl">登录</a>-->
<#--        </#if>-->
    </div>
</div>

<div style="color:red">${error!}</div>
<div class="content w">
    <@leftmenu/>
    <div class="main_cont">
        <#nested />
    </div>
</div>
</body>
</html>
</#macro>
