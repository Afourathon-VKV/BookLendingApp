alter table book_lending add constraint BL_STUDENT foreign key (student_id) references student(id);
alter table book_lending add constraint BL_BOOK foreign key (book_code) references book(id);