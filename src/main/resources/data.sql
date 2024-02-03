insert into method (method_name) values ('Гештальт');
insert into method (method_name) values ('КПТ');
insert into method (method_name) values ('Психоанализ');
insert into method (method_name) values ('Арт-терапия');
insert into method (method_name) values ('Схематерапия');
insert into method (method_name) values ('Коучинг');

insert into specialization (specialization_name) values ('Близкие отношения');
insert into specialization (specialization_name) values ('Выгорание');
insert into specialization (specialization_name) values ('Нейроотличность');
insert into specialization (specialization_name) values ('Карьера');
insert into specialization (specialization_name) values ('Депрессия');
insert into specialization (specialization_name) values ('Религия');
insert into specialization (specialization_name) values ('Послеродовая депрессия');


insert into client (name, email) values ('Соня', 's.zlo@yandex.ru');
insert into client (name, email) values ('Влада', 'v@mail.ru');

insert into userr (email, password, fk_id,role) values ('s.zlo@yandex.ru','pass','1','CLIENT');
insert into userr (email, password, fk_id,role) values ('v@mail.ru','pass','2','CLIENT');


insert into psychologist (name, lname, email, town, videocon, confirmed, description, age, education, practice, registration, price, image) values ('Мария','Шурова', 'm@mail.ru', 'Выкса',true,true,'Ваш психолог, работаю с аутизмом и СДВГ', '1995-11-19', 'МГУ','2016-10-17','2023-05-04', 3000,'https://www.factroom.ru/wp-content/uploads/2018/04/Depositphotos_21186817_m-2015-1.jpg');
insert into psychologist (name, lname, email, town, videocon, confirmed, description, age, education, practice, registration, price, image) values ('Павел','Дягтев', 'asasa@yandex.ru', 'Муром',true,true,'Работаю с парами и семьями, а также с подростками', '1998-08-30', 'МГУ','2020-05-19','2022-11-01', 4500,'https://belissimma.ru/wp-content/uploads/2021/02/2-5.jpg');

insert into psychologist_method(psychologist_id, method_id) values (1, 2);
insert into psychologist_method(psychologist_id, method_id) values (1, 1);
insert into psychologist_method(psychologist_id, method_id) values (2, 2);

insert into psychologist_specialization(psychologist_id,specialization_id) values (1, 3);
insert into psychologist_specialization(psychologist_id,specialization_id) values (1, 2);
insert into psychologist_specialization(psychologist_id,specialization_id) values (2, 4);
insert into psychologist_specialization(psychologist_id,specialization_id) values (2, 1);

insert into sessions (format,day_session, time_session, status, psychologist_id, client_id) values (true, '2024-05-01', '14:30','W', 1, 1);
insert into sessions (format,day_session, time_session, status, psychologist_id, client_id) values (false, '2024-01-01', '14:30','F', 1, 2);

--insert into cp_session(session_id, psychologist_id, client_id) values(1,1,2);
--insert into cp_session(session_id, psychologist_id, client_id) values(2,2,2);
--insert into cp_session(psychologist_id,session_id) values (1, 1);
--insert into cp_session(psychologist_id,session_id) values (2, 2);


