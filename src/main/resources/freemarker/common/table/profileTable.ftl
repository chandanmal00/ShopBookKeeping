
<table class="table table-striped table-bordered table-condensed" id="entity">
    <thead>
        <tr>
            <th>Entity</th>
            <th>EntityValue</th>
        </tr>
    </thead>
    <tbody>

        <tr>
            <td>Key</td>
            <td>${entity.getUniqueKey()!""}</td>
        </tr>

        <#include "/common/table/row/nameRow.ftl">
        <#include "/common/table/row/loyaltyRow.ftl">
        <#include "/common/table/row/locationRow.ftl">
        <#include "/common/table/row/nationalIdentityRow.ftl">

        <#if entity.commission??>
           <tr>
                <td>${local["Commission"]}</td>
                <td>${entity.getCommission()!0}%</td>

           </tr>
        </#if>
        <#if entity.age??>
           <tr>
                <td>${local["Age"]}</td>
                <td>${entity.getAge()!""}</td>

           </tr>
        </#if>
        <#if entity.tin??>
           <tr>
                <td>${local["Tinnumber"]}</td>
                <td>${entity.getTin()!""}</td>
           </tr>
        </#if>
    </tbody>
</table>