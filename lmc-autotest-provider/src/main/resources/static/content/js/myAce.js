function bindAce(id,value){
    var editor = ace.edit(id, {
        maxLines: 150, // 最大行数，超过会自动出现滚动条
        minLines: 20, // 最小行数，还未到最大行数时，编辑器会自动伸缩大小
        fontSize: 14, // 编辑器内字体大小
        theme: 'ace/theme/chrome', // 默认设置的主题
        mode: 'ace/mode/javascript', // 默认设置的语言模式
        tabSize: 4,// 制表符设置为 4 个空格大小
        //readOnly: false //只读
       // mode: "ace/mode/javascript",
       // selectionStyle: "text"
    });

    //console.log(value);
    editor.setValue(value);
    editor.gotoLine(0, 0);
    return editor;
}

// {
//     name : "api.bb", //名称
//     value : "api.bb",//值，这就是匹配我们输入的内容，比如输入s或者select,这一行就会出现在提示框里，可根据自己需求修改，就是你想输入什么显示出北京呢，就改成什么
//     //caption: "222",//字幕，下拉提示左侧内容,这也就是我们输入前缀匹配出来的内容，所以这里必须包含我们的前缀
//     meta: "111", //类型，下拉提示右侧内容
//     //type: "local",//可写为keyword
//     score : 1000 // 让它排在最上面，类似权值的概念
// }

function bindTaskAutocompletion(editor){
    //启用提示菜单
    editor.setOptions({
        enableBasicAutocompletion: true,
        enableSnippets: true,
        enableLiveAutocompletion: true
    });
    //自定义补全
    var languageTools = ace.require("ace/ext/language_tools");
    languageTools.addCompleter({
        getCompletions: function(editor, session, pos, prefix, callback) {
            setTimeout("jhcAutoWidth()","20");
            callback(null,  [
                {
                    name : "api.streamSql2",
                    value : "api.streamSql2(String sql,Object[] ps,function(dataMap){})",
                    meta: "api数据流读取",
                    score : 1000
                },
                {
                    name : "api.writeSample",
                    value : "api.writeSample(Object sample)",
                    meta: "样本写入",
                    score : 1000
                },
                {
                    name : "api.log",
                    value : "api.log(Object info)",
                    meta: "日志打印",
                    score : 1000
                },
                {
                    name : "api.error",
                    value : "api.error(Object info)",
                    meta: "错误日志",
                    score : 1000
                },
                {
                    name : "api.debug",
                    value : "api.debug(Object info)",
                    meta: "调试打印",
                    score : 1000
                },
                {
                    name : "api.nowFormat",
                    value : "api.nowFormat(String format)",
                    meta: "当前时间格式",
                    score : 1000
                },
                {
                    name : "api.ps.?",
                    value : "api.ps.?",
                    meta: "动态参数",
                    score : 1000
                },
                {
                    name : "api.sleep",
                    value : "api.sleep(Integer time)",
                    meta: "睡眠n毫秒",
                    score : 1000
                },
                {
                    name : "api.httpPost",
                    value : "api.httpPost(String url,Object json)",
                    meta: "http Post Json请求支持,返回String",
                    score : 1000
                },
                {
                    name : "api.httpGet",
                    value : "api.httpGet(String url)",
                    meta: "http Get请求支持,返回String",
                    score : 1000
                },
                {
                    name : "api.httpPostForm",
                    value : "api.httpPostForm(String url,Object form)",
                    meta: "http Post Form请求支持,返回String",
                    score : 1000
                }
            ]);
        }
    });

}

function bindJobAutocompletion(editor){
    //启用提示菜单
    editor.setOptions({
        enableBasicAutocompletion: true,
        enableSnippets: true,
        enableLiveAutocompletion: true
    });
    //自定义补全
    var languageTools = ace.require("ace/ext/language_tools");
    languageTools.addCompleter({
        getCompletions: function(editor, session, pos, prefix, callback) {
            setTimeout("jhcAutoWidth()","20");
            callback(null,  [
                {
                    name : "api.querySql",
                    value : "api.querySql(String sql,Object[] ps)",
                    meta: "sql读取处理",
                    score : 1000
                },
                {
                    name : "api.log",
                    value : "api.log(Object info)",
                    meta: "日志打印",
                    score : 1000
                },
                {
                    name : "api.error",
                    value : "api.error(Object info)",
                    meta: "错误日志",
                    score : 1000
                },
                {
                    name : "api.debug",
                    value : "api.debug(Object info)",
                    meta: "调试打印",
                    score : 1000
                },
                {
                    name : "api.nowFormat",
                    value : "api.nowFormat(String format)",
                    meta: "当前时间格式",
                    score : 1000
                },
                {
                    name : "api.ps.?",
                    value : "api.ps.?",
                    meta: "动态参数",
                    score : 1000
                },
                {
                    name : "api.sleep",
                    value : "api.sleep(Integer time)",
                    meta: "睡眠n毫秒",
                    score : 1000
                },
                {
                    name : "api.httpPost",
                    value : "api.httpPost(String url,Object json)",
                    meta: "http Post Json请求支持,返回String",
                    score : 1000
                },
                {
                    name : "api.httpGet",
                    value : "api.httpGet(String url)",
                    meta: "http Get请求支持,返回String",
                    score : 1000
                },
                {
                    name : "api.httpPostForm",
                    value : "api.httpPostForm(String url,Object form)",
                    meta: "http Post Form请求支持,返回String",
                    score : 1000
                },
                {
                    name : "api.openTask",
                    value : "api.openTask(Integer taskid)",
                    meta: "启动任务",
                    score : 1000
                },
                {
                    name : "api.openTask2",
                    value : "api.openTask2(Integer taskid,Object params)",
                    meta: "启动任务,可传入动态参数",
                    score : 1000
                },
                {
                    name : "api.closeTask",
                    value : "api.closeTask(Integer taskid)",
                    meta: "关闭任务",
                    score : 1000
                },
                {
                    name : "api.isTaskRunning",
                    value : "api.isTaskRunning(Integer taskid)",
                    meta: "任务是否在运行",
                    score : 1000
                },
                {
                    name : "api.isTaskExist",
                    value : "api.isTaskExist(Integer taskid)",
                    meta: "任务是否存在",
                    score : 1000
                }
            ]);
        }
    });

}

/*自适应提示框宽度*/
var stringWidth = function(fontSize, content) {
    var $span = $('<span></span>').hide().css('font-size', fontSize).text(content);
    var w = $span.appendTo('body').width();
    $span.remove();
    return w;
};


function jhcAutoWidth(){
    //if($('.ace_autocomplete').is(":hidden"))return true;
    var jige=$('.ace_autocomplete').find('.ace_line').length;
    if(jige<1)return '';
    var maxText='';
    for(var i=0;i<jige;i++){
        var nowText=$('.ace_autocomplete').find('.ace_line').eq(i).text();
        if(nowText.length>maxText.length)maxText=nowText;
    }
    var jhcWidth=200+stringWidth('20',maxText);
    $('.ace_autocomplete').css('width',jhcWidth+'px');
}
// setInterval("jhcAutoWidth()","1000");