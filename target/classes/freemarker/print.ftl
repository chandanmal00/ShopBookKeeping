<html>
<head>
    <#include "/common/meta.ftl">
    <#include "/css/head.css">
    <title>${APP_TITLE} PRINT ${ENTITY_NAME}: for key ${key} days</title>
</head>

<body>
    <div class="container-fluid">
        <#include "/common/header.ftl">
        <br>
        <#include "/common/errors.ftl">
        <#if entityObject??>
            <h2>${ENTITY_NAME}: ${summaryType} summary Page for last ${DAY} ${type} ending date ${endingDate}</h2>
        <#else>
            <h2>${ENTITY_NAME}: ${summaryType} summary Page for last ${DAY} ${type}</h2>
        </#if>
        <#include "/common/table/list_daily_table.ftl">

    </div>
    <#include "/js/footer_js.ftl">
    <#include "/js/alerts_js.ftl">

</body>
</html>

