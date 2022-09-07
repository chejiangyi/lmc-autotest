
$(function (){
    $("input[datatype='file']").each(function (){
        var parent=$(this).parent();
        parent.append("<form action='"+system_upload_url+"' method='post' enctype='multipart/form-data'><span class='system_file_info'></span><input name='system_file' type='file' onchange='uploadfile(this)'/></form>");
        $(this).hide();
        $(this).bind('change', function() {
            var parent=$(this).parent();
            var info = parent.find('.system_file_info');
            var url = $(this).val();
            if(url=='')
            {info.html('');parent.find('form')[0].reset();}
            else {
                var template="<a href='{url}' target='_blank'>{file}</a><input class='system_delete' type='button' onclick='deletefile(this)' value='删除'/>";
                template=template.replaceAll("{url}",url).replaceAll("{file}",getParameterByName("filename",url));
                info.html(template);
            }
        });
    });
});

function deletefile(item){
    var file = $(item).parent().parent().parent().find("input[datatype='file']");
    file.val('').change();
}

function uploadfile(item){
    console.log('上传文件');
    $(item).parent().ajaxSubmit( {
        url : system_upload_url,
        type : 'POST',
        dataType : 'json',
        success : function(data) {
            if(data.code>0){
                var parent=$(item).parent().parent();
                var file = parent.find("input[datatype='file']");
                file.val(data.data).change();
            }else {
                alert(data.message);
            }
        }
    });
}

function getfilename(url) {
    var str = url;
    str = str.substring(str.lastIndexOf("/") + 1);
    str = str.substring(0, str.lastIndexOf("."));
    return str;
}

function getParameterByName(name, url) {
    if (!url) url = window.location.href;
    name = name.replace(/[\[\]]/g, "\\$&");
    var regex = new RegExp("[?&]" + name + "(=([^&#]*)|&|#|$)"),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';
    return decodeURIComponent(results[2].replace(/\+/g, " "));
}