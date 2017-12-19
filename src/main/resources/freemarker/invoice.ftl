<!doctype html>
<html lang="en">
<head>
    <#include "/common/meta.ftl">
    <#include "/css/head.css">
    <title>${operation} for ${entity}</title>

</head>

<body>
    <div class="container-fluid">
      <div class="no_show">
        <#include "/common/header.ftl">
        <br>
        <#include "/common/errors.ftl">
        <div class="hidden-print" style="text-align: right;">
        <a class="btn btn-info hidden-print"  onclick="printPage();" href="#">Print <i class="fa fa-print fa-2x" aria-hidden="true"></i> </a>
        </div>
        <#assign perc =  entityObject.getDiscountPercent()!0>
        <#assign percValue = (perc)/100>
        <#assign percAmount =  percValue * entityObject.getActualAmount()>
    </div>
    <div class="row panel panel-default">
        <div class="col-xs-6 text-left">
                    <div>
                        <h3>${broker.getFirmName()}</h3>
                    </div>
                    <div>
                        <p>
                            Proprietor: ${broker.getProprietor()}<br>
                            ${broker.getLocation().getAddress()}, ${broker.getLocation().getPlace()},Dist: ${broker.getLocation().getDistrict()} <br>
                        </p>
                    </div>
        </div>
        <div class="col-xs-6 text-right">
            <h2 class="hidden-print">INVOICE</h2>
            <p>
            <strong>Invoice No:</strong> ${entityObject.get_id()}<br>
            <strong>Date of Transaction:</strong> ${entityObject.getCreationDate()}<br>
            <strong>Salesman</strong>: ${entityObject.getSalesman()}
            </p>
        </div>
    </div>
    <div class="row text-left panel panel-info" style="font-size: larger;" >
    <br>
            <strong>  Name</strong>: ${entityObject.getCustomer()}
            <#--
                    <#if entityObject.getCustomer().location??>
                        <span style="font-size: smaller;">, ${entityObject.getCustomer().getLocation().address!"NO_ADDRESS_FILE"}
                        , ${entityObject.getCustomer().getLocation().place!"NO_PLACE_FILE"} </span>
                    <#else>
                        <span style="font-size: smaller;">, NO_ADDRESS_FILE<span>
                    </#if>
                    -->
    <br>
    <br>
    </div>
    <!-- / end client details section -->
    <table class="table table-bordered table-condensed">
        <thead>
        <tr>
            <th>
                <strong>Row</strong>
            </th>
            <th>
                <strong>Barcode</strong>
            </th>
            <th>
                <strong>Price</strong>
            </th>
            <th>
                <strong>Quantity</strong>
            </th>
            <th class="text-right">
                <strong>Amount</strong>
            </th>
        </tr>
        </thead>
        <tbody>
        <#assign cnt = 1>

                            <#list entityObject.getItemSolds() as itemSold >
                                                <tr>
                                                     <td>${cnt}</td>
                                                     <td>${itemSold.getBarcode()!""}</td>
                                                     <td>${itemSold.getPrice()!0}</td>
                                                     <td>${itemSold.getQuantity() * -1}</td>

                                                     <td class="text-right">${itemSold.getAmount()!""}</td>
                                                 </tr>
                                             <#assign cnt = cnt+1>
                             </#list>

            <tr>

                <td>
                    <tr><td>Discount</td><td class="text-right">(${entityObject.getDiscount()})</td></tr>


                    <tr><td>Discount Percentage Amount</td><td class="text-right">(${entityObject.getDiscountPercent()!0}%), amount: (${percAmount})</td></tr>
                    <tr><td>Additional Charges</td><td class="text-right">${entityObject.getAdditionalCharges()}</td></tr>
                    <tr><td>PaymentType</td><td class="text-right">${entityObject.getPaymentType()}</td></tr>
                    <tr><td>Created By</td><td class="text-right">${entityObject.getCreatedBy()!"root"}</td></tr>
                </td>
<#assign discountToRemove =  entityObject.getDiscount() + percAmount >

                <#assign charges = entityObject.getAdditionalCharges()!0>
                <td>Total Discount: (${discountToRemove})</td>
                <td>Total Charges:${charges}</td>
                <td></td>
                <td>Total:</td>
                <td class="text-right">${entityObject.getActualAmount()}</td>

            </tr>
            <tr>
                            <td></td>
                            <td></td>
                            <td></td>
                <td>Total Others</td>
<#if discountToRemove gt charges>
                <td class="text-right">(${discountToRemove-charges})</td>
                <#else>
                <td class="text-right">${charges-discountToRemove}</td>
                </#if>
            </tr>

        </tbody>
    </table>
    <div class="row text-right">
        <div class="col-xs-2 col-xs-offset-8">
            <p>
                <strong>
                    Sub Total : <br>
                    TAX : <br>
                    Total : <br>
                    Amount Paid : <br>
                    Balance : <br>
                </strong>
            </p>
        </div>
        <div class="col-xs-2">
        <#assign amount = entityObject.getAmount()>
        <#assign bal = amount - entityObject.getPaidAmount()>
            <strong>
                ${amount} <br>
                N/A <br>
                ${amount} <br>
                ${entityObject.getPaidAmount()} <br>
                ${bal} <br>
            </strong>
        </div>
    </div>
<div class="row text-right">
    <h4><u>Signature</u></h4>
    </div>
    <#include "/common/footer.ftl">
</div>
    <script>
    function printPage() {
            window.print();
        }


    </script>
    <#include "/js/footer_js.ftl">
    <#include "/js/alerts_js.ftl">
</body>
</html>