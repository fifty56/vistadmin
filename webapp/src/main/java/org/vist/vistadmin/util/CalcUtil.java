package org.vist.vistadmin.util;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vist.vistadmin.domain.CompletedClass;
import org.vist.vistadmin.domain.Course;
import org.vist.vistadmin.domain.CourseIncome;
import org.vist.vistadmin.domain.CourseStudent;
import org.vist.vistadmin.domain.CourseStudentDiscount;
import org.vist.vistadmin.domain.CourseTeacher;
import org.vist.vistadmin.domain.Discount;
import org.vist.vistadmin.domain.Student;
import org.vist.vistadmin.domain.Teacher;
import org.vist.vistadmin.domain.common.Data;
import org.vist.vistadmin.domain.common.DiscountType;
import org.vist.vistadmin.web.CourseController;

public class CalcUtil {

	private static Logger logger = LoggerFactory.getLogger(CalcUtil.class);
	
	public static final int ONE_DAY_IN_MILLIS =  24 * 60 * 60 * 1000;
	
	/******************************
	 * 
	 *   Course Income utilities
	 *   
	 ******************************/
	
	public static double getTotalGrossAmountOfPayedCourseIncomes(List<CourseIncome> courseIncomeList) {
		double amount = 0;
		if(courseIncomeList != null) {
			for (CourseIncome courseIncome : courseIncomeList) {
				if(courseIncome.isPayed()) {
					amount += courseIncome.getAmount();
				}				
			}
		}
		return amount;
	}

	public static double getTotalGrossAmountOfUnpayedCourseIncomes(List<CourseIncome> courseIncomeList) {
		double amount = 0;
		if(courseIncomeList != null) {
			for (CourseIncome courseIncome : courseIncomeList) {
				if(!courseIncome.isPayed()) {
					amount += courseIncome.getAmount();
				}				
			}
		}
		return amount;
	}
	/******************************
	 * 
	 *   Completed Class utilities
	 *   
	 ******************************/
	
	public static double getTotalNettAmountOfPayedCompletedClasses(List<CompletedClass> completedClassList) {
		double amount = 0;
		if(completedClassList != null) {
			for (CompletedClass completedClass : completedClassList) {
				if(completedClass.getPayed()) {
					amount += completedClass.getNumberOfClasses() * completedClass.getCourse().getTeacherClassPrice();
				}				
			}
		}
		return amount;
	}

	public static double getTotalNettAmountOfUnpayedCompletedClasses(List<CompletedClass> completedClassList) {
		double amount = 0;
		if(completedClassList != null) {
			for (CompletedClass completedClass : completedClassList) {
				if(!completedClass.getPayed()) {
					amount += completedClass.getNumberOfClasses() * completedClass.getCourse().getTeacherClassPrice();
				}				
			}
		}
		return amount;
	}

	public static double getTotalGrossAmountOfPayedCompletedClasses(List<CompletedClass> completedClassList) {
		double amount = 0;
		if(completedClassList != null) {
			for (CompletedClass completedClass : completedClassList) {
				if(completedClass.getPayed()) {
					amount += getCompletedClassGrossAmount(completedClass);									
				}				
			}
		}
		return amount;
	}

	public static double getTotalGrossAmountOfUnpayedCompletedClasses(List<CompletedClass> completedClassList) {
		double amount = 0;
		if(completedClassList != null) {
			for (CompletedClass completedClass : completedClassList) {
				if(!completedClass.getPayed()) {
					amount += getCompletedClassGrossAmount(completedClass);
				}				
			}
		}
		return amount;
	}
	
	private static double getCompletedClassGrossAmount(CompletedClass cc) {
		double amount = cc.getNumberOfClasses() * cc.getCourse().getTeacherClassPrice();
		//TODO - check CC VAT flag
		boolean courseVAT = cc.getCourse().getVat();
		boolean teacherVAT = false;
		if(cc.getTeacher() != null && cc.getTeacher().getTeacherBillingData() != null) {
			teacherVAT = cc.getTeacher().getTeacherBillingData().getVAT();
		}
		if(courseVAT && teacherVAT) {
			amount *= Data.VAT_PERCENT;
		}
		return amount;
	}
	
	public static int getSumOfCompletedClasses(List<CompletedClass> completedClassList) {
		int amount = 0;
		if(completedClassList != null) {
			for (CompletedClass completedClass : completedClassList) {
				amount += completedClass.getNumberOfClasses();				
			}
		}
		return amount;
	}
	
