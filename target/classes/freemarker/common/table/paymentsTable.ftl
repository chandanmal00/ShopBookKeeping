<#assign cnt = 1>
<table class="table table-striped table-bordered table-condensed entityTable" id="entityPayment">
    <thead>
        <tr>
            <th>Row</th>
            <th>Entity Key</th>
            <th>PaymentSlip Key</th>
            <th>Amount</th>
            <th>Tag</th>
            <th>CreatedBy</th>
        </tr>
    </thead>
    <tbody>


<#list payments as payment>
       <tr>
            <td>${cnt}</td>
            <td>${entity.getUniqueKey()}</td>
            <td><a class="btn btn-info" href="/invoice/payment/${payment.getUniqueKey()}">PaymentSlip <i class="fa fa-print" aria-hidden="true"></i></a></td>
            <td>${payment.getAmount()!""}</td>
            <td>${payment.getTag()!""}</td>
            <td>${payment.getCreatedBy()!"root"}</td>
        </tr>

<#-- ${cnt}: ${payment} -->
<#assign cnt = cnt+1>
</#list>

    </tbody>
</table>