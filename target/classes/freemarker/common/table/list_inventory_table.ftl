
<#assign cnt = 1>
<table class="table table-striped entityTable" id="entityPayment">
    <thead>
        <tr>
            <th>Row</th>
            <th>${local["Barcode"]}</th>
            <th>StockAdded</th>
            <th>Sold</th>
            <th>Available</th>
            <th>TotalCost</th>
            <th>TotalRevenueSoFar</th>
            <th>PossibleFutureRevenue</th>
            <th>CountOfEntries</th>
        </tr>
    </thead>
<tbody>


    <#list entityList as entityMember>
        <tr>

            <td>${cnt}</td>
            <td><a href="/item/${entityMember['_id']!""}">${entityMember['_id']!""}</a></td>
            <#--<td><a href="/details/item/${entityMember['_id']!""}">${entityMember['_id']!""}</a></td>-->
            <td>${entityMember['stock']}</td>
            <td>${entityMember['sold']*-1}</td>
            <td>${entityMember['stock'] -  (-1*entityMember['sold'])}</td>
            <td>${entityMember['totalCost']}</td>
            <td>${entityMember['totalRevenue']}</td>
            <td>${entityMember['avgPriceSold'] * (entityMember['stock'] -  (-1*entityMember['sold'])) }</td>
            <td>${entityMember['total']}</td>
        </tr>
        <#assign cnt = cnt+1>
    </#list>

</tbody>
</table>