-- Database setup
DROP DATABASE IF EXISTS ChessTournament;
CREATE DATABASE ChessTournament;
USE ChessTournament;
FLUSH PRIVILEGES;

DROP TABLE IF EXISTS Tournament;
DROP TABLE IF EXISTS Organizer;
DROP TABLE IF EXISTS Player;
DROP TABLE IF EXISTS Arbiter;
DROP TABLE IF EXISTS Match;
DROP TABLE IF EXISTS Tournament_Entry;

DROP VIEW IF EXISTS Players_In_Tournament;
DROP VIEW IF EXISTS Player_Matches;

-- Table Creation
CREATE TABLE Tournament(
    Tournament_ID INT NOT NULL AUTO_INCREMENT UNIQUE,
    Tournament_Organizer VARCHAR(50) NOT NULL,
    Tournament_Name VARCHAR(50) NOT NULL,
    Tournament_Location VARCHAR(50) NOT NULL,
    Tournament_Date DATE NOT NULL,
    Tournament_System VARCHAR(50) NOT NULL,
    CONSTRAINT chk_System 
    CHECK (
        Tournament_System in ('Round Robin','Single Elimination','Swiss System')
    ),
    PRIMARY KEY (Tournament_ID)
    FOREIGN KEY (Tournament_Organizer) REFERENCES Organizer(Organizer_ID)
);

CREATE TABLE Organizer(
    Organizer_ID INT NOT NULL AUTO_INCREMENT UNIQUE,
    Organizer_Name VARCHAR(50) NOT NULL,
    Organizer_Email VARCHAR(50) NOT NULL,
    Organizer_Phone VARCHAR(50) NOT NULL,
    Organizer_Address VARCHAR(50) NOT NULL,
    PRIMARY KEY (Organizer_ID)
);

CREATE TABLE Player(
    Player_ID INT NOT NULL AUTO_INCREMENT UNIQUE,
    Player_Name VARCHAR(50) NOT NULL,
    Player_Bithdate DATE NOT NULL,
    Player_Nationality VARCHAR(50) NOT NULL,
    Player_Gender VARCHAR(50) NOT NULL,
    Player_Email VARCHAR(50) NOT NULL,
    Player_Phone VARCHAR(50) NOT NULL,
    Player_Address VARCHAR(50) NOT NULL,
    PRIMARY KEY (Player_ID)
);

CREATE TABLE Player_Statistics(
    Player_ID INT NOT NULL,
    Player_Rating INT NOT NULL,
    Player_Title VARCHAR(50) NOT NULL,
    Player_Matches_Played INT NOT NULL,
    Player_Matches_Won INT NOT NULL,
    Player_Matches_Drawn INT NOT NULL,
    PRIMARY KEY (Player_ID),
    FOREIGN KEY (Player_ID) REFERENCES Player(Player_ID)
);

CREATE TABLE Arbiter(
    Arbiter_ID INT NOT NULL AUTO_INCREMENT UNIQUE,
    Arbiter_Name VARCHAR(50) NOT NULL,
    Arbiter_Email VARCHAR(50) NOT NULL,
    Arbiter_Phone VARCHAR(50) NOT NULL,
    Arbiter_Address VARCHAR(50) NOT NULL,
    Arbiter_Certification VARCHAR(50) NOT NULL,
    Arbiter_Certification_Date DATE NOT NULL,
    CONSTRAINT Arbiter_Certification_Date_Check 
    CHECK (
        Arbiter_Certification_Date >= DATEADD(YEAR,-10,GETDATE())
        ),
    PRIMARY KEY (Arbiter_ID)
);

