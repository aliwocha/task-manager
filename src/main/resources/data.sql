INSERT into category(id, name) VALUES
    (1, 'No category'),
    (2, 'Personal'),
    (3, 'Home'),
    (4, 'Work'),
    (5, 'Life'),
    (6, 'Shopping'),
    (7, 'Health'),
    (8, 'Travel');

INSERT into task(id, title, description, priority, status, deadline, category_id) VALUES
    (1, 'Zrobić zakupy', 'Spodnie, buty, sweter', 'LOW', 'NEW', null, 6),
    (2, 'Pomalować ściany', '', 'MEDIUM', 'NEW', '2021-03-30', 3),
    (3, 'Stworzyć aplikację', 'Aplikacja To-Do List', 'HIGH', 'IN_PROGRESS', '2021-04-10', 4),
    (4, 'Ukończyć kurs ''Spring - zadania''', 'Kurs programowania z javastart.pl', 'HIGH', 'COMPLETED', '2021-03-24', 4),
    (5, 'Zadzwoń do babci', '', 'MEDIUM', 'NEW', '2021-03-28', 1);