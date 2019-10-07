
import React from "react";
import {Tabs, TabList, Tab, TabPanel} from 'react-tabs';
import {Button} from 'react-bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css';
import axios from 'axios';
import ButtonGroup from "react-bootstrap/ButtonGroup";
export default class App extends React.Component {

    constructor(props){
        super(props);
        this.updateLoginValue = this.updateLoginValue.bind(this);
        this.updateInvites = this.updateInvites.bind(this);
        this.setSelectedGame = this.setSelectedGame.bind(this);
        this.postExample = this.postExample.bind(this);
    }

    state = {
        loggedIn: true,
        username: null,
        activeGames: ["0001", "0002", "0005"],
        completedGames: ["0003", "0004"],
        users: ['Brian', 'Dave'],
        passwords: ['Crane', 'Wells'],
        selectedGame: null,
        invites: " ",
        gameState:
            {
                "gameID": "00000",
                "playerOne": "Bob",
                "playerTwo": "Sally",
                "turn": "Bob",
                "turnNumber": 0,
                "board": [
                    ["r1 ", "__ ", "__ ", "__ ", "__ ", "__ ", "__ "],
                    ["__ ", "__ ", "__ ", "__ ", "__ ", "__ ", "__ "],
                    ["__ ", "__ ", "__ ", "__ ", "__ ", "__ ", "__ "],
                    ["__ ", "__ ", "__ ", "__ ", "__ ", "__ ", "__ "],
                    ["__ ", "__ ", "__ ", "__ ", "__ ", "__ ", "__ "],
                    ["__ ", "__ ", "__ ", "__ ", "__ ", "__ ", "__ "],
                    ["__ ", "__ ", "__ ", "__ ", "__ ", "__ ", "__ "],
                    ["__ ", "__ ", "__ ", "__ ", "__ ", "__ ", "__ "],
                    ["__ ", "__ ", "__ ", "__ ", "__ ", "__ ", "__ "],
                ],
                "availableMoves": [
                    ["__", "__", "__", "__", "__", "__", "__"],
                    ["__", "__", "__", "__", "__", "__", "__"],
                    ["__", "__", "__", "__", "__", "__", "__"],
                    ["__", "__", "__", "__", "__", "__", "__"],
                    ["__", "__", "__", "__", "__", "__", "__"],
                    ["__", "__", "__", "__", "__", "__", "__"],
                    ["__", "__", "__", "__", "__", "__", "__"],
                    ["__", "__", "__", "__", "__", "__", "__"],
                    ["__", "__", "__", "__", "__", "__", "__"]
                ],
                "winner": "",
                "startTime": "",
                "endTime": ""
            },
        apiConfig:{
            url:'http://localhost:8080',
            payload: "action=login&username=dummy_user&password=iforgot123",
            headers: {
                'Content-Type': 'application/text',
                'Access-Control-Allow-Methods': 'GET,PUT,POST,DELETE,PATCH,OPTIONS',
            }
        }
    };

    postExample(data) {
        console.log("Making request");
        var self = this;
        axios.post('http://localhost:8080',
            // "action=move_piece&gameID=1234&username=dummy_user&password=iforgot123&row=8&column=0",
            data,
            {
                headers: {
                    'Content-Type': 'application/json',
                    'Access-Control-Allow-Methods': 'GET,PUT,POST,DELETE,PATCH',
                }
            })
            .then(function (response) {
                let availableMovesProto = response.data.availableMoves;
                console.log(response.data);
                if (typeof availableMovesProto == 'undefined') {
                    return
                }
                availableMovesProto = availableMovesProto.split("|");
                let availableMoves = [];
                availableMovesProto.forEach(function (element) {
                    let elems = element.split(",");
                    let row = [];
                    elems.forEach(function (elem) {
                        row.push(elem);
                    });
                    availableMoves.push(row);
                });
                console.log(availableMoves);
                let boardStateProto = response.data.board;
                boardStateProto = boardStateProto.split("|");
                let boardState = [];
                boardStateProto.forEach(function (element) {
                    let elems = element.split(",");
                    let row = [];
                    elems.forEach(function (elem) {
                        row.push(elem);
                    });
                    boardState.push(row);
                });
                console.log(boardState);
                response.data.availableMoves = availableMoves;
                response.data.board = boardState;
                self.setState({
                    gameState: response.data
                })
            })
            .catch();
        let board = this.state.gameState.board;
        console.log(this.state.gameState);
    }

