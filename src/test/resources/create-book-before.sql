
insert into person(id_person, name, year_of_birth, username, password, role, activation_code, registration_at)
values(1, 'test', 2000, 'test', 'test', 'ADMIN', 'null', '2023-02-11');

insert into book(id_book, id_person, name_of_book, date_of_writing, author, taken_at, img_url)
values (1, 1, 'book', 2000, 'test', '2023-02-03', '/images/demons.jpg');
insert into book(id_book, id_person, name_of_book, date_of_writing, author, taken_at, img_url)
values (2, 1, 'book', 2000, 'test', '2023-02-03', '/images/demons.jpg');
insert into book(id_book, id_person, name_of_book, date_of_writing, author, taken_at, img_url)
values (3, 1, 'book', 2000, 'test', '2023-02-03', '/images/demons.jpg');


