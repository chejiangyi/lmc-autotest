
function yhPost(url, data, success, error) {
    yhAjax('post', true,url, { 'Content-Type': 'application/json;charset=utf8' }, data, success, error)
}
function yhGet(url, data, success, error) {
    yhAjax('get',true, url, { 'Content-Type': 'application/json;charset=utf8' }, data, success, error)
}

function yhAjax(type, async,url, headers, ajaxData, success, error) {
    headers=Object.assign(headers,{"login-token":localStorage.getItem('login-token')});
    $.ajax({
        url: yhurl+url,
        type: type,
        //dateType:'json',
        dataType: 'json',
        // beforeSend: function (xhr) {
        //     xhr.setRequestHeader("login-token:'" + localStorage.getItem('login-token') + "'")
        // },
        headers: headers,
        data: JSON.stringify(ajaxData),
        async: async,
        success: function (data) {
            if (success != null) success(data)
        },
        error: function (data) {
            //token无效
            if (
                data.code == 600207 ||
                data.code == 600201 ||
                data.code == 400001001 ||
                data.code == 160100005 ||
                data.code == 160100008 ||
                data.code == 160100009 ||
                data.code == 160100010
            ) {
                var token = login(yhuser, yhpass);
                if(token!=""){
                    localStorage.setItem("login-token",token);
                    return yhAjax(type, url, headers, ajaxData, success, error);
                }
            }
            if (error != null) error(data)
        },
    })
}
function login(username, password) {
    if(username=="")
        return "";
    var token = "";
    $.ajax({
        url: yhurl+'/ucenter/login/web',
        type: 'post',
        dateType: 'json',
        data: JSON.stringify({
            password: password,
            username: username,
        }),
        processData: false,
        async: false,
        headers: {
            'content-type': 'application/json;charset=UTF-8',
        },
        success: function (data, status, xhr) {
            if (data.code == 200000) {
                console.log(data, xhr.getResponseHeader('login-token'))
                token= xhr.getResponseHeader('login-token');
            }else{
                console.warn(window.location+"模拟登陆用户失败");
            }
        },
        fail(err) {
        },
    });
    return token;
}
//检测token是否有效,并模拟登陆
//checkToken();
function checkToken(){
    $.ajax({
        url: yhurl+"/ucenter/login/check/token?token="+localStorage.getItem("login-token"),
        type: 'get',
        dateType: 'json',
        data: "",
        processData: false,
        async: false,
        headers: {
            'content-type': 'application/json;charset=UTF-8',
        },
        success: function (data, status, xhr) {
            if (data.code == 200000&&data.data!=null) {
                console.log("token有效");
            }else{
                var token = login(yhuser, yhpass);
                if(token!="") {
                    localStorage.setItem("login-token", token);
                }
            }
        },
        fail(err) {
        },
    });
}
