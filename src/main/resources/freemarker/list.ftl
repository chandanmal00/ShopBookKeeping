
<html>
<head>
    <#include "/common/meta.ftl">
    <#include "/css/head.css">
    <title>${APP_TITLE} ${ENTITY_NAME}: ${local["List"]} Page</title>
</head>

<body>
    <div class="container-fluid">
        <#include "/common/header.ftl">
        <br>
        <#if entity=="customer" >
        <#include "/common/table/${entity}_better_table.ftl">
        <#elseif entity == "salesman">
        <#include "/common/table/${entity}_better_table.ftl">
        <#else>
        <#include "/common/table/${entity}_table.ftl">
        </#if>

    </div>
    <#include "/common/footer.ftl">
    <#include "/js/footer_js.ftl">
    <#include "/js/alerts_js.ftl">

</body>
</html>