	//apply completed class VAT when exists
	public static void setCompletedClassExpense(List<CompletedClass> completedClassList, boolean checkVATFlag) {
    	for(CompletedClass completedClass : completedClassList) {
    		Teacher teacher = completedClass.getTeacher();
    		Course course = completedClass.getCourse();
    		double VATMultipler = 1.0;    		
    		if(checkVATFlag && teacher.getTeacherBillingData() != null && teacher.getTeacherBillingData().getVAT() && course.getVat()) {
        		VATMultipler = Data.VAT_PERCENT;
        	}
    		int expense = (int)(course.getTeacherClassPrice() * completedClass.getNumberOfClasses() * VATMultipler);
    		completedClass.setExpense(expense);
    	}
    }
	
	/******************************
	 * 
	 *   Course utilities
	 *   
	 ******************************/	 
	
	public static int getNumberOfClassesPerWeek(Course course) {
		int retVal = -1;	
		if(course.getTimeOfClasses() != null && !course.getTimeOfClasses().equals("")) {
    		if(!course.getTimeOfClasses().equals(CourseController.FLEXIBLE_TIME)) {    	
    			retVal = 0;
	    		String[] tokens = course.getTimeOfClasses().split(";");
		    	for (String str : tokens) {		    		
		    		retVal += Integer.parseInt(str.substring(14,15));	    				    		
		    	}
    		}
		}
		return retVal;
	}
	
	
	public static int getEstimatedNumberOfAllClassesByCourseDates(Course course) {		
		return getEstimatedNumberOfAllClassesByDates(course, course.getStartDate(), course.getEndDate());			
	}
	
	public static int getEstimatedNumberOfAllClassesByDates(Course course, Date fromDate, Date endDate) {
		int retVal = -1;
		if( ( fromDate.equals(course.getStartDate()) || fromDate.after(course.getStartDate()) ) && ( endDate.equals(course.getEndDate()) || endDate.before(course.getEndDate()) ) ) {
			long courseLengthLong = endDate.getTime() - fromDate.getTime() + ONE_DAY_IN_MILLIS;		
			int courseLengthDays = (int) Math.round(((double)courseLengthLong) / 1000 / 60 / 60 / 24);
			int numberOfClassPerWeek = getNumberOfClassesPerWeek(course);
			double estimatedNumOfHoursDouble = ((double)(numberOfClassPerWeek * courseLengthDays)) / 7;
			int estimatedNumOfHours = (new Double(estimatedNumOfHoursDouble)).intValue();
			return estimatedNumOfHours;	
		}
		return retVal;
	}
	
	public static double getOneClassCourseProfit(List<CourseStudent> courseStudentList, List<CourseStudentDiscount> courseStudentDiscountList, Course course) {
		double studentPrice = getAllStudentOneClassPrice(courseStudentList, course, courseStudentDiscountList);	 
		return studentPrice - course.getTeacherClassPrice();
	}
	
	public static double getPayPerClassCourseVATForNumberOfClasses(List<CourseStudent> courseStudentList, List<CourseStudentDiscount> courseStudentDiscountList, Course course, double numberOfClasses) {
		double allStudentOneClassPrice = getAllStudentOneClassPrice(courseStudentList, course, courseStudentDiscountList);
		double estimatedTotalNetAmount = allStudentOneClassPrice * numberOfClasses * (Data.VAT_PERCENT - 1);
		return estimatedTotalNetAmount;
	}
	
	/******************************
	 * 
	 *   Student utilities
	 *   
	 ******************************/
	
	public static double getStudentCoursePrice(Student student, Course course, Set<Discount> studentDiscountSet) {         			
		logger.debug("check student: " + student.getLabelString());    		
		double defIncome = course.getMoneyPerStudent();
		double studentAllIncome = defIncome;
		logger.debug("student defIncomet: " + defIncome);		
		if(studentDiscountSet != null) {    			
			for(Discount discount: studentDiscountSet) {
				logger.debug("student has discount: " + discount.getLabelString());
				if(discount.getType().equals(DiscountType.FIX_PRICE) && !course.getPayPerClasses()) {
					studentAllIncome -= discount.getAmount();
				} else if(discount.getType().equals(DiscountType.PERCENTAGE)) {
					studentAllIncome -= defIncome * discount.getAmount() / 100;
				}
				logger.debug("affter applying discopunt, studentAllIncome: " + studentAllIncome);
			}
		}
		logger.debug("student allIncome to return: " + studentAllIncome);
		return studentAllIncome;
	}
	
