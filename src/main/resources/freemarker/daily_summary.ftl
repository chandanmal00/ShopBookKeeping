<html>
<head>
    <#include "/common/meta.ftl">
    <#include "/css/head.css">
    <title>${APP_TITLE} Daily summary Page for last ${DAY} days</title>
</head>

<body>

    <div class="container-fluid">
        <#include "/common/header.ftl">
        <br>
        <#include "/common/errors.ftl">
        <#--
        <div id="chartDivId" style="margin-top:20px; margin-left:20px; width:400px; height:300px;"></div>
        <div id="chartDivIdInfo" style="margin-top:20px; margin-left:20px; width:400px; height:300px;"></div>

        -->

        <#if endingDate??>
            <h2>${summaryType} summary Page for last ${DAY} ${type} ending date: <small>${endingDate}</small></h2>
        <#else>
            <h2>${summaryType} summary Page for last ${DAY} ${type}</h2>
        </#if>
        <br>


        <#assign ENTITY_NAME = "joinMap">
        <#assign entityList = joinMap>
        <#assign ENTITY_TABLE_NAME = "joinMap">
        <h4>${summaryType} summary for last ${DAY} ${type}, ending <small>${endingDate}</small></h4>
        <#include "/common/table/list_join_table.ftl">
<br>
        <#assign ENTITY_NAME = "transaction">
        <#assign entityList = transactionList>
        <#assign ENTITY_TABLE_NAME = "transaction">
        <h4>${summaryType} ${ENTITY_NAME} summary for last ${DAY} ${type}, ending <small>${endingDate}</small></h4>
        <#include "/common/table/list_daily_table.ftl">

<br>
        <#assign ENTITY_NAME = "payment">
        <#assign entityList = paymentList>
        <#assign ENTITY_TABLE_NAME = "payment">
        <h4>${summaryType} ${ENTITY_NAME} summary for last ${DAY} ${type}, ending: <small>${endingDate}</small></h4>
        <#include "/common/table/list_daily_table.ftl">

    </div>

<#include "/common/footer.ftl">
    <#include "/js/footer_js.ftl">
    <#include "/js/alerts_js.ftl">

</body>
</html>