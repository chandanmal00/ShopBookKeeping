  <script src="//code.jquery.com/jquery-1.10.2.js"></script>
  <script src="//code.jquery.com/ui/1.11.4/jquery-ui.js"></script>
  <link rel="stylesheet" href="/resources/demos/style.css">
  <script>
  $(function() {
    var availableTags = [
Alabama 	AL 	Montana 	MT
Alaska 	AK 	Nebraska 	NE
Arizona 	AZ 	Nevada 	NV
Arkansas 	AR 	New Hampshire 	NH
California 	CA 	New Jersey 	NJ
Colorado 	CO 	New Mexico 	NM
Connecticut 	CT 	New York 	NY
Delaware 	DE 	North Carolina 	NC
Florida 	FL 	North Dakota 	ND
Georgia 	GA 	Ohio 	OH
Hawaii 	HI 	Oklahoma 	OK
Idaho 	ID 	Oregon 	OR
Illinois 	IL 	Pennsylvania 	PA
Indiana 	IN 	Rhode Island 	RI
Iowa 	IA 	South Carolina 	SC
Kansas 	KS 	South Dakota 	SD
Kentucky 	KY 	Tennessee 	TN
Louisiana 	LA 	Texas 	TX
Maine 	ME 	Utah 	UT
Maryland 	MD 	Vermont 	VT
Massachusetts 	MA 	Virginia 	VA
Michigan 	MI 	Washington 	WA
Minnesota 	MN 	West Virginia 	WV
Mississippi 	MS 	Wisconsin 	WI
Missouri 	MO 	Wyoming 	WY
    ];
    $( "#tags" ).autocomplete({
      source: availableTags
    });
  });
  </script>

  <div class="ui-widget">
    <label for="tags">Tags: </label>
    <input id="tags">
  </div>