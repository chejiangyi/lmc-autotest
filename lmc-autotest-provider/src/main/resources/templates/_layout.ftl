<#--头部样式填充-->
<#macro _style_head>
    <style>
         <#if user??>
         <#else>
            .main_cont{
                margin-left: 5px;
            }
         </#if>
    </style>

</#macro>
<#--<#setting boolean_format="true,false" >-->
<#macro _leftmenu>
    <#if user??>
        <div class="leftmenu fl">
            <ul class="level1">
                <li>
    <#--                <p>任务管理<i></i></p>-->
                    <ul class="level2">
                        <li><a href="/task/index/">任务管理</a></li>
                    </ul>
                    <ul class="level2">
                        <li><a href="/report/index/">压测报告</a></li>
                    </ul>
                    <ul class="level2">
                        <li><a href="/job/index/">定时计划</a></li>
                    </ul>
                    <ul class="level2">
                        <li><a href="/node/index/">节点状态</a></li>
                    </ul>
                    <ul class="level2">
                        <li><a href="/log/index/">执行日志</a></li>
                    </ul>
                    <ul class="level2">
                        <li><a href="/sample/index">采样查询</a></li>
                    </ul>
                    <#if user??&&user.isAdmin()>
                    <ul class="level2">
                        <li><a href="/user/index">用户管理</a></li>
                    </ul>
                    </#if>
                    <ul class="level2">
                        <li><a target="_blank" href="https://gitee.com/chejiangyi/lmc-autotest">帮助文档</a></li>
                    </ul>
                </li>
            </ul>
        </div>
    </#if>
</#macro>

<#macro _footer>
<#--    <div class="footer">by 车江毅</div>-->
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

${Html.s("_html",html)}
<#macro _layout stylehead=_style_head foot=_footer leftmenu=_leftmenu >
    <!DOCTYPE html>
    <html>
    <head>
        <meta charset="utf-8"/>
        <meta name="viewport" content="width=device-width"/>
        <title> ${Html.g("pagetitle")!} by 车江毅</title>
        <link href="/content/css/css.css" rel="stylesheet"/>
        <#--<link href="/content/HighChartPackCss.css" rel="stylesheet"/>-->
        <link href="/content/pager.css" rel="stylesheet"/>
        <link href="/content/themes/base/jquery.ui.all.css" rel="stylesheet"/>
        <script src="/scripts/jquery-1.8.2.min.js" type="text/javascript"></script>
        <script src="/scripts/jquery-ui-1.8.24.js" type="text/javascript"></script>
<#--        <script src="/content/datepicker/WdatePicker.js"></script>-->
        <script src="/content/js/common.js"></script>
        <#--<script src="/scripts/jquery.unobtrusive-ajax.min.js"></script>-->
        <#--<script src="/scripts/jquery.validate.min.js"></script>-->
        <#--<script src="/scripts/jquery.validate.unobtrusive.min.js"></script>-->
        <@stylehead/>
        <style>
            .mydetail{margin-bottom: 20px;}
        </style>
    </head>
    <body>
    <div class="header">
        <span style="color: white; font-size: 25px; margin-left: 25px; font-weight: bold; font-family: 微软雅黑;">BSF全链路自动化测试平台</span>
        <div class="fr mr10">
            <#if user??>
                <span id="toptips" class="remind fl" title="${user.roleName()}"><span id="toptips1">${user.username!}</span></span>
                <a href="/loginout" class="fl">退出</a>
            <#else>
                <a href="/" class="fl">登录</a>
            </#if>
        </div>
    </div>
    <div class="content w">
        <@leftmenu/>
        <div class="main_cont">
            <div style="color:red">${error!}</div>
            <#nested />
        </div>
    </div>
    <@foot/>
    </body>
    </html>
</#macro>
