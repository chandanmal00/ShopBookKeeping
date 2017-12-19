
      <script>
      var options = {

        url: function(phrase) {
          return "/listFull/${entity}";
        },

        getValue: function(element) {
          return element.barcode;
        },

        ajaxSettings: {
          dataType: "json",
          method: "GET",
          data: {
            dataType: "json"
          }
        },

        preparePostData: function(data) {
          data.phrase = $("#${ENTITY_NAME}Id").val();
          return data;
        },

        requestDelay: 400

      };


      $("#${ENTITY_NAME}Id").easyAutocomplete(options);
    </script>