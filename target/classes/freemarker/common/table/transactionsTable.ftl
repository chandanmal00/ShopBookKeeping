<#assign cnt = 1>
<table class="table table-striped table-bordered table-condensed entityTable" id="transactions">
    <thead>
        <tr>
            <th>Row</th>
            <th>TransactionKey</th>
            <th>Customer Name</th>
            <th>Amount</th>
            <th>Salesman Name</th>
            <th>CreatedBy</th>
        </tr>
    </thead>
    <tbody>


<#list transactions as transaction>
       <tr>
            <td>${cnt}</td>

            <td><a class="btn btn-info" href="/invoice/transaction/${transaction.getUniqueKey()}">Invoice <i class="fa fa-print" aria-hidden="true"></i></a></td>
            <td><a href="/customer/${transaction.getCustomer()}">${transaction.getCustomer()}</a></td>
            <td>${transaction.amount}</td>
            <td><a href="/salesman/${transaction.getSalesman()}">${transaction.getSalesman()}</a></td>
            <td>${transaction.getCreatedBy()!"root"}</td>
        </tr>

<#-- ${cnt}: ${transaction} -->
<#assign cnt = cnt+1>
</#list>

    </tbody>
</table>