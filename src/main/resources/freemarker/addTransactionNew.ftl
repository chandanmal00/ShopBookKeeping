<!DOCTYPE html>
<html lang="en">
  <head>
    <#include "/common/meta.ftl">
    <#include "/css/head.css">
    <title>Add ${local["Transaction"]} for ${APP_TITLE}</title>
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
                <div class="alert alert-success" role="alert">Successfully added ${local["Transaction"]} for ${local["Customer"]}: <b>${customer!""}</b>
                <a href="/invoice/${entityActual}/${entityObject.getUniqueKey()}" class="btn btn-info">Print <i class="fa fa-print" aria-hidden="true"></i></a>
                <p>
                  Want to add one more transaction: <a href="/add/transaction">Click here</a>
                </p>
                </div>

                <br>
                Below are the details added:<br>
                <#assign cnt = 1>
                <table class="table table-striped" id="entityPayment">
                <thead>
                </thead>
                <tbody>
                <#if entityObject.itemSolds??>
                    <#list entityObject.getItemSolds() as itemSold >
                                        <tr>
                                             <td>${cnt}</td>
                                             <td>${itemSold.getUniqueKey()!""}</td>
                                             <td>${itemSold.getQuantity()*-1!""}</td>
                                             <td>${itemSold.getPrice()!""}</td>
                                             <td>${itemSold.getAmount()!""}</td>
                                         </tr>
                                     <#assign cnt = cnt+1>
                     </#list>

                 </#if>

