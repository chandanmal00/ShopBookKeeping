 <h4>list of all ${ENTITY_NAME}</h4>
   <#assign cnt = 1>
     <table class="table table-striped entityTable" id="entityPayment">
        <thead>
            <tr>
                <th>Row</th>
                <th>${ENTITY_NAME} Key</th>
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
                         <td>${entityMember.getUniqueKey()}</td>
                         <td><a href="/item/${entityMember.getBarcode()}">${entityMember.getBarcode()!""}</a></td>
                         <#--<td><a href="/details/item/${entityMember.getBarcode()}">${entityMember.getBarcode()!""}</a></td>-->
                         <td>${entityMember.getQuantity()!""}</td>
                         <#if entityMember.company??>
                            <td>${entityMember.getCompany().getUniqueKey()!""}</td>
                         <#else>
                            <#if entityMember.transactionId??>
                            <td><a href="/invoice/transaction/${entityMember.getTransactionId()}">${entityMember.getTransactionId()}</a></td>
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
