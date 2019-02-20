INSERT INTO POOL VALUES(1, "Matosinhos");
INSERT INTO POOL VALUES(2, "Perafita");
INSERT INTO POOL VALUES(3, "Senhora da Hora");
INSERT INTO POOL VALUES(4, "Custoias");
INSERT INTO POOL VALUES(5, "Guifoes");
INSERT INTO POOL VALUES(6, "Leca do Baldio");

INSERT INTO COACH (USERNAME, PASSWORD, NAME, POOL_ID, POOLMASTER) VALUES("cam", 'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3', "Camilo", 1, 0);
INSERT INTO COACH (USERNAME, PASSWORD, NAME, POOL_ID, POOLMASTER) VALUES("xaninha", "xana", "Alexandra", 1, 0);

INSERT INTO SWIMMER (NAME, DOB, COACH_ID, POOL_ID) VALUES("Duarte", "1997-02-11", 1, 1);
INSERT INTO SWIMMER (NAME, DOB, COACH_ID, POOL_ID) VALUES("Mafalda", "1998-03-21", 1, 1);
INSERT INTO SWIMMER (NAME, DOB, COACH_ID, POOL_ID) VALUES("Ines", "1996-06-30", 2, 1);
INSERT INTO SWIMMER (NAME, DOB, COACH_ID, POOL_ID) VALUES("Mario", "2000-05-30", 2, 1);
INSERT INTO SWIMMER (NAME, DOB, COACH_ID, POOL_ID) VALUES("Lucas", "2001-11-03", 1, 2);
INSERT INTO SWIMMER (NAME, DOB, COACH_ID, POOL_ID) VALUES("Mafalda C.", "1998-03-21", 1, 1);
INSERT INTO SWIMMER (NAME, DOB, COACH_ID, POOL_ID) VALUES("Ines R.", "1998-03-21", 1, 1);
INSERT INTO SWIMMER (NAME, DOB, COACH_ID, POOL_ID) VALUES("Mario t.", "1998-03-21", 1, 1);
INSERT INTO SWIMMER (NAME, DOB, COACH_ID, POOL_ID) VALUES("Nuno H.", "1998-03-21", 1, 1);

INSERT INTO CONTEST (STYLE, DAYOFCONTEST, SWIMER_ID) VALUES("FreeStyle", "2018-03-03", 1);
INSERT INTO CONTEST (STYLE, DAYOFCONTEST, SWIMER_ID) VALUES("FreeStyle", "2018-03-03", 2);
INSERT INTO CONTEST (STYLE, DAYOFCONTEST, SWIMER_ID) VALUES("FreeStyle", "2018-03-03", 3);
INSERT INTO CONTEST (STYLE, DAYOFCONTEST, SWIMER_ID) VALUES("FreeStyle", "2018-03-03", 4);


INSERT INTO PARTIAL (CONTEST_ID, PARTIAL_TIME) VALUES(1, 13.5);
INSERT INTO PARTIAL (CONTEST_ID, PARTIAL_TIME) VALUES(1, 13.5);
INSERT INTO PARTIAL (CONTEST_ID, PARTIAL_TIME) VALUES(1, 13.5);
INSERT INTO PARTIAL (CONTEST_ID, PARTIAL_TIME) VALUES(1, 13.5);

.timer on


--ver tempos de um nadador através do nome
SELECT SUM(PARTIAL.PARTIAL_TIME) AS TOTALTIME
FROM PARTIAL
WHERE PARTIAL.CONTEST_ID IN (

  SELECT DISTINCT CONTEST_ID
  FROM CONTEST, SWIMMER
  WHERE CONTEST.SWIMER_ID IN (

    SELECT DISTINCT SWIMMER.ID
    FROM SWIMMER
    WHERE SWIMMER.NAME="Duarte"
  )
);

--ver nadadores de um treinador através do nome
SELECT DISTINCT SWIMMER.NAME, SWIMMER.DOB
FROM SWIMMER, COACH
WHERE SWIMMER.COACH_ID IN (
  SELECT DISTINCT COACH.ID
  FROM COACH
  WHERE COACH.NAME="Camilo");

CREATE TRIGGER SWIMMER_DELETE
BEFORE DELETE ON SWIMMER
FOR EACH ROW
BEGIN
  DELETE FROM CONTEST
  WHERE SWIMER_ID=OLD.ID;
END;

CREATE TRIGGER CONTEST_DELETE
BEFORE DELETE ON CONTEST
FOR EACH ROW
BEGIN
  DELETE FROM PARTIAL
  WHERE CONTEST_ID=OLD.ID;
END;
