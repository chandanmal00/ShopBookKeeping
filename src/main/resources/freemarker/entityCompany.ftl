<html>
<head>
    <#include "/common/meta.ftl">
    <#include "/css/head.css">
    <title>${APP_TITLE} ${ENTITY_NAME}: ${entity.uniqueKey!""} Home</title>
</head>

<body>
<div class="container-fluid">
    <#include "/common/header.ftl">
    <br>
    <h4><strong>${ENTITY_NAME} Profile: ${entity.uniqueKey} <a href="/edit/${ENTITY_NAME}/${entity.uniqueKey}"><i class="fa fa-pencil-square-o" aria-hidden="true"></i></a></strong></strong></h4>
    <div>
        <#include "/common/table/profileTable.ftl">
    </div>

    <h3>Items bought from ${ENTITY_NAME}:${entity.uniqueKey}</h3>
    <#if entityList??>
    <div>
            <#include "/common/table/list_itemBuy_table.ftl">
        </div>

    </#if>
</div>
    <#include "/js/footer_js.ftl">
    <#include "/js/alerts_js.ftl">
</body>
</html>