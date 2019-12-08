# User Stories & Tasks

## As a user, I would like to be able to create and play games (1)
### Acceptance Criteria
#### Client:
* Board should be viewable
* Board should be able to be interacted with (I/O)
* Board should show valid moves
* Games should be able to be started from a list
* Games should be able to be created
#### Server:
* Should be able to handle new games
* Should be able to handle viewing games
* Should be able to handle making moves
* Should be able to handle deleting games
* Should properly interact withAs a user, I would like to be able to create and play games (1) database(s)
* Should be able to return a list of viewable games
#### Logic:
* Should be able to handle game creation
* Should be able to handle giving valid moves
* Should be able to handle making a move
* Should be able to handle winning/losing

### Associated Tasks 
* #125 GameServer handle forfeit game **(3)**
* #172 Client handle forfeit game

## As a user, I would like to create/delete and login/logout of my user account (2)
### Acceptance Criteria:
#### Client:
* Send in user info to register an account
* Send in user credentials to log in to an existing account
* Log out of an account
* Delete an existing account
#### Server:
* Handle user creation
* Handle user deletion
* Handle login requests
* Handle logout requests

### Associated Tasks
* #134 Client handle user unregistration **(1)**
* #136 GameServer handle user unregistration **(1)**
* #137 GameServer handle propagation of user unregistration to games **(3)**
* #138 Database handle propagation of game status whenever a user unregisters **(3)**

## As a user, I would like to be able to view match histories (3)
### Acceptance Criteria:
#### Client:
* Match history should be viewable
#### Server:
* Should be able to get match history from database
* Should update database with match history when game ends
* Should update database match history if a user is deleted

### Associated Tasks
* #126 GameServer handle view game list **(3)**
* #140 Match History is Viewable
* #141 Get Match History from Server on Request
* #142 Update Match History when Game Ends

