var placeholderControl = function (){
    baseControl.call(this);
    this.type="placeholder";
    this.text="占位控件";
    this.img="/content/form/img/areaControl.png";
    this.width="300px";
    this.widthPercent=30;
    this.height="100px";

    this.template=placeholderControl.prototype.template=function (){
        return baseControl.prototype.template.call(this).replace("{template}","");
    }

    this.load=placeholderControl.prototype.load = function (){
        let dom = this.dom();
        if(dom.length>0){
            if(this.store==true) {
                dom.width(this.width);
                dom.height(this.height);
                dom.css("top", this.top);
                dom.css("left", this.left);
                dom.css("order",this.order);
                LkFormObj.refreshOrder();
                // this.insertOrderDom();
            }else{
                //默认未移动
                dom.css("position","static");
            }
        }else {
        }
    }

    this.insertOrderDom=function (){
        // //计算位置，插入元素
        // if(this.order<=1){
        //     LkFormObj.dom().prepend(this.dom());
        // }else {
        // LkFormObj.dom().prepend(this.dom());
        // LkFormObj.refreshOrder();
        // let cs = LkFormObj.getOrderControls();
        // for(var c of cs){
        //     LkFormObj.dom().append(this.dom());
        // }
    }

}