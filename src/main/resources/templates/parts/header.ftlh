<#import "/spring.ftl" as spring/>
<#macro header logo url page sort>
    <header class="header">
        <div class="container">
            <div class="header__inner">
                <div class="header__inner-logo">
                    <a href="${url}"><@spring.message ("${logo}") /></a>
                </div>

                <div class="lang-choose">
                    <#if page?has_content || sort?has_content>
                        <#if page?has_content && sort?has_content>
                            <a href="?lang=en&${page}&${sort}">EN</a>
                            <span class="lang-choose__separator">/</span>
                            <a href="?lang=ua&${page}&${sort}">UA</a>
                        <#elseif page?has_content>
                            <a href="?lang=en&${page}">EN</a>
                            <span class="lang-choose__separator">/</span>
                            <a href="?lang=ua&${page}">UA</a>
                        <#else >
                            <a href="?lang=en&${sort}">EN</a>
                            <span class="lang-choose__separator">/</span>
                            <a href="?lang=ua&${sort}">UA</a>
                        </#if>
                    <#else >
                        <a href="?lang=en">EN</a>
                        <span class="lang-choose__separator">/</span>
                        <a href="?lang=ua">UA</a>
                    </#if>


                </div>

                <div class="header__inner-menu">
                    <a href="/logout"><@spring.message ("logOut") /></a>
                </div>
            </div>
        </div>
    </header>
</#macro>