</tbody>
</table>

                    <ul>
                        <li>${local["Amount"]}: <b>${totalAmount!""}</b></li>
                        <li>${local["AmountPaid"]}: <b>${paidAmount!""}</b></li>
                        <li>${local["PaymentType"]}: <b>${paymentType!""}</b></li>
                        <li>${local["Salesman"]}: <b>${salesman!""}</b></li>
                        <li>${local["Discount"]} : ${discountAmount!""}</li>
                        <li>${local["DiscountPercentage"]} : ${discountPercent!""}%</li>
                        <li>${local["AdditionalCharges"]} : ${additionalCharges!""}</li>
                    </ul>
                </p>
            </div>
        <#else>

 <div class="new_post_form">
                <form action="/add/transaction" method="POST">
                    <#include "/common/errors.ftl">
            <div class="row">
              <div class="col-xs-10 text-center">
                <h2>Add ${local["Customer"]} ${local["Transaction"]} to the system</h2>
               </div>
             </div>
                    <div class="row text-center">
                      <div class="col-xs-3 row required"><span class="left">${local["Salesman"]}:</span><input type="text" placeholder="Salesman Name" name="salesman" id="salesmanId" size="10" value="${salesman!""}"></div>
                      <div class="col-xs-4 row required"><span class="left">${local["CustomerName"]}:</span><input type="text" placeholder="Customer NickName" name="customer" id="customerId" size="10" value="${customer!""}"></div>
                      <div class="col-xs-3 row required"><span class="left">${local["DateOfTransaction"]}:</span><input type="text" placeholder="yyyy-mm-dd" name="dt" id="dtId" size="10" value="${dt!""}"></div>
                    </div>

                    <div class="row text-center">
                      <div class="col-xs-1">${local["Barcode"]}*</div>
                      <div class="col-xs-1">${local["ProductType"]}</div>
                      <div class="col-xs-1">${local["ItemType"]}</div>
                      <div class="col-xs-1">${local["Size"]}</div>
                      <div class="col-xs-1">${local["Brand"]}</div>
                      <div class="col-xs-1">${local["Quantity"]}</div>
                      <div class="col-xs-1">${local["Price"]}</div>
                      <div class="col-xs-1">${local["Amount"]}</div>
                    </div>
                    <div id="itemSold">

                        <div id="itemSignature_0" class="row text-center"> 
                        <div class="col-xs-1"><input type="text" class="item" placeholder="barcode" name="barcode_0" id="itemId_0" size="30" value="${barcode!""}"></div> 
                        <div class="col-xs-1"><input type="text" placeholder="productType" name="productType_0" id="productTypeId_0" size="30" value="${productType!""}"></div> 
                        <div class="col-xs-1"><input type="text" placeholder="itemType" name="itemType_0" id="itemTypeId_0" size="30" value="${itemType!""}"></div> 
                        <div class="col-xs-1"><input type="text" placeholder="size" name="size_0" id="sizeId_0" size="30" value="${size!""}"></div> 
                        <div class="col-xs-1"><input type="text" placeholder="brand" name="brand_0" id="brandId_0" size="30" value="${brand!""}"></div> 
                        <div class="col-xs-1"><input type="text" class="target" placeholder="quantity" name="quantity_0" id="quantityId_0" size="30" value="${quantity!""}"></div> 
                        <div class="col-xs-1"><input type="text" class="target" placeholder="price" name="price_0" id="priceId_0" size="30" value="${price!""}"></div> 
                        <div class="col-xs-1"><input type="text" class="amount" placeholder="amount" name="amount_0" id="amountId_0" size="30" value="${amount!""}"></div> 
                        </div>

                        <div id="itemSignature_1" class="row text-center"> 
                        <div class="col-xs-1"><input type="text" class="item" placeholder="barcode" name="barcode_1" id="itemId_1" size="30" value="${barcode!""}"></div> 
                        <div class="col-xs-1"><input type="text" placeholder="productType" name="productType_1" id="productTypeId_1" size="30" value="${productType!""}"></div> 
                        <div class="col-xs-1"><input type="text" placeholder="itemType" name="itemType_1" id="itemTypeId_1" size="30" value="${itemType!""}"></div> 
                        <div class="col-xs-1"><input type="text" placeholder="size" name="size_1" id="sizeId_1" size="30" value="${size!""}"></div> 
                        <div class="col-xs-1"><input type="text" placeholder="brand" name="brand_1" id="brandId_1" size="30" value="${brand!""}"></div> 
                        <div class="col-xs-1"><input type="text" class="target" placeholder="quantity" name="quantity_1" id="quantityId_1" size="30" value="${quantity!""}"></div> 
                        <div class="col-xs-1"><input type="text" class="target" placeholder="price" name="price_1" id="priceId_1" size="30" value="${price!""}"></div> 
                        <div class="col-xs-1"><input type="text" class="amount" placeholder="amount" name="amount_1" id="amountId_1" size="30" value="${amount!""}"></div> 
                        </div>

                        <div id="itemSignature_2" class="row text-center"> 
                        <div class="col-xs-1"><input type="text" class="item" placeholder="barcode" name="barcode_2" id="itemId_2" size="30" value="${barcode!""}"></div> 
                        <div class="col-xs-1"><input type="text" placeholder="productType" name="productType_2" id="productTypeId_2" size="30" value="${productType!""}"></div> 
                        <div class="col-xs-1"><input type="text" placeholder="itemType" name="itemType_2" id="itemTypeId_2" size="30" value="${itemType!""}"></div> 
                        <div class="col-xs-1"><input type="text" placeholder="size" name="size_2" id="sizeId_2" size="30" value="${size!""}"></div> 
                        <div class="col-xs-1"><input type="text" placeholder="brand" name="brand_2" id="brandId_2" size="30" value="${brand!""}"></div> 
                        <div class="col-xs-1"><input type="text" class="target" placeholder="quantity" name="quantity_2" id="quantityId_2" size="30" value="${quantity!""}"></div> 
                        <div class="col-xs-1"><input type="text" class="target" placeholder="price" name="price_2" id="priceId_2" size="30" value="${price!""}"></div> 
                        <div class="col-xs-1"><input type="text" class="amount" placeholder="amount" name="amount_2" id="amountId_2" size="30" value="${amount!""}"></div> 
                        </div>

                        <div id="itemSignature_3" style="display:none" class="row text-center"> 
                        <div class="col-xs-1"><input type="text" class="item" placeholder="barcode" name="barcode_3" id="itemId_3" size="30" value="${barcode!""}"></div> 
                        <div class="col-xs-1"><input type="text" placeholder="productType" name="productType_3" id="productTypeId_3" size="30" value="${productType!""}"></div> 
                        <div class="col-xs-1"><input type="text" placeholder="itemType" name="itemType_3" id="itemTypeId_3" size="30" value="${itemType!""}"></div> 
                        <div class="col-xs-1"><input type="text" placeholder="size" name="size_3" id="sizeId_3" size="30" value="${size!""}"></div> 
                        <div class="col-xs-1"><input type="text" placeholder="brand" name="brand_3" id="brandId_3" size="30" value="${brand!""}"></div> 
                        <div class="col-xs-1"><input type="text" class="target" placeholder="quantity" name="quantity_3" id="quantityId_3" size="30" value="${quantity!""}"></div> 
                        <div class="col-xs-1"><input type="text" class="target" placeholder="price" name="price_3" id="priceId_3" size="30" value="${price!""}"></div> 
                        <div class="col-xs-1"><input type="text" class="amount" placeholder="amount" name="amount_3" id="amountId_3" size="30" value="${amount!""}"></div> 
                        </div>
                        <div id="itemSignature_4" style="display:none" class="row text-center"> 
                        <div class="col-xs-1"><input type="text" class="item" placeholder="barcode" name="barcode_4" id="itemId_4" size="30" value="${barcode!""}"></div> 
                        <div class="col-xs-1"><input type="text" placeholder="productType" name="productType_4" id="productTypeId_4" size="30" value="${productType!""}"></div> 
                        <div class="col-xs-1"><input type="text" placeholder="itemType" name="itemType_4" id="itemTypeId_4" size="30" value="${itemType!""}"></div> 
                        <div class="col-xs-1"><input type="text" placeholder="size" name="size_4" id="sizeId_4" size="30" value="${size!""}"></div> 
                        <div class="col-xs-1"><input type="text" placeholder="brand" name="brand_4" id="brandId_4" size="30" value="${brand!""}"></div> 
                        <div class="col-xs-1"><input type="text" class="target" placeholder="quantity" name="quantity_4" id="quantityId_4" size="30" value="${quantity!""}"></div> 
                        <div class="col-xs-1"><input type="text" class="target" placeholder="price" name="price_4" id="priceId_4" size="30" value="${price!""}"></div> 
                        <div class="col-xs-1"><input type="text" class="amount" placeholder="amount" name="amount_4" id="amountId_4" size="30" value="${amount!""}"></div> 
                        </div>
                        <div id="itemSignature_5" style="display:none" class="row text-center"> 
                        <div class="col-xs-1"><input type="text" class="item" placeholder="barcode" name="barcode_5" id="itemId_5" size="30" value="${barcode!""}"></div> 
                        <div class="col-xs-1"><input type="text" placeholder="productType" name="productType_5" id="productTypeId_5" size="30" value="${productType!""}"></div> 
                        <div class="col-xs-1"><input type="text" placeholder="itemType" name="itemType_5" id="itemTypeId_5" size="30" value="${itemType!""}"></div> 
                        <div class="col-xs-1"><input type="text" placeholder="size" name="size_5" id="sizeId_5" size="30" value="${size!""}"></div> 
                        <div class="col-xs-1"><input type="text" placeholder="brand" name="brand_5" id="brandId_5" size="30" value="${brand!""}"></div> 
                        <div class="col-xs-1"><input type="text" class="target" placeholder="quantity" name="quantity_5" id="quantityId_5" size="30" value="${quantity!""}"></div> 
                        <div class="col-xs-1"><input type="text" class="target" placeholder="price" name="price_5" id="priceId_5" size="30" value="${price!""}"></div> 
                        <div class="col-xs-1"><input type="text" class="amount" placeholder="amount" name="amount_5" id="amountId_5" size="30" value="${amount!""}"></div> 
                        </div>
                        <div id="itemSignature_6" style="display:none" class="row text-center"> 
                        <div class="col-xs-1"><input type="text" class="item" placeholder="barcode" name="barcode_6" id="itemId_6" size="30" value="${barcode!""}"></div> 
                        <div class="col-xs-1"><input type="text" placeholder="productType" name="productType_6" id="productTypeId_6" size="30" value="${productType!""}"></div> 
                        <div class="col-xs-1"><input type="text" placeholder="itemType" name="itemType_6" id="itemTypeId_6" size="30" value="${itemType!""}"></div> 
                        <div class="col-xs-1"><input type="text" placeholder="size" name="size_6" id="sizeId_6" size="30" value="${size!""}"></div> 
                        <div class="col-xs-1"><input type="text" placeholder="brand" name="brand_6" id="brandId_6" size="30" value="${brand!""}"></div> 
                        <div class="col-xs-1"><input type="text" class="target" placeholder="quantity" name="quantity_6" id="quantityId_6" size="30" value="${quantity!""}"></div> 
                        <div class="col-xs-1"><input type="text" class="target" placeholder="price" name="price_6" id="priceId_6" size="30" value="${price!""}"></div> 
                        <div class="col-xs-1"><input type="text" class="amount" placeholder="amount" name="amount_6" id="amountId_6" size="30" value="${amount!""}"></div> 
                        </div>
                        <div id="itemSignature_7" style="display:none" class="row text-center"> 
                        <div class="col-xs-1"><input type="text" class="item" placeholder="barcode" name="barcode_7" id="itemId_7" size="30" value="${barcode!""}"></div> 
                        <div class="col-xs-1"><input type="text" placeholder="productType" name="productType_7" id="productTypeId_7" size="30" value="${productType!""}"></div> 
                        <div class="col-xs-1"><input type="text" placeholder="itemType" name="itemType_7" id="itemTypeId_7" size="30" value="${itemType!""}"></div> 
                        <div class="col-xs-1"><input type="text" placeholder="size" name="size_7" id="sizeId_7" size="30" value="${size!""}"></div> 
                        <div class="col-xs-1"><input type="text" placeholder="brand" name="brand_7" id="brandId_7" size="30" value="${brand!""}"></div> 
                        <div class="col-xs-1"><input type="text" class="target" placeholder="quantity" name="quantity_7" id="quantityId_7" size="30" value="${quantity!""}"></div> 
                        <div class="col-xs-1"><input type="text" class="target" placeholder="price" name="price_7" id="priceId_7" size="30" value="${price!""}"></div> 
                        <div class="col-xs-1"><input type="text" class="amount" placeholder="amount" name="amount_7" id="amountId_7" size="30" value="${amount!""}"></div> 
                        </div>
                        <div id="itemSignature_8" style="display:none" class="row text-center"> 
                        <div class="col-xs-1"><input type="text" class="item" placeholder="barcode" name="barcode_8" id="itemId_8" size="30" value="${barcode!""}"></div> 
                        <div class="col-xs-1"><input type="text" placeholder="productType" name="productType_8" id="productTypeId_8" size="30" value="${productType!""}"></div> 
                        <div class="col-xs-1"><input type="text" placeholder="itemType" name="itemType_8" id="itemTypeId_8" size="30" value="${itemType!""}"></div> 
                        <div class="col-xs-1"><input type="text" placeholder="size" name="size_8" id="sizeId_8" size="30" value="${size!""}"></div> 
                        <div class="col-xs-1"><input type="text" placeholder="brand" name="brand_8" id="brandId_8" size="30" value="${brand!""}"></div> 
                        <div class="col-xs-1"><input type="text" class="target" placeholder="quantity" name="quantity_8" id="quantityId_8" size="30" value="${quantity!""}"></div> 
                        <div class="col-xs-1"><input type="text" class="target" placeholder="price" name="price_8" id="priceId_8" size="30" value="${price!""}"></div> 
                        <div class="col-xs-1"><input type="text" class="amount" placeholder="amount" name="amount_8" id="amountId_8" size="30" value="${amount!""}"></div> 
                        </div>

                        <div id="itemSignature_9" style="display:none" class="row text-center"> 
                        <div class="col-xs-1"><input type="text" class="item" placeholder="barcode" name="barcode_9" id="itemId_9" size="30" value="${barcode!""}"></div> 
                        <div class="col-xs-1"><input type="text" placeholder="productType" name="productType_9" id="productTypeId_9" size="30" value="${productType!""}"></div> 
                        <div class="col-xs-1"><input type="text" placeholder="itemType" name="itemType_9" id="itemTypeId_9" size="30" value="${itemType!""}"></div> 
                        <div class="col-xs-1"><input type="text" placeholder="size" name="size_9" id="sizeId_9" size="30" value="${size!""}"></div> 
                        <div class="col-xs-1"><input type="text" placeholder="brand" name="brand_9" id="brandId_9" size="30" value="${brand!""}"></div> 
                        <div class="col-xs-1"><input type="text" class="target" placeholder="quantity" name="quantity_9" id="quantityId_9" size="30" value="${quantity!""}"></div> 
                        <div class="col-xs-1"><input type="text" class="target" placeholder="price" name="price_9" id="priceId_9" size="30" value="${price!""}"></div> 
                        <div class="col-xs-1"><input type="text" class="amount" placeholder="amount" name="amount_9" id="amountId_9" size="30" value="${amount!""}"></div> 
                        </div>

                        <div id="itemSignature_10" style="display:none" class="row text-center"> 
                        <div class="col-xs-1"><input type="text" class="item" placeholder="barcode" name="barcode_10" id="itemId_10" size="30" value="${barcode!""}"></div> 
                        <div class="col-xs-1"><input type="text" placeholder="productType" name="productType_10" id="productTypeId_10" size="30" value="${productType!""}"></div> 
                        <div class="col-xs-1"><input type="text" placeholder="itemType" name="itemType_10" id="itemTypeId_10" size="30" value="${itemType!""}"></div> 
                        <div class="col-xs-1"><input type="text" placeholder="size" name="size_10" id="sizeId_10" size="30" value="${size!""}"></div> 
                        <div class="col-xs-1"><input type="text" placeholder="brand" name="brand_10" id="brandId_10" size="30" value="${brand!""}"></div> 
                        <div class="col-xs-1"><input type="text" class="target" placeholder="quantity" name="quantity_10" id="quantityId_10" size="30" value="${quantity!""}"></div> 
                        <div class="col-xs-1"><input type="text" class="target" placeholder="price" name="price_10" id="priceId_10" size="30" value="${price!""}"></div> 
                        <div class="col-xs-1"><input type="text" class="amount" placeholder="amount" name="amount_10" id="amountId_10" size="30" value="${amount!""}"></div> 
                        </div>

                        <div id="itemSignature_11" style="display:none" class="row text-center"> 
                        <div class="col-xs-1"><input type="text" class="item" placeholder="barcode" name="barcode_11" id="itemId_11" size="30" value="${barcode!""}"></div> 
                        <div class="col-xs-1"><input type="text" placeholder="productType" name="productType_11" id="productTypeId_11" size="30" value="${productType!""}"></div> 
                        <div class="col-xs-1"><input type="text" placeholder="itemType" name="itemType_11" id="itemTypeId_11" size="30" value="${itemType!""}"></div> 
                        <div class="col-xs-1"><input type="text" placeholder="size" name="size_11" id="sizeId_11" size="30" value="${size!""}"></div> 
                        <div class="col-xs-1"><input type="text" placeholder="brand" name="brand_11" id="brandId_11" size="30" value="${brand!""}"></div> 
                        <div class="col-xs-1"><input type="text" class="target" placeholder="quantity" name="quantity_11" id="quantityId_11" size="30" value="${quantity!""}"></div> 
                        <div class="col-xs-1"><input type="text" class="target" placeholder="price" name="price_11" id="priceId_11" size="30" value="${price!""}"></div> 
                        <div class="col-xs-1"><input type="text" class="amount" placeholder="amount" name="amount_11" id="amountId_11" size="30" value="${amount!""}"></div> 
                        </div>

                        <div id="itemSignature_12" style="display:none" class="row text-center"> 
                        <div class="col-xs-1"><input type="text" class="item" placeholder="barcode" name="barcode_12" id="itemId_12" size="30" value="${barcode!""}"></div> 
                        <div class="col-xs-1"><input type="text" placeholder="productType" name="productType_12" id="productTypeId_12" size="30" value="${productType!""}"></div> 
                        <div class="col-xs-1"><input type="text" placeholder="itemType" name="itemType_12" id="itemTypeId_12" size="30" value="${itemType!""}"></div> 
                        <div class="col-xs-1"><input type="text" placeholder="size" name="size_12" id="sizeId_12" size="30" value="${size!""}"></div> 
                        <div class="col-xs-1"><input type="text" placeholder="brand" name="brand_12" id="brandId_12" size="30" value="${brand!""}"></div> 
                        <div class="col-xs-1"><input type="text" class="target" placeholder="quantity" name="quantity_12" id="quantityId_12" size="30" value="${quantity!""}"></div> 
                        <div class="col-xs-1"><input type="text" class="target" placeholder="price" name="price_12" id="priceId_12" size="30" value="${price!""}"></div> 
                        <div class="col-xs-1"><input type="text" class="amount" placeholder="amount" name="amount_12" id="amountId_12" size="30" value="${amount!""}"></div> 
                        </div>
                        <div id="itemSignature_13" style="display:none" class="row text-center"> 
                        <div class="col-xs-1"><input type="text" class="item" placeholder="barcode" name="barcode_13" id="itemId_13" size="30" value="${barcode!""}"></div> 
                        <div class="col-xs-1"><input type="text" placeholder="productType" name="productType_13" id="productTypeId_13" size="30" value="${productType!""}"></div> 
                        <div class="col-xs-1"><input type="text" placeholder="itemType" name="itemType_13" id="itemTypeId_13" size="30" value="${itemType!""}"></div> 
                        <div class="col-xs-1"><input type="text" placeholder="size" name="size_13" id="sizeId_13" size="30" value="${size!""}"></div> 
                        <div class="col-xs-1"><input type="text" placeholder="brand" name="brand_13" id="brandId_13" size="30" value="${brand!""}"></div> 
                        <div class="col-xs-1"><input type="text" class="target" placeholder="quantity" name="quantity_13" id="quantityId_13" size="30" value="${quantity!""}"></div> 
                        <div class="col-xs-1"><input type="text" class="target" placeholder="price" name="price_13" id="priceId_13" size="30" value="${price!""}"></div> 
                        <div class="col-xs-1"><input type="text" class="amount" placeholder="amount" name="amount_13" id="amountId_13" size="30" value="${amount!""}"></div> 
                        </div>
                        <div id="itemSignature_14" style="display:none" class="row text-center"> 
                        <div class="col-xs-1"><input type="text" class="item" placeholder="barcode" name="barcode_14" id="itemId_14" size="30" value="${barcode!""}"></div> 
                        <div class="col-xs-1"><input type="text" placeholder="productType" name="productType_14" id="productTypeId_14" size="30" value="${productType!""}"></div> 
                        <div class="col-xs-1"><input type="text" placeholder="itemType" name="itemType_14" id="itemTypeId_14" size="30" value="${itemType!""}"></div> 
                        <div class="col-xs-1"><input type="text" placeholder="size" name="size_14" id="sizeId_14" size="30" value="${size!""}"></div> 
                        <div class="col-xs-1"><input type="text" placeholder="brand" name="brand_14" id="brandId_14" size="30" value="${brand!""}"></div> 
                        <div class="col-xs-1"><input type="text" class="target" placeholder="quantity" name="quantity_14" id="quantityId_14" size="30" value="${quantity!""}"></div> 
                        <div class="col-xs-1"><input type="text" class="target" placeholder="price" name="price_14" id="priceId_14" size="30" value="${price!""}"></div> 
                        <div class="col-xs-1"><input type="text" class="amount" placeholder="amount" name="amount_14" id="amountId_14" size="30" value="${amount!""}"></div> 
                        </div>
                    </div>
                    <div id="addedItemsSold">
                    </div>
