<html>
<head>
    <#include "/common/meta.ftl">
    <#include "/css/head.css">
    <title>${APP_TITLE} ${entity} Detail Page</title>
</head>

<body>
    <div class="container-fluid">
        <#include "/common/header.ftl">
        <br>
        <h2>Details for ${entity} for key ${key}</h2>
        <#if entityObject??>
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
                            <td>Barcode</td>
                            <td>${entityObject.getBarcode()}</td>
                        </tr>
                        <td>ItemType</td>
                        <td>${entityObject.getItemType()!""}</td>
                        </tr>
                        <tr>
                        <td>ProductType</td>
                        <td>${entityObject.getProductType()!""}</td>
                        </tr>
                        <tr>  <td>Brand</td>
                        <td>${entityObject.getBrand()!""}</td>
                        </tr>
                        <tr> <td>ListPrice</td>
                        <td>${entityObject.getListPrice()!""}</td>
                        </tr>
                        <tr> <td>PurchasePrice</td>
                        <td>${entityObject.getPurchasePrice()!""}</td>
                        </tr>
                        <tr>  <td>Size</td>
                        <td>${entityObject.getSize()!""}</td>
                        </tr>
                        <tr> <td>Group</td>
                        <td>${entityObject.getGroup()!""}</td>
                        </tr>
                        <tr> <td>EventDate</td>
                        <td>${entityObject.getEventDate()!"root"}</td>
                        </tr>
                        <tr>  <td>CreatedDate</td>
                        <td>${entityObject.getCreationDate()!"root"}</td>
                        </tr>
                        <tr> <td>Created By</td>
                        <td>${entityObject.getCreatedBy()!"root"}</td>
                        </tr>
                    </tbody>
                </table>
                <#else>
                   <p>
                   <strong><font color="red">ERROR</font></strong>: Something went wrong, check with ADMIN or Developer
                   </p>
                </#if>
        <#include "/common/errors.ftl">
    </div>
    <#include "/js/footer_js.ftl">
    <#include "/js/alerts_js.ftl">

</body>
</html>