    <script>
    $( "#${ENTITY_NAME}Id" ).autocomplete({
           minLength: 1,
           source: function( request, response ) {
           var searchParam  = request.term;
           $.ajax({
               dataType: "json",
               type : 'Get',
               url: '/searchKeys/${entity}/'+searchParam,
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
           <#if search??>
           select: function( event, ui ) {
               window.location.href = window.location.origin + "/details/${entity}/"+ui.item.value;
            },
           </#if>
   });

    </script>


