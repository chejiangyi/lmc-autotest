var LkApi = function (){
    this.success=2000;
    this.fail=500;
    this.saveForm=function (request,func){
        // var mock ={form_key:"",form_type:1,form_name:"",form_design_type:0,form_design_model:"",form_control_property_model:""};
        $.post("/api/saveform/", request, function(response){
            func(response);
        });
    }
    this.getForm=function (request,func){
        //var mock={form_key:""}
        $.post("/api/getform/", request, function(response){
            console.log(response);
            func(response);
        });
    }
    this.deleteForm=function (request,func){
        $.post("/api/deleteform/", request, function(response){
            func(response);
        });
    }

}
var LKApiObj=new LkApi();