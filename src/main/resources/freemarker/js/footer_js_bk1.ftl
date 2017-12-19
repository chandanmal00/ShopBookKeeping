    <script src="//code.jquery.com/jquery-1.10.2.js"></script>
    <script src="//code.jquery.com/ui/1.11.4/jquery-ui.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="/jquery.jcarousellite.min.js"></script>
    <script type="text/javascript" src="//cdn.jsdelivr.net/jquery.bootstrapvalidator/0.5.0/js/bootstrapValidator.min.js"></script>
    <script>
    $(function() {
        var cache = {};
        $( "#search" ).autocomplete({
          minLength: 2,
          delay: 300,
          source: function( request, response ) {
             var term = request.term.trim();
             if ( term in cache ) {
               response( cache[ term ] );
               return;
             }

             $.getJSON( "/get_suggestions/"+term, request, function( data, status, xhr ) {
               cache[ term ] = data;
               response( data );
             });
          },
          focus: function( event, ui ) {
           $( "#search" ).val( ui.item.text );
            return false;
          },
          select: function( event, ui ) {
            window.location.href = "/post/"+ui.item.payload.permalink;
            //$( "#search" ).val( ui.item.text );
            return false;
          }
        })
        .autocomplete( "instance" )._renderItem = function( ul, item ) {
          return $( "<li>" )
            //.append( "<a>" + item.text + " " + item.payload.skills.join(",") + "<br>" + item.payload.permalink + "</a>" )
            .append( "<a>" + item.text + " "+ item.payload.city + " "+ item.payload.state + " " + item.payload.skills.join(",") + "</a>" )
            .appendTo( ul );
        };

    });

    </script>
