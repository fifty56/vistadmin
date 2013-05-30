insert into address (id, city, street, version, zip_address) values (1, 'Szigetszentmiklos', 'Biro Lajos utca 111', 0, 2310);
insert into student (id, address, billing_address, parent_born_date, parent_email_address, parent_first_name, mother_name, parent_mother_name,  born_place, parent_born_place,
	parent_last_name, parent_phone_number, born_date, email_address, first_name, last_name, phone_number, company, company_student_names, address_str, parent_address_str,
	version, status, company_reg_number, company_tax_number
	) values (1, 1, null, null, null, null, null, null, null, null,null, null, '1978-05-16 00:00:00',
	'fifty56@freemail.hu', 'Gabor', 'Bartha', '30-2277726', 0, '','','', 0, 0, '', '');
insert into student_languages (student, languages) values (1, 0);
insert into student_languages (student, languages) values (1, 1);


insert into address (id, city, street, version, zip_address) values (2, 'Ozd', 'Nemzetor ut 13', 0, 3600);
insert into student (id, address, billing_address, parent_born_date, parent_email_address, parent_first_name, mother_name, parent_mother_name,  born_place, parent_born_place,
	parent_last_name, parent_phone_number, born_date, email_address, first_name, last_name, phone_number, company, company_student_names, address_str, parent_address_str,
	version, status, company_reg_number, company_tax_number
	) values (2, 2, null, null, null, null,  null, null,null,null, null, null, '1978-01-11 00:00:00',
	'veresst@freemail.hu', 'Tamas', 'Veress', '20-1227721', 0, '','','',  0, 0, '', '');
insert into student_languages (student, languages) values (2, 0);


insert into address (id, city, street, version, zip_address) values (3, 'Budapest', 'Petofi ut 1', 0, 1119);
insert into student (id, address, billing_address, parent_born_date, parent_email_address, parent_first_name, mother_name, parent_mother_name,  born_place, parent_born_place,
	parent_last_name, parent_phone_number, born_date, email_address, first_name, last_name, phone_number, company, company_student_names, address_str, parent_address_str,
	version, status, company_reg_number, company_tax_number
	) values (3, 3, null, null, null, null, null, null, null, null, null,null, '1966-03-01 00:00:00',
	'katalin@gmail.com', 'Kata', 'Kiss', '70-9873456', 0, '','','',  0, 0, '', '');
insert into student_languages (student, languages) values (3, 0);


insert into address (id, city, street, version, zip_address) values (4, 'Szszm', 'Kossuth utca 23', 0, 2310);
insert into address (id, city, street, version, zip_address) values (5, 'Szszm', 'Kossuth utca 23', 0, 2310);
insert into billing_address (id, address, name, postal_address, version) values 
(1, 5, 'kicsike BT', null, 0);
insert into student (id, address, billing_address, parent_born_date, parent_email_address,
parent_first_name, mother_name, parent_mother_name,   born_place, parent_born_place,
parent_last_name, parent_phone_number, born_date, email_address, first_name, last_name, phone_number, company, company_student_names,address_str, parent_address_str,
 version, status, company_reg_number, company_tax_number
 ) values (4, 4, 1, '1975-05-21 00:00:00', 'kanya@gmail.com',
'Anyuka', null, null, null,null, 'Kicsike', '30-9988776', '2001-08-18 00:00:00', 'kn@gmail.com', 'Nikike', 'Kicsike', null, 0, '','','',  0, 0, '', '');
insert into student_languages (student, languages) values (3, 1);


insert into address (id, city, street, version, zip_address) values (6,'Tokol', 'Fo ut 1',0, 2316);
insert into address (id, city, street, version, zip_address) values (7,'Tokol', 'Fo ut 1', 0,2316);
insert into address (id, city, street, version, zip_address) values (8,'Dunaharaszti', 'Iparos ut 5', 0,2330);
insert into billing_address (id, address, name, postal_address, version) 
values (2, 8, 'Cegnev', 7, 0);
insert into student (id, address, billing_address, parent_born_date, parent_email_address, 
parent_first_name, mother_name, parent_mother_name,  born_place, parent_born_place, parent_last_name, parent_phone_number, 
born_date, email_address, 
first_name, last_name,  company, company_student_names,phone_number, address_str, parent_address_str,version, status, company_reg_number, company_tax_number
) 
values (5, 6, 2, null,null, null,  null, null,null, null, null, null,
'2001-08-18 00:00:00', 'lk@cegnev.hu', 'Lajos', 'Kovacs',  0, '','30-5676545','','',  0, 1, '', '')
insert into student_languages (student, languages) values (3, 2);


