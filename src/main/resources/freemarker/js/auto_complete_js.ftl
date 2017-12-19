    <script>
    var markup =  ${shop.getMarkupMultiplier()!1}

        function getMarker(obj) {
          var marker = ""
          if(obj.id.split("_").length>1) {
             marker = obj.id.split("_")[1];
          }
          return marker;
         }


$( ".item" ).autocomplete({
           minLength: 1,
           source: function( request, response ) {
           var searchParam  = request.term;
           $.ajax({
               dataType: "json",
               type : 'Get',
               url: '/search/${entity}/'+searchParam,
               success: function(data) {
                   $('.item').removeClass('ui-autocomplete-loading');
                   // hide loading image

                   response( $.map( data, function(item) {
                       return item;
                   }));
               },
               error: function(data) {
                   $('input.item').removeClass('ui-autocomplete-loading');
               }
           });
           },
           delay: 200,
           focus: function( event, ui ) {
                   marker = getMarker(this);
                   //$( "#itemSignature_"+marker+ " #${ENTITY_NAME}Id_"+marker ).val( ui.item.barcode );
                   $( "#${ENTITY_NAME}Id_"+marker ).val( ui.item.barcode );
                   var j =parseInt(marker);
                   while(j< parseInt(marker)+3) {
                     $("#itemSignature_"+j).show();
                     j=j+1;
                   }
                   $( "#countItemsId" ).val(j);
                   $('#countItemsId').attr("value",j);
                   return false;

           },
           select: function( event, ui ) {
             marker = getMarker(this);
             $( "#${ENTITY_NAME}Id_"+marker ).val( ui.item.barcode );
             $('#${ENTITY_NAME}Id_'+marker).attr("value", ui.item.barcode);
             $("#productTypeId_"+marker ).val( ui.item.productType );
             $('#productTypeId_'+marker).attr("value", ui.item.productType);
             $( "#itemTypeId_"+marker).val( ui.item.itemType );
             $('#itemTypeId_'+marker).attr("value", ui.item.itemType);
             $( "#sizeId_" +marker).val( ui.item.size );
             $('#sizeId_'+marker).attr("value", ui.item.size);
             $( "#priceId_" +marker).val( ui.item.purchasePrice * parseFloat(markup)     );
             $('#priceId_'+marker).attr("value", ui.item.purchasePrice.toFixed(2));
             $( "#brandId_" +marker).val(ui.item.brand );
             $('#brandId_'+marker).attr("value", ui.item.brand);
             $( "#quantityId_" +marker).val(1);
             $('#quantityId_'+marker).attr("value", 1);
             var total = parseFloat($('#quantityId_'+marker).val()) * parseFloat($('#priceId_'+marker).val())
             $('#amountId_'+marker).val(total.toFixed(2));
             $('#amountId_'+marker).attr("value", total.toFixed(2));
  make_calculations(this);
  changeTotal(this);

             return false;
           }
         })
         .autocomplete( "instance" )._renderItem = function( ul, item ) {
           return $( "<li>" )
             .append( "" + item.value + "</li>" )
             .appendTo( ul );
         };



  </script>