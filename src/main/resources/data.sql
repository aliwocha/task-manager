INSERT INTO roles(role_name) VALUES
    ('ROLE_USER'),
    ('ROLE_ADMIN');

--user - pass: User1234
--admin - pass: Admin1234
INSERT INTO users(login, password, email, enabled, registration_date, role_id) VALUES
    ('user', '$2a$10$Pef2YReykJBsFlPMiQDN/Osuw.giwWfpmOWDfJV0MRV6QK.SMr7uu', 'mail@gmail.com', true, TIMESTAMP '2022-01-01 12:00:00',
        SELECT id FROM roles WHERE role_name = 'ROLE_USER'),
    ('admin', '$2a$10$kb6QF30IUX.S2GmzoWLUFOCnf/UBzP1nJ73P2/g8nsC6FY6o9Jd/2', 'mail.net@gmail.com', true, TIMESTAMP '2022-01-01 12:00:00',
        SELECT id FROM roles WHERE role_name = 'ROLE_ADMIN');

INSERT INTO categories(category_name) VALUES
    ('No category'),
    ('Personal'),
    ('Home'),
    ('Work'),
    ('Life'),
    ('Shopping'),
    ('Health'),
    ('Travel');

INSERT INTO tasks(title, description, priority, status, deadline, category_id) VALUES
    ('Zrobić zakupy', 'Spodnie, buty, sweter', 'LOW', 'NEW', null,
        SELECT id FROM categories WHERE category_name = 'Shopping'),
    ('Pomalować ściany', '', 'MEDIUM', 'NEW', '2021-03-30',
        SELECT id FROM categories WHERE category_name = 'Home'),
    ('Stworzyć aplikację', 'Aplikacja To-Do List', 'HIGH', 'IN_PROGRESS', DATE '2021-04-10',
        SELECT id FROM categories WHERE category_name = 'Work'),
    ('Ukończyć kurs ''Spring - zadania''', 'Kurs programowania z javastart.pl', 'HIGH', 'COMPLETED', DATE '2021-03-24',
        SELECT id FROM categories WHERE category_name = 'Work'),
    ('Złożyć życzenia babci', '', 'MEDIUM', 'NEW', '2021-03-28',
        SELECT id FROM categories WHERE category_name = 'No category');