insert into address (id, city, street, version, zip_address) values (9, 'Szigetszentmiklos', 'Biro Lajos utca 113', 0, 2310);
insert into student (id, address, billing_address, parent_born_date, parent_email_address, parent_first_name, mother_name, 
parent_mother_name,  born_place, parent_born_place,	parent_last_name, parent_phone_number, 
born_date, email_address, first_name, last_name, phone_number,  company, company_student_names,address_str, parent_address_str,
	version, status, company_reg_number, company_tax_number) values (6, 9, null, null, null, null,  null, 
	null,null, null,null, null, 
	'1978-05-16 00:00:00',	'zsakuka@a.hu', 'zsaku', 'Majom', '30-2277726',  0,'','',  '',0, 0, '', '');
insert into student_languages (student, languages) values (6, 1);



insert into address (id, city, street, version, zip_address) values (10, 'Ozd', 'Nemzetor ut 13', 0, 3600);
insert into address (id, city, street, version, zip_address) values (30,'Tokol', 'Fo ut 1', 0,2316);
insert into address (id, city, street, version, zip_address) values (31,'Dunaharaszti', 'Iparos ut 5', 0,2330);
insert into billing_address (id, address, name, postal_address, version) 
values (3, 31, 'Cegnek a neve', 30, 0);
insert into student (id, address, billing_address, parent_born_date, parent_email_address, parent_first_name, mother_name, parent_mother_name,  born_place, parent_born_place,
	parent_last_name, parent_phone_number, born_date, email_address, first_name, last_name, phone_number,  company, company_student_names,address_str, parent_address_str,
	version, status, company_reg_number, company_tax_number) values (7, 10, 3, null, null, null, null, null, null, null, null, null, '1978-05-16 00:00:00',
	'zsakuka@a1.hu', 'ceges', 'neve', '30-2277726', 1, 'Szabo Lajos;Kiss Peter;Batori Zsolt','','',  0, 0,'','');
insert into student_languages (student, languages) values (7, 1);

insert into address (id, city, street, version, zip_address) values (11, 'Szigetszentmiklos', 'Biro Lajos utca 113', 0, 2310);
insert into student (id, address, billing_address, parent_born_date, parent_email_address, parent_first_name, mother_name, parent_mother_name,  born_place, parent_born_place,
	parent_last_name, parent_phone_number, born_date, email_address, first_name, last_name, phone_number,  company, company_student_names,address_str, parent_address_str,
	version, status, company_reg_number, company_tax_number
	) values (8, 11, null, null, null, null, null, null, null, null, null, null, '1978-05-16 00:00:00',
	'zsakukab@a.hu', 'instant', 'user', '30-2277726', 0, '','','',  0, 0, '', '');
insert into student_languages (student, languages) values (8, 1);
insert into student_languages (student, languages) values (8, 0);

insert into address (id, city, street, version, zip_address) values (12, 'Szigetszentmiklos', 'Biro Lajos utca 113', 0, 2310);
insert into student (id, address, billing_address, parent_born_date, parent_email_address, parent_first_name, mother_name, parent_mother_name,  born_place, parent_born_place,
	parent_last_name, parent_phone_number, born_date, email_address, first_name, last_name, phone_number,  company, company_student_names,address_str, parent_address_str,
	version, status, company_reg_number, company_tax_number
	) values (9, 12, null, null, null, null, null, null, null,null, null, null, '1978-05-16 00:00:00',
	'zsakukab@a.h1u', 'ceges 11', 'user', '30-2277726', 1, 'Szabo Lajos;Kiss Peter;Batori Zsolt','','',  0, 0, '', '');
insert into student_languages (student, languages) values (8, 1);
insert into student_languages (student, languages) values (8, 0);







insert into address (id, city, street, version, zip_address) values (101, 'Szigetszentmiklos', 'Biro Lajos 111', 0, 2310)
insert into address (id, city, street, version, zip_address) values (102, 'Szigetszentmiklos', 'Biro Lajos 111', 0, 2310)
insert into teacher_billing_data (id, vat, account_number, address, company_number, name,
tax_number, version) values (1, 0, '11773030-13136701', 102, '1212', 'Bartha-Becskei Zsuzsanna', '197856543', 0);
insert into teacher (id, address, born_date, email_address, first_name, last_name, phone_number, mother_name,   born_place, address_str, 
teacher_billing_data, version, status) values (1, 101, '1980-08-21 00:00:00', 'becskei.zsuzsa@vilagnyelvek.hu', 
'Zsuzsanna', 'Bartha-Becskei','30-3491607',null,'','', 1, 0, 0);
insert into teacher_languages (teacher, languages) values (1, 0);


