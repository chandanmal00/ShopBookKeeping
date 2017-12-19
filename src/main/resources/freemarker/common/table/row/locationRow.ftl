<#if entity.location??>
       <tr>
            <td>${local["Place"]}</td>
            <td>${entity.getLocation().getPlace()!""}</td>

       </tr>
       <tr>
            <td>${local["Address"]}</td>
            <td>${entity.getLocation().getAddress()!""}</td>

       </tr>
       <tr>
            <td>${local["Taluka"]}</td>
            <td>${entity.getLocation().getTaluka()!""}</td>

       </tr>
       <tr>
            <td>${local["District"]}</td>
            <td>${entity.getLocation().getDistrict()!""}</td>

       </tr>
       <tr>
            <td>${local["State"]}</td>
            <td>${entity.getLocation().getState()!""}</td>

       </tr>

</#if>