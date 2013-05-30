package org.vist.vistadmin.web;

import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.vist.vistadmin.domain.TeacherPayment;

@RequestMapping("/teacherpayments")
@Controller
@RooWebScaffold(path = "teacherpayments", formBackingObject = TeacherPayment.class)
public class TeacherPaymentController  extends BaseController  {
}
