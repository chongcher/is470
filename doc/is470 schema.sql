#create new schema
DROP SCHEMA IF EXISTS IS470;
CREATE SCHEMA IF NOT EXISTS IS470;
USE IS470;

#create users table
CREATE TABLE USERS(
User_ID VARCHAR(255) NOT NULL PRIMARY KEY,
PasswordHash VARCHAR(255) NOT NULL,
IsFaculty BOOLEAN NOT NULL
);

#create lectures table
CREATE TABLE LECTURES(
Lecture_ID VARCHAR(255) NOT NULL PRIMARY KEY,
Keywords VARCHAR(255)
);


#create responses table
CREATE TABLE RESPONSES(
User_ID VARCHAR(255) NOT NULL,
Lecture_ID VARCHAR(255) NOT NULL,
Response VARCHAR(255) NOT NULL,
Keywords VARCHAR(255) NOT NULL,
CONSTRAINT RESPONSES_PK PRIMARY KEY (User_ID,Lecture_ID),
CONSTRAINT RESPONSES_FK1 FOREIGN KEY (User_ID) REFERENCES USERS(User_ID),
CONSTRAINT RESPONSES_FK2 FOREIGN KEY (Lecture_ID) REFERENCES LECTURES(Lecture_ID)
);

#insert admin
INSERT INTO USERS VALUES ("admin","$2a$08$Y64iZgb3nb6laJSTC/VX7.8qRhIIr9RgTAZ/JTviN2VFj5k7RhbjW",1);
INSERT INTO LECTURES VALUES("week_1",null);