 <h2>list of all ${ENTITY_NAME}</h2>
   <#assign cnt = 1>
     <table class="table table-striped entityTable" id="entityPayment">
        <thead>
            <tr>
                <th>Row</th>
                <th>${local["Barcode"]}</th>
                <th>${local["Quantity"]}</th>
                <th>${local["Company"]}/${local["Transaction"]}</th>
                <th>transactionType</th>
                <th>EventDate</th>
                <th>CreatedDate</th>
                <th>Created By</th>
            </tr>
        </thead>
        <tbody>
             <#list entityList as entityMember>

                    <tr>
                         <td>${cnt}</td>
                         <#--<td><a href="/${entity}/${entityMember.getUniqueKey()}">${entityMember.getUniqueKey()}</a></td>-->
                         <#--<td><a href="/details/item/${entityMember.getBarcode()}">${entityMember.getBarcode()!""}</a></td>-->
                         <td><a href="/item/${entityMember.getBarcode()}">${entityMember.getBarcode()!""}</a></td>
                         <#assign quantity = entityMember.getQuantity()>
                         <#if entityMember.getQuantity() lt 0 >
                            <#assign quantity = -1 * quantity>
                         </#if>
                         <td>${quantity}</td>
                         <#if entityMember.details?? && entityMember.details.company??>
                            <td><a href="/company/${entityMember.getDetails().getCompany()}">${entityMember.getDetails().getCompany()!""}</a></td></td>
                         <#else>
                            <#if entityMember.details?? && entityMember.details.invoiceId??>
                            <td><a href="/invoice/transaction/${entityMember.getDetails().getInvoiceId()}">${entityMember.getDetails().getInvoiceId()}</a></td>
                            <#else>
                            <td></td>
                            </#if>
                         </#if>
                         <td>${entityMember.getItemTransactionType().toString()}</td>
                         <td>${entityMember.getEventDate()!""}</td>
                         <td>${entityMember.getCreationDate()!""}</td>
                         <td>${entityMember.getCreatedBy()!"root"}</td>
                     </tr>
                 <#assign cnt = cnt+1>
             </#list>
        </tbody>
     </table>
