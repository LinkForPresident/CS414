
import React from "react";
import {Tabs, TabList, Tab, TabPanel} from 'react-tabs';
import {Button} from 'react-bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css';
import axios from 'axios';
export default class App extends React.Component {
    state = {
        loggedIn: false,
        activeGames : ["0001", "0002", "0005"],
        completedGames: ["0003", "0004"],
        users : ['Brian', 'Dave'],
        passwords : ['Crane', 'Wells'],
        selectedGame: null,
        boardState: {

        },
        apiConfig:{
            url:'localhost:8080',
            payload: "action=login&username=dummy_user&password=iforgot123",
            headers: {
                'Content-Type': 'application/text',
                'Access-Control-Allow-Methods': 'GET,PUT,POST,DELETE,PATCH,OPTIONS',
            }
        }
    };

    postExample () {
        console.log("tests");
        axios.post('localhost:8080',
            "action=login&username=dummy_user&password=iforgot123",
            {
                headers: {
                    'Content-Type': 'application/text',
                    'Access-Control-Allow-Methods': 'GET,PUT,POST,DELETE,PATCH,OPTIONS',
                }
            })
            .then(response => console.log(response))
            .catch()
    }


    postExampleNew () {
        console.log("fdsa");
        axios.post(this.state.apiConfig.url,
            this.state.apiConfig.payload,
            {headers: this.state.apiConfig.headers})
            .then(response => console.log(response))
            .catch()
    }



    setSelectedGame(e) {
        this.setState({
            selectedGame: e.target.value
        });
    }

    render() {
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
                            setSelectedGame={this.setSelectedGame.bind(this)}/>
                        <Board selectedGame={this.state.selectedGame} />
                    </TabPanel>
                    <TabPanel><GameRules/></TabPanel>
                    <TabPanel><History postExample={this.postExample.bind(this)}/></TabPanel>
                    <TabPanel><Invite/></TabPanel>
                    <TabPanel><Register users = {this.state.users} passwords = {this.state.passwords}/></TabPanel>
                    <TabPanel><Login users = {this.state.users} passwords = {this.state.passwords}/></TabPanel>
                    <TabPanel><User/></TabPanel>
                </Tabs>
            </div>
        )
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
}


class Board extends Games {
    constructor(props) {
        super(props);
        this.state = {
            background_src: "./images/dou_shou_qi_jungle_game-board.jpg",
        };

    }
    render() {
        return(
            <div className={'Board'}>
                <p>Put the actual board here for game ({this.props.selectedGame})</p>
                <img src={this.state.background_src} alt={"board Image"} />
            </div>
        )
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
    render() {
        return (
            <div className={'InvitePage'}>
                <p>Invite Goes here</p>
            </div>
        )
    }
}

class Login extends React.Component {
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
        let i;
        let userMatchFound = false
        for(i = 0; i<users.length; i++) {
            if(this.state.username === users[i]) {
                userMatchFound = true
                if(this.state.password === passwords[i]) {
                    alert('Password matches for username: ' + users[i])
                }
                else {
                    alert('Password DOES NOT match for username: ' + users[i])
                }
            }
        }
        if(!userMatchFound) {
            alert('Username ' + this.state.username + ' not found in list of Registered Users! ')
        }
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