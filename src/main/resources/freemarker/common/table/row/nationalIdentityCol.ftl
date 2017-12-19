<#if entityMember.nationalIdentity??>
    <td>${entityMember.getNationalIdentity().getAadhar()!""}</td>
    <td>${entityMember.getNationalIdentity().getPan()!""}</td>
<#else>
    <td></td>
    <td></td>
</#if>
