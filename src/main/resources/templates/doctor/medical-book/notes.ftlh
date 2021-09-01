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
    <title>Medical book</title>

    <link rel="stylesheet" href="/header.css">
    <link rel="stylesheet" href="/global.css">

    <script src="https://kit.fontawesome.com/dd59a6bb17.js" crossorigin="anonymous"></script>
</head>
<body>

<#import '../../parts/header.ftlh' as h>
<@h.header 'doctor' "/doctor" "page=${page.number}" "${sort}"/>

<section class="section section-show-all">
    <div class="container">
        <div class="section-show-all__add-button">
            <a class="btn btn-primary"
               href="/doctor/patients/${patient.id}/medical-book/create-note"><@spring.message ("newNote") /></a>
        </div>

        <div class="section__inner section-show-all">
            <h2 class="section__title section-show-all__title"><@spring.message ("medicalBook") /></h2>

            <table class="table table-striped">
                <thead>
                <tr>
                    <th scope="col">ID
                        <a href="${url}?sort=id&direction=DESC"><i class="fa fa-solid fa-arrow-down"></i></a>
                        <a href="${url}?sort=id&direction=ASC"><i class="fa fa-solid fa-arrow-up"></i></a>
                    </th>
                    <th scope="col"><@spring.message ("date") />
                        <a href="${url}?sort=creationDate&direction=DESC"><i class="fa fa-solid fa-arrow-down"></i></a>
                        <a href="${url}?sort=creationDate&direction=ASC"><i class="fa fa-solid fa-arrow-up"></i></a>
                    </th>
                    <th scope="col"><@spring.message ("view") /></th>
                </tr>
                </thead>
                <tbody>
                <#list page.content as medicalNote>
                    <tr>
                        <th scope="row">${medicalNote.id}</th>
                        <th scope="row">${medicalNote.creationDate}</th>
                        <td>
                            <a class="btn btn-success medical-note-view-link"
                               href="/doctor/patients/${patient.id}/medical-book/note/${medicalNote.id}"><@spring.message ("view") />
                                <#if (medicalNote.surgery?? && medicalNote.surgeryDone==false) || (medicalNote.procedures?? && medicalNote.proceduresDone==false)>
                                    <i class="fa fa-solid fa-eye"></i>
                                </#if>
                            </a>
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