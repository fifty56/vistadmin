var jq = jQuery.noConflict();
jq().ready(function() {
	if (jq("#_flexibleClassTime_id").is(":checked"))
	{
		jq("#multidd_container").hide("fast");
	} else {			
		jq("#multidd_container").show("fast");
	}
	
  jq('#add_cousetime').click(function() {
	  var dayOp = jq('#select_days option:selected');
	  var hourFromOp = jq('#select_from_hour option:selected');
	  var minFromOp = jq('#select_from_min option:selected');
	  var hourTillOp = jq('#select_till_hour option:selected');
	  var minTillOp = jq('#select_till_min option:selected');
	  var numOfClass = jq('#select_class_number option:selected');
	  
	  var opText = dayOp.text() + ": " + hourFromOp.text() + ":" + minFromOp.text() + " - " + hourTillOp.text() + ":" + minTillOp.text() + " (" + numOfClass.text() + ")";
	  var opVal = dayOp.val() + "@" + hourFromOp.val() + ":" + minFromOp.val() + "-" + hourTillOp.val() + ":" + minTillOp.val() + "|" + numOfClass.val();

	  var op = new Option(opText, opVal);	    
	  var list = jq("#_classTimesList_id");
	  list.append(op);	  
	  return false;
  });  
  jq('#remove_cousetime').click(function() {  
    jq('#_classTimesList_id option:selected').remove();
    return false;
  });
  jq("#_flexibleClassTime_id").click(function(){
		// If checked
	  if (jq("#_flexibleClassTime_id").is(":checked"))
		{
			jq("#multidd_container").hide("fast");
		} else {			
			jq("#multidd_container").show("fast");
		}
  });  
  jq('#course').submit(function() {
	if (jq("#_flexibleClassTime_id").is(":checked")) {
		jq('#_classTimesList_id option').empty();  		
	} 
	jq('#_classTimesList_id option').each(function(i) {  
		jq(this).attr("selected", "selected");  
	});	
  });   
  
  //instant details
  if(jq("#instant_course_details").length > 0) {
	  jq('#courseFormat').change(function(){
		  //opText = jq("#courseFormat option:selected").text();
		  opVal = jq("#courseFormat option:selected").val();          
          if(opVal == 'INSTANT') {
        	  jq("#instant_course_details").show('slow');
          } else {
        	  jq("#instant_course_details").hide('slow');
          }
      });
  }
});  



