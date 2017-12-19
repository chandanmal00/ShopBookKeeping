<html>
<head>
    <#include "/common/meta.ftl">
    <#include "/css/head.css">
    <title>${APP_TITLE} ${ENTITY_NAME}: daily summary Page for last ${DAY} days</title>
</head>

<body>
    <div class="container-fluid">
        <#include "/common/header.ftl">
        <br>
        <#include "/common/errors.ftl">
        <#if endingDate??>
            <h2>${ENTITY_NAME}: ${summaryType} summary Page for last ${DAY} ${type} ending date: <small> ${endingDate} </small></h2>
        <#else>
            <h2>${ENTITY_NAME}: ${summaryType} summary Page for last ${DAY} ${type}</h2>
        </#if>
        <#assign ENTITY_TABLE_NAME = ENTITY_NAME>
        <#include "/common/table/list_daily_table.ftl">

    </div>
    <#include "/common/footer.ftl"> 
    <#include "/js/footer_js.ftl">
    <#include "/js/alerts_js.ftl">

</body>
</html>