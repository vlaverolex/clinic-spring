<#import "/spring.ftl" as spring/>
<!DOCTYPE html>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- Latest compiled and minified CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"
          integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
    <!-- Optional theme -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css"
          integrity="sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp" crossorigin="anonymous">
</head>
<body>
<div class="container">
    <div class="row">
        <div class="col-md-6 col-md-offset-3">
            <div class="panel panel-default" style="margin-top:45px">
                <div class="panel-heading">
                    <h3 class="panel-title"><@spring.message ("login") /></h3>
                    <div class="lang-choose" >
                        <a href="?lang=en">EN</a>
                        <span class="lang-choose__separator">/</span>
                        <a href="?lang=ua">UA</a>
                    </div>
                </div>
                <div class="panel-body">
                    <#if logout>
                        <div class="alert alert-info" role="alert"><@spring.message ("loggedOutSuccessfully") /></div>
                    </#if>
                    <#if error>
                        <div class="alert alert-danger" role="alert"><@spring.message ("invalidUsernameOrPassword") /></div>
                    </#if>

                    <form action="/login" method="post">
                        <input name="${_csrf.parameterName}" value="${_csrf.token}" type="hidden">
                        <div class="form-group">
                            <label for="username"><@spring.message ("username") /></label>
                            <input required type="text" class="form-control" id="username" placeholder="<@spring.message ("username") />"
                                   name="username">
                        </div>
                        <div class="form-group">
                            <label for="password"><@spring.message ("password") /></label>
                            <input required type="password" class="form-control" id="password" placeholder="<@spring.message ("password") />"
                                   name="password">
                        </div>
                        <button type="submit" class="btn btn-primary"><@spring.message ("LogIn") /></button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>