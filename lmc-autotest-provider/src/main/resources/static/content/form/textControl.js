var textControl = function (){
    baseControl.call(this);
    this.type="text";
    this.text="文本框";
    this.img="/content/form/img/textControl.png";
    this.width="300px";
    this.widthPercent=30;
    this.height="20px";
    /*自定义*/
    this.placeholder="";
    this.defaultValue="";
    this.regex="";
    this.regexError="";
    this._controlPropertyTemplate="<ul>" +
        "<li><label>默认值:</label><input json='defaultValue' level='1' type='text' style='width: 200x'/></li>" +
        "<li><label>提示值:</label><input json='placeholder' level='1' type='text' style='width: 200px'/></li>" +
        "<li><label>内容校验:</label><select json='regex' level='1'><option value='nullCheck'>空检查</option></select></li>" +
        "<li><label>错误提示:</label><input json='regexError' level='1' type='text' style='width: 200px'/></li>" +
        "</ul>";
    this.template=textControl.prototype.template=function (){
        return baseControl.prototype.template.call(this).replace("{template}","<label>{text}</label><input style='width:auto;height:auto;flex-grow: 1; ' placeholder='{placeholder}' />");
    }
    this.propertyHtml=textControl.prototype.propertyHtml=function (){
         let ps= baseControl.prototype.propertyHtml.call(this).concat([{tab:"控件配置",html:this._controlPropertyTemplate}]);
         return ps;
    }
    this.load=textControl.prototype.load = function (){
        if(this.dom().length>0){
            this.dom().find("input").attr("placeholder",this.placeholder);
            this.dom().find("input").attr("defaultValue",this.defaultValue);
            this.dom().find("input").attr("regex",this.regex);
            this.dom().find("input").attr("regexError",this.regexError);
        }
        baseControl.prototype.load.call(this);
    }
    this.getPropertyJson=textControl.prototype.getPropertyJson=function (){
        let o= baseControl.prototype.getPropertyJson.call(this);
        o["defaultValue"]=this.defaultValue;
        o["placeholder"]=this.placeholder;
        o["regex"]=this.regex;
        o["regexError"]=this.regexError;
        return o;
    }
}