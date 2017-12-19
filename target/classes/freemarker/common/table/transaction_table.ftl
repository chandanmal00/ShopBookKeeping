<#if entityList?size gt 0>
 <h4>${local["List"]} of all ${ENTITY_NAME}</h4>
   <#assign cnt = 1>
     <table class="table table-striped entityTable" id="entityPayment">
        <thead>
            <tr>


                <th>Row</th>
                <th>${ENTITY_NAME} Key</th>
                <th>printInvoice</th>
                <th>${local["Customer"]}</th>
                <th>${local["Barcode"]} SoldCount</th>
                <th>${local["Amount"]}</th>
                <th>${local["AmountPaid"]}</th>
                <th>${local["Balance"]}</th>
                <th>${local["Salesman"]}</th>
                <th>${local["PaymentType"]}</th>
                <th>Created By</th>
                                <th>${local["DiscountPercentage"]}</th>
                                <th>${local["Discount"]}</th>
                                <th>${local["AdditionalCharges"]}</th>

            </tr>
        </thead>
        <tbody>
             <#list entityList as entityMember>
                    <tr>
                         <td>${cnt}</td>
                          <td><a href="/invoice/transaction/${entityMember.getUniqueKey()}">${entityMember.getUniqueKey()}</a></td>
                          <td><a class="btn btn-info" href="/invoice/transaction/${entityMember.getUniqueKey()}">Print <i aria-hidden="true" class="fa fa-print"></i></a></td>
                         <td><a href="/customer/${entityMember.getCustomer()}">${entityMember.getCustomer()}</a></td>
                         <td>${entityMember.getItemSolds()?size}</td>
                         <td>${entityMember.getAmount()}</td>
                         <td>${entityMember.getPaidAmount()}</td>
                         <#assign bal=entityMember.getAmount()-entityMember.getPaidAmount()>
                         <td>${bal}</td>
                         <td><a href="/salesman/${entityMember.getSalesman()}">${entityMember.getSalesman()}</a></td>
                         <td>${entityMember.getPaymentType()}</td>
                         <td>${entityMember.getCreatedBy()!"root"}</td>
                         <td>${entityMember.getDiscountPercent()}</dh>
                          <td>${entityMember.getDiscount()}</td>
                          <td>${entityMember.getAdditionalCharges()}</td>
                     </tr>
                 <#assign cnt = cnt+1>
             </#list>
        </tbody>
     </table>
<#else>
     <div class="alert alert-info" role="alert">No Results for the query : <strong>${query!""}</strong></div>
</#if>