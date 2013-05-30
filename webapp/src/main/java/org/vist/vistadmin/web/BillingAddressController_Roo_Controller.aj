// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package org.vist.vistadmin.web;

import java.io.UnsupportedEncodingException;
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
import org.vist.vistadmin.domain.BillingAddress;
import org.vist.vistadmin.service.AddressService;
import org.vist.vistadmin.service.BillingAddressService;
import org.vist.vistadmin.web.BillingAddressController;

privileged aspect BillingAddressController_Roo_Controller {
    
    @Autowired
    BillingAddressService BillingAddressController.billingAddressService;
    
    @Autowired
    AddressService BillingAddressController.addressService;
    
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String BillingAddressController.create(@Valid BillingAddress billingAddress, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, billingAddress);
            return "billingaddresses/create";
        }
        uiModel.asMap().clear();
        billingAddressService.saveBillingAddress(billingAddress);
        return "redirect:/billingaddresses/" + encodeUrlPathSegment(billingAddress.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(params = "form", produces = "text/html")
    public String BillingAddressController.createForm(Model uiModel) {
        populateEditForm(uiModel, new BillingAddress());
        return "billingaddresses/create";
    }
    
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String BillingAddressController.show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("billingaddress", billingAddressService.findBillingAddress(id));
        uiModel.addAttribute("itemId", id);
        return "billingaddresses/show";
    }
    
    @RequestMapping(produces = "text/html")
    public String BillingAddressController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("billingaddresses", billingAddressService.findBillingAddressEntries(firstResult, sizeNo));
            float nrOfPages = (float) billingAddressService.countAllBillingAddresses() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("billingaddresses", billingAddressService.findAllBillingAddresses());
        }
        return "billingaddresses/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String BillingAddressController.update(@Valid BillingAddress billingAddress, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, billingAddress);
            return "billingaddresses/update";
        }
        uiModel.asMap().clear();
        billingAddressService.updateBillingAddress(billingAddress);
        return "redirect:/billingaddresses/" + encodeUrlPathSegment(billingAddress.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String BillingAddressController.updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, billingAddressService.findBillingAddress(id));
        return "billingaddresses/update";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String BillingAddressController.delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        BillingAddress billingAddress = billingAddressService.findBillingAddress(id);
        billingAddressService.deleteBillingAddress(billingAddress);
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/billingaddresses";
    }
    
    void BillingAddressController.populateEditForm(Model uiModel, BillingAddress billingAddress) {
        uiModel.addAttribute("billingAddress", billingAddress);
        uiModel.addAttribute("addresses", addressService.findAllAddresses());
    }
    
    String BillingAddressController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
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