insert into address (id, city, street, version, zip_address) values (103, 'Szigetszentmiklos', 'Ady Endre 23', 0, 2310)
insert into address (id, city, street, version, zip_address) values (104, 'Szigetszentmiklos', 'Fo ut 1', 0, 2310)
insert into teacher_billing_data (id, vat, account_number, address, company_number, name,
tax_number, version) values (2, 1, '11773031-21214456', 104, '5687', 'Tanar Bt', '197856543', 0);
insert into teacher (id, address, born_date, email_address, first_name, last_name, phone_number,  mother_name,   born_place,  address_str,
teacher_billing_data, version, status) values (2, 103, '1981-07-15 00:00:00', 'Kata@tanarbt.hu', 
'Katalin', 'Tanarno','20-3123123',null,'','', 2, 0, 0);
insert into teacher_languages (teacher, languages) values (2, 1);
insert into teacher_languages (teacher, languages) values (2, 2);

insert into address (id, city, street, version, zip_address) values (105, 'Szigetszentmiklos', 'Kassai ut 11', 0, 2310)
insert into address (id, city, street, version, zip_address) values (106, 'Szigetszentmiklos', 'Kassai ut 11', 0, 2310)
insert into teacher_billing_data (id, vat, account_number, address, company_number,name,
tax_number, version) values (3, 1, '11773030-13136701', 106, '1212', 'tanarneni 1', '197856543', 0);
insert into teacher (id, address, born_date, email_address, first_name, last_name, phone_number,  mother_name,   born_place,  address_str,
teacher_billing_data, version, status) values (3, 105, '1981-05-27 00:00:00', 'angolt@vilagnyelvek.hu', 
'tanarneni 1', 'angol','30-3644537',null,'','', 3, 0, 0);
insert into teacher_languages (teacher, languages) values (3, 0);

insert into address (id, city, street, version, zip_address) values (107, 'Tokol', 'Tokoli ut 55', 0, 2315)
insert into address (id, city, street, version, zip_address) values (108, 'Tokol', 'Tokoli ut 55', 0, 2315)
insert into teacher_billing_data (id, vat, account_number, address, company_number, 
name, tax_number, version) values (4, 0, '11773030-13136567', 108, '1212', 'tanarneni 1 nemet', '197856543', 0);
insert into teacher (id, address, born_date, email_address, first_name, last_name, phone_number,  mother_name,   born_place,  address_str,
teacher_billing_data, version, status) values (4, 107, '1979-01-12 00:00:00', 'nemett@vilagnyelvek.hu', 
'tanarneni 1', 'nemet','20-3644537',null,'','', 4, 0, 1);
insert into teacher_languages (teacher, languages) values (4, 1);

insert into address (id, city, street, version, zip_address) values (109, 'Tokol', 'Tokoli ut 55', 0, 2315)
insert into address (id, city, street, version, zip_address) values (110, 'Tokol', 'Tokoli ut 55', 0, 2315)
insert into teacher_billing_data (id, vat, account_number, address, company_number, 
name, tax_number, version) values (5, 1, '11773030-13136567', 109, '1212', 'tanarneni 1 nemet', '197856543', 0);
insert into teacher (id, address, born_date, email_address, first_name, last_name, phone_number,  mother_name,   born_place,  address_str,
teacher_billing_data, version, status) values (5, 110, '1979-01-12 00:00:00', 'kuka@vilagnyelvek.hu', 
'jolesz angol', 'tanci','20-3644537',null, '','', 5, 0, 0);
insert into teacher_languages (teacher, languages) values (5, 1);
insert into teacher_languages (teacher, languages) values (5, 0);










insert into course (id, book, comment, course_format, course_id, course_level, company, class_specialization_type, 
end_date, lang, money_per_student, pay_per_classes, start_date, status, sum_of_classes, vat, room, time_of_classes, 
teacher_class_price, version, course_type, creation_date) values (1, 'english start', 'no comment', 0, 1, 0, 0,0,
'2012-11-26 00:00:00', 0, 35000, 0,'2012-05-25 00:00:00', 0, 40, 1, 1, '0@10:00-11:00|1', 1200, 0, 0, '2011-05-25 00:00:00');

