
      <script>
        $(function() {
          $.getJSON("/listFull/${entity}", function(data){
              $( "#${ENTITY_NAME}Id" ).autocomplete({
                minLength: 1,
                source: data,
                focus: function( event, ui ) {
                  $( "#${ENTITY_NAME}Id" ).val( ui.item.barcode );
                  return false;
                },
                select: function( event, ui ) {
                  $( "#${ENTITY_NAME}Id" ).val( ui.item.barcode );
                  return false;
                }
              })
              .autocomplete( "instance" )._renderItem = function( ul, item ) {

                return $( "<li>" )
                  .append( "<a>" + item.barcode + "<br>" + item.barcode + "</a>" )
                  .appendTo( ul );
              };
            });

          });
    </script>