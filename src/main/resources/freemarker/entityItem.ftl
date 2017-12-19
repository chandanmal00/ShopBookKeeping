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


    <h4><strong>${ENTITY_NAME} Profile: ${entity.getBarcode()} <a href="/edit/${ENTITY_NAME}/${entity.getBarcode()}"><i class="fa fa-pencil-square-o" aria-hidden="true"></i></a></strong></strong></h4>
    <div>
        <#include "/common/table/profileItemTable.ftl">
    </div>


    <#if entityList??>
    <h3>Item inventory status: <small>${entity.uniqueKey}</small></h3>
    <div>
            <#include "/common/table/list_inventory_table.ftl">
        </div>

    </#if>


        <#if transactions??>
        <h3>Transaction details for Item: <small>${entity.uniqueKey}</small></h3>
        <#assign entityList=transactions>
        <div>
                <#include "/common/table/itemTransaction_table.ftl">
                <#--<#include "/common/table/itemTransactionEntity_table.ftl">-->
            </div>

        </#if>
</div>
    <#include "/js/footer_js.ftl">
    <#include "/js/alerts_js.ftl">
</body>
</html>