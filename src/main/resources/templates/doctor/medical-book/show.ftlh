<#import "/spring.ftl" as spring/>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css"
          integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
    <title>Nurses</title>

    <link rel="stylesheet" href="/header.css">
    <link rel="stylesheet" href="/global.css">
</head>
<body>

<#import '../../parts/header.ftlh' as h>
<@h.header 'doctor' "/doctor"/>

<section class="section section-show-all">
    <div class="container">
        <div class="section__inner section-show-all">
            <h2 class="section__title section-show-all__title">Note</h2>

            <table class="table table-striped">
                <thead>
                <tr>
                    <th scope="col">ID</th>
                    <th scope="col"><@spring.message ("username") /></th>
                    <th scope="col"><@spring.message ("diagnosis") /></th>
                    <th scope="col"><@spring.message ("procedures") /></th>
                    <#if note.surgery??>
                        <th scope="col"><@spring.message ("surgery") /></th>
                        <th scope="col"><@spring.message ("surgeryPerform") /></th>
                    </#if>
                    <th scope="col"><@spring.message ("proceduresPerform") /></th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <th scope="row">${note.id}</th>
                    <th scope="row">${patient.username}</th>
                    <th scope="row">${note.diagnosis}</th>
                    <th scope="row">${note.procedures}</th>
                    <#if note.surgery??>
                        <th scope="row">${note.surgery}</th>
                        <th scope="row">
                            <form method="post"
                                  action="/doctor/patients/${patient.id}/medical-book/note/${note.id}/surgery-done">
                                <input name="${_csrf.parameterName}" value="${_csrf.token}" type="hidden">
                                <#if note.surgeryDone==true>
                                    <@spring.message ("performed") />
                                <#else>
                                    <button type="submit" class="btn btn-success"><@spring.message ("perform") /></button>
                                </#if>
                            </form>
                        </th>
                    </#if>
                    <th scope="row">
                        <form method="post"
                              action="/doctor/patients/${patient.id}/medical-book/note/${note.id}/procedures-done">
                            <input name="${_csrf.parameterName}" value="${_csrf.token}" type="hidden">
                            <#if note.proceduresDone==true>
                                <@spring.message ("performed") />
                            <#else>
                                <button type="submit" class="btn btn-success"><@spring.message ("perform") /></button>
                            </#if>
                        </form>
                    </th>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</section>
</body>
</html>