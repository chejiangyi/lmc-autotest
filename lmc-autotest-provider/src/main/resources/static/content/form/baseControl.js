
var baseControl= function (){
    this.autoKey=function(key) {
        return key+"-"+new Date().getTime().toString();
    }
    //基本配置
    this.key=this.autoKey("无");
    this.img="/content/form/img/textControl.png";
    this.text="无";
    this.type="";
    this.access=[];
    //绝对定位方式
    this.left="0px"
    this.top="0px";
    this.width="auto";
    this.height="auto";
    //相对定位方式
    this.order=0;
    this.widthPercent=0;
    this.justify="flex-start";
    //是否持久化到数据库
    this.store=true;

    //this._controlToolTemplate="<div class='lkControlTool'><a href='javascript:LkFormObj.getControl(\"{key}\").edit()'>编辑</a><a href='javascript:LkFormObj.getControl(\"{key}\").remove()'>删除</a></div>";

    this._basePropertyTemplate_absolute="<ul>" +
        "<li><label>名称:</label><input type='text' json='text' level='1' style='width:200px'/></li>" +
        "<li><label>位置:</label>X<input type='text' json='left' level='1' readonly='readonly' style='width:50px'/>,Y<input type='text' readonly='readonly' json='top' level='1' style='width:50px'/></li>" +
        "<li><label>大小:</label>宽<input type='text' json='width' level='1' readonly='readonly' style='width:100px'/>,高<input type='text' readonly='readonly' json='height' level='1' style='width:100px'/></li>" +
        "</ul>";
    this._basePropertyTemplate_flex="<ul>" +
        "<li><label>名称:</label><input type='text' json='text' level='1' style='width:200px'/></li>" +
        "<li><label>位置:</label>第<input type='text' readonly='readonly' json='order' level='1' style='width:50px'/>个</li>" +
        "<li><label>对齐:</label><select json='justify' level='1'><option value='flex-start'>居左</option><option value='center'>居中</option><option value='flex-end'>居右</option></select>" +
        "<li><label>大小:</label>宽<input type='text' json='widthPercent' level='1' readonly='readonly' style='width:100px'/>%,高<input type='text' readonly='readonly' json='height' level='1' style='width:100px'/></li>" +
        "</ul>";
    this._accessPropertyTemplate="<ul><li><input type='button' value='添加权限'/></li></ul>";

    this.template=baseControl.prototype.template=function (){
        if(LkFormObj.form_design_type==0){
            return "<div class='lkControl' key='{key}' type='{type}' style='left:{left};top:{top};width:{width};height:{height};display:flex'>{template}</div>";
        }
        else{
            return "<div class='lkControl' key='{key}' type='{type}' style='width:{widthPercent}%;height:{height};display:flex;justify-content:{justify};order:{order}'>{template}</div>";
        }
    }

    this.propertyHtml=baseControl.prototype.propertyHtml=function (){
        if(LkFormObj.form_design_type==0){
            return [{tab:"基本配置",html:this._basePropertyTemplate_absolute},{tab:"权限配置",html:this._accessPropertyTemplate}];
        }
        else{
            return [{tab:"基本配置",html:this._basePropertyTemplate_flex},{tab:"权限配置",html:this._accessPropertyTemplate}];
        }
    }
    this.dom = function (){
        return $('.lkControl[key='+this.key+']');
    }
    this.init=baseControl.prototype.init = function (from){
        bindJsonToJson(from,this);
    }
    this.edit=baseControl.prototype.edit = function (){
        let html="";
        let litab="";
        let i=0;
        for(let h of this.propertyHtml()){
            i=i+1;
            litab+="<li><a href=\"#lktabs-{index}\">{tab}</a></li>".replace("{index}",i+"").replace("{tab}",h.tab);
            html+="<div id='lktabs-{index}'>{html}</div>".replace("{index}",i+"").replace("{html}",h.html);
        }
        $('.lkTabs').html("<ul>"+litab+"</ul>"+html);
        $( ".lkTabs" ).tabs();
        jsonToDialog('lkDialogId',this);
        $('.lkDialog').dialog( "open");
        $('.lkDialog').attr("key",this.key);
    }

    this.load = baseControl.prototype.load = function (){
        let dom = this.dom();
        if(dom.length>0){
            dom.children("label").text(this.text);
            dom.width(this.width);dom.height(this.height);
            dom.css("top", this.top);
            dom.css("left", this.left);
            dom.css("order",this.order);
        }else {
            let html=this.template();
            for(let key of Object.keys(this.getDesignModel())){
               html = html.replace("{" + key + "}", this[key]);
            }
            console.log(html);
            $('.lkform').prepend(html);
            this.setState(false);
        }

    }
    this.setState=baseControl.prototype.setState = function (edit){
        let dom=this.dom();
        if(edit){
            dom.addClass("lkEdit");
            if(LkFormObj.form_design_type==0) {
                this.dom().draggable({
                    drag: function (event, ui) {
                        let key = $(this).attr("key");
                        let c = LkFormObj.getControl(key);
                        c.left = ui.position.left + "px";
                        c.top = ui.position.top + "px";
                        c.store=true;
                    }
                });
            }
            this.dom().resizable({
                resize: function( event, ui ) {
                    let key=$(this).attr("key");
                    let c = LkFormObj.getControl(key);
                    c.width = ui.size.width + "px";
                    c.height = ui.size.height + "px";
                    c.widthPercent=LkFormObj.computeWidthPercent(ui.size.width);
                    c.store=true;
                }});
            let a=1;
            //this.dom().prepend(this._controlToolTemplate.replace("{key}",this.key).replace("{key}",this.key));
        }else {
            dom.removeClass("lkEdit");
            this.dom().draggable("destroy");
            this.dom().resizable("destroy");
            //this.dom().remove('.lkControlTool');
        }
    }

    this.remove=baseControl.prototype.remove=function (){
        delete LkFormObj.getControls()[this.key];
        this.dom().remove();
    }
    this.save=baseControl.prototype.save=function (){
        dialogToJson("lkDialogId",this);
        this.load();
    }
    this.getPropertyJson=baseControl.prototype.getPropertyJson=function (){
        return {key:this.key};
    }
    this.getDesignModel=baseControl.prototype.getDesignModel=function (){
        let o={};
        for(let key of Object.keys(this)){
            if(!key.startsWith("_")&&!$.isFunction(this[key])){
                o[key]=this[key];
            }
        }
        return o;
    }

}

