    <script src="/jquery_1_11_4/external/jquery/jquery.js"></script>
    <script src="/jquery_1_11_4/jquery-ui.js"></script>
    <script src="/bootstrap.min.js"></script>
    <script type="text/javascript" src="/bootstrapValidator.min.js"></script>
    <script type="text/javascript" src="/jquery.dataTables.min.js"></script>
    <script type="text/javascript" src="/dataTables.buttons.min.js"></script>
    <script type="text/javascript" src="/buttons.print.min.js"></script>
    <script type="text/javascript" src="/buttons.flash.min.js"></script>
    <script type="text/javascript" src="/buttons.html5.min.js"></script>
    <script type="text/javascript" src="/jszip.min.js"></script>
    <script type="text/javascript" src="/pdfmake.min.js"></script>
    <script type="text/javascript" src="/vfs_fonts.js"></script>
    <script type="text/javascript" src="/dataTables.fixedHeader.min.js"></script>
    <script type="text/javascript" src="/bootstrap-datepicker.min.js"></script>

    <!--hotkeys-->
    <script src="/jquery.hotkeys.js"></script>

    <!-- plotting -->
    <script src="/RGraph.common.core.js"></script>
    <script src="/RGraph.line.js"></script>
    <script src="/RGraph.common.dynamic.js"></script>
    <script src="/RGraph.common.tooltips.js"></script>
    <script src="/RGraph.common.key.js"></script>

    <!--i18n-->
    <script type="text/javascript" src="/i18n.js"></script>

        <script>

            //This page is a result of an autogenerated content made by running test.html with firefox.
            function hotKeys(){
                //$(document).bind(null, 'ctrl+l', clickHandler);
                $(document).bind('keydown', 'ctrl+l', loginHandler);
                $(document).bind('keydown', 'ctrl+t', addTransactionHandler);
                $(document).bind('keydown', 'ctrl+k', addPaymentHandler);
                $(document).bind('keydown', 'ctrl+j', addInventorytHandler);
                $(document).bind('keydown', 'ctrl+shift+k', searchCustomerPayment);
                $(document).bind('keydown', 'ctrl+shift+j', searchCustomer);
                $(document).bind('keydown', 'ctrl+shift+t', searchTransaction);
                $(document).bind('keydown', 'ctrl', showTips);
                $(document).bind('keydown', 'shift', goToHome);
                $(document).bind('keydown', 'esc', hideTips);
                $(document).bind('keydown', '1', showHelp);
                $(document).bind('keydown', '2', hideHelp);
                $(document).bind('keydown', 'ctrl+1', displayDaily);
                $(document).bind('keydown', 'ctrl+2', displayQuarterly);
            }


            function goToHome(event){
              window.location.href=window.location.origin;
              return false;
            }
            function loginHandler(event){
              window.location.href=window.location.origin+"/login";
              return false;
            }

            function showHelp(event){
                  $('.help').show();
                  $('.tips').show();
                  return false;
            }
            function hideHelp(event){
                  $('.help').hide();
                  $('.tips').hide();
                  return false;
            }
            function showTips(event){
                  $('.tips').show();
                  return false;
            }
            function hideTips(event){
                  $('.tips').hide();
                  return false;
            }
            function displayDaily(event){
                  window.location.href=window.location.origin+"/last7days";
                  return false;
            }
            function displayQuarterly(event){
                  window.location.href=window.location.origin+"/quarterly";
                  return false;
            }
            function searchTransaction(event){
                  window.location.href=window.location.origin+"/searchEntity/transaction";
                  return false;
            }
            function searchCustomer(event){
                  window.location.href=window.location.origin+"/searchEntity/customer";
                  return false;
            }
            function searchCustomerPayment(event){
                  window.location.href=window.location.origin+"/searchEntity/payment";
                  return false;
            }
            function addTransactionHandler(event){
                  window.location.href=window.location.origin+"/add/transaction";
                  return false;
            }

            function addPaymentHandler(event){
                  window.location.href=window.location.origin+"/add/payment";
                  return false;
            }

            function addInventorytHandler(event){
                  window.location.href=window.location.origin+"/add/item";
                  return false;
            }
            function addKhareeddar(event){
                  window.location.href=window.location.origin+"/addKhareeddar";
                  return false;
            }
            function addCustomer(event){
                  window.location.href=window.location.origin+"/add/customer";
                  return false;
            }

<!--plotting code-->
function GetCellValuesAsVectors(table) {
    var arr = [];
    var indexDate = 0;
    var flag=0;
    var dataObj = new Object();

    var n = table.rows.length;
    //This will keep the index for the date field
    var indexDate = 0;
    var dataColumns = [];
    var headers = [];
    for (var r = 0; r < n; r++) {
        var m = table.rows[r].cells.length;
        for(c=0;c<m;c++) {
            if(r==0) {
                if('Date'===table.rows[r].cells[c].innerHTML) {
                indexDate=c;
                }
                headers.push(table.rows[r].cells[c].innerHTML);
                continue;
            }
            if(arr[c]==undefined) {
                arr[c] = [];
            }

            //Handle other columns as float
            if(indexDate != c)  {
                var v =parseFloat(table.rows[r].cells[c].innerHTML.replace(/,/g,""));
                arr[c].push(v);

            } else {
                //formatting date
                var value = table.rows[r].cells[c].innerHTML;
                var dtArr = value.split("-");
                if(dtArr.length>1) {
                   arr[c].push(dtArr.slice(1).join("/"));
                } else {
                   arr[c].push(value);
                }
                //arr[c].push(table.rows[r].cells[c].innerHTML);
            }
        }
    }

    dataObj.data=arr;
    dataObj.headers = headers;
    dataObj.dataColumns = dataColumns;
    return dataObj
}

function drawGraph(dataObj,divIdGraph, title) {

    //Tool Tips
    var tips = [];
    for(var i=2;i<dataObj.data.length;i++) {
        for(j=0;j<dataObj.data[i].length;j++) {
            tips.push("value: "+dataObj.data[i][j].toLocaleString() + "<br>date: " + dataObj.data[1][j]);
        }
    }

    var line = new RGraph.Line({
        id: divIdGraph,
        data: dataObj.data.slice(2),

        options: {
        key: dataObj.headers.slice(2),
        keyPosition: 'gutter',
        tickmarks: 'circle',
        gutterLeft: 75,
        gutterRight: 55,
        linewidth: 3,
        shadow: true,
        labels: dataObj.data[1],
        yaxispos: 'right',
        tooltips: tips,
        labelsOffsetx: 10,
        labelsPosition:'edge',
        //title: title
        textAccessible: true,
        tooltipsHighlight: false
        }
    }).draw();
}

//Table search/sort
window.onload=function() {
$('table.entityTable').DataTable(
{
    dom: 'Bfrtip',
    fixedHeader: true,
    lengthMenu: [
                [ 10, 25, 50, 100, -1 ],
                [ '10 rows', '25 rows', '50 rows','100 rows', 'Show all' ]
            ],
    buttons: [
        'pageLength','copy', 'csv','excel', 'pdf','print'
    ]
});


    $('.input-daterange').datepicker({
        weekStart: 11,
        format: "yyyy-mm-dd",
    });

     hotKeys();

};
        </script>