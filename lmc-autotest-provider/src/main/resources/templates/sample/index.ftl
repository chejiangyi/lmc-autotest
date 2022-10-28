<#import "../_layout.ftl" as layout>
${Html.s("pagetitle","采样查询")}
<@layout._layout>
<div class="head">
    <div class="title">
    ${pagetitle}
    </div>
</div>
<div class="orderlist m10 myorder">
    <form action="/sample/index/" method="post" id="searchForm">
        <div class="search">
            <label>采样表</label>
            <select id="type" name="table">
                <#list tables as item>
                    <option value="${item}" ${Html.w(item==table,"selected='selected'","")}>${item}</option>
                </#list>
            </select>
            <label>Where SQl</label>
            <input type="text" class="text longtext" style="width: 450px" name="sql" value="${sql!}" />${Html.help("普通的sql查询模式,如: url like '%192.168%' and operator_type!='未知'")}
            <input type="hidden" id="download" name="download" value="0"/>
            <input type="submit" class="btn1" value="搜索" accesskey="S" onclick="$('#download').val('0');return true;" />
            <input type="submit" class="btn1" value="下载" accesskey="D" onclick="$('#download').val('1');return true;" />${Html.help("一次最多能下载2万条样本")}
             <#if user.isAdmin()>
                <input type="button" class="btn1" value="仅保留最近2周样本" accesskey="D" onclick="clearSample()" />${Html.help("清理过多样本信息,仅保留最近2周自动录制的样本")}
            </#if>
        </div>
    </form>
    <div>
        <input type="file" name="file" id="file" />
        <input type="submit" class="btn1" id="btnUpload" value="上传到我的样本" accesskey="U" />${Html.help("上传样本到我的(当前用户名)样本表中,用户可通过自定义的样本进行压测!请“下载”样本(删掉数据后)为模板进行导入。")}
        <input type="button" class="btn1" value="清空我的样本" accesskey="D" onclick="clearMy()" />${Html.help("清空我的样本文件,如果要指定条件清除,请到数据库中手工清理!")}
    </div>
    <div class="tab_cont">
        <div class="List">
            <@_list/>
        </div>
    </div>
</div>
</@layout._layout>
<#macro _list >
<input type="hidden" name="query_table" id="query_table" value="${table}"/>
<table class="mytable" width="100%">
    <tr>
        <th style="width:3%">Id</th>
        <th style="width:80px">app_name<br/>服务名</th>
        <th style="width:100px">formip<br/>来源IP</th>
        <th style="width:10%">url</th>
        <th style="width:50px">method</th>
        <th style="width:40%">header</th>
        <th style="width:30%">body</th>
        <th style="width:100px">create_time<br/>创建时间</th>
        <th style="width:100px">traceid${Html.help("调用链id,涉及到AutoTest的调用链传递")}</th>
        <th style="width:100px">trace_top${Html.help("堆栈顶部,是否是AutoTest的调用链第一个")}</th>
        <th style="width:50px">operator_type${Html.help("操作类型,自动检测程序是否涉及到update,insert,delete之类的操作")}</th>
        <th style="width:5%">操作</th>
    </tr>
    <#list model as item>
        <tr data-id="${item.id}">
            <td>${item.id}</td>
            <td>${item.app_name}</td>
            <td>${item.fromip}</td>
            <td  style="word-break: break-all">${item.url}</td>
            <td>${item.method}</td>
            <td style="word-break: break-all" title="${item.header?html}">${Html.cutstring(item.header,300)}</td>
            <td style="word-break: break-all" title="${item.body?html}"> ${Html.cutstring(item.body,300)}</td>
            <td>${Html.p(item.create_time)}</td>
            <td style="word-break: break-all">${Html.p(item.traceid)}</td>
            <td>${Html.p(item.trace_top)}</td>
            <td>${Html.p(item.operator_type)}</td>
            <td><a href="javascript:check(${item.id})" class="btn">模拟请求</a></td>
        </tr>
    </#list>
</table>
<div class="total pt10">
    <@layout._pager/>
</div>
</#macro>
<script type="text/javascript">
    $('#btnUpload').click(function(){
        var files = $('#file')[0].files[0];//单个
        var data = new FormData();
        data.append('file', files);

        $.ajax({
            url: '/sample/import',
            type: 'POST',
            data: data,
            cache: false,
            processData: false,
            contentType: false,
            success:function(data){
                if (data.code < 0) {
                    alert(data.message);
                } else {
                    alert('上传成功');
                }
            }
        });
    });
    function clearSample() {
        if(!confirm("请确认清理2周自动录制的样本,删除后不可恢复？"))
        {
            return;
        }
        $.ajax({
            url: '/sample/clearsample/',
            type: "post",
            data: {
            },
            success: function (data) {
                if (data.code > 0) {
                    window.location.reload();
                }
                else {
                    alert(data.message);
                }
            }
        });
    }
    function clearMy() {
        if(!confirm("请确认清空我的样本,删除后不可恢复？"))
        {
            return;
        }
        $.ajax({
            url: '/sample/clearmy/',
            type: "post",
            data: {
            },
            success: function (data) {
                if (data.code > 0) {
                    window.location.reload();
                }
                else {
                    alert(data.message);
                }
            }
        });
    }

    function check(id) {
        $.ajax({
            url: '/sample/check/',
            type: "post",
            data: {
                id:id,
                table:$('#query_table').val()
            },
            success: function (data) {
                if (data.code > 0) {
                    alert(data.data);
                }
                else {
                    alert(data.message);
                }
            }
        });
    }
</script>