	public static Map<Student, Set<Discount>> getCourseDiscountSetMapByStudent(List<CourseStudentDiscount> courseStudentDiscountList) { 
    	Map<Student, Set<Discount>> studentDiscountMap = new HashMap<Student, Set<Discount>>();
		
    	for(CourseStudentDiscount courseStudentDiscount : courseStudentDiscountList) {
    		Set<Discount> courseDiscountSet;
    		Student student = courseStudentDiscount.getStudent();
    		if(studentDiscountMap.containsKey(student)) {
    			courseDiscountSet = studentDiscountMap.get(student);
    		} else {
    			courseDiscountSet = new HashSet<Discount>();
    		}
    		courseDiscountSet.add(courseStudentDiscount.getDiscount());
    		studentDiscountMap.put(student, courseDiscountSet);
    	}
    	return studentDiscountMap;
    }
	
	/******************************
	 * 
	 *   Course + Completed Class utilities
	 *   
	 ******************************/
	
	public static double getCourseRemainingNettExpenseFromcompletedClasses(Course course, List<CompletedClass> completedCalssList) {
		int numOfComplClasses = 0;
		if(completedCalssList != null) {			
			for (CompletedClass completedClass : completedCalssList) {
				if(completedClass.getCourse().getCourseId(). equals(course.getCourseId())) {
					numOfComplClasses += completedClass.getNumberOfClasses();
				}
			}
		}
		int numOfRemainingClasses = course.getSumOfClasses() - numOfComplClasses;
		return numOfRemainingClasses * course.getTeacherClassPrice();
	}
	
	

	/******************************
	 * 
	 *   Course + Student + Discounts
	 *   
	 ******************************/

	public static double getAllStudentOneClassPrice(List<CourseStudent> courseStudentList, Course course, List<CourseStudentDiscount> courseStudentDiscountList) {
		double finalAmount = 0.0;
		for (CourseStudent courseStudent : courseStudentList) {
			Student student = courseStudent.getStudent();
			 Map<Student, Set<Discount>> discountMap = getCourseDiscountSetMapByStudent(courseStudentDiscountList);			
			double amount = getStudentCoursePrice(student, course, discountMap.get(student));
			finalAmount += amount;
		}		
		return finalAmount;
	}

	
	//????????????
	//TODO check these
	
	
	public static double getOneClassGrossTeacherPrice(Course course, Teacher teacher) {
		double VATMultipler = 1.0;
		if(teacher.getTeacherBillingData() != null && teacher.getTeacherBillingData().getVAT() && course.getVat()) {
    		VATMultipler = Data.VAT_PERCENT;
    	}
		double teacherPrice = course.getTeacherClassPrice() * VATMultipler;
		return teacherPrice;
	}
	
	
	public static double getStudentCoursePrice(Student student, Course course, List<CourseStudentDiscount> courseStudentDiscountList) {		
		Map<Course, Set<Discount>> courseDiscountMap = CalcUtil.getCourseDiscountSetMapByCourse(courseStudentDiscountList); 
		Set<Discount> discounts = courseDiscountMap.get(course);		
		return getStudentCoursePrice(student, course, discounts);
	} 
	

	
	public static Map<Course, Set<Discount>> getCourseDiscountSetMapByCourse(List<CourseStudentDiscount> courseStudentDiscountList) { 
    	Map<Course, Set<Discount>> courseDiscountMap = new HashMap<Course, Set<Discount>>();
		
    	for(CourseStudentDiscount courseStudentDiscount : courseStudentDiscountList) {
    		Set<Discount> courseDiscountSet;
    		Course course = courseStudentDiscount.getCourse();
    		if(courseDiscountMap.containsKey(course)) {
    			courseDiscountSet = courseDiscountMap.get(course);
    		} else {
    			courseDiscountSet = new HashSet<Discount>();
    		}
    		courseDiscountSet.add(courseStudentDiscount.getDiscount());
    		courseDiscountMap.put(course, courseDiscountSet);
    	}
    	return courseDiscountMap;
    }

	
	
	

	
	public static void setCourseExpense(List<CourseTeacher> courseTeacherList) {
		for (CourseTeacher courseTeacher : courseTeacherList) {
			Course course = courseTeacher.getCourse();
			Teacher teacher = courseTeacher.getTeacher();
			
			double VATMultipler = 1.0;
    		if(teacher.getTeacherBillingData() != null && teacher.getTeacherBillingData().getVAT() && course.getVat()) {
        		VATMultipler = Data.VAT_PERCENT;
        	}
    		
    		//TODO
    		//int courseExpense = (int)(courseTeacher.getNumerOfClasses() * course.getTeacherClassPrice() * VATMultipler);
    		//course.setAllExpenseWithTeacherVAT(courseExpense);
		}
		
	}

	
}