insert into course (id, book, comment, course_format, course_id, course_level, company, class_specialization_type,
end_date, lang, money_per_student, pay_per_classes, start_date, status, sum_of_classes, vat, room, time_of_classes, 
teacher_class_price, version, course_type, creation_date) values (2, 'deutsch start 1', 'no comment', 0, 2, 0, 0,0, 
'2012-11-23 00:00:00', 1, 45000, 0,'2012-07-05 00:00:00', 0, 40, 0, 1, '0@10:00-10:45|1;3@16:00-17:30|2', 1100, 0, 1, '2011-05-25 00:00:00');

insert into course (id, book, comment, course_format, course_id, course_level,  company,class_specialization_type,
end_date, lang, money_per_student, pay_per_classes, start_date, status, sum_of_classes, vat, room, time_of_classes, 
teacher_class_price, version, course_type, creation_date) values (3, 'english intermediate', 'angol kozepes', 1, 3, 2, 0,0, 
'2012-11-26 00:00:00', 0, 65000, 0,'2012-08-01 00:00:00', 1, 50, 1, 1, 'FLEX', 1300, 0, 2, '2011-05-25 00:00:00');

insert into course (id, book, comment, course_format, course_id, course_level,  company,class_specialization_type,
end_date, lang, money_per_student, pay_per_classes, start_date, status, sum_of_classes, vat, room, time_of_classes, 
teacher_class_price, version, course_type, creation_date) values (4, 'german intermediate', 'nemet kozepes', 1, 4, 2, 0, 0,
'2012-11-28 00:00:00', 1, 75000, 0,'2012-06-01 00:00:00', 1, 60, 1, 1, '0@10:00-10:45|1;3@16:00-17:30|2;5@15:00-15:30|1', 1100, 0, 2, '2011-05-25 00:00:00');

insert into course (id, book, comment, course_format, course_id, course_level,  company,class_specialization_type,
end_date, lang, money_per_student, pay_per_classes, start_date, status, sum_of_classes, vat, room, time_of_classes, 
teacher_class_price, version, course_type, creation_date) values (5, 'english intermediate', 'angol kozepes', 1, 5, 2, 0, 0,
'2012-11-28 00:00:00', 0, 55000, 0,'2012-06-01 00:00:00', 3, 60, 1, 1, '3@16:00-20:00|4', 1300, 0, 2, '2011-05-25 00:00:00');

insert into course (id, book, comment, course_format, course_id, course_level,  company,class_specialization_type,
end_date, lang, money_per_student, pay_per_classes, start_date, status, sum_of_classes, vat, room, time_of_classes, 
teacher_class_price, version, course_type, creation_date) values (6, 'corp english intermediate', 'ceges angol kozepes', 1, 'ceges1', 2, 1,0, 
'2012-10-28 00:00:00', 0, 4000, 1,'2012-06-01 00:00:00', 0, 0, 1, 1, '3@16:00-20:00|4',  1300, 0, 2, '2011-05-25 00:00:00');

insert into course (id, book, comment, course_format, course_id, course_level,  company,class_specialization_type,
end_date, lang, money_per_student, pay_per_classes, start_date, status, sum_of_classes, vat, room, time_of_classes, 
teacher_class_price, version, course_type, creation_date) values (7, 'instan english', '', 2, 'instant 1', 2,  0,0,
'2012-11-28 00:00:00', 0, 3200, 1,'2012-06-01 00:00:00', 1, 0, 0, 1, 'FLEX',  1300, 0, 0, '2011-05-25 00:00:00');



