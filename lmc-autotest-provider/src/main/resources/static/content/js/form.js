function init() {
    var objGo = go.GraphObject.make;  // for conciseness in defining templates

    var contextMenu =
        objGo("ContextMenu",
            // objGo("ContextMenuButton",
            //     objGo(go.TextBlock, "发布"),
            //     {
            //         click: function (e, obj) {
            //             menu("发布",e,obj);
            //         }
            //     }),
            // objGo("ContextMenuButton",
            //     objGo(go.TextBlock, "测试"),
            //     {
            //         click: function (e, obj) {
            //             menu("测试",e,obj);
            //         }
            //     }),
            {width:100}
        );

    myDiagram =
        objGo(go.Diagram, "myDiagramDiv",  // must name or refer to the DIV HTML element
            {
                grid: objGo(go.Panel, "Grid",
                    objGo(go.Shape, "LineH", { stroke: "lightgray", strokeWidth: 0.5 }),
                    objGo(go.Shape, "LineH", { stroke: "gray", strokeWidth: 0.5, interval: 10 }),
                    objGo(go.Shape, "LineV", { stroke: "lightgray", strokeWidth: 0.5 }),
                    objGo(go.Shape, "LineV", { stroke: "gray", strokeWidth: 0.5, interval: 10 })
                ),
                initialContentAlignment: go.Spot.LeftSide, // 居中显示内容
                allowDrop: true,  // must be true to accept drops from the Palette
                "undoManager.isEnabled": true
            });
    myDiagram.nodeTemplate =
        objGo(go.Node, "Spot",
            { locationSpot: go.Spot.Center },
            new go.Binding("location", "location", go.Point.parse).makeTwoWay(go.Point.stringify),
            new go.Binding("json", "json").makeTwoWay(),
            objGo(go.Panel, "Auto",
                new go.Binding("desiredSize", "size", go.Size.parse).makeTwoWay(go.Size.stringify),
                objGo(go.Shape, "Rectangle",  // default figure
                    {
                        portId: "",
                        cursor: "pointer",
                        strokeWidth: 2,
                        fromLinkable:false,
                        toLinkable:false
                    },new go.Binding("fill","background").makeTwoWay()),
                    // ,new go.Binding("fromLinkable","fromLinkable").makeTwoWay()
                    // ,new go.Binding("toLinkable","toLinkable").makeTwoWay()),
                objGo(go.TextBlock,
                    {
                        margin: 8,
                        maxSize: new go.Size(160, NaN),
                        wrap: go.TextBlock.WrapFit,
                        //editable: true
                    },
                    new go.Binding("text","title").makeTwoWay())
            ),
            {doubleClick : function (e, node){nodeDoubleClick(e, node);}},
            {contextMenu:contextMenu}
        );

    // myDiagram.linkTemplate =
    //     objGo(go.Link,
    //         {
    //             routing: go.Link.Normal,
    //             curve: go.Link.JumpOver,
    //         },
    //         new go.Binding("points").makeTwoWay(),
    //         objGo(go.Shape,  // the link path shape
    //             { isPanelMain: true, strokeWidth: 2 }),
    //         objGo(go.Shape,  // the arrowhead
    //             { toArrow: "Standard", stroke: null }),
    //         objGo(go.TextBlock,  //用来显示线上的label
    //             {
    //                 textAlign: "center",
    //                 segmentOffset: new go.Point(0, -10),
    //                 font: "10pt helvetica, arial, sans-serif",
    //                 stroke: "#555555",
    //                 margin: 4
    //             },
    //             new go.Binding("text", "title").makeTwoWay()
    //         ),
    //         {doubleClick : function (e, link){linkDoubleClick(e, link);}},
    //     );

    // //添加监听线生成事件
    // myDiagram.addDiagramListener("LinkDrawn", function(e) {
    //     // //一个节点只能一根线
    //     // var node = myDiagram.findNodeForKey(e.subject.data.from);
    //     // if(node.findLinksOutOf().count>1) {
    //     //     myDiagram.model.removeLinkData(e.subject.data);
    //     // }
    // })
    //监听节点生成事件
    //var nodeIndex="-new";
    myDiagram.addDiagramListener("externalobjectsdropped", function(e) {
        e.subject.each(function(n){
            var nodeData= myDiagram.model.findNodeDataForKey(n.data.key);
            myDiagram.model.setDataProperty(nodeData, 'title', n.data.title);
        });
    })

    myPalette =
        objGo(go.Palette, "myPaletteDiv",
            {
                maxSelectionCount: 1,
                nodeTemplateMap: myDiagram.nodeTemplateMap,  // share the templates used by myDiagram
                model: new go.GraphLinksModel([  // specify the contents of the Palette
                    Object.create(customNodeTemplate.Control),
                    Object.create(customNodeTemplate.Custom)
                ])
            });

    //load();  // load an initial diagram from some JSON text
    myDiagram.model.linkKeyProperty="key";
    myDiagram.model.copyNodeDataFunction= function (obj,m){
        return  copyNode(obj,m);
    }
}