        async handleLoginRequest(event, url, payload, headers){
            var resp = await axios.post(url,
                       payload,
                       headers,
                        )
                        .then(function(response){
                        console.log(response);
                        return response;
                        }
                        )
                        .catch()
                        return resp.data;
        }
        async handleSendInvite(event, url, payload, headers){
            var resp = await axios.post(url,
                       payload,
                       headers,
                        )
                        .then(function(response){
                        console.log(response);
                        return response;
                        }
                        )
                        .catch()
                        return resp.data;
        }
        async handleAcceptInvite(url, payload, headers){
            var resp = await axios.post(url,
                       payload,
                       headers,
                        )
                        .then(function(response){
                        console.log(response);
                        return response;
                        }
                        )
                        .catch()
                        return resp.data;
        }
        async handleDeclineInvite(url, payload, headers){
            var resp = await axios.post(url,
                       payload,
                       headers,
                        )
                        .then(function(response){
                        console.log(response);
                        return response;
                        }
                        )
                        .catch()
                        return resp.data;
        }

        updateLoginValue(loginVal, username, invites){
        console.log(username);
        console.log(invites);
            this.setState({
                loggedIn: loginVal,
                username: username,
                invites: invites,
            });
        }
        updateInvites(invites){
            this.setState({
                invites: invites
            });
        }

    setGameState(gameId) {
        console.log("calling api for game state...");
        axios.post(
            this.state.apiConfig.url,
            "action=view_game&gameID=1234",
            {headers: this.state.apiConfig.headers}
        ).then((response) => console.log(response));
        console.log("Game State set.")
    }


    setSelectedGame(e) {
        console.log("making request to the server for game");
        this.setState({
            selectedGame: e.target.value
        });
        this.setGameState(e.target.value);
    }

    handleLogin(username, password) {
        var self = this;
        console.log("calling api for game state...");
        axios.post(
            'http://localhost:8080',
            "action=login&username=" + username + "&password=" + password,
            {headers: {
            'Content-Type': 'application/json',
                'Access-Control-Allow-Methods': 'GET,PUT,POST,DELETE,PATCH',
        }}
        ).then(function (response) {
            const loggeedIn = response.data.loggedIn;
            self.setState({
                loggedIn: response.data
            })
        })
    }

