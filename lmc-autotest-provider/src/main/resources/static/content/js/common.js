﻿$(function () {
    $("td[title]").dblclick(function () {
        var title = $(this).attr('title'); alert(title);
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