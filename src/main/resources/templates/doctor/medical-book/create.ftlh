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

    <link rel="stylesheet" href="/header.css">
    <link rel="stylesheet" href="/global.css">

    <title>Note</title>

</head>
<body>

<#import '../../parts/header.ftlh' as h>
<@h.header 'doctor' "/doctor" "" ""/>


<div class="container">
    <div class="row">
        <div class="col-md-6 col-md-offset-3">
            <div class="panel panel-default" style="margin-top:45px">
                <div class="panel-heading">
                    <h3 class="panel-title"><@spring.message ("addingNote") /></h3>
                </div>
                <div class="panel-body">
                    <form action="/doctor/patients/${patient.id}/medical-book/create-note" method="post">
                        <input name="${_csrf.parameterName}" value="${_csrf.token}" type="hidden">
                        <div class="form-group">
                            <label for="diagnosis"><@spring.message ("diagnosis") /></label>
                            <input required type="text" class="form-control" id="diagnosis"
                                   placeholder="<@spring.message ("diagnosis") />"
                                   name="diagnosis">
                        </div>
                        <div class="form-group">
                            <label for="procedures"><@spring.message ("procedures") /></label>
                            <textarea required  class="form-control" id="procedures"
                                      placeholder="<@spring.message ("procedures") />"
                                      name="procedures"></textarea>
                        </div>
                        <div class="form-group">
                            <label for="surgery"><@spring.message ("surgery") /></label>
                            <textarea  class="form-control" id="surgery"
                                      placeholder="<@spring.message ("surgery") />"
                                      name="surgery"></textarea>
                        </div>
                        <button type="submit" class="btn btn-success"><@spring.message ("add") /></button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>