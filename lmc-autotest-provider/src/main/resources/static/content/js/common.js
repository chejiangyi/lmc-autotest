$(function () {
    $("td[title]").dblclick(function () {
        var title = $(this).attr('title');
        //alert(title);
        $("<div style='word-break: break-all'>"+title+"</div>").dialog({title:"信息",minWidth:450,minHeight:500});
    });

    $("td[title]").each(function () {
        $(this).attr('title', $(this).attr('title') + "\r\n" + "【双击弹框】");
    });

    $("img[title]").each(function () {
        $(this).attr('title', "【帮助】" + $(this).attr('title') + "\r\n" + "【双击弹框】");
    });

    $("img[title]").dblclick(function () {
        var title = $(this).attr('title'); alert(title);
    });

    $(".back").on("click", function () {
        window.history.go(-1);
    });
});


//添加AddAntiForgeryToken
function addAntiForgeryToken(data) {
    data.__RequestVerificationToken = $('input[name=__RequestVerificationToken]').val();
    return data;
};

// 入参 fmt-格式
Date.prototype.format = function(fmt) {
    var o = {
        "M+": this.getMonth() + 1,                 //月份
        "d+": this.getDate(),                    //日
        "h+": this.getHours(),                   //小时
        "m+": this.getMinutes(),                 //分
        "s+": this.getSeconds(),                 //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds()             //毫秒
    };
    if(/(y+)/.test(fmt)){
        fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));
    }

    for(var k in o){
        if(new RegExp("("+ k +")").test(fmt)){
            fmt = fmt.replace(
                RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
        }
    }

    return fmt;
}