insert into course_teacher (id, course, numer_of_classes, end_date, start_date, teacher, version) values 
(1, 1, 30, '2012-08-01 00:00:00', '2012-05-25 00:00:00', 1, 0);
insert into course_teacher (id, course,  numer_of_classes, end_date, start_date, teacher, version) values 
(2, 1, 10, '2012-11-11 00:00:00', '2012-08-02 00:00:00', 3, 0);
insert into course_teacher (id, course,  numer_of_classes, end_date, start_date, teacher, version) values 
(3, 2, 40, '2012-08-15 00:00:00', '2012-07-05 00:00:00', 2, 0);
insert into course_teacher (id, course,  numer_of_classes, end_date, start_date, teacher, version) values 
(4, 2, 20, '2012-11-23 00:00:00', '2012-08-16 00:00:00', 4, 0);
insert into course_teacher (id, course, numer_of_classes, end_date, start_date, teacher, version) values 
(5, 3, 30, '2012-11-01 00:00:00', '2012-05-25 00:00:00', 1, 0);
insert into course_teacher (id, course, numer_of_classes, end_date, start_date, teacher, version) values 
(6, 4, 60, '2012-11-01 00:00:00', '2012-05-25 00:00:00', 2, 0);
insert into course_teacher (id, course, numer_of_classes, end_date, start_date, teacher, version) values 
(7, 6, 0, '2012-11-01 00:00:00', '2012-05-25 00:00:00', 2, 0);
insert into course_teacher (id, course, numer_of_classes, end_date, start_date, teacher, version) values 
(8, 7, 0, '2012-11-28 00:00:00', '2012-05-25 00:00:00', 1, 0);
insert into course_teacher (id, course, numer_of_classes, end_date, start_date, teacher, version) values 
(9, 7, 0, '2012-11-28 00:00:00', '2012-05-25 00:00:00', 3, 0);

insert into course_student (id, course, end_date, start_date, student, version) 
values (1, 1, '2012-09-15 00:00:00', '2012-05-25 00:00:00', 1, 0);
insert into course_student (id, course, end_date, start_date, student, version) 
values (2, 1, '2012-09-15 00:00:00', '2012-05-25 00:00:00', 2, 0);
insert into course_student (id, course, end_date, start_date, student, version) 
values (3, 1, '2012-09-15 00:00:00', '2012-05-25 00:00:00', 3, 0);
insert into course_student (id, course, end_date, start_date, student, version) 
values (4, 2, '2012-09-23 00:00:00', '2012-07-05 00:00:00', 4, 0);
insert into course_student (id, course, end_date, start_date, student, version) 
values (5, 4, '2012-09-23 00:00:00', '2012-07-05 00:00:00', 6, 0);
insert into course_student (id, course, end_date, start_date, student, version) 
values (6, 4, '2012-09-23 00:00:00', '2012-07-05 00:00:00', 3, 0);
insert into course_student (id, course, end_date, start_date, student, version, company_student_names) 
values (7, 6, '2012-09-23 00:00:00', '2012-07-05 00:00:00', 7, 0, 'Szabo Lajos;Kiss Peter');
insert into course_student (id, course, end_date, start_date, student, version) 
values (8, 3, '2012-08-03 00:00:00', '2012-07-05 00:00:00', 4, 0);
insert into course_student (id, course, end_date, start_date, student, version) 
values (9, 7, '2012-08-03 00:00:00', '2012-07-05 00:00:00', 8, 0);


insert into completed_class (id, comp_week, comp_month, comp_year, course, curr_date, number_of_classes, bill_order_number, deadline_date, payment_date, contains_test_class,
payed, teacher, version) values (1, 1, 5, 2012, 1,  '2012-06-01 00:00:00', 8, '123654', null, '2012-07-15 00:00:00', 0, 1, 1, 0);
insert into completed_class (id, comp_week, comp_month, comp_year, course, curr_date, number_of_classes, bill_order_number, deadline_date,  payment_date,contains_test_class,
payed, teacher, version) values (2, 2, 6, 2012, 1,  '2012-06-10 00:00:00', 4, '123654', null,  '2012-07-15 00:00:00', 0, 0, 3, 0);
insert into completed_class (id, comp_week, comp_month, comp_year, course, curr_date, number_of_classes,  bill_order_number, deadline_date, payment_date,contains_test_class,
payed, teacher, version) values (3, 3, 5, 2012, 4,  '2012-05-10 00:00:00', 8, '123654', '2012-07-10 00:00:00',  '2012-07-10 00:00:00', 0, 1, 2, 0);
insert into completed_class (id, comp_week, comp_month, comp_year, course, curr_date, number_of_classes,  bill_order_number, deadline_date, payment_date,contains_test_class,
payed, teacher, version) values (4, 4, 6, 2012, 4,  '2012-06-10 00:00:00', 10, '123654', '2012-07-22 00:00:00', null, 0, 0, 2, 0);
insert into completed_class (id, comp_week, comp_month, comp_year, course, curr_date, number_of_classes,  bill_order_number, deadline_date, payment_date,contains_test_class,
payed, teacher, version) values (5, 1, 5, 2012, 2,  '2012-05-10 00:00:00', 10, '123654', '2012-07-10 00:00:00',  '2012-07-12 00:00:00', 0, 0, 2, 0);
insert into completed_class (id, comp_week, comp_month, comp_year, course, curr_date, number_of_classes,  bill_order_number, deadline_date, payment_date,contains_test_class,
payed, teacher, version) values (6, 1, 5, 2012, 6,  '2012-05-10 00:00:00', 30, '123654', '2012-07-20 00:00:00',  '2012-07-13 00:00:00', 0, 1, 2, 0);
insert into completed_class (id, comp_week, comp_month, comp_year, course, curr_date, number_of_classes,  bill_order_number, deadline_date, payment_date,contains_test_class,
payed, teacher, version) values (7, 1, 7, 2012, 7,  '2012-05-10 00:00:00', 3, '123654', '2012-07-20 00:00:00',  '2012-07-13 00:00:00', 0, 1, 1, 0);
insert into completed_class (id, comp_week, comp_month, comp_year, course, curr_date, number_of_classes,  bill_order_number, deadline_date, payment_date,contains_test_class,
payed, teacher, version) values (8, 1, 7, 2012, 7,  '2012-05-10 00:00:00', 3, '123654', '2012-07-20 00:00:00',  '2012-07-13 00:00:00', 0, 1, 3, 0);

