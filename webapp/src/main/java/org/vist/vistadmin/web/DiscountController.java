package org.vist.vistadmin.web;

import java.text.SimpleDateFormat;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.vist.vistadmin.domain.Discount;
import org.vist.vistadmin.web.validator.DiscountValidator;
import org.vist.vistadmin.web.validator.StudentValidator;

@RequestMapping("/discounts")
@Controller
@RooWebScaffold(path = "discounts", formBackingObject = Discount.class)
public class DiscountController extends BaseController {
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_TIME_FORMAT_STR);
	    dateFormat.setLenient(false);

	    // true passed to CustomDateEditor constructor means convert empty String to null
	    binder.registerCustomEditor(java.util.Date.class, new CustomDateEditor(dateFormat, true));
				
        binder.setValidator(new DiscountValidator(binder.getValidator()));
    }
	
	
}
