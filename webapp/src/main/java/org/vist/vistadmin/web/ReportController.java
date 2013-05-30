package org.vist.vistadmin.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.vist.vistadmin.domain.BillingAddress;
import org.vist.vistadmin.domain.Course;

import java.io.UnsupportedEncodingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
import org.vist.vistadmin.service.CourseService;
import org.vist.vistadmin.service.CourseWithMoreCurrentTeachers;
import org.vist.vistadmin.reporting.VistReportException;
import org.vist.vistadmin.reporting.VistReportService;
import org.vist.vistadmin.web.BillingAddressController;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@RequestMapping("/report")
@Controller
public class ReportController extends BaseController {

	private static final Logger LOGGER =  LoggerFactory.getLogger(ReportController.class);
	
	@Autowired
	CourseService courseService;
	
	@Autowired
	VistReportService reportService;
	
	@Autowired
	private ApplicationContext context;
	
	@RequestMapping(method = RequestMethod.GET , value = "/{type}/{format}/{id}")
	public void generatePdfReport(@PathVariable("type") String type, @PathVariable("format") String format, 
				@PathVariable("id") Long id, ModelAndView modelAndView,Locale locale, HttpServletRequest httpServletRequest, HttpServletResponse response) throws Exception {
		
		/*Course course = courseService.findCourse(courseId);
		Map<String,Object> parameterMap = new HashMap<String,Object>();		
		//JRDataSource JRdataSource = new JRBeanCollectionDataSource(usersList);		
		//parameterMap.put("datasource", JRdataSource);				
		parameterMap.put("course", course);
		modelAndView = new ModelAndView("pdfReport", parameterMap);	
		return modelAndView;*/
		if(type.equals("classsheet")) {
			reportService.generateClassSheetReport(id, format, context, locale, response);	
		} else if(type.equals("studentcontract")) {
			reportService.generateStudentContractReport(id, format, context, locale, response);
		} else if(type.equals("teachercontract")) {
			reportService.generateTeacherContractReport(id, format, context, locale, response);
		}
	}

	
	
	/*@RequestMapping(method = RequestMethod.GET , value = "xls")
	public ModelAndView generateXlsReport(ModelAndView modelAndView){
	
	logger.debug("--------------generate XLS report----------");

	Map<String,Object> parameterMap = new HashMap<String,Object>();
	
	List<User> usersList = userDao.retrieveAllRegisteredUsers();
	
	JRDataSource JRdataSource = new JRBeanCollectionDataSource(usersList);
	
	parameterMap.put("datasource", JRdataSource);
	
	//xlsReport bean has ben declared in the jasper-views.xml file
	modelAndView = new ModelAndView("xlsReport", parameterMap);
	
	return modelAndView;
	 
}//generatePdfReport

	@RequestMapping(method = RequestMethod.GET , value = "csv")
	public ModelAndView generateCsvReport(ModelAndView modelAndView){
	
	logger.debug("--------------generate CSV report----------");
	
	Map<String,Object> parameterMap = new HashMap<String,Object>();
	
	List<User> usersList = userDao.retrieveAllRegisteredUsers();
	JRDataSource JRdataSource = new JRBeanCollectionDataSource(usersList);
	
	parameterMap.put("datasource", JRdataSource);
	
	//xlsReport bean has ben declared in the jasper-views.xml file
	modelAndView = new ModelAndView("csvReport", parameterMap);
	
	return modelAndView;
	 
	    }//generatePdfReport

	@RequestMapping(method = RequestMethod.GET , value = "html")
	public ModelAndView generateHtmlReport(ModelAndView modelAndView){
	
	logger.debug("--------------generate HTML report----------");
	
	Map<String,Object> parameterMap = new HashMap<String,Object>();
	
	List<User> usersList = userDao.retrieveAllRegisteredUsers();
	
	JRDataSource JRdataSource = new JRBeanCollectionDataSource(usersList);
	
	parameterMap.put("datasource", JRdataSource);
	
	//xlsReport bean has ben declared in the jasper-views.xml file
	modelAndView = new ModelAndView("htmlReport", parameterMap);
	
	return modelAndView;
	 
	}//generatePdfReport
*/
}
