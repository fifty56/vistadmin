alter table course delete colum weeklyNumberOfClasses;
update course set time_of_classes='';

alter table Student add column comapny bit(1);
update course set comapny=0;

alter table Student add column comapny_student_names varchar(2000);
update course set comapny_student_names='';

alter table completed_calss add column bill_order_number varchar(255);

alter table completed_calss add column deadline_date date;
alter table course_income add column deadline_date date;

alter table completed_calss add column payment_date date;
alter table course_income add column payment_date date;