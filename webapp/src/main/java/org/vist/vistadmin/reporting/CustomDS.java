package org.vist.vistadmin.reporting;

import java.util.List;

import org.vist.vistadmin.domain.Course;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;


public class CustomDS implements JRDataSource {

		private List<Course> courseList;
		
		private int index = -1;
		

		/**
		 *
		 */
		public CustomDS()
		{
		}

		public void setCourseList(List<Course> courses) {
			this.courseList = courses;
		}

		/**
		 *
		 */
		public boolean next() throws JRException
		{
			index++;

			return (index < courseList.size());
		}


		/**
		 *
		 */
		public Object getFieldValue(JRField field) throws JRException
		{
			Object value = null;
			
			String fieldName = field.getName();
			
			if ("courseId".equals(fieldName))
			{
				
				value = courseList.get(index).getCourseId();
			}
			
			return value;
		}


	}

