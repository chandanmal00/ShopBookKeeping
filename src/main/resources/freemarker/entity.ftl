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

    <h3>${ENTITY_NAME} transactions and payment totals:</h3>
    <div>
        <table class="table table-striped" id="entity">
            <thead>
                <tr>

                    <th>TotalTransactionAmount</th>
                    <#if totalPaymentAmount??>
                        <th>TotalPaymentAmount</th>

                      <th>Balance</th>
                    </#if>
                </tr>
            </thead>
            <tbody>
                <tr>

                    <td> ${totalTransactionAmount}</td>
                <#if totalPaymentAmount??>
                    <td>${totalPaymentAmount}</td>

                    <td> ${totalTransactionAmount - totalPaymentAmount}</td>
                </#if>
                </tr>

            </tbody>
        </table>
    </div>

    <#if joinMapWeekly??>
        <#assign joinMap = joinMapWeekly>
        <h3>Daily summary for last week:</h3>
        <#assign ENTITY_TABLE_NAME = "Daily">
        <#include "/common/table/list_join_table.ftl">
    <br>
    </#if>


    <#if joinMapMonthly??>
        <#assign joinMap = joinMapMonthly>
        <h3>Quarterly Summary by Month:</h3>
        <#assign ENTITY_TABLE_NAME = "Quarterly">
        <#include "/common/table/list_join_table.ftl">
    <br>
    </#if>


    <h3>More details for ${ENTITY_NAME}</h3>
    <h4><strong>${ENTITY_NAME} Daily Transactions for the last week:</strong></h4>
    <#assign entityList = weeklyTransactions >
    <#assign ENTITY_TABLE_NAME = "weeklyTransactions">
    <div>
        <#include "/common/table/list_daily_table.ftl">
    </div>

     <#if weeklyPayment??>
        <h4><strong>${ENTITY_NAME} Daily Payments for the last week:</strong></h4>

        <#assign entityList = weeklyPayment >
        <#assign ENTITY_TABLE_NAME = "weeklyPayment">
        <div>
            <#include "/common/table/list_daily_table.ftl">
        </div>
        </#if>

    <h4><strong>${ENTITY_NAME} Quarterly Transactions by Month:</strong></h4>
    <#assign entityList = monthlyTransactions >
    <#assign ENTITY_TABLE_NAME = "monthlyTransactions">
    <div>
        <#include "/common/table/list_daily_table.ftl">
    </div>

    <#if monthlyPayment??>
    <h4><strong>${ENTITY_NAME} Quarterly Payments by Month:</strong></h4>

    <#assign entityList = monthlyPayment >
    <#assign ENTITY_TABLE_NAME = "monthlyPayment">
    <div>
        <#include "/common/table/list_daily_table.ftl">
    </div>
    </#if>


    <#if payments??>
    <h4><strong>${ENTITY_NAME} Payments:</strong></h4>(showing last ${rows} only)
    <div>
        <#include "/common/table/paymentsTable.ftl">
        <#--<#include "/common/table/kisaanPayment_table.ftl">-->
    </div>

    </#if>

    <h4><strong>${ENTITY_NAME} Transactions:</strong></h4>(showing last ${rows} only)
    <div>
        <#include "/common/table/transactionsTable.ftl">
    </div>
</div>
    <#include "/js/footer_js.ftl">
    <#include "/js/alerts_js.ftl">
</body>
</html>