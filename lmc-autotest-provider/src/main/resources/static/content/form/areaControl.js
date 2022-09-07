var areaControl = function (){
    baseControl.call(this);
    this.type="area";
    this.text="区域";
    this.img="/content/form/img/areaControl.png";
    this.width="300px";
    this.widthPercent=30;
    this.height="100px";

    this.template=areaControl.prototype.template=function (){
        return baseControl.prototype.template.call(this).replace("{template}","");
    }
}
