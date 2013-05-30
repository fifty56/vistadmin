--installed: 2012-07-26
alter table course add column comapny bit(1);
update course set comapny=0;

alter table course_student add column company_student_names varchar(2000);
update course_student set company_student_names='';

alter table completed_class add column contains_test_class bit(1);
update completed_class set contains_test_class=0;