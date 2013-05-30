alter table course_income modify column amount double;

alter table course add column room int(11);
update course set room = 1;
ALTER TABLE  course MODIFY room INT(11) NOT NULL;

alter table course add column time_of_classes VARCHAR(255);
alter table course add column weekly_number_of_classes int(4);
update course set weekly_number_of_classes = 0;
ALTER TABLE  course MODIFY weekly_number_of_classes INT(4) NOT NULL;
