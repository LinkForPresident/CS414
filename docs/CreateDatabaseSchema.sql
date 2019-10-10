CREATE DATABASE [IF NOT EXISTS] Jungle;
USE Jungle;
DROP TABLE [IF EXISTS] Invite;
DROP TABLE [IF EXISTS] Game;
DROP TABLE [IF EXISTS] Friend;
DROP TABLE [IF EXISTS] User;

-- UserNames must be unique, and at the time of registration, we must require all 3 attributes
CREATE TABLE [IF NOT EXISTS] User (
  UserName VARCHAR(64) NOT NULL,
  Email VARCHAR(100) NOT NULL,
  Password VARCHAR(100) NOT NULL, -- Assumption that this is hashed, or secured in some way.
  PRIMARY KEY (UserName),
  UNIQUE(UserName),
  ON DELETE NO ACTION
);

-- For all intensive purposes, we can ignore this table until we want to implement it. No other tables refernce it.
CREATE TABLE [IF NOT EXISTS] Friend (
  User1 VARCHAR(64) NOT NULL,
  User2 VARCHAR(64) NOT NULL,
  PRIMARY KEY (User1, User2),
  FOREIGN KEY (User1) REFERENCES User(UserName),
  FOREIGN KEY (User2) REFERENCES User(UserName),
);

-- Assumptions
-- The Game object is created prior to a player accepting an invite, so we need to account for nulls
-- We'll limit usernames to 64 characters
CREATE TABLE [IF NOT EXISTS] Game (
  Id INTEGER auto_increment NOT NULL,
  Player1 VARCHAR(64) NOT NULL,
  Player2 VARCHAR(64) NULL,
  BoardState VARCHAR(2000) NULL, -- We could do Not Null here.
  GameState VARCHAR(50) NOT NULL DEFAULT "Invited", -- Initialized, Active, Forfeit, Completed
  StartDate DateTime NULL,
  EndDate DateTime NULL,
  LastMoveTime DateTime NULL, --ToDo handled by a trigger for every update on the BoardState record after innitialization
  Winner VARCHAR(64) NULL,
  PRIMARY KEY (Id),
  FOREIGN KEY (Player1) REFERENCES User(UserName),
);

CREATE TABLE [IF NOT EXISTS] Invite (
  Inviter VARCHAR(64) NOT NULL,
  Invitee VARCHAR(64) NULL,
  Game Integer NOT NULL,
  InvitedOn DATE NOT NULL,
  PRIMARY KEY (Inviter, Invitee),
  FOREIGN KEY (Inviter) REFERENCES User(UserName),
  FOREIGN KEY (Invitee) REFERENCES User(UserName),
  FOREIGN KEY (Game) REFERENCES Game(Id),
);
