INSERT into roles(id, name) VALUES
    (1, 'ROLE_USER'),
    (2, 'ROLE_ADMIN');

--user1/user2 - pass: user
--admin - pass: admin
INSERT into users(id, login, password, email, enabled, role_id) VALUES
    (1, 'user1', '$2a$12$H9Nubp16Jemw4y52ilMqr.RTVcwQc/pm9pR8uqf.ioqS8eTRhiFGq', 'arturliwocha@gmail.com', true, 1),
    (2, 'user2', '$2a$12$H9Nubp16Jemw4y52ilMqr.RTVcwQc/pm9pR8uqf.ioqS8eTRhiFGq', 'arturliwocha1@gmail.com', false, 1),
    (3, 'admin', '$2a$12$9FmjWD0L1vIhb4f1O7m33.WrE3MH7JZRO8iOFfbSI7VyfZg6ohQaq', 'arturliwocha2@gmail.com', true, 2);

INSERT into categories(id, name) VALUES
    (1, 'No category'),
    (2, 'Personal'),
    (3, 'Home'),
    (4, 'Work'),
    (5, 'Life'),
    (6, 'Shopping'),
    (7, 'Health'),
    (8, 'Travel');

INSERT into tasks(id, title, description, priority, status, deadline, category_id) VALUES
    (1, 'Zrobić zakupy', 'Spodnie, buty, sweter', 'LOW', 'NEW', null, 6),
    (2, 'Pomalować ściany', '', 'MEDIUM', 'NEW', '2021-03-30', 3),
    (3, 'Stworzyć aplikację', 'Aplikacja To-Do List', 'HIGH', 'IN_PROGRESS', '2021-04-10', 4),
    (4, 'Ukończyć kurs ''Spring - zadania''', 'Kurs programowania z javastart.pl', 'HIGH', 'COMPLETED', '2021-03-24', 4),
    (5, 'Zadzwoń do babci', '', 'MEDIUM', 'NEW', '2021-03-28', 1);