CREATE TABLE Match(
    Match_ID INT NOT NULL AUTO_INCREMENT UNIQUE,
    Match_Tournament_ID INT NOT NULL,
    Match_Time TIME NOT NULL,
    Match_Player_ID_1 INT NOT NULL,
    Match_Player_ID_2 INT NOT NULL,
    PRIMARY KEY (Match_ID),
    FOREIGN KEY (Match_Tournament_ID) REFERENCES Tournament(Tournament_ID)
    FOREIGN KEY (Match_Player_ID_1) REFERENCES Player(Player_ID)
    FOREIGN KEY (Match_Player_ID_1) REFERENCES Player(Player_ID)
);

CREATE TABLE Tournament_Entry(
    Tournament_Entry_ID INT NOT NULL AUTO_INCREMENT UNIQUE,
    Tournament_Entry_Player_ID INT NOT NULL,
    Tournament_Entry_Tournament_ID INT NOT NULL,
    Tournament_Result VARCHAR(50) NOT NULL,
    PRIMARY KEY (Tournament_Entry_ID),
    FOREIGN KEY (Tournament_Entry_Player_ID) REFERENCES Player(Player_ID),
    FOREIGN KEY (Tournament_Entry_Tournament_ID) REFERENCES Tournament(Tournament_ID)
);

-- Further Constraints
ALTER TABLE Tournament AUTO_INCREMENT=1000;
ALTER TABLE Organizer AUTO_INCREMENT=1000;
ALTER TABLE Player AUTO_INCREMENT=1000;
ALTER TABLE Arbiter AUTO_INCREMENT=1000;
ALTER TABLE Match AUTO_INCREMENT=1000;
ALTER TABLE Tournament_Entry AUTO_INCREMENT=1000;

-- View Creation
CREATE VIEW Players_In_Tournament AS
    SELECT Player_Name, Tournament_Name
    FROM Player, Tournament, Tournament_Entry
    WHERE Player.Player_ID = Tournament_Entry.Tournament_Entry_Player_ID
    AND Tournament.Tournament_ID = Tournament_Entry.Tournament_Entry_Tournament_ID;

CREATE VIEW Player_Matches AS
    SELECT Player_Name, Match_Time, Match_Tournament_ID, Match_Result
    FROM Player, Match, Tournament_Entry
    WHERE Player.Player_ID = Tournament_Entry.Tournament_Entry_Player_ID
    AND Match.Match_Tournament_ID = Tournament_Entry.Tournament_Entry_Tournament_ID;

-- Role Creation
CREATE ROLE 'administrator','organizer','arbiter',;
GRANT ALL ON ChessTournament.* TO 'administrator' WITH GRANT OPTION;
GRANT INSERT,UPDATE,SELECT,DELETE ON ChessTournament.* TO 'organizer';
GRANT INSERT,UPDATE,SELECT ON ChessTournament.Match,ChessTournament.Player,ChessTournament.Player_Statistics  TO 'arbiter';
-- User Creation
CREATE USER 'admin'@'localhost' IDENTIFIED BY 'admin';
CREATE USER 'irish_chess_union'@'localhost' IDENTIFIED BY 'irish_chess_union';
CREATE USER 's_gaffney'@'localhost' IDENTIFIED BY 's_gaffney';

GRANT 'administrator' TO 'admin'@'localhost';
GRANT 'organizer' TO 'irish_chess_union'@'localhost';
GRANT 'arbiter' TO 's_gaffney'@'localhost';

-- Table Poppulation
INSERT INTO Tournament VALUES ('FIDE', 'World Championship 2021', 'New York, USA', '2021-04-16', 'Round Robin');
INSERT INTO Tournament VALUES ('Trinity Chess Club', 'Trinity Championship 2021', 'Dublin, IE', '2021-11-11', 'Swiss System');
INSERT INTO Tournament VALUES ('Lichess.org', 'Daily Blitz Tournament 12/04/20', 'lichess.org/tournament/BxYkrTtj', '2020-04-12', 'Single Elimination');
INSERT INTO Tournament VALUES ('Irish Chess Union', 'European Individual Championship 2021', 'Dublin, IE', '2021-05-09', 'Swiss System');
INSERT INTO Tournament VALUES ('US Chess Federation', 'Junior Festival 2021', 'Los Angles, USA', '2021-06-31', 'Round Robin');

