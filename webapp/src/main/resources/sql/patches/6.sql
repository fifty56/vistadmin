--installed: 2012.08.31
alter table student add column company_reg_number varchar(30);
update student set company_reg_number='';

alter table student add column company_tax_number varchar(30);
update student set company_tax_number='';

alter table course add column class_specialization_type bit(1);
update course set class_specialization_type=0;

