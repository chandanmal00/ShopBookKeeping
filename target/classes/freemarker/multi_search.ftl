<html>
<head>
    <#include "/common/meta.ftl">
    <#include "/css/head.css">
    <title>${APP_TITLE} Multi Search Page</title>
</head>

<body>
<#assign no_results=0>
    <div class="container-fluid">
        <#include "/common/header.ftl">

        <br>

        <#if item?size gt 0>
        <#assign no_results=1>
        <#assign entityList = item>
        <#assign entityStr = "item">
        <#assign entity = "item">
        <#assign ENTITY_NAME = "Item">
        <#include "/common/table/item_table.ftl">
        </#if>

        <#if customer?size gt 0>
        <#assign no_results=1>
        <#assign entityList = customer>
        <#assign entity = "customer">
        <#assign entityStr = "customer">
        <#assign ENTITY_NAME = "Customer">
        <#include "/common/table/customer_table.ftl">
        </#if>

        <#if salesman?size gt 0>
        <#assign no_results=1>
        <#assign entityList = salesman>
        <#assign entity = "salesman">
        <#assign entityStr = "salesman">
        <#assign ENTITY_NAME = "Salesman">
        <#include "/common/table/salesman_table.ftl">
        </#if>

        <#if company?size gt 0>
        <#assign no_results=1>
            <#assign entityList = company>
            <#assign entityStr = "company">
            <#assign entity = "company">
            <#assign ENTITY_NAME = "Company">
            <#include "/common/table/company_table.ftl">
        </#if>

        <#if transaction?size gt 0>
        <#assign no_results=1>
        <#assign entityList = transaction>
        <#assign entityStr = "transaction">
        <#assign entity = "transaction">
        <#assign ENTITY_NAME = "Transaction">
        <#include "/common/table/transaction_table.ftl">
        </#if>

        <#if payment?size gt 0>
        <#assign no_results=1>
        <#assign entityList = payment>
        <#assign entity = "payment">
        <#assign entityStr = "payment">
        <#assign ENTITY_NAME = "Payment">
        <#include "/common/table/payment_table.ftl">
        </#if>

        <#if itemTransaction?? && itemTransaction?size gt 0 >
            <#assign no_results=1>
            <#assign entityList = itemTransaction>
            <#assign entity = "itemTransaction">
            <#assign entityStr = "itemTransaction">
            <#assign ENTITY_NAME = "itemTransaction">
            <#include "/common/table/itemTransaction_table.ftl">
        </#if>

        <#if no_results==0>
        <div class="alert alert-info" role="alert">NO Results for the query : <strong>${query!""}</strong></div>
        </#if>

    </div>

    <#include "/js/footer_js.ftl">
    <#include "/js/alerts_js.ftl">

</body>
</html>