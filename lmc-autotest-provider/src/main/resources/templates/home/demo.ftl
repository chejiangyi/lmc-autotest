<#import "../_layout.ftl" as layout>
${Html.s("pagetitle","demo示范")}
<@layout._layout>
    <link href="/content/form/css/absolute.css" rel="stylesheet"/>
    <script src="https://sf3-cn.feishucdn.com/obj/feishu-static/lark/passport/qrcode/LarkSSOSDKWebQRCode-1.0.2.js"></script>
<div class="head">
    <div class="title draggable">
        表单引擎-绝对定位322 ${Html.help("表单引擎-绝对定位")}
    </div>
</div>
<div>
    <div id="aaaa"></div>
    <div id="测试表单1" class="lkform" style="width: 500px;height: 800px;background-color: #FFF5EE">
    </div>
</div>

    <script>
        var goto ="https://passport.feishu.cn/suite/passport/oauth/authorize?client_id=cli_a27e07ce52ba5013&redirect_uri=${Html.g("url")}&response_type=code&state=state123456";
        var QRLoginObj = QRLogin({
            id:"aaaa",
            goto: goto ,
            width: "500",
            height: "500",
            style: "width:500px;height:600px"//可选的，二维码html标签的style属性
        });
        var handleMessage = function (event) {
            var origin = event.origin;
            // 使用 matchOrigin 方法来判断 message 来自页面的url是否合法
            if( QRLoginObj.matchOrigin(origin) ) {
                var loginTmpCode = event.data;
                alert("loginTempCode:"+loginTmpCode);
                // 在授权页面地址上拼接上参数 tmp_code，并跳转
                window.location.href = goto+'&tmp_code='+loginTmpCode;
            }
        };
        if (typeof window.addEventListener != 'undefined') {
            window.addEventListener('message', handleMessage, false);}
        else if (typeof window.attachEvent != 'undefined') {
            window.attachEvent('onmessage', handleMessage);
        }
        // $(function() {
        //     LkFormObj.load("测试表单1");
        // });
    </script>
</@layout._layout>