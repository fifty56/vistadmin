// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.vist.vistadmin.web;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;
import org.vist.vistadmin.domain.Discount;
import org.vist.vistadmin.domain.common.DiscountType;
import org.vist.vistadmin.service.DiscountService;
import org.vist.vistadmin.web.DiscountController;

privileged aspect DiscountController_Roo_Controller {
    
    @Autowired
    DiscountService DiscountController.discountService;
    
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String DiscountController.create(@Valid Discount discount, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, discount);
            return "discounts/create";
        }
        uiModel.asMap().clear();
        discountService.saveDiscount(discount);
        return "redirect:/discounts/" + encodeUrlPathSegment(discount.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(params = "form", produces = "text/html")
    public String DiscountController.createForm(Model uiModel) {
        populateEditForm(uiModel, new Discount());
        return "discounts/create";
    }
    
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String DiscountController.show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("discount", discountService.findDiscount(id));
        uiModel.addAttribute("itemId", id);
        return "discounts/show";
    }
    
    @RequestMapping(produces = "text/html")
    public String DiscountController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("discounts", discountService.findDiscountEntries(firstResult, sizeNo));
            float nrOfPages = (float) discountService.countAllDiscounts() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("discounts", discountService.findAllDiscounts());
        }
        return "discounts/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String DiscountController.update(@Valid Discount discount, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, discount);
            return "discounts/update";
        }
        uiModel.asMap().clear();
        discountService.updateDiscount(discount);
        return "redirect:/discounts/" + encodeUrlPathSegment(discount.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String DiscountController.updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, discountService.findDiscount(id));
        return "discounts/update";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String DiscountController.delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        Discount discount = discountService.findDiscount(id);
        discountService.deleteDiscount(discount);
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/discounts";
    }
    
    void DiscountController.populateEditForm(Model uiModel, Discount discount) {
        uiModel.addAttribute("discount", discount);
        uiModel.addAttribute("discounttypes", Arrays.asList(DiscountType.values()));
    }
    
    String DiscountController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
        String enc = httpServletRequest.getCharacterEncoding();
        if (enc == null) {
            enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
        }
        try {
            pathSegment = UriUtils.encodePathSegment(pathSegment, enc);
        } catch (UnsupportedEncodingException uee) {}
        return pathSegment;
    }
    
}
