alter table book_lending add constraint BL_STUDENT foreign key (roll_no) references student(roll_no);
alter table book_lending add constraint BL_BOOK foreign key (book_code) references book(code);