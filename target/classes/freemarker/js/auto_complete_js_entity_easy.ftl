
<script>


var options = {
    url: "/listFull/${entity}",

    getValue: "barcode",

    template: {
        type: "description",
        fields: {
            description: "productType"
        }
    },

    list: {
        match: {
            enabled: true
        }
    },

    theme: "plate-dark"
};

$("#${ENTITY_NAME}Id").easyAutocomplete(options);

    </script>