INSERT INTO Organizer VALUES('FIDE', 'admin@fide.org', 0871234567, 'Lausanne, Switzerland');
INSERT INTO Organizer VALUES('Trinity Chess Club', 'chess@tcd.ie', 0209153529, 'Dublin, IE');
INSERT INTO Organizer VALUES('Lichess.org', 'jd742@tcd.ie', 0209140461, 'San Francisco, CA');
INSERT INTO Organizer VALUES('Irish Chess Union', 'david@icu.org', 02091404617, 'Dublin, IE');
INSERT INTO Organizer VALUES('US Chess Federation', 'admin@ucf.org', 0209119916, 'New York, USA');

INSERT INTO Player VALUES('John Doe', '2089-05-21', 'USA', 'Male', 'johnx492@gmail.com', 0209177806, 'New York, USA')
INSERT INTO Player VALUES('Magnus Carlsene', '2091-03-12', 'Norwegian', 'Male', 'mc90@gmail.com', 0209124872, 'Tonsberg, NR')
INSERT INTO Player VALUES('Ding Liren', '1975-09-01', 'USA', 'Male', 'dingl4592@gmail.com', 0209137064, 'Tokoyo, JP')
INSERT INTO Player VALUES('Levon Aronian', '1999-06-21', 'USA', 'Male', 'laronian2@gmail.com', 0209157042, 'San Francisco, USA')
INSERT INTO Player VALUES('Eli Millar', '2000-02-08', 'USA', 'Male', 'elimillar@gmail.com', 0209115503, 'Dublin, IE')


INSERT INTO Player_Statistics VALUES (1001, 1005, 'None', 100, 40, 2);
INSERT INTO Player_Statistics VALUES (1002, 2587, 'GM', 1450, 1202, 200);
INSERT INTO Player_Statistics VALUES (1003, 2499, 'GM', 356, 254, 53);
INSERT INTO Player_Statistics VALUES (1004, 2551, 'GM', 459, 372, 24);
INSERT INTO Player_Statistics VALUES (1005, 2341, 'IM', 281, 154, 29);

INSERT INTO Aribiter VALUES('Jack Dorsey', 'jd87@gmail.com', 0771234567, 'London, UK', 'FA-D', '2016-06-21');
INSERT INTO Aribiter VALUES('Clark Kent', 'ckent@gmail.com', 0209147799, 'Metropolis, USA', 'IA-D', '1013-02-19');
INSERT INTO Aribiter VALUES('Kerciku, Suzana', 'suzana1235@gmail.com', 0209115503, 'New Dehli, India', 'FA-D', '2019-04-31');
INSERT INTO Aribiter VALUES('Lazaj, Shkelqim', 'lazajS56@gmail.com', 0209125789, 'jakarta, Indenosia', 'FA-D', '2021-12-01');
INSERT INTO Aribiter VALUES('Shehu, Olsi', 'osli127Shehu@gmail.com', 0209184217, 'London, UK', 'FA-D', '2015-04-05');

INSERT INTO Match VALUES(1001, '12:12:12', 1003, 1002);
INSERT INTO Match VALUES(1001, '07:14:16', 1004, 1002);
INSERT INTO Match VALUES(1003, '11:23:45', 1001, 1004);
INSERT INTO Match VALUES(1003, '09:07:18', 1002, 1005);
INSERT INTO Match VALUES(1003, '06:31:24', 1005, 1003);

INSERT INTO Tournament_Entry VALUES(1001, 1003, 'Lost');
INSERT INTO Tournament_Entry VALUES(1002, 1001, 'Won');
INSERT INTO Tournament_Entry VALUES(1003, 1001, 'Lost');
INSERT INTO Tournament_Entry VALUES(1004, 1001, 'Lost');
INSERT INTO Tournament_Entry VALUES(1005, 1003, 'Won');