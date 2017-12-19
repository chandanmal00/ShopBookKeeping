<html>
<head>
    <#include "/common/meta.ftl">
    <#include "/css/head.css">
    <title>${APP_TITLE} Inventory details as of today</title>
</head>

<body>
    <div class="container-fluid">
        <#include "/common/header.ftl">
        <br>
        <#include "/common/errors.ftl">

<#if itemTransactionSummary??>
<h4>Quick Item Stats:</h4>
  <table class="table table-striped" id="entityPayment">
  <thead>
  <th>Key</th>
  <th>Value</th>
  </thead>
      <tbody>
      <tr><td>totalItemTransactions</td><td>${itemTransactionSummary['totalItemTransactions']}</td></tr>
      <tr><td>totalItemBuyQuantity</td><td>${itemTransactionSummary['totalItemBuyQuantity']}</td></tr>
      <tr><td>totalItemSellQuantity</td><td>${-1*itemTransactionSummary['totalItemSellQuantity']}</td></tr>
      <tr><td>BalanceQuantity</td><td>${itemTransactionSummary['totalItemBuyQuantity']-(-1*itemTransactionSummary['totalItemSellQuantity'])}</td></tr>
      <tr><td>TotalCost</td><td>${itemTransactionSummary['totalItemBuyAmount']}</td></tr>
      <tr><td>TotalRevenues</td><td>${itemTransactionSummary['totalItemSellAmount']}</td></tr>
      <tr><td>NetProfit/Loss</td><td>${itemTransactionSummary['totalItemSellAmount']- itemTransactionSummary['totalItemBuyAmount']}</td></tr>
  </tbody>
  </table>

</#if>

<#if markdownItemCandidates??>
<h4>Markdown candidates bought before Date: ${dt!""} but still has inventory left <small>(rows limited to ${limit})</small></h4>
<#assign entityList = markdownItemCandidates>
<#include "/common/table/list_inventory_table.ftl">
</#if>
<#if inventoryList??>
        <#assign entityList = inventoryList>
        <div>
        <ol>
        <#list 0..10 as i>
          <#if i == page>
          <#else>
             <li><a href="/inventory/status/${i}">Next Page for Inventory ${i}</a></li>
          </#if>
        </#list>
        </ol>
        </div>
        <h4>Inventory status as of today - (page ${page}): <small>(only first ${cntMax} items</small>)</h4>
        <#include "/common/table/list_inventory_table.ftl">
</#if>

    </div>
    <#include "/js/footer_js.ftl">
    <#include "/js/alerts_js.ftl">

</body>
</html>