<#if entityList?size gt 0>
<h4>${local["List"]} of all ${ENTITY_NAME}</h4>
<#assign cnt = 1>
<table class="table table-striped entityTable" id="entityPayment">
     <thead>
         <tr>
             <th>Row</th>
             <th>${ENTITY_NAME} Key</th>
             <th>${local["Customer"]} Key</th>
             <th>${local["Amount"]}</th>
             <th>Tag</th>
             <th>PaymentSlip</th>
             <th>CreatedBy</th>
         </tr>
     </thead>
     <tbody>

         <#list entityList as entityMember>
                <tr>
                     <td>${cnt}</td>
                     <td><a href="/invoice/${entity}/${entityMember.getUniqueKey()}">${entityMember.getUniqueKey()}</a></td>
                     <td><a href="/customer/${entityMember.getCustomer()}">${entityMember.getCustomer()}</a></td>
                     <td>${entityMember.getAmount()}</td>
                     <td>${entityMember.getTag()!""}</td>
                     <td><a class="btn btn-info" href="/invoice/${entity}/${entityMember.getUniqueKey()}">PaymentSlip <i class="fa fa-print" aria-hidden="true"></i>
                     </a></td>
                     <td>${entityMember.getCreatedBy()!"root"}</td>
                 </tr>
         <#assign cnt = cnt+1>
         </#list>

     </tbody>
</table>
<#else>
     <div class="alert alert-info" role="alert">No Results for the query : <strong>${query!""}</strong></div>
</#if>