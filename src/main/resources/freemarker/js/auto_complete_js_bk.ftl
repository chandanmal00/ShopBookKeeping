    <script>
    var markup =  ${shop.getMarkupMultiplier()!1}
     $.getJSON("/listFull/${entity}", function(data){

         $( "#${ENTITY_NAME}Id" ).autocomplete({
           minLength: 0,
           source: data,
           focus: function( event, ui ) {
             $( "#${ENTITY_NAME}Id" ).val( ui.item.barcode );
             alert($(this).attr("id"));
             return false;
           },
           select: function( event, ui ) {
             $( "#${ENTITY_NAME}Id" ).val( ui.item.barcode );
             $('#${ENTITY_NAME}Id').attr("value", ui.item.barcode);
             $( "#productTypeId" ).val( ui.item.productType );
             $('#productTypeId').attr("value", ui.item.productType);
             $( "#itemTypeId" ).val( ui.item.itemType );
             $('#itemTypeId').attr("value", ui.item.itemType);
             $( "#sizeId" ).val( ui.item.size );
             $('#sizeId').attr("value", ui.item.size);
             $( "#priceId" ).val( ui.item.purchasePrice * parseFloat(markup)     );
             $('#priceId').attr("value", ui.item.purchasePrice);
             $( "#brandId" ).val(ui.item.brand );
             $('#brandId').attr("value", ui.item.brand);

             return false;
           }
         })
         .autocomplete( "instance" )._renderItem = function( ul, item ) {
           return $( "<li>" )
             .append( "" + item.barcode + item.value + "</li>" )
             .appendTo( ul );
         };


     });



  </script>