<br>
                    <div class="row text-center">
                        <div class="col-xs-2">${local["PaymentType"]}</div>
                        <div class="col-xs-2">${local["ActualTotal"]}</div>
                        <div class="col-xs-2">${local["Discount"]}</div>
                        <div class="col-xs-2">${local["AdditionalCharges"]}</div>
                        <div class="col-xs-2">${local["DiscountPercentage"]}</div>
                    </div>
                    <div class="row text-center">
                        <div class="col-xs-2">
                             <select id="paymentTypeId" name="paymentType">
                               <option value = "Cash">Cash</option>
                               <option value = "CreditCard">CreditCard</option>
                               <option value = "Check">Check</option>
                               <option value = "Other">Other</option>
                             </select>
                        </div>

                        <div class="col-xs-2"> <input  class="amountFire" type="text" placeholder="actualTotal" name="actualTotal" id="actualTotalId" size="30" value="${actualTotal!""}">
                         </div>
                         <div class="col-xs-2"> <input  class="amountFire" type="text" placeholder="discountAmount" name="discountAmount" id="discountAmountId" size="30" value="${discountAmount!""}">
                         </div>
                         <div class="col-xs-2">   <input  class="amountFire" type="text" placeholder="additionalCharges" name="additionalCharges" id="additionalChargesId" size="30" value="${additionalCharges!""}">
                         </div>
                         <div class="col-xs-2">   <input class="amountFire" type="text" placeholder="discountPercent" name="discountPercent" id="discountPercentId" size="30" value="${discountPercent!""}">
                        </div>
                    </div>
                    <div class="row text-right">
                        <div class="input-group col-xs-10">
                        <span class="input-group-addon" id="basic-addon1">${local["AmountPaid"]}:</span>
                          <input type="text" class="form-control" name="paidAmount" placeholder="paidAmount" size=30 id="paidAmountId" value="${paidAmount!""}" aria-describedby="basic-addon1">
                          <span class="input-group-addon" id="basic-addon1">${local["TotalAmount"]}:</span>
                          <input type="text" class="form-control" name="totalAmount" placeholder="totalAmount" size=30 id="totalAmountId" value="${totalAmount!""}" aria-describedby="basic-addon1">
                        </div>
                    </div>

                    <input type="hidden" id="countItemsId" name="countItems" value="3">
                    <input type="submit" value="Add ${local["Customer"]} ${local["Transaction"]}" class="btn btn-info btn-block">
                </form>
            </div>
        </#if>

    </div>


    <#include "/common/footer.ftl">
    <#include "/js/footer_js.ftl">
    <#include "/js/alerts_js.ftl">
        <script>
          var i = 4;
          var html = "";
          function removeRow(i) {
            $('#addedItemsSold #itemSignature_'+i).remove();
            $('#addedItemsSold #removeButton_'+i).remove();
          }

          $('#addRowId').on('click', function () {
            html = $('#itemSold #itemSignature')[0].outerHTML;

            $('#addedItemsSold').append(html);
            var removeButton="<button id=\"removeButton_"+i+"\" onclick=\"removeRow("+i+")\" type=\"button\" id=\"addRowId\" class=\"btn btn-danger\">Remove Row</button>";
            $('#addedItemsSold').append(removeButton);
            $('#addedItemsSold #itemId').attr("id", "itemId_"+i);
            $('#addedItemsSold #brandId').attr("id", "brandId_"+i);
            $('#addedItemsSold #priceId').attr("id", "priceId_"+i);
            $('#addedItemsSold #itemTypeId').attr("id", "itemTypeId_"+i);
            $('#addedItemsSold #sizeId').attr("id", "sizeId_"+i);
            $('#addedItemsSold #quantityId').attr("id", "quantityId_"+i);
            $('#addedItemsSold #amountId').attr("id", "amountId_"+i);
            $('#addedItemsSold #productTypeId').attr("id", "productTypeId_"+i);

            $('#addedItemsSold #itemId_'+i).attr("name", "barcode_"+i);
                        $('#addedItemsSold #brandId_'+i).attr("name", "brand_"+i);
                        $('#addedItemsSold #priceId_'+i).attr("name", "price_"+i);
                        $('#addedItemsSold #itemTypeId_'+i).attr("name", "itemType_"+i);
                        $('#addedItemsSold #productTypeId_'+i).attr("name", "productType_"+i);
                        $('#addedItemsSold #sizeId_'+i).attr("name", "size_"+i);
                        $('#addedItemsSold #quantityId_'+i).attr("name", "quantity_"+i);
                        $('#addedItemsSold #amountId_'+i).attr("name", "amount_"+i);

//update quantity and amount also
$('#addedItemsSold #quantityId_'+i).attr("value", $('#itemSold #quantityId').val() );
$('#addedItemsSold #quantityId_'+i).val($('#itemSold #quantityId').val());
$('#addedItemsSold #amountId_'+i).attr("value", $('#itemSold #amountId').val() );
$('#addedItemsSold #amountId_'+i).val($('#itemSold #amountId').val());

//Reset quantity and amount for original Row
$('#itemSold #quantityId').val("");
$('#itemSold #amountId').val("");

//Reset the id of itemSignature
            $('#addedItemsSold #itemSignature').attr("id", "itemSignature_"+i);

            //Set values to empty again
                         $( "#itemId" ).val("");
                         $('#itemId').attr("value", "");
                         $( "#productTypeId" ).val("");
                         $('#productTypeId').attr("value", "");
                         $( "#itemTypeId" ).val( "");
                         $('#itemTypeId').attr("value", "");
                         $( "#sizeId" ).val( "" );
                         $('#sizeId').attr("value","");
                         $( "#priceId" ).val("" );
                         $('#priceId').attr("value", "");
                         $( "#brandId" ).val("");
                         $('#brandId').attr("value","");
            //Set countItems count
                         $( "#countItemsId" ).val(i);
                         $('#countItemsId').attr("value",i);

            i=i+1
          })



