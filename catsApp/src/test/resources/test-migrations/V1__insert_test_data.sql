INSERT INTO catsAppSchema.owners (id, first_name, last_name, birthdate)
VALUES
    (DEFAULT, 'Mihail', 'Adyanov', '2002-02-02'),
    (DEFAULT, 'Pavel', 'Milyutin', '2004-03-05'),
    (DEFAULT, 'Sergey', 'Petrov', '2003-08-25'),
    (DEFAULT, 'Vladimir', 'Gusev', '2002-06-14'),
    (DEFAULT, 'Roman', 'Makarov', '2005-05-27');

INSERT INTO catsAppSchema.cats (id, name, breed, color, birthdate, owner_id)
VALUES
    (DEFAULT, 'Tom', 'Siberian', 'GREY', '2018-06-15', 1),
    (DEFAULT, 'Bella', 'Persian', 'WHITE', '2017-04-10', 2),
    (DEFAULT, 'Simba', 'Bengal', 'ORANGE', '2019-09-05', 3),
    (DEFAULT, 'Luna', 'Maine Coon', 'BLACK', '2020-12-20', 4),
    (DEFAULT, 'Milo', 'British Shorthair', 'SEMICOLOR', '2016-01-11', 5);