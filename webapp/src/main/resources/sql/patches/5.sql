--installed: 2012-08-08
alter table student add column address_str varchar(100);
update student set address_str='';

alter table student add column parent_address_str varchar(100);
update student set parent_address_str='';

alter table teacher add column address_str varchar(100);
update teacher set address_str='';

