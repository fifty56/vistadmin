--installed: 2012-08-08
alter table student add column mother_name varchar(100);
update student set mother_name='';

alter table student add column parent_mother_name varchar(100);
update student set parent_mother_name='';

alter table teacher add column mother_name varchar(100);
update teacher set mother_name='';





alter table student add column born_place varchar(100);
update student set born_place='';

alter table student add column parent_born_place varchar(100);
update student set parent_born_place='';

alter table teacher add column born_place varchar(100);
update teacher set born_place='';