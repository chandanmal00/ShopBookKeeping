    <script>

    function getMarker(obj) {
      var marker = ""
      if(obj.id.split("_").length>1) {
         marker = "_"+obj.id.split("_")[1];
      }
      return marker;
     }

     $.getJSON("/listFull/${entity}", function(data){

         $( "#itemSold .${ENTITY_NAME}" ).autocomplete({
           minLength: 0,
           source: data,
           focus: function( event, ui ) {
             $( "#${ENTITY_NAME}Id" ).val( ui.item.barcode );
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
             $( "#priceId" ).val( ui.item.purchasePrice );
             $('#priceId').attr("value", ui.item.purchasePrice);
             $( "#brandId" ).val(ui.item.brand );
             $('#brandId').attr("value", ui.item.brand);

             return false;
           }
         })
         .autocomplete( "instance" )._renderItem = function( ul, item ) {
           return $( "<li>" )
             .append( "" + item.barcode + "</li>" )
             .appendTo( ul );
         };

         $( "#addedItemsSold" ).on('focusin','input .item',function(){
         $(this).autocomplete({

                    minLength: 0,
                    source: data,
                    focus: function( event, ui ) {

                      marker = getMarker(this);
                      $( "#itemSignature"+marker+ " .${ENTITY_NAME}" ).val( ui.item.barcode );
                      return false;
                    },
                    select: function( event, ui ) {
                      marker = getMarker(this);
                      $( "#itemSignature"+marker+ " .${ENTITY_NAME}" ).val( ui.item.barcode );
                      $("#itemSignature"+marker+ "#${ENTITY_NAME}").attr("value", ui.item.barcode);
                      $("#itemSignature"+marker+ " #productTypeId" ).val( ui.item.productType );
                      $("#itemSignature"+marker+ " #productTypeId").attr("value", ui.item.productType);
                      return false;
                    }
                  })

     .autocomplete( "instance" )._renderItem = function( ul, item ) {
                         return $( "<li>" )
                           .append( "" + item.barcode + "</li>" )
                           .appendTo( ul );
                       };

     });



  });


  </script>