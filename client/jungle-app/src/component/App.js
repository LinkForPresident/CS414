
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
        selectedGame: null,
        boardState: {

        },
        apiConfig:{
            url:'http://129.82.44.122:8080',
            payload: "action=login&username=dummy_user&password=iforgot123",
            headers: {
                'Content-Type': 'application/text',
                'Access-Control-Allow-Methods': 'GET,PUT,POST,DELETE,PATCH,OPTIONS',
            }
        }
    };

    postExample () {
        console.log("asdf");
        axios.post('http://129.82.44.122:8080',
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
        console.log("asdf");
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
                        <Tab>Game</Tab>
                        <Tab>History</Tab>
                        <Tab>Invite</Tab>
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
                    <TabPanel><History postExample={this.postExample.bind(this)}/></TabPanel>
                    <TabPanel><Invite/></TabPanel>
                    <TabPanel><Login/></TabPanel>
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
        this.state = {isToggleOn: true};
        this.handleClick = this.handleClick.bind(this)
    }

    handleClick() {
        this.setState(state => ({
            isToggleOn: !state.isToggleOn
        }));
    }

    render() {
        return (
            <div className={'LoginPage'}>
                <Button variant="primary" onClick={this.handleClick}>Login Placeholder</Button>
                <p>{this.state.isToggleOn ? 'ON' : 'OFF'}</p>
            </div>
        )
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