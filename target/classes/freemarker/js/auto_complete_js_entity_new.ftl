    <script>
      $(function() {
        $.getJSON("/listKeys/${entity}", function(data)
        {
            $( "#${ENTITY_NAME}Id" ).autocomplete({
              source: data,
              delay: 100,
              minLength: 1
            })
        });
      });

  </script>