$('.target').on('change', function(){
  make_calculations(this);
  changeTotal(this);
});

$('.amountFire').on('change', function(){
  changeTotal(this);
});


$('.target').on('keypress', function(){
  make_calculations(this);
  changeTotal(this);
});

function changeTotal(obj) {
  var sum = 0;

  i=0;
  while(i<$('.amount').length) {
      var value = $('.amount').get(i).value;
      if(parseFloat(value)!='NAN' && value.trim()!="" ){
        sum+=parseFloat(value);
        //console.log("sum:"+sum);
      }
      i++;
  }


   $('#actualTotalId').val(sum);
   $('#actualTotalId').attr("value", sum);

    var discount = $('#discountAmountId').val();
    var additionalCharges = $('#additionalChargesId').val();
    var discountPercent = $('#discountPercentId').val();
    var paidAmount =  $('#paidAmountId').val()

    if(discountPercent!=undefined && parseFloat(discountPercent) != 'NaN' && discountPercent.trim()!="") {
      //console.log("discountPercent:"+discountPercent);
      var perc=  (100 - parseFloat(discountPercent))/100;
      //console.log("perc:"+perc);
      //console.log("sum:"+sum);
      sum =  parseFloat(perc) * parseFloat(sum)
    }

      if(discount!=undefined && parseFloat(discount) != 'NaN' && discount.trim()!="") {
     // console.log("discount:"+discount);
      //console.log("sum:"+sum);
      sum=parseFloat(sum) - parseFloat(discount)
    }

     if(additionalCharges!=undefined && parseFloat(additionalCharges) != 'NaN' && additionalCharges.trim()!="") {
     // console.log("additionalCharges:"+additionalCharges);
      //console.log("sum:"+sum);
      sum  = parseFloat(sum) + parseFloat(additionalCharges);

    }

   $('#totalAmountId').val(sum.toFixed(2));
   $('#totalAmountId').attr("value", sum.toFixed(2));
}

function make_calculations(obj) {

  var marker = ""
  if(obj.id.split("_").length>1) {
     marker = "_"+obj.id.split("_")[1];
  }
  var total = parseFloat($('#quantityId'+marker).val()) * parseFloat($('#priceId'+marker).val())
  $('#amountId'+marker).val(total.toFixed(2));
  $('#amountId'+marker).attr("value", total.toFixed(2));

}



        </script>
    <#if success?? >
    <#else>
        <#assign ENTITY_NAME = ENTITY_ITEM!"">
        <#assign entity = entity_item!"">
        <#include "/js/auto_complete_js.ftl">

        <#assign ENTITY_NAME = ENTITY_CUSTOMER!"">
        <#assign entity = entity_customer!"">
        <#include "/js/auto_complete_js_entity.ftl">

        <#assign entity = entity_salesman!"">
        <#assign ENTITY_NAME = ENTITY_SALESMAN!"">
        <#include "/js/auto_complete_js_entity.ftl">
    </#if>
</body>
</html>

