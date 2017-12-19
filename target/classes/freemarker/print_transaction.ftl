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
            <a class="btn btn-info hidden-print" href="/invoice/transaction/${entityObject.getUniqueKey()}">Invoice for Transaction <i class="fa fa-print fa-2x" aria-hidden="true"></i> </a>
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
                    <td>Date of ${entity}</td>
                    <td>${entityObject.getCreationDate()}</td>
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