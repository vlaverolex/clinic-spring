<#import "/spring.ftl" as spring/>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- Latest compiled and minified CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"
          integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
    <!-- Optional theme -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css"
          integrity="sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp" crossorigin="anonymous">

    <title>Nurse</title>

    <link rel="stylesheet" href="/header.css">
    <link rel="stylesheet" href="/global.css">

</head>
<body>

<#import '../../parts/header.ftlh' as h>
<@h.header 'admin' "/admin" "" ""/>


<section class="section">
    <div class="container">
        <div>
            <a style="text-transform: capitalize !important;" class="btn btn-info back-button"
               href="/admin/nurses"><@spring.message ("back")/></a>
        </div>

        <div class="row">
            <div class="col-md-6 col-md-offset-3">
                <div class="panel panel-default" style="margin-top:45px">
                    <div class="panel-heading">
                        <h3 class="panel-title"><@spring.message ("addingNurse") /></h3>
                    </div>
                    <div class="panel-body">
                        <@spring.bind ("nurse.username")/>
                        <#if spring.status.error>
                            <#if spring.status.errorCode == "size.user.username">
                                <div class="alert alert-danger"
                                     role="alert"><@spring.message ("size.user.username")/></div>
                            <#elseif spring.status.errorCode == "duplicate.user.username">
                                <div class="alert alert-danger"
                                     role="alert"><@spring.message ("duplicate.user.username")/></div>
                            </#if>
                        <#else>
                            <@spring.bind "nurse.password"/>
                            <#if spring.status.error>
                                <#if spring.status.errorCode == "size.user.password">
                                    <div class="alert alert-danger"
                                         role="alert"><@spring.message ("size.user.password")/></div>
                                <#elseif spring.status.errorCode == "different.user.password">
                                    <div class="alert alert-danger"
                                         role="alert"><@spring.message ("different.user.password")/></div>
                                </#if>
                            </#if>
                        </#if>


                        <form action="/admin/nurses/create" method="post">
                            <input name="${_csrf.parameterName}" value="${_csrf.token}" type="hidden">
                            <div class="form-group">
                                <label for="username"><@spring.message ("username") /></label>
                                <input required type="text" class="form-control" id="username"
                                       placeholder="<@spring.message ("username") />"
                                       name="username">
                            </div>
                            <div class="form-group">
                                <label for="password"><@spring.message ("password") /></label>
                                <input required type="password" class="form-control" id="password"
                                       placeholder="<@spring.message ("password") />"
                                       name="password">
                            </div>
                            <div class="form-group">
                                <label for="confirmPassword"><@spring.message ("confirmPassword") /></label>
                                <input required type="password" class="form-control" id="confirmPassword"
                                       placeholder="<@spring.message ("confirmPassword") />"
                                       name="confirmPassword">
                            </div>
                            <div class="form-group">
                                <label for="birthday"><@spring.message ("birthday") /></label>
                                <input required type="date" class="form-control" id="birthday"
                                       placeholder="<@spring.message ("birthday") />"
                                       name="birthday" max="${maxDate.toString()}">
                            </div>
                            <button type="submit" class="btn btn-success"><@spring.message ("add") /></button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>
</body>
</html>