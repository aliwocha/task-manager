INSERT into roles(id, role_name) VALUES
    (1, 'ROLE_USER'),
    (2, 'ROLE_ADMIN');

--user - pass: user
--admin - pass: admin
INSERT into users(id, login, password, email, enabled, role_id) VALUES
    (1, 'user', '$2a$10$Pef2YReykJBsFlPMiQDN/Osuw.giwWfpmOWDfJV0MRV6QK.SMr7uu', 'mail@gmail.com', true, 1),
    (2, 'admin', '$2a$10$kb6QF30IUX.S2GmzoWLUFOCnf/UBzP1nJ73P2/g8nsC6FY6o9Jd/2', 'mail.net@gmail.com', true, 2);

INSERT into categories(id, category_name) VALUES
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