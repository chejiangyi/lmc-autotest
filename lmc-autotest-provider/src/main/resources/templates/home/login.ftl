<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <script src="/scripts/jquery-1.8.2.min.js" type="text/javascript"></script>
    <script src="/scripts/jquery-ui-1.8.24.js" type="text/javascript"></script>
    <title>登录</title>
    <style>
        * {
            margin: 0;
            padding: 0;
        }
        .login-content {
            width: 100vw;
            height: 100vh;
            margin: 0 auto;
            background-image: url("/content/images/login_bg.jpg");
            background-repeat: no-repeat;
            background-position: center center;
            background-size: cover;
            display: flex;
            align-items: center;
            justify-content: center;
        }
        .login-modal-body {
            width: 512px;
            height: 400px;
        }
        .modal-content {
            width: 100%;
            height: 100%;
            border-radius: 4px;
            background: rgba(241, 227, 199, 1);
            margin-top: 95px;
            padding: 40px 48px;
            box-sizing: border-box;
        }
        .modal-content > h3 {
            width: 100%;
            height: 48px;
            text-align: center;
            font-family: PingFangSC-Medium;
            font-size: 24px;
            font-weight: 500;
            line-height: 24px;
            color: #3d3d3d;
            margin-bottom: 40px;
        }
        .modal-content > div {
            width: 416px;
            height: 56px;
            display: flex;
            align-items: center;
            border-radius: 6px;
            background: #fff7e8;
            box-sizing: border-box;
            border: 1px solid rgba(102, 102, 102, 0.35);
        }
        .modal-content > div > img {
            margin: 0 4px 0 16px;
        }
        .login-name,
        .login-password {
            flex: 1 1 auto;
            height: 100%;
            color: #262626;
            font-weight: 500;
            outline: none;
            background: #fff7e8;
            border: none;
            font-size: 16px;
            border-radius: 6px;
        }
        .login-name::placeholder,
        .login-password::placeholder {
            font-size: 16px;
            color: #bfbfbf;
        }
        .modal-content > div:nth-child(2) {
            margin-bottom: 24px;
        }
        .modal-content > div:nth-child(3) {
            margin-bottom: 40px;
        }
        .login-button {
            width: 100%;
            height: 56px;
            border-radius: 6px;
            background: #cb2419;
            font-size: 20px;
            font-weight: 500;
            line-height: 28px;
            color: #ffffff;
            outline: none;
            border: none;
            cursor: pointer;
        }
    </style>
</head>
<body>
<div class="login-content">
    <div class="login-modal-body">
        <div class="modal-content">
            <h3>自动化测试登陆</h3>
            <div>
                <input class="login-name" type="text" placeholder="请输入用户名" />
            </div>
            <div>
                <input
                        class="login-password"
                        type="password"
                        placeholder="请输入密码"
                />
            </div>
            <button class="login-button">登录</button>
        </div>
    </div>
</div>
</body>
<script>
    $(function () {
        // 登录
        $(".login-button").click(function () {
            var name = $(".login-name").val();
            var password = $(".login-password").val();
            var loginData = {
                username: name,
                password: password,
            };
            console.log(loginData);
            $.ajax({
                url: "/login/",
                type: "post",
                contentType: "application/json",
                data: JSON.stringify(loginData),
                success: function (res) {
                    if (res.code === 200) {
                        window.location.href = "/index/";
                    }else{
                        alert(res.message)
                    }
                },
                error: function (err) {
                    console.error(err);
                },
            });
        });
    });
</script>
</html>
