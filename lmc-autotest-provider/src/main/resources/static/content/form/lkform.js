document.write("<script src=\"/content/form/api.js\"></script>");
document.write("<script src=\"/content/form/baseControl.js\"></script>");
document.write("<script src=\"/content/form/textControl.js\"></script>");
document.write("<script src=\"/content/form/areaControl.js\"></script>");
document.write("<script src=\"/content/form/placeholderControl.js\"></script>");
document.write("<script src=\"/content/form/databind.js\"></script>");
var LkForm = function (){
    //控件集合字典
    this.controls = {};
    this.id="";//form_key
    //0=主表单,1=扩展表单,2=子表单
    this.form_type=0;
    this.form_name="默认表单";
    //0=绝对定位表单,1=相对定位表单
    this.form_design_type=0;

    this.getControls=function (){
        return this.controls;
    }
    this.getControl=function (key){
        return this.controls[key];
    }
    this.dom = function (){
        return $("#"+this.id);
    }
    this.getPropertyJson=function (){
        let json = {};
        for(let key in this.controls){
            json[key]=this.controls[key].getPropertyJson();
        }
        return json;
    }
    this.getDesignModel=function (){
        let json = {};
        for(let key in this.controls){
            if(this.controls[key]["store"]==true) {
                json[key] = this.controls[key].getDesignModel();
            }
        }
        return json;
    }
    this.load=function (key){
        this.id=key;
        if(!this.dom().hasClass("lkform")){
            alert("表单必须包含lkform样式");
            return;
        }
        this.dom().mouseup(function(e){
            LkToolObj.dragEnd(e);
        });
        this.dom().mouseleave(function(e){
            LkToolObj.dragEnd(e);
        });

        //获取表单信息
        LKApiObj.getForm({form_key:this.id},function (response){
                if(response.code==LKApiObj.success) {
                    let dic={};
                    if (response != null && response.data != null) {
                         dic = JSON.parse(response.data.form_design_model);
                    }
                    //旧表单兼容
                    $(".lkControl[type='placeholder']").each(function (i){
                        let key = $(this).attr("key");
                        if(dic[key]==null) {
                            dic[key] = {
                                key: key,
                                type: "placeholder",
                                left: 0,
                                top: 0,
                                text: "系统控件",
                                width: $(this).width() + "px",
                                height: $(this).height() + "px",
                                widthPercent: LkFormObj.computeWidthPercent($(this).width()),
                                store:false
                            };
                            console.log(dic[key]);
                        }
                    });
                    if(Object.keys(dic).length>0) {
                        let jsonlist = [];
                        for (let key of Object.keys(dic)) {
                            jsonlist.push(dic[key]);
                        }
                        //根据order进行排序加载
                        jsonlist.sort(compareJson('order', "desc"));
                        console.log(jsonlist);
                        //加载表单
                        for (let j of jsonlist) {
                            let c = LkFormObj.createControl(j["type"]);
                            c.init(j);
                            c.load();
                            if(j["type"]=="placeholder"){
                                //占位符只加载一次
                                LkFormObj.dom().prepend(c.dom());
                            }
                            LkFormObj.getControls()[j["key"]] = c;
                        }
                    }
                    LkFormObj.refreshOrder();
               }
                LkFormObj.setState(false);
        });

    }
    this.refreshOrder=function (){
        this.dom().children(".lkControl").each(function(i){
            let key = $(this).attr('key');
            $(this).css('order',LkFormObj.getControl(key).order+"");
        });
    }
    this.setState=function (edit){
        let template="<div class='lkformTool'><a href='javascript:LkFormObj.edit()'>编辑表单布局</a></div>";

        if(edit){
            //this.dom().draggable();
            if(this.form_design_type!=0){
                this.dom().sortable({
                    items: ".lkControl",
                    stop: function( event, ui ) {
                        let key = ui.item.attr('key');
                        LkFormObj.getControl(key).store=true;
                        this.dom().children(".lkControl").each(function(i){
                            let key = $(this).attr('key');
                            $(this).css('order',LkFormObj.getControl(key).order+"");
                        });
                    }
                });
            }
            this.dom().resizable({maxWidth:800, maxHeight:600});
            template="<div class='lkformTool'><a href='javascript:LkFormObj.save()'>保存表单布局</a>&nbsp;&nbsp;&nbsp;<a href='LkFormObj.reset()'>重置表单布局</a></div>";
            for(let key in this.controls){
                this.controls[key].setState(true);
            }
            $.contextMenu({
                selector: "#"+this.id+" .lkControl",
                autoHide:true,
                callback: function(menu, options) {
                    let key = $(this).attr("key");
                    if(menu=="edit"){
                        LkFormObj.getControl(key).edit();
                    }else if(menu=="delete"){
                        LkFormObj.getControl(key).remove();
                    }
                },
                items: {
                    "edit": {name: "编辑", icon: "edit"},
                    "delete": {name: "删除", icon: "delete"}
                }
            });
            LkToolObj.load();
        }else{
            //this.dom().draggable("destroy");
            this.dom().sortable("destroy");
            $.contextMenu('destroy');
            for(let key in this.controls){
                this.controls[key].setState(false);
            }
            this.dom().resizable("destroy");
            LkToolObj.unload();

        }
        $('.lkformTool').remove();
        $('.lkform').prepend(template);
    }
    this.edit=function (){
        this.setState(true);
    }
    this.save=function (){
        //保存表单信息
        LKApiObj.saveForm({form_key:this.id,form_type:this.form_type,form_name:this.form_name,form_design_type:0,
            form_design_model:JSON.stringify(this.getDesignModel()), form_control_property_model:JSON.stringify(this.getPropertyJson())},function (response){
            if(response.code==LKApiObj.success) {
                LkFormObj.setState(false);
            }
        });
    }
    this.reset=function (){
        //重置表单信息，重置旧版本
        this.setState(false);
    }
    this.createControl=function (type){
        if(type=="text"){
            return new textControl();
        }else if(type=="area"){
            return new areaControl();
        }else if(type=="placeholder"){
            return new placeholderControl();
        }
    }
    this.computeWidthPercent=function (width){
        return parseFloat(width)/LkFormObj.dom().width()*100;
    }
}


