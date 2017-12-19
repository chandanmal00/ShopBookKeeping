

function GetCellValues(ticks1,headers,targetCols) {
          var table = document.getElementById('joinMapId');

          for (var r = 0, n = table.rows.length; r < n; r++) {
              var cols = new Array();
              for (var c = 1, m = table.rows[r].cells.length; c < m-1; c++) {
                if(r==0) {
                  headers.push(table.rows[r].cells[c].innerHTML);
                } else {
                  if(c==1) {
                  ticks1.push(table.rows[r].cells[c].innerHTML);

                  } else {


                  cols.push(parseFloat(table.rows[r].cells[c].innerHTML.replace(/,/g,'')));
                  }

                }

              }
            if(r!=0) {
            targetCols.push(cols);
            }
          }
          console.log(headers);
          console.log(ticks1);
          console.log(targetCols);

      }

      function NewGetCellValues(ticks1,headers,targetCols) {
                var table = document.getElementById('joinMapId');

                for (var r = 0, n = table.rows.length; r < n; r++) {
                    var cols = new Array();
                    for (var c = 1, m = table.rows[r].cells.length; c < m; c++) {
                      if(r==0) {
                        headers.push(table.rows[r].cells[c].innerHTML);
                        if(c>1) {
                          targetCols[c-2] = new Array();
                        }
                      } else {
                        if(c==1) {
                        ticks1.push(table.rows[r].cells[c].innerHTML);

                        } else {
                        if(c>1) {
                        targetCols[c-2].push(parseFloat(table.rows[r].cells[c].innerHTML.replace(/,/g,''))/10000);
                        }
                        //cols.push(parseFloat(table.rows[r].cells[c].innerHTML.replace(/,/g,'')));
                        }

                      }

                    }

                }
                console.log(headers);
                console.log(ticks1);
                console.log(targetCols);

            }


function drawChart() {

  var headers = new Array();
  var targetCols = new Array();
  var ticks1 = new Array();

  NewGetCellValues(ticks1,headers,targetCols);
  console.log("New values::");
  console.log(headers);
  console.log(targetCols);
  console.log(ticks1);
  // chart data
  var dataArray =targetCols;//[[1,2,3,4,5],[3,4,5,6,7],[5,6,8,9,10],[7,8,9,10,11],[9,10,11,12,13]];

  // x-axis ticks

 // var ticks = [1,2,3,4,5];//ticks1;
  var ticks = ticks1;

  // chart rendering options
  var options = {
    seriesDefaults: {
      renderer:$.jqplot.BarRenderer
    },
    axes: {
      xaxis: {
        renderer: $.jqplot.CategoryAxisRenderer,
        ticks: ticks
      }
    }
  };

  // draw the chart
  //$.jqplot('chartDivId', dataArray, options);


  //var plot1 = $.jqplot ('chartDivId', dataArray);

  plot1 = $.jqplot('chartDivId', dataArray, {
              // Only animate if we're not using excanvas (not in IE 7 or IE 8)..
              animate: !$.jqplot.use_excanvas,
              seriesDefaults:{
                  renderer:$.jqplot.BarRenderer,
                  pointLabels: { show: true }
              },
              axes: {
                  xaxis: {
                      renderer: $.jqplot.CategoryAxisRenderer,
                      ticks: ticks
                  }
              },
              highlighter: { show: false }
          });

          $('#chartDivId').bind('jqplotDataClick',
              function (ev, seriesIndex, pointIndex, data) {
                  $('#chartDivIdInfo').html('series: '+seriesIndex+', point: '+pointIndex+', data: '+data);
              }
          );
}