insert into course_income (id, amount, course, curr_date, student, version, year, month, week, payed, fix_discount, deadline_date, payment_date) 
values (1, 30000, 1, '2012-06-01 00:00:00', 1, 0, -1, -1, -1, 0, 0, '2012-07-01 00:00:00', '2012-07-01 00:00:00');
insert into course_income (id, amount, course, curr_date, student, version, year, month, week, payed, fix_discount, deadline_date, payment_date) 
values (2, 10000, 4, '2012-06-01 00:00:00', 6, 0, -1, -1, -1, 1, 0, '2012-07-02 00:00:00', '2012-07-01 00:00:00');
insert into course_income (id, amount, course, curr_date, student, version, year, month, week, payed, fix_discount, deadline_date, payment_date) 
values (3, 15000, 4, '2012-06-01 00:00:00', 3, 0,  -1, -1, -1,  1, 0, null, '2012-07-03 00:00:00');
insert into course_income (id, amount, course, curr_date, student, version, year, month, week,payed, fix_discount, deadline_date, payment_date) 
values (4, 100500, 6, '2012-06-01 00:00:00', 7, 0,  2012, 5, 1, 0, 0, null, null);
insert into course_income (id, amount, course, curr_date, student, version, year, month, week,payed, fix_discount, deadline_date, payment_date) 
values (5, 100500, 6, '2012-06-01 00:00:00', 7, 0,  2012, 5, 2, 0, 0, '2012-07-21 00:00:00', null);
insert into course_income (id, amount, course, curr_date, student, version, year, month, week,payed, fix_discount, deadline_date, payment_date) 
values (6, 100500, 6, '2012-06-01 00:00:00', 7, 0,  2012, 5, 3, 1, 0, '2012-07-21 00:00:00', null);
insert into course_income (id, amount, course, curr_date, student, version, year, month, week,payed, fix_discount, deadline_date, payment_date) 
values (7, 100500, 6, '2012-06-01 00:00:00', 7, 0,  2012, 5, 4, 0, 0, '2012-09-21 00:00:00', null);
insert into course_income (id, amount, course, curr_date, student, version, year, month, week,payed, fix_discount, deadline_date, payment_date) 
values (8, 10500, 7, '2012-06-01 00:00:00', 8, 0,  2012, 5, 4, 1, 0, '2012-09-21 00:00:00', null);

insert into discount (id, amount, comment, name, type, version) values (1, 8.0, 
'torzstanuloknak 8% kedvezmeny', 'Torzstanulo', 0, 0);

insert into discount (id, amount, comment, name, type, version) values (2, 5.0, 
'masodik beiratkozo 5% kedvezmeny', '2. beiratkozo', 0, 0);

insert into discount (id, amount, comment, name, type, version) values (3, 6000, 
'rokon - 6000 Ft', 'rokonnak 6000-el olcsobb minden', 1, 0);

insert into discount (id, amount, comment, name, type, version) values (4, 3000, 
'testvereknek', 'tesoknak 3000-el olcsobb minden', 1, 0);





insert into course_student_discount (id, comment, course, discount, student, version) values (1, 
'', 1, 1, 1, 0);
insert into course_student_discount (id, comment, course, discount, student, version) values (2, 
'', 1, 4, 1, 0);

insert into course_student_discount (id, comment, course, discount, student, version) values (3, 
'', 1, 3, 2, 0);

