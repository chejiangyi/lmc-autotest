/**
 * dialog 配置和json转换
 * @param dialogId
 */
// dialog 转 json
function dialogToJson(dialogId,json){
    var d = $('#'+dialogId);
    // var nodeKey = d.find('[name="key"]').attr('value');
    // var node=myDiagram.findNodeForKey(nodeKey);
    // var json = (node.part.data.json==null?{}:node.part.data.json);
    jsonBind(d,json,1);
    // node.part.data.json=json;
 }
function jsonBind(element,json,level){
    element.find('[json][level="'+level+'"]').each(function(i){
        var key = $(this).attr('json');
        var jsontype = $(this).attr('jsontype');
        if ("list" == jsontype) {
            json[key] = [];
            var parentElement = $(this);
            parentElement.children().each(function (e) {
                var t = {};
                jsonBind($(this), t, level+1);
                json[key].push(t);
            });
        } else if ("array" == jsontype) {
            json[key] = $(this).val().split(',');
        } else if("form"==jsontype){
            // var parentElement = $(this);
            json[key]={};
            jsonBind($(this), json[key], level+1);
        }
        else {
            // if (key == 'title') {
            //     var title= $(this).val();
            //     myDiagram.model.commit(function(m) {
            //         var nodeData = m.findNodeDataForKey(nodeKey);
            //         m.setDataProperty(nodeData, 'title', title);
            //     },"更新title");
            //     json[key] = $(this).val();
            // } else
            //     if (key == 'type') {
            //     json[key] = $(this).attr('data');
            // } else {
            console.log($(this).val());
                json[key] = $(this).val();
            // }
        }
    });
}

// json 转 dialog
function jsonToDialog(dialogId,json){
    var d = $('#'+dialogId);
    //d.find('[name="key"]').attr('value',nodeKey);
    bindJson(d,json,1);
}
function bindJson(element,json,level){
    if(json==null){
        json={};
    }
    element.find('[json][level="'+level+'"]').each(function(i){
        var key = $(this).attr('json');
        var jsontype = $(this).attr('jsontype');
        if("list"==jsontype){
            //var parent = $(this);
            var js=json[key];
            $(this).html("");
            for(var index in js){
                var template=$('#'+$(this).attr('template')).clone(true).children();
                var j =js[index];
                bindJson(template,j,level+1);
                $(this).append(template);
            }
        }
        else if("array"==jsontype){
            $(this).val(json[key]==null?"":json[key].join(','));
        }
        else if("form"==jsontype){
            //var parent = $(this);
            bindJson($(this),json[key],level+1);
        }
        else {
            // if(key=='title'){
            //     json[key]=node.part.data.title;
            // }
            try {

                $(this).val(json[key]);
            }catch (e){
                console.debug(this);
            }
        }
    });
}

// json 转 json
function bindJsonToJson(from,to){
    console.log(from);
    for(let key of Object.keys(from)){
        to[key]=from[key];
    }
}

function compareJson(property,order){
    return function(a,b){
        let value1 = a[property];
        let value2 = b[property];
        if(order=="asc") {
            return value1 - value2;
        }else{
            return value2 - value1;
        }
    }
}

