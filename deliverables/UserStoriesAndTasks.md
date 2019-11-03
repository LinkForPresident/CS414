# User Stories & Tasks

## As a user, I would like to be able to create and play games (1)
### Acceptance Criteria
#### Client:
* Board should be viewable
* Board should be able to be interacted with (I/O)
* Board should show valid moves
* Games should be able to be started from a list
* Games should be able to be created
* Games should be able to be forfeited

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

* #125
GameServer handle forfeit game **(2)**
* #126
GameServer handle view game list **(2)**
* #172
Client handle forfeit game **(2)**
* #110
GameServer create game API support **(2)**
* #111
GameServer move API support **(1)**
* #112
GameServer view game API support **(2)**
* #113
Database support of game creation **(2)**
* #114
Database support of game piece moves **(2)**
* #122
GameServer handle Create game **(2)**
* #123
GameServer handle view game **(2)**
* #124
GameServer handle move piece **(2)**
* #127
Implement game creation in GameLogic **(3)**
* #129
Implement logic to handle making move and changing turns in GameLogic **(3)**
* #130
Implement back-end logic to handle updating valid moves in GameLogic **(5)**
* #131
Implement handling win condition detection in GameLogic **(3)**
* #163
As a user I want the game to be up to date as soon as I visit the Games page **(2)**
* #20
Implement move validity detection **(2)**
* #22
Handle game logic POST requests **(2)**
* #23
Client game board **(3)**
* #31
Create a front end interface to view and interact with the game **(3)**
* #42
Create a game board with buttons that can make requests and update app state based on responses **(5)**
* #84
Refactor back-end game logic into distinct classes for easier front-end data digestion **(Not estimated)**
* #89
Display Piece, Environment, and available move data **(8)**


## As a user, I would like to know when no moves are possible (1)
### Acceptance Criteria:
#### Logic:
* Count the number of available legal moves a player has
* End the game if no moves are available for a player on their turn

### Associated Tasks
* #133
End the game if no moves are available **(1)**
* #75
Implement method of detecting when no legal moves are available for the current turn's player **(5)**