    render() {
        if(this.state.loggedIn){
        return (
            <div className='menu navigation-menu'>
                <Tabs>
                    <TabList>
                        <Tab>Home</Tab>
                        <Tab>Games</Tab>
                        <Tab>Game Rules</Tab>
                        <Tab>History</Tab>
                        <Tab>Invite</Tab>
                        <Tab>Register</Tab>
                        <Tab>Login</Tab>
                        <Tab>User</Tab>
                    </TabList>
                    <TabPanel><Home/></TabPanel>

                    <TabPanel>
                        <Games
                            activeGames={this.state.activeGames}
                            completedGames={this.state.completedGames}
                            setSelectedGame={this.setSelectedGame}
                            loggedIn={this.state.loggedIn}
                        />
                        <Board selectedGame={this.state.selectedGame}
                               gameState={this.state.gameState}
                               postExample={this.postExample}
                               loggedIn={this.state.loggedIn}
                        />
                    </TabPanel>
                    <TabPanel><GameRules/></TabPanel>
                    <TabPanel><History postExample={this.postExampleNew}/></TabPanel>
                    <TabPanel><Invite apiConfig = {this.state.apiConfig} handleSendInvite = {this.handleSendInvite} handleAcceptInvite = {this.handleAcceptInvite}
                                                          invites={this.state.invites} username={this.state.username} handleDeclineInvite = {this.handleDeclineInvite} updateInvites={this.updateInvites}/></TabPanel>
                    <TabPanel><Register users = {this.state.users} passwords = {this.state.passwords}/></TabPanel>
                    <TabPanel><Login apiConfig = {this.state.apiConfig} handleLoginRequest = {this.handleLoginRequest} loggedIn={this.state.loggedIn}
                    updateLoginValue={this.updateLoginValue}/></TabPanel>
                    <TabPanel><User/></TabPanel>
                </Tabs>
            </div>
        )
        }else{
        return(
        <div className='menu navigation-menu'>
                        <Tabs>
                            <TabList>


                                <Tab>Login</Tab>
                            </TabList>
                            <TabPanel><Login apiConfig = {this.state.apiConfig} handleLoginRequest = {this.handleLoginRequest} loggedIn={this.state.loggedIn}
                            updateLoginValue={this.updateLoginValue}/></TabPanel>

                        </Tabs>
                    </div>
                )
        }
    }
}

class Home extends React.Component {
    render() {
        return (
            <div className={'HomePage'}>
                <p>Home Goes here</p>
            </div>
        )
    }
}

class Games extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            activeGames: this.props.activeGames,
            completedGames: this.props.completedGames,
        };

    }

    render() {
        if (this.props.loggedIn) {

            const activeGamesList = this.state.activeGames.map((game) =>
                <li className={'list-group-item list-group-item-dark'}>
                    <Button onClick={this.props.setSelectedGame} value={game}
                    >
                        Game Id: {game}
                    </Button> request json</li>);
            const completedGamesList = this.state.completedGames.map((game) =>
                <li className={'list-group-item list-group-item-dark'}>
                    <Button onClick={this.props.setSelectedGame} value={game}
                    >
                        Game Id: {game}
                    </Button> request json</li>);
            return (
                <div className={'GamesPage'}>
                    <h2>Active Games</h2>
                    <ul className={'list-group list-group-horizontal'}>
                        {activeGamesList}
                    </ul>
                    <h2>Completed Games</h2>
                    <ul className={'list-group list-group-horizontal'}>
                        {completedGamesList}
                    </ul>

                </div>
            )
        }
        else {
            return("");
        }
    }
}


class Board extends Games {
    constructor(props) {
        super(props);
        this.state = {
            background_src: "./images/dou_shou_qi_jungle_game-board.jpg",
            selected_piece: null,
        };
    }

    setSelectedPiece(e) {
        console.log("making request to the server for game");
        this.setState({
            selected_piece: e.target.value
        })
    }

    render() {
        if (this.props.loggedIn) {
            const gameBoard = this.props.gameState.board.map((game, row_index) =>
                <li className={'game-row'}>
                    {game.map((piece, column_index) =>
                        <button
                            id={row_index.toString() + "," + column_index.toString()}
                            value={row_index.toString() + "," + column_index.toString()}
                            className={"game-buttons"}
                            onClick={() => {
                                this.props.postExample("action=move_piece&gameID=1234&username=dummy_user&password=iforgot123&row=" + row_index + "&column=" + column_index)
                            }}
                            // onClick={this.props.postExample()}
                        >
                            {piece}
                        </button>
                    )}</li>
            );

            return (
                <div className={'Board'}>
                    <p>Put the actual board here for game ({this.props.selectedGame})</p>
                    <ul className={"board-ul"}>
                        {gameBoard}
                    </ul>
                    <img src={this.state.background_src} alt={"board Image"}/>
                </div>
            )
        }
        else {
            return("");
        }
    }
}

class History extends React.Component {
    constructor(props) {
        super(props);
    }
    render() {
        return (
            <div className={'HistoryPage'}>
                <button className='button' onClick={this.props.postExample}>
                    Click Me
                </button>
            </div>
        )
    }
}