function copyNode(obj,m){
    var n = obj;
    if(n.type=="Start"||n.type=="End"){
        return null;
    }
    var a={};
    $.extend(true,a, obj);
    return a;
}

/***
 * 节点模板
 */
var customNodeTemplate={
    "Control":{ title: "标准控件", type:"Control", background: "#99FF99",fromLinkable:true,toLinkable:true,dialog:"control-dialog",json:null},
    "Custom":{ title: "自定义", type:"Custom", background: "#FFFF99",fromLinkable:true,toLinkable:true,dialog:"custom-dialog",json:null},
  };
//var customLinkTemplate={"from": "", "to": "","title":"","json":{"script":"","title":"","next":""}};

/**
 * dialog 配置和json转换
 * @param dialogId
 */
function dialogToJson(dialogId){
    var d = $('#'+dialogId);
    var nodeKey = d.find('[name="key"]').attr('value');
    var node=myDiagram.findNodeForKey(nodeKey);
    var json = (node.part.data.json==null?{}:node.part.data.json);
    jsonBind(d,json,node,nodeKey,1);
    node.part.data.json=json;
}
function jsonBind(element,json,node,nodeKey,level){
    element.find('[json][level="'+level+'"]').each(function(i){
        var key = $(this).attr('json');
        var jsontype = $(this).attr('jsontype');
        if ("list" == jsontype) {
            json[key] = [];
            var parentElement = $(this);
            parentElement.children().each(function (e) {
                var t = {};
                jsonBind($(this), t, node, nodeKey,level+1);
                json[key].push(t);
            });
        } else if ("array" == jsontype) {
            json[key] = $(this).val().split(',');
        } else if("form"==jsontype){
            var parentElement = $(this);
            json[key]={};
            jsonBind($(this), json[key], node, nodeKey,level+1);
        }
        else {
            if (key == 'title') {
                var title= $(this).val();
                myDiagram.model.commit(function(m) {
                    var nodeData = m.findNodeDataForKey(nodeKey);
                    m.setDataProperty(nodeData, 'title', title);
                },"更新title");
                json[key] = $(this).val();
            } else if (key == 'type') {
                json[key] = $(this).attr('data');
            } else {
                json[key] = $(this).val();
            }
        }
    });
}
function jsonToDialog(dialogId,nodeKey){
    console.log(nodeKey);
    var d = $('#'+dialogId);
    d.find('[name="key"]').attr('value',nodeKey);
    var node=myDiagram.findNodeForKey(nodeKey);
    var json = node.part.data.json;
    bindJson(d,json,node,1);
}

function bindJson(element,json,node,level){
    if(json==null){
        json={};
    }
    element.find('[json][level="'+level+'"]').each(function(i){
        var key = $(this).attr('json');
        var jsontype = $(this).attr('jsontype');
        if("list"==jsontype){
            var parent = $(this);
            var js=json[key];
            $(this).html("");
            for(var index in js){
                var template=$('#'+$(this).attr('template')).clone(true).children();
                var j =js[index];
                bindJson(template,j,node,level+1);
                $(this).append(template);
            }
        }
        else if("array"==jsontype){
            $(this).val(json[key]==null?"":json[key].join(','));
        }
        else if("form"==jsontype){
            var parent = $(this);
            bindJson($(this),json[key],node,level+1);
        }
        else {
            if(key=='title'){
                json[key]=node.part.data.title;
            }
            try {
                $(this).val(json[key]);
            }catch (e){
                console.debug(this);
            }
        }
    });
}

/** 保存，加载*/
function modelToJson(){
    //debugger;
    var nodeJson=[];
    for(var n=myDiagram.nodes;n.next();) {
        var node = n.value;
        if(node.part.data.json==null){
            alert(node.part.data.title+"节点未配置,请配置后保存！");
            return null;
        }else {
            node.part.data.json['location'] = node.part.data.location;
            node.part.data.json['title'] = node.part.data.title;
            nodeJson.push(node.part.data.json);
        }
    }
    var j = {};j.fields=nodeJson;
    return j;
}

function jsonToModel(json){
     var model = { "class": "go.GraphLinksModel", "nodeDataArray": [], "linkDataArray": []};
    //add key
    var key=0;
    for(var i=0;i<json.fields.length;i++) {
        key++;
        var n = json.fields[i];
        n["key"]=-1-key;
    }
    //画节点
    for(var i=0;i<json.fields.length;i++) {
        var n = json.fields[i];
        var type = n['type'];
        var nNode = copy(customNodeTemplate[type]);
        nNode['title']=n['title'];
        nNode['location']=n['location'];
        nNode['json']=n;
        nNode['key']=n["key"];
        model.nodeDataArray.push(nNode);

    }
    return model;
}

function copy(obj){
    return JSON.parse(JSON.stringify(obj));
}

function makeBlob() {
    //通过gojs API获取画布的img对象
    var img = myDiagram.makeImage({scale: 1,});
// 将图片的src属性作为URL地址
    var url = img.src;
    var a = document.createElement('a');
    var event = new MouseEvent('click');
    a.download = '流程图片';
    a.href = url;
    a.dispatchEvent(event);
  }
