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

    <title>Doctors</title>

    <link rel="stylesheet" href="/header.css">
    <link rel="stylesheet" href="/global.css">

    <script src="https://kit.fontawesome.com/dd59a6bb17.js" crossorigin="anonymous"></script>
</head>
<body>

<#import '../../parts/header.ftlh' as h>
<@h.header 'admin' "/admin"/>

<section class="section section-show-all">
    <div class="container">
        <div class="section__inner section-show-all">
            <h2 class="section__title section-show-all__title"><@spring.message ("doctors")/></h2>

            <table class="table table-striped">
                <thead>
                <tr>
                    <th scope="col">ID</th>
                    <th scope="col"><@spring.message ("username")/>
                        <a href="${url}?sort=username&direction=DESC"><i class="fa fa-solid fa-arrow-down"></i></a>
                        <a href="${url}?sort=username&direction=ASC"><i class="fa fa-solid fa-arrow-up"></i></a>
                    </th>
                    <th scope="col"><@spring.message ("birthday")/>
                        <a href="${url}?sort=birthday&direction=DESC"><i class="fa fa-solid fa-arrow-down"></i></a>
                        <a href="${url}?sort=birthday&direction=ASC"><i class="fa fa-solid fa-arrow-up"></i></a>
                    </th>
                    <th scope="col"><@spring.message ("category")/></th>
                    <th scope="col"><@spring.message ("patientVolume")/>
                        <a href="${url}?sort=patientVolume&direction=DESC"><i class="fa fa-solid fa-arrow-down"></i></a>
                        <a href="${url}?sort=patientVolume&direction=ASC"><i class="fa fa-solid fa-arrow-up"></i></a>
                    </th>
                    <th scope="col"><@spring.message ("select")/></th>
                </tr>
                </thead>
                <tbody>
                <#list page.content as doctor>
                    <tr>
                        <th scope="row">${doctor.id}</th>
                        <td>${doctor.username}</td>
                        <td>${doctor.birthday}</td>
                        <td><@spring.message ("${doctor.category}")/></td>
                        <td>${doctor.patients?size}</td>
                        <td>
                            <form method="post" action="/admin/patients/${id}/add-doctor/${doctor.id}">
                                <input name="${_csrf.parameterName}" value="${_csrf.token}" type="hidden">
                                <button type="submit" class="btn btn-success"><@spring.message ("select")/></button>
                            </form>
                        </td>
                    </tr>
                </#list>
                </tbody>
            </table>
        </div>
        <#import '../../parts/pager.ftlh' as p>
        <@p.pager url page sort/>
    </div>
</section>

</body>
</html>

