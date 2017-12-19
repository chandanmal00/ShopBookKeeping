<div id="div_cvs_${ENTITY_TABLE_NAME}">
<br>
<canvas id="cvs_${ENTITY_TABLE_NAME}" width="900" height="300">
</canvas>
<br>
</div>

<#assign cnt = 1>
<table class="table table-striped entityTable" id="entityPayment_${ENTITY_TABLE_NAME}">
    <thead>
        <tr>
            <th>Row</th>
            <th>${local["Date"]}</th>
            <th>${local["Amount"]}</th>
            <#if ENTITY_NAME=="kisaanTransaction">
              <th>KhareeddarAmount</th>
            </#if>
        </tr>
    </thead>
<tbody>


    <#list entityList as entityMember>
        <tr>

            <td>${cnt}</td>
            <td>${entityMember['_id']!""}</td>
            <td>${entityMember['amount']}</td>
            <#if ENTITY_NAME=="kisaanTransaction">
              <td>${entityMember['amountKhareeddar']}</td>
            </#if>
        </tr>
        <#assign cnt = cnt+1>
    </#list>

</tbody>
</table>


<script>
$( document ).ready(function() {
    var idName = '${ENTITY_TABLE_NAME}';
    var table = document.getElementById("entityPayment_"+idName);
    var dataObj = GetCellValuesAsVectors(table);
    if(dataObj.data.length>0) {
      if ($(window).width()<1200)  {
         $("#cvs_${ENTITY_TABLE_NAME}").width($(window).width()*(2.2/3));
      }
      //$("#cvs_${ENTITY_TABLE_NAME}").height($(window).height()*(2.2/3));
      drawGraph(dataObj,"cvs_"+idName,idName+ " Summary");
    } else {
      $('#div_cvs_'+idName).hide();
    }
});
</script>