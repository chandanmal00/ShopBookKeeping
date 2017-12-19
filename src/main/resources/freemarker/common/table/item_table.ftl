<#if entityList?size gt 0>
 <h4>list of all ${ENTITY_NAME}</h4>
   <#assign cnt = 1>
     <table class="table table-striped entityTable" id="entityPayment">
        <thead>
            <tr>
                <th>Row</th>
                <th>${ENTITY_NAME} Key</th>
                <th>${local["Barcode"]}</th>
                <th>${local["ItemType"]}</th>
                <th>${local["ProductType"]}</th>
                <th>${local["Brand"]}</th>
                <th>${local["ListPrice"]}</th>
                <th>${local["PurchasePrice"]}</th>
                <th>${local["Size"]}</th>
                <th>${local["Group"]}</th>
                <th>EventDate</th>
                <th>CreatedDate</th>
                <th>Created By</th>
            </tr>
        </thead>
        <tbody>
             <#list entityList as entityMember>
                    <tr>
                         <td>${cnt}</td>
                         <td><a href="/${entity}/${entityMember.getUniqueKey()}">${entityMember.getUniqueKey()}</a></td>
                         <td>${entityMember.getBarcode()!""}</td>
                         <td>${entityMember.getItemType()!""}</td>
                         <td>${entityMember.getProductType()!""}</td>
                         <td>${entityMember.getBrand()!""}</td>
                         <#if entityMember.getListPrice() gt 0>
                         <td>${entityMember.getListPrice()!""}</td>
                         <#else>
                         <td></td>
                         </#if>
                         <td>${entityMember.getPurchasePrice()!""}</td>
                         <td>${entityMember.getSize()!""}</td>
                         <td>${entityMember.getGroup()!""}</td>
                         <td>${entityMember.getEventDate()!"root"}</td>
                         <td>${entityMember.getCreationDate()!"root"}</td>
                         <td>${entityMember.getCreatedBy()!"root"}</td>
                     </tr>
                 <#assign cnt = cnt+1>
             </#list>
        </tbody>
     </table>
<#else>
     <div class="alert alert-info" role="alert">No Results for the query : <strong>${query!""}</strong></div>
</#if>