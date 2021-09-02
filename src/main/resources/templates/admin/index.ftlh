<#import "/spring.ftl" as spring/>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">

    <link rel="stylesheet" href="/header.css">
    <link rel="stylesheet" href="/global.css">

    <title>Admin</title>
</head>
<body onload="noBack();" onpageshow="if (event.persisted) noBack();" onunload="">

<#import '../parts/header.ftlh' as h>
<@h.header 'admin' "/admin" "" ""/>

<section class="section section-index">
    <div class="container">
        <div class="section__inner section-index__inner">
            <div class="section__inner-item">
                <a href="/admin/doctors"><@spring.message ("doctors") /></a>
            </div>
            <div class="section__inner-item">
                <a href="/admin/nurses"><@spring.message ("nurses") /></a>
            </div>
            <div class="section__inner-item">
                <a href="/admin/patients"><@spring.message ("patients") /></a>
            </div>
        </div>
    </div>
</section>

<script type="text/javascript">
    window.history.forward();
    function noBack() {
        window.history.forward();
    }
</script>
</body>
</html>