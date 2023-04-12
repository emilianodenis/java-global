-- Copy this file into c:/dev/data ???

SET @burglar = 'burglar';
SET @thief = 'thief';
SET @gardener = 'gardener';
SET @magician = 'magician';
SET @archer = 'archer';
SET @king = 'king';

SET @burglar_id = NULL;
SET @thief_id = NULL;
SET @gardener_id = NULL;
SET @magician_id = NULL;
SET @archer_id = NULL;
SET @king_id = NULL;

INSERT INTO PROFESSION (DESCRIPTION)
VALUES (@burglar);
INSERT INTO PROFESSION (DESCRIPTION)
VALUES (@thief);
INSERT INTO PROFESSION (DESCRIPTION)
VALUES (@gardener);
INSERT INTO PROFESSION (DESCRIPTION)
VALUES (@magician);
INSERT INTO PROFESSION (DESCRIPTION)
VALUES (@archer);
INSERT INTO PROFESSION (DESCRIPTION)
VALUES (@king);

SELECT SET (@burglar_id, ID) FROM PROFESSION WHERE DESCRIPTION = @burglar;
SELECT SET (@thief_id, ID) FROM PROFESSION WHERE DESCRIPTION = @thief;
SELECT SET (@gardener_id, ID) FROM PROFESSION WHERE DESCRIPTION = @gardener;
SELECT SET (@magician_id, ID) FROM PROFESSION WHERE DESCRIPTION = @magician;
SELECT SET (@archer_id, ID) FROM PROFESSION WHERE DESCRIPTION = @archer;
SELECT SET (@king_id, ID) FROM PROFESSION WHERE DESCRIPTION = @king;

INSERT INTO CHARACTER (FIRST_NAME, LAST_NAME, EMAIL, PROFESSION, PROFESSION_ID)
VALUES ('Frodo', 'Baggins', 'frodo@baggins.lotr', 'burglar', @burglar_id);
INSERT INTO CHARACTER (FIRST_NAME, LAST_NAME, EMAIL, PROFESSION, PROFESSION_ID)
VALUES ('Bilbo', 'Baggins', 'bilbo@baggins.lotr', 'thief', @thief_id);
INSERT INTO CHARACTER (FIRST_NAME, LAST_NAME, EMAIL, PROFESSION, PROFESSION_ID)
VALUES ('Samwise', 'Gamegee', 'sam@wise.lotr', 'gardener', @gardener_id);
INSERT INTO CHARACTER (FIRST_NAME, LAST_NAME, EMAIL, PROFESSION, PROFESSION_ID)
VALUES ('Gandalf', 'The Grey', 'gandalf@sorcerer.lotr', 'magician', @magician_id);
INSERT INTO CHARACTER (FIRST_NAME, LAST_NAME, EMAIL, PROFESSION, PROFESSION_ID)
VALUES ('Legolas', 'Greenleaf', 'legolas@rivendale.lotr', 'archer', @archer_id);
INSERT INTO CHARACTER (FIRST_NAME, LAST_NAME, EMAIL, PROFESSION, PROFESSION_ID)
VALUES ('Aragorn', 'Elendil', 'aragorn@gondor.lotr', 'king', @king_id);