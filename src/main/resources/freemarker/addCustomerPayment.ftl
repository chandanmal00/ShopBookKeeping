<!DOCTYPE html>
<html lang="en">
  <head>
    <#include "/common/meta.ftl">
    <#include "/css/head.css">
    <title>Add Customer Payment for ${APP_TITLE}</title>
    <style>
        #state {
        display: block;
        font-weight: bold;
        margin-bottom: 1em;
        }
    </style>

</head>

<body>
    <div class="container-fluid">
        <#include "/common/header.ftl">
        <#if success?? >
            <div>
                <p>
                    <div class="alert alert-success" role="alert">Successfully added ${local["CustomerPayment"]} for ${local["Customer"]}: <b>${customer}</b> with ${local["Amount"]}: <b>${amount}</b>
                    <br>Print <a href="/invoice/${entityActual}/${entityObject.get_id()}" target="_blank" class="btn btn-info">PaymentSlip <i class="fa fa-print" aria-hidden="true"></i></a>
                    <p>
                      Want to add one more payment: <a href="/add/payment">Click here</a>
                    </p>
                    </div>

                    <br>
                    <#if tag??>
                        Tag Provided: <b>${tag!""}</b>
                    </#if>
                </p>

            </div>
        <#else>

            <div class="new_post_form">
                <form action="/add/payment" method="POST">
                    <#include "/common/errors.ftl">
                    <h2>Add ${local["Customer"]} ${local["Payment"]} to the system</h2>
                        <div class="row required"><div class="left">${local["Customer"]} Key:</div> <div class="right"><input type="text" placeholder="Customer Nick Name" name="customer" id="${ENTITY_NAME}Id" size="120" value="${customer!""}"></div></div>
                        <div class="row required"><div class="left">${local["Amount"]}:</div> <div class="right"> <input type="text" placeholder="Amount" name="amount" size="120" value="${amount!""}"> </div></div>
                        <div class="row required"><div class="left">${local["PaymentDate"]}:</div> <div class="right"> <input type="text" placeholder="date in yyyy-mm-dd" name="dt" size="120" value="${dt!""}"> </div></div>
                        <div class="row"><div class="left">${local["TransactionTag"]}:</div> <div class="right"> <input type="text" placeholder="tag your transaction" name="tag" size="120" value="${tag!""}"></div></div>

                        <div class="row">
                             <div class="left">${local["PaymentType"]}:</div>
                             <div class="right">
                             <select id="paymentTypeId" name="paymentType">
                               <option value = "Cash">Cash</option>
                               <option value = "CreditCard">CreditCard</option>
                               <option value = "Check">Check</option>
                               <option value = "Other">Other</option>
                             </select>
                             </div>
                        </div>
                    <input type="submit" value="Add ${local["Customer"]} ${local["Payment"]}" class="btn btn-info btn-block">
                </form>
            </div>
        </#if>

    </div>
    <#include "/common/footer.ftl">
    <#include "/js/footer_js.ftl">
    <#include "/js/alerts_js.ftl">
    <#include "/js/auto_complete_js_entity.ftl">
</body>
</html>