<!DOCTYPE html>
<html lang="en">
  <head>
        <#include "/common/meta.ftl">
        <#include "/css/head.css">
        <#include "/css/profile.css">
        <title>Search Page for Jain Traveller</title>

    <#if username??>
        <title>${APP_TITLE} search listings for ${username}</title>
    <#else>
        <title>${APP_TITLE} search listings for guest</title>
    </#if>

    </head>
    <body>
        <div class="container">
            <#include "/common/header.ftl">
            <h2>Results for ${search}, Page: ${index}, Total Results:${count} </h2>
            <#if countPages gt 0>
                <nav>
                    <#include "/common/pagination.ftl">
                </nav>
            </#if>

            </p>
            <#assign cnt = 0>
            <#list searchHitList as hit>
                <#assign post=hit.getSource()>
                <#include "/common/listing_display.ftl">
            </#list>

            <#if countPages gt 0>
             <#include "/common/pagination.ftl">
            </#if>

        </div>
        <#include "/js/footer_js.ftl">
        <#include "/js/alerts_js.ftl">
        <#include "/js/carousel_js.ftl">
        <#include "/js/ajax_refresh_js.ftl">
    </body>
</html>

