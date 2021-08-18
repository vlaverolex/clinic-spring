<#import "/spring.ftl" as spring/>
<#macro header logo url>
    <header class="header">
        <div class="container">
            <div class="header__inner">
                <div class="header__inner-logo">
                    <a href="${url}"><@spring.message ("${logo}") /></a>
                </div>

                <div class="lang-choose" >
                    <a href="?lang=en">EN</a>
                    <span class="lang-choose__separator">/</span>
                    <a href="?lang=ua">UA</a>
                </div>

                <div class="header__inner-menu">
                    <a href="/logout"><@spring.message ("logOut") /></a>
                </div>
            </div>
        </div>
    </header>
</#macro>