class GameRules extends React.Component {
    render() {
        return (
            <div className={'GaneRules'}>
                <p>Rules of the Game of Jungle</p>

                <p>Each player has eight pieces, different animals, with different degrees of power. Here are the pieces, their English names, and their relative powers, indicated by number:</p>

                <p>the pieces of dou shou qi and their powers (the jungle game or Chinese animal chess)</p>
                <p>The Animals depicted on the pieces shown here are very interesting abstractions. Your set may look like this, or may have the same animals depicted in a different style — and the same animals may be depicted differently on the board as well. You may want to pencil the pieces’ value numbers, 1 through 8, on the underside of each piece, or keep this chart on hand, to help you become acquainted with the pieces as you play.</p>

                <p>Object of the Game</p>

                <p> To win the game, one player must successfully move any animal into the Den of the opponent. (see Den in the diagram above</p>

                <p>Movement of the Pieces</p>

                <p>The black (or darker) pieces have the first move. All pieces have the same basic move, although some have special powers (described below). The basic move is just one space either forward, backward, left or right. The pieces never move diagonally.</p>

                <p>Captures</p>

                <p>An animal is captured (or “eaten”) by an opposing animal moving onto its square, as in chess or Stratego. But the attacking animal must be of equal or higher power than the one being captured. For instance, the Tiger (6) can capture the Tiger (6), Leopard (5) or Dog (4), but the Dog can not capture the Leopard or Tiger.</p>

                <p>Special Powers</p>

                <p>1) The Rat, although it is the least powerful piece, has the power to capture the Elephant. The Elephant can not capture the Rat. It is said that this is because the rat can creep in under the Elephant’s ear and eat his brain (!).</p>

                <p>2) The Rat, and no other animal, can move freely in the water. It can not, however, attack the Elephant from the water.</p>

                <p>3) Both the Lion and the Tiger can jump over the water, moving from one bank straight forward, backward, left or right (like a rook in chess) to the first square of dry land on the other side. They may capture in this move as well. The Lion and Tiger may not, however, jump over a rat if it is in the way, in the water.</p>

                <p> The Traps</p>

                <p>Each side has three Trap squares surrounding its Den. A player may move on and off of his own Trap squares with no effect. If, however, a player moves onto the opponent’s trap square, that piece loses all of its power, and may be captured by any of the defending pieces.</p>

                <p>The Den</p>

                <p> Animals are not allowed to move into their own Dens. When an animal moves into the opponent’s Den, it has won the game.</p>
            </div>
        )
    }
}

class Invite extends React.Component {

    constructor(props){
        super(props);
        this.state = {invitedPlayer: "", apiConfig: this.props.apiConfig}
        this.handleInvitedPlayerChange = this.handleInvitedPlayerChange.bind(this);
        this.handleSendInvite = this.handleSendInvite.bind(this);
        this.handleAcceptInvite = this.handleAcceptInvite.bind(this);
        this.handleDeclineInvite = this.handleDeclineInvite.bind(this);
    }

    render() {
    console.log(this.props.invites);
        if(typeof this.props.invites !== "undefined"){    // gotta be a better way of doing this...
        var playerInvites = this.props.invites.split(",");
        playerInvites.pop();
        console.log(playerInvites);
        var inviteButtons = playerInvites.map((elem) => <div style={{display:'block'}}>{elem}<button onClick={
            this.handleAcceptInvite.bind(this, elem)}>Accept</button>
            <button onClick={this.handleDeclineInvite.bind(this, elem)}>Decline</button></div>);
        console.log(inviteButtons);
        }

        return (
            <div className={'InvitePage'}>
                <form onSubmit={(e) => this.handleSendInvite(e, this.state.apiConfig.url,
                            "action=send_invite&playerOne=" + this.props.username + "&playerTwo=" + this.state.invitedPlayer,
                            this.props.apiConfig.headers)}>
                                <label>
                                    Username of player to invite:
                                    <input type="text" value={this.state.invitedPlayer} onChange={this.handleInvitedPlayerChange} />
                                </label>
                                <input type="submit" value="Submit" />
                            </form>
                            {inviteButtons}

            </div>


        )
    }

