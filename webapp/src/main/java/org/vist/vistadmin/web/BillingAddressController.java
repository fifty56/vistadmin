package org.vist.vistadmin.web;

import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.vist.vistadmin.domain.BillingAddress;

@RequestMapping("/billingaddresses")
@Controller
@RooWebScaffold(path = "billingaddresses", formBackingObject = BillingAddress.class)
public class BillingAddressController  extends BaseController {
}
