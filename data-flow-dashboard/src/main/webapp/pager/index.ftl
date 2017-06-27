<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <!-- Meta, title, CSS, favicons, etc. -->
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>DataFlow</title>

    <link href="https://cdn.bootcss.com/bootstrap/3.3.6/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/font-awesome/4.6.3/css/font-awesome.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/nprogress/0.2.0/nprogress.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/animate.css/3.5.0/animate.min.css" rel="stylesheet">
    <!-- Custom Theme Style -->
    <link href="styles/custom/custom.min.css" rel="stylesheet">
</head>

<body class="login">
<div>
    <a class="hiddenanchor" id="signup"></a>
    <a class="hiddenanchor" id="signin"></a>

    <div class="login_wrapper">
        <div class="animate form login_form">
            <section class="login_content">
                <form id="loginForm">
                    <h1>Login Form</h1>
                    <div>
                        <input id="username" type="text" class="form-control" placeholder="Username"
                               required="required"/>
                    </div>
                    <div>
                        <input id="password" type="password" class="form-control" placeholder="Password"
                               required="required"/>
                    </div>
                    <div>
                        <a class="btn btn-default submit" id="login">Log in</a>
                    <#--<a class="reset_pass" href="#">Lost your password?</a>-->
                    </div>

                    <div class="clearfix"></div>

                    <div class="separator">
                    <#--<p class="change_link">New to site?-->
                    <#--<a href="#signup" class="to_register"> Create Account </a>-->
                    <#--</p>-->

                        <div class="clearfix"></div>
                        <br/>

                        <div>
                            <h1><i class="fa fa-paw"></i> Data flow</h1>
                        </div>
                    </div>
                </form>
            </section>
        </div>
    </div>
</div>
<script src="https://cdn.bootcss.com/jquery/2.2.4/jquery.min.js"></script>
<script src="javascript/validator/validator.min.js"></script>
<script>
    var login = function () {
        if (!validator.checkAll($("#loginForm"))) {
            return false;
        }
        var username = $("#username").val();
        var password = $("#password").val();
        $.ajax({
            type: 'POST',
            url: "login",
            data: {username: username, password: password},
            dataType: "json"
        }).done(function (data) {
            if (data.responseStatus == 200) {
                window.location.reload();
            } else {
                console.log(data.errorMessage)
            }
        })
    };
    $("#login").on("click", login)

    $(document).keydown(function (event) {
        if (event.which == 13) {
            login()
        }
    });
</script>
</body>
</html>
