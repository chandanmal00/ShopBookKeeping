
       <#if entity.nationalIdentity??>
       <tr>
            <td>${local["AadharCardNumber"]}</td>
            <td>${entity.getNationalIdentity().getAadhar()!""}</td>
       </tr>
       <tr>
            <td>${local["PANnumber"]}</td>
            <td>${entity.getNationalIdentity().getPan()!""}</td>
       </tr>
       </#if>