var LkTool = function (){
    this.tools = ["text","area"];
    //tool 拖拽效果
    let draging=false;
    let dragtype="";
    this._template="<div class='lktool'><div><img onmousedown='LkToolObj.dragMoveStart()' onmouseup='LkToolObj.dragMoveEnd()' class='lktoolMove' src='/content/form/img/move.png'></div><div class='lktoolContainer'></div></div>";
    this._controlTemplate ="<div onmousedown='LkToolObj.dragStart(\"{type}\")'><img class='lkicon' src='{src}' alt='{text}'/><span>{text}</span></div>";
    this._controlToolTemplate="<div class='lkControlTool'><img class='lkicon' src='' alt='编辑'><img class='lkicon' src='' alt='删除'></div>";
    this._dialogTemplate="<div id='lkDialogId' class='lkDialog'><div class='lkTabs'></div></div>";
    this.load = function (){
        //加载工具栏
        LkFormObj.dom().prepend(this._template);
        LkFormObj.dom().prepend(this._dialogTemplate);
        for(let tool of this.tools){
            let o = LkFormObj.createControl(tool);
            console.log(o);
            this.dom().find('.lktoolContainer').append(this._controlTemplate.replace("{src}",o.img)
                .replace("{type}",o.type)
                .replace("{text}",o.text)
                .replace("{text}",o.text));
        }
        $('.lkDialog').dialog( {
            title:"控件信息",
            autoOpen: false,
            width:"500",
            height:"300",
            buttons: [{
                        text: "保存",
                        click: function() {
                            let key =  $('.lkDialog').attr("key");
                            let c = LkFormObj.getControl(key);
                            c.save();
                            $( this ).dialog( "close" );
                        }
                    },{
                        text: "关闭",
                        click: function() {
                            $( this ).dialog( "close" );
                        }
                }
            ],
            beforeClose:function (){
                $( ".lkTabs" ).html("");
                $( ".lkTabs" ).tabs("destroy");
        }} );

    }
    this.dragMoveStart=function (){
        this.dom().draggable();
    }
    this.dragMoveEnd=function (){
        this.dom().draggable("destroy");
    }
    this.unload = function (){
        this.dom().html("");
        $('.lkControl').removeClass("lkEdit");
        $('.lkControlTool').remove();
        $('.lkDialog').dialog( "destroy" );
        $('.lkDialog').remove();
        this.dom().remove();
    }

    this.dom=function (){
        return $('.lktool');
    }

    this.dragStart=function (type){
        draging=true;
        dragtype=type;
        LkFormObj.dom().addClass("lkdrag");
    }
    this.dragEnd=function (e){
        if(draging==true) {
            //console.log("退出"+e);
            draging = false;
            LkFormObj.dom().removeClass("lkdrag");
            let c = LkFormObj.createControl(dragtype);
            if(LkFormObj.form_design_type==0){
                c.left = e.offsetX + "px";
                c.top = e.offsetY + "px";
            }
            c.load();
            c.setState(true);
            LkFormObj.getControls()[c.key]=c;
            c.refreshActualOrder();
            //$.contextMenu('update');
        }
    }
}

//全局表单对象
var LkFormObj=new LkForm();
var LkToolObj=new LkTool();
