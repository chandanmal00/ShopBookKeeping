<html>
<head>
    <#include "/common/meta.ftl">
    <#include "/css/head.css">
    <title>${APP_TITLE} ${entity} Print Page</title>
</head>

<body>
    <div class="container-fluid">
        <#include "/common/header.ftl">
        <br>
        <h2>Bill for ${entity} for key ${key}</h2>
        <#include "/common/errors.ftl">
        <#if entityObject??>
        <div class="hidden-print" style="text-align: right;">
            <a class="btn btn-info hidden-print" href="/invoice/payment/${entityObject.getUniqueKey()}">See Payment Invoice <i class="fa fa-print fa-2x" aria-hidden="true"></i> </a>
        </div>
        <table class="table table-striped" id="entityPayment">
            <thead>
                <tr>
                    <th>Item</th>
                    <th>Item Details</th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td>ItemId</td>
                    <td>${entityObject.get_id()}</td>
                </tr>
                <tr>
                    <td>PaymentType</td>
                    <td>${entityObject.getPaymentType()!"Other"}</td>
                </tr>
                <tr>
                    <td>Date of Payment</td>
                    <td>${entityObject.getEventDate()}</td>
                </tr>
                <tr>
                    <td>Date of Creation of ${entity}</td>
                    <td>${entityObject.getCreationDate()}</td>
                </tr>
                <tr>
                    <td>Current Date</td>
                    <td>${dateStr}</td>
                </tr>

                <tr>
                    <td>Customer Key</td>
                    <td>${entityObject.getCustomer()}</td>
                </tr>
                <tr>
                    <td>Amount</td>
                    <td>${entityObject.getAmount()}</td>
                </tr>
                <tr>
                    <td>Tag</td>
                    <td>${entityObject.getTag()!""}</td>
                </tr>
            </tbody>
        </table>
        <#else>
           <p>
           <strong><font color="red">ERROR</font></strong>: Something went wrong, check with ADMIN or Developer
           </p>
        </#if>

    </div>
    <#include "/js/footer_js.ftl">
    <#include "/js/alerts_js.ftl">

</body>
</html>