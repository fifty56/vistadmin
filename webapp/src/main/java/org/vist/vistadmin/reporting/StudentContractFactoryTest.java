package org.vist.vistadmin.reporting;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.vist.vistadmin.domain.Course;
import org.vist.vistadmin.domain.PersonalData;
import org.vist.vistadmin.domain.Student;

public class StudentContractFactoryTest {

	public static Collection getStudent() {
			ArrayList al = new ArrayList();
			Student st = new Student();
			PersonalData pd = new PersonalData();
			pd.setFirstName("firstName");
			pd.setLastName("lastName");
			st.setPersonalData(pd);
			al.add(st);
			return al;
	}
	

	
}
