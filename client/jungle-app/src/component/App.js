import React from "react";
import {Tabs, TabList, Tab, TabPanel} from 'react-tabs';
import 'bootstrap/dist/css/bootstrap.min.css';
import axios from 'axios';

// Custom Components
import Games from './Games';
import Home from './Home';
import GameRules from './GameRules';
import Invite from './Invite';
import Board from './Board';
import History from "./History";
import Login from "./Login";
import Register from "./Register";
import User from './User';

export default class App extends React.Component {

    constructor(props) {
        super(props);
        this.updateLoginValue = this.updateLoginValue.bind(this);
        this.updateInvites = this.updateInvites.bind(this);
        this.setSelectedGame = this.setSelectedGame.bind(this);
        this.postExample = this.postExample.bind(this);
    }

    state = {
        loggedIn: false,
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
        apiConfig: {
            url: 'http://localhost:8080',
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
                availableMovesProto = availableMovesProto.substring(0, availableMovesProto.length - 2);
                availableMovesProto = availableMovesProto.split(",|");
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
                boardStateProto = boardStateProto.substring(0, boardStateProto.length - 2);
                boardStateProto = boardStateProto.split(",|");
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
        // let board = this.state.gameState.board;
        console.log(this.state.gameState);
    }

    async handleGeneralRequest(event, url, payload, headers) {
        let resp = await axios.post(url,
            payload,
            headers,
        )
            .then(function (response) {
                    console.log(response);
                    return response;
                }
            )
            .catch();
        return resp.data;
    }

    async handleAcceptInvite(url, payload, headers) {
        var resp = await axios.post(url,
            payload,
            headers,
        )
            .then(function (response) {
                    console.log(response);
                    return response;
                }
            )
            .catch();
        return resp.data;
    }

    async handleDeclineInvite(url, payload, headers) {
        var resp = await axios.post(url,
            payload,
            headers,
        )
            .then(function (response) {
                    console.log(response);
                    return response;
                }
            )
            .catch();
        return resp.data;
    }

    updateLoginValue(loginVal, username, invites) {
        console.log(username);
        console.log(invites);
        this.setState({
            loggedIn: loginVal,
            username: username,
            invites: invites,
        });
    }

    updateInvites(invites) {
        this.setState({
            invites: invites
        });
    }

    setGameState(gameId) {
        console.log(gameId);
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
            {
                headers: {
                    'Content-Type': 'application/json',
                    'Access-Control-Allow-Methods': 'GET,PUT,POST,DELETE,PATCH',
                }
            }
        ).then(function (response) {
            // const loggedIn = response.data.loggedIn;
            self.setState({
                loggedIn: response.data
            })
        })
    }

    render() {
        if (this.state.loggedIn) {
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
                        <TabPanel><Invite apiConfig={this.state.apiConfig} handleGeneralRequest={this.handleGeneralRequest}
                                          handleAcceptInvite={this.handleAcceptInvite}
                                          invites={this.state.invites} username={this.state.username}
                                          handleDeclineInvite={this.handleDeclineInvite}
                                          updateInvites={this.updateInvites}/></TabPanel>
                        <TabPanel><Register users={this.state.users} passwords={this.state.passwords}/></TabPanel>
                        <TabPanel><Login apiConfig={this.state.apiConfig} handleGeneralRequest={this.handleGeneralRequest}
                                         loggedIn={this.state.loggedIn}
                                         updateLoginValue={this.updateLoginValue}/></TabPanel>
                        <TabPanel><User/></TabPanel>
                    </Tabs>
                </div>
            )
        } else {
            return (
                <div className='menu navigation-menu'>
                    <Tabs>
                        <TabList>
                            <Tab>Register</Tab>
                            <Tab>Login</Tab>
                        </TabList>
                        <TabPanel><Register users={this.state.users} passwords={this.state.passwords}/></TabPanel>
                        <TabPanel>

                            <Login apiConfig={this.state.apiConfig} handleGeneralRequest={this.handleGeneralRequest}
                                         loggedIn={this.state.loggedIn}
                                         updateLoginValue={this.updateLoginValue}/>
                        </TabPanel>

                    </Tabs>
                </div>
            )
        }
    }
}
