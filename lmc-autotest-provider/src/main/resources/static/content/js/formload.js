function readonly(element){
    $(element).find('textarea,input,select').attr("disabled","");
}
function sys_form_init(){
    console.log("系统表单初始化");
    $('div[fieldtype]').each(function(i){
        var fieldtype = $(this).attr("fieldtype");
        if(fieldtype=="Control"){
            //console.log(this);
            var controlType = $(this).attr("controlType");
            var bindScript=$(this).attr("bindScript");
            var bindVariable=$(this).attr("bindVariable");
            var initScript=$(this).attr("initScript");
            var access = $(this).attr("access");
            var value = null;
            if(typeof read_variables != "undefined") {
                if (bindVariable != null && read_variables[bindVariable] != null) {
                    value = read_variables[bindVariable];
                }
            }
            if(controlType=="TextArea"){
                var c=$(this).find('textarea');
                c.val(bindScript);
                if(c.val()==""){
                    c.val(initScript);
                }
                if(value!=null){
                    c.val(value);
                }
            }else if(controlType=="Text"||controlType=="Date"||controlType=="DateTime"){
                var c=$(this).find('input');
                c.val(bindScript);
                if(c.val()==""){
                    c.val(initScript);
                }
                if(value!=null){
                    c.val(value);
                }
            }else if(controlType=="DropDownList"){
                var c=$(this).find('select');
                var init = initScript.toString().split(',');
                for(var i=0;i<init.length;i++){
                    var o=init[i];
                    c.append("<option value='{value}'>{value}</option>".replaceAll("{value}",o));
                }
                var value2=bindScript;
                if(value!=null){
                    value2=value;
                }
                if(c.attr('multiple')!=undefined){
                    c.val(value2.split(','));
                }else{
                    c.val(value2);
                }

            }else if(controlType=="Content"){
                var c=$(this).find('span[type="content"]');
                c.html(bindScript);
                if(c.html()==""){
                    c.html(initScript);
                }
                if(value!=null){
                    c.html(value);
                }
            }else if(controlType=="File"){
                var c=$(this).find('input[datatype="file"]');
                c.val(bindScript);
                if(c.val()==""){
                    c.val(initScript);
                }
                if(value!=null){
                    c.val(value);
                }
                c.val(c.val()).change();
            }
            if(access=="Read"){
                readonly(this);
            }
        }
    });
}
function sys_form_submit(){
    console.log("系统提交");
    var form_submit= {};var error="";
    $('div[fieldtype]').each(function(i){
        var fieldtype = $(this).attr("fieldtype");
        if(fieldtype=="Control"){
            //console.log(this);
            var controlType = $(this).attr("controlType");
            var bindVariable=$(this).attr("bindVariable");
            var access = $(this).attr("access");
            var require=$(this).attr("require");
            var regexFormat=$(this).attr("regexFormat");
            var regexError=$(this).attr("regexError");
            var id=$(this).attr("id");
            if(bindVariable==null||bindVariable==""||access!="Write"){
                return true;
            }
            var value = "";
            if(controlType=="TextArea"){
                var c=$(this).find('textarea');
                value=c.val();
            }else if(controlType=="Text"||controlType=="Date"||controlType=="DateTime"){
                var c=$(this).find('input');
                value=c.val();
            }else if(controlType=="DropDownList"){
                var c=$(this).find('select');
                if(c.attr('multiple')!=undefined){
                    var value2=c.val();
                    if(value2==null){value2=[];}
                    value=value2.join(',');
                }else{
                    value=c.val();
                }
            }else if(controlType=="Content"){
                //
            }else if(controlType=="File"){
                var c=$(this).find('input[datatype="file"]');
                value=c.val();
            }
            if(require.toLowerCase()=="true"&&value==""){
                error+=id+"内容不能为空!\r\n";
            }
            if(regexFormat!=null&&regexFormat!=""){
                if(!eval(regexFormat).test(value)) {
                    error += id + regexError + "\r\n";
                }
            }
            form_submit[bindVariable]=value;
        }
    });
    form_submit["system_error"]=error;
    return form_submit;
}
function checkSystemError(submitVariables){
    if(submitVariables['system_error']!=''){alert(submitVariables['system_error']);return true;}
    return false;
}
function filterNoWriteAccessVariable(submit_variable){
    var variables={};
    for(var i=0,l=submit_variable.length;i<l;i++){
        for(var key in submit_variable[i]){
            if(write_variables.hasOwnProperty(key)){
                variables.put(key,submit_variable[i][key]);
            }
        }
    }
    return variables;
}

function changeUrlArg(url, arg, val){
    var pattern = arg+'=([^&]*)';
    var replaceText = arg+'='+val;
    return url.match(pattern) ? url.replace(eval('/('+ arg+'=)([^&]*)/gi'), replaceText) : (url.match('[\?]') ? url+'&'+replaceText : url+'?'+replaceText);
}

function goTo() {
    window.location = changeUrlArg(window.location.href.toString(), "node", $('#sys_debug_node').val());
}