    handleInvitedPlayerChange(event) {
            event.preventDefault();
            this.setState({invitedPlayer: event.target.value});
        }
    async handleSendInvite(event, url, payload, headers){
            event.preventDefault();
            this.props.handleSendInvite(event, url, payload, headers)
            .then(response => this.props.updateInvites(response.invites));

        }
    async handleAcceptInvite(playerOne){
            console.log("playerOne is: " + playerOne);
            this.props.handleAcceptInvite(this.state.apiConfig.url, "action=accept_invite&playerOne=" + playerOne + "&playerTwo=" + this.props.username, this.props.apiConfig.headers)
            .then(response => this.props.updateInvites(response.invites));


        }
    async handleDeclineInvite(playerOne){

            this.props.handleAcceptInvite(this.state.apiConfig.url, "action=decline_invite&playerOne=" + playerOne + "&playerTwo=" + this.props.username, this.props.apiConfig.headers)
            .then(response => this.props.updateInvites(response.invites));

        }
}

class Login extends React.Component {
    constructor(props) {
        super(props);
        this.state = {username: '', password: '', apiConfig: this.props.apiConfig, loggedIn: this.props.loggedIn};
        console.log(this.state);
        console.log(this.props);

        this.handleNameChange = this.handleNameChange.bind(this);
        this.handlePasswordChange = this.handlePasswordChange.bind(this);
        this.handleSubmit = this.handleLoginSubmit.bind(this);
    }

    handleNameChange(event) {
        event.preventDefault();
        this.setState({username: event.target.value});
    }

    handlePasswordChange(event) {
        event.preventDefault();
        this.setState({password: event.target.value});
    }

    async handleLoginSubmit(event, url, payload, headers){
        event.preventDefault();
        this.props.handleLoginRequest(event, url, payload, headers)
        .then(response => this.props.updateLoginValue(response.loggedIn, response.username, response.invites));

    }


    render() {
        return (
            <form onSubmit={(e) => this.handleSubmit(e, this.state.apiConfig.url,
            "action=login&username=" + this.state.username + "&password=" + this.state.password,
            this.props.apiConfig.headers)}>
                <label>
                    Username:
                    <input type="text" value={this.state.username} onChange={this.handleNameChange} />
                    Password:
                    <input type="text" type="password" value={this.state.password} onChange={this.handlePasswordChange} />
                    <input type="text" value="login" hidden />
                </label>
                <input type="submit" value="Submit"/>
            </form>
        );
    }
}

class Register extends React.Component {
    constructor(props) {
        super(props);
        this.state = {username: '', password: '', users: this.props.users, passwords: this.props.passwords};

        this.handleNameChange = this.handleNameChange.bind(this);
        this.handlePasswordChange = this.handlePasswordChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleNameChange(event) {
        this.setState({username: event.target.value});
    }

    handlePasswordChange(event) {
        this.setState({password: event.target.value});
    }

    handleSubmit(event) {
        event.preventDefault();
        let users = this.state.users;
        let passwords = this.state.passwords;
        this.state.users.add(this.state.username);
        this.state.passwords.add(this.state.password);
        event.preventDefault();
    }

    render() {
        return (
            <form onSubmit={this.handleSubmit}>
                <label>
                    Name:
                    <input type="text" value={this.state.username} onChange={this.handleNameChange} />
                    Password:
                    <input type="text" value={this.state.password} onChange={this.handlePasswordChange} />
                </label>
                <input type="submit" value="Submit" />
            </form>
        );
    }
}

class User extends React.Component {
    render() {
        return (
            <div className={'UserPage'}>
                <p>User Goes here</p>
            </div>
        )
    }
}