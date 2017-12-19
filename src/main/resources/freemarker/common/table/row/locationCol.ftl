

<#if entityMember.location??>
    <td>${entityMember.getLocation().getPlace()!""}</td>
    <td>${entityMember.getLocation().getAddress()!""}</td>
    <td>${entityMember.getLocation().getTaluka()!""}</td>
    <td>${entityMember.getLocation().getDistrict()!""}</td>
    <td>${entityMember.getLocation().getState()!""}</td>
<#else>
    <td></td>
    <td></td>
    <td></td>
    <td></td>
    <td></td>
</#if>