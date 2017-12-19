<!DOCTYPE html>
<html lang="en">
  <head>
    <#include "/common/meta.ftl">
    <#include "/css/head.css">
    <title>Edit ${local["Item"]} for ${APP_TITLE}</title>
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
                            <div class="alert alert-success" role="alert">Successfully updated ${entity} with name: <b>${barcode}</b>
                             <br>
                            Want to edit again: <a href="/edit/item/${barcode}">Click Here <i class="fa fa-pencil-square-o fa-lg" aria-hidden="true"></i></a>
                            <br>
                            See details for the ${entity}: <a href="/item/${barcode}">Click Here <i class="fa fa-user fa-lg" aria-hidden="true"></i></a>
                            <br>
                            Add another ${entity}: <a href="/add/item">Click Here <i class="fa fa-plus-square fa-lg" aria-hidden="true"></i></a>

                </div>
                <br>

                    Below are the details added:<br>
                    <ul>
                        <li>${local["Barcode"]}: <b>${barcode!""}</b></li>
                        <li>${local["ItemType"]}: <b>${itemType!""}</b></li>
                        <li>${local["ProductType"]}: <b>${productType!""}</b></li>
                        <li>${local["Brand"]}: <b>${brand!""}</b></li>
                        <li>${local["Size"]}: <b>${size!""}</b></li>
                        <li>${local["ListPrice"]}: <b>${listPrice!""}</b></li>
                        <li>${local["PurchasePrice"]}: <b>${purchasePrice!""}</b></li>
                        <li>${local["Group"]}: <b>${group!""}</b></li>
                    </ul>
                </p>
            </div>
        <#else>
            <div class="new_post_form">
                <form action="/edit/item" method="POST">
                    <#include "/common/errors.ftl">
                    <h2>Update ${local["Item"]} in the system</h2>

                        <div class="row required"><div class="left">${local["Barcode"]}:</div> <div class="right"><input type="text" placeholder="barcode" name="barcode" id="barcodeId" size="120" value="${barcode!""}"></div></div>
                        <div class="row required"><div class="left">${local["ItemType"]}:</div> <div class="right"> <input type="text" placeholder="Denim, Trousers" name="itemType" id="itemTypeId" size="120" value="${itemType!""}"></div></div>
                        <div class="row required"><div class="left">${local["ProductType"]}:</div> <div class="right"> <input type="text" placeholder="Jeans, Saree" name="productType" id="productTypeId" size="120" value="${productType!""}"></div></div>
                        <div class="row required"><div class="left">${local["Brand"]}:</div> <div class="right"> <input type="text" placeholder="Levi, Lee..." name="brand" id="brandId" size="120" value="${brand!""}"></div></div>
                        <div class="row"><div class="left">${local["ListPrice"]}:</div> <div class="right"> <input type="text" placeholder="msrp of the item" name="listPrice" size="120" value="${listPrice!""}"></div></div>
                        <div class="row required"><div class="left">${local["PurchasePrice"]}:</div> <div class="right"> <input type="text" placeholder="purchasePrice" name="purchasePrice" size="120" value="${purchasePrice!""}"></div></div>
                        <div class="row"><div class="left">${local["Size"]}:</div> <div class="right"> <input type="text" placeholder="L, XL" name="size"  size="120" value="${size!""}"></div></div>
                        <div class="row"><div class="left">${local["Group"]}:</div> <div class="right"> <input type="text" placeholder="group..." name="item" size="120" value="${group!""}"></div></div>


                    <input type="submit" value="Update ${local["Item"]}" class="btn btn-info btn-block">
                </form>
            </div>
        </#if>

    </div>
    <#include "/common/footer.ftl">
    <#include "/js/footer_js.ftl">
    <#include "/js/alerts_js.ftl">
</body>
</html>

