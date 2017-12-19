
<#assign cnt = 1>
<table class="table table-striped entityTable" id="entityPayment">
    <thead>
        <tr>
            <th>Row</th>
            <th>${local["Barcode"]}</th>
            <th>${local["EventDate"]}</th>
            <th>StockAdded</th>
            <th>TotalCost</th>
            <th>CountOfEntries</th>
        </tr>
    </thead>
<tbody>


    <#list entityList as entityMember>
        <tr>

            <td>${cnt}</td>
            <td><a href="/details/item/${entityMember['_id']['barcode']!""}">${entityMember['_id']['barcode']!""}</a></td>
            <td>${entityMember['_id']['dt']!""}</a></td>
            <td>${entityMember['stock']}</td>
            <td>${entityMember['totalCost']}</td>
            <td>${entityMember['total']}</td>
        </tr>
        <#assign cnt = cnt+1>
    </#list>

</tbody>
</table>