 <h4>${local["List"]} of all ${ENTITY_NAME}</h4>
   <#assign cnt = 1>
     <table class="table table-striped entityTable" id="entityPayment">
        <thead>
            <tr>
                <th>Row</th>
                <th>${ENTITY_NAME} Key</th>
                <th>${local["TransactionAmount"]}</th>
                <th>${local["Commission"]}</th>
                <th>Created By</th>
            </tr>
        </thead>
        <tbody>

             <#list joinMap?keys as key>
             <#if joinMap[key].first??>
             <tr>
                 <td>${cnt}</td>
                 <td><a href="/${entity}/${key}">${key}</a></td>
<#assign transactionAmount=0>
                 <#if joinMap[key].second??>
                   <#assign transactionAmount = joinMap[key].getSecond()['total'] >
                 </#if>
                 <td>${transactionAmount}</td>
                 <#if joinMap[key].first??>
                   <#assign entityMember = joinMap[key].getFirst()['obj']>
                     <#if entityMember.commission??>
                       <td>${entityMember.getCommission()!0}</td>
                      <#else>
                       <td></td>
                      </#if>
                      <td>${entityMember.getCreatedBy()!"root"}</td>
                 <#else>

                   <td>0</td>
                   <td>root</td>
                 </#if>
             </tr>
             <#assign cnt = cnt+1>
                    </#if>
         </#list>

        </tbody>
     </table>
