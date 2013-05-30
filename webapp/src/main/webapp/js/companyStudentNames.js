function setCompany(isCompany) {
	if(isCompany) {
		jq("#company_panel_container").show("fast");		
		jq("#notcompany_fields_container").hide("fast");
		jq("#_c_org_vist_vistadmin_domain_Studentx_firstName_id").find("label").text("Kapcsolattartó keresztnév:");
		jq("#_c_org_vist_vistadmin_domain_Studentx_lastName_id").find("label").text("Kapcsolattartó vezetéknév:");
		jq("#_c_org_vist_vistadmin_domain_Studentx_emailAddress_id").find("label").text("Kapcsolattartó email:");
		jq("#_title_student_address_panel_id").find('span').first().next().text("Telephely címe");
	} else {
		jq("#company_panel_container").hide("fast");
		jq("#notcompany_fields_container").show("fast");
		jq("#_c_org_vist_vistadmin_domain_Studentx_firstName_id").find("label").text("Keresztnév:");
		jq("#_c_org_vist_vistadmin_domain_Studentx_lastName_id").find("label").text("Vezetéknév:");
		jq("#_c_org_vist_vistadmin_domain_Studentx_emailAddress_id").find("label").text("Email cím:");
		jq("#_title_student_address_panel_id").find('span').first().next().text("Cím");		
	}
}
var jq = jQuery.noConflict();
jq().ready(function() {
	if(jq("#_company_id").length > 0) {
		setCompany(jq("#_company_id").is(":checked"));					
	}
	
  	jq('#add_cousetime').click(function() {
  	  var opText = '';
  	  var opVal = '';
  	  if(jq("#input_new_student_name").length > 0) {
		  var newStudentName = jq('#input_new_student_name').val();	  	  
		  opText = newStudentName;
		  opVal = newStudentName;
	  } else {
		  opText = jq("#availableStudentNames option:selected").text();
		  opVal = jq("#availableStudentNames option:selected").val();
	  }
	  var op = new Option(opText, opVal);	    
	  var list = jq("#_classTimesList_id");
	  list.append(op);	  
	  return false;
  });
  	
  jq('#remove_cousetime').click(function() {  
    jq('#_classTimesList_id option:selected').remove();
    return false;
  });
  
  if(jq("#_company_id").length > 0) {
	  jq("#_company_id").click(function(){
		// If checked
		  setCompany(jq("#_company_id").is(":checked"));		  
	  });
  }
  var form = jq('#student');
  if(jq("#courseStudent").length > 0) {
	  form = jq('#courseStudent');
  }
  
  form.submit(function() {
	  if(jq("#_flexibleClassTime_id").length > 0) {
		  if (jq("#_flexibleClassTime_id").is(":checked")) {
			  jq('#_classTimesList_id option').empty();  		
		  }
	  }
	  jq('#_classTimesList_id option').each(function(i) {  
		  jq(this).attr("selected", "selected");  
	  });	
  });   
});  



