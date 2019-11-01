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

    // state of components shared between components must live here, that way when one is updated the other will "react"
    // to the changes.
    state = {
        loggedIn: false,
        username: null,
        password: null,
        activeGames: ["0001", "0002", "0005"],
        completedGames: ["0003", "0004"],
        selectedGame: null,
        invites: " ",
        gameState:
            {
                "gameID": "1234",
                "playerOne": "Bob",
                "playerTwo": "Sally",
                "turn": "Bob",
                "turnNumber": 0,
                "board": [
                    [
                        {"environment": null, "piece": "r7", "available": false},
                        {"environment": null, "piece": null, "available": false},
                        {"environment": "trap", "piece": null, "available": false},
                        {"environment": "den", "piece": null, "available": false},
                        {"environment": "trap", "piece": null, "available": false},
                        {"environment": null," piece": null, "available": false},
                        {"environment": null," piece": "r6", "available": false}
                    ],
                    [
                        {"environment": null, "piece": null, "available": false},
                        {"environment": null, "piece": "r4", "available": false},
                        {"environment": null, "piece": null, "available": false},
                        {"environment": "trap", "piece": null, "available": false},
                        {"environment": null, "piece": null, "available": false},
                        {"environment": null, "piece": "r2", "available": false},
                        {"environment": null, "piece": null, "available": false}
                    ],
                    [
                        {"environment": null, "piece": "r1", "available": false},
                        {"environment": null, "piece": null, "available": false},
                        {"environment": null, "piece": "r5", "available": false},
                        {"environment": null, "piece": null, "available": false},
                        {"environment": null, "piece": "r3", "available": false},
                        {"environment": null, "piece": null, "available": false},
                        {"environment": null, "piece": "r8", "available": false}
                    ],
                    [
                        {"environment": null, "piece": null, "available": false},
                        {"environment": "water", "piece": null, "available": false},
                        {"environment": "water", "piece": null, "available": false},
                        {"environment": null, "piece": null, "available": false},
                        {"environment": "water", "piece": null, "available": false},
                        {"environment": "water", "piece": null, "available": false},
                        {"environment": null, "piece": null, "available": false}
                    ],
                    [
                        {"environment": null, "piece": null, "available": false},
                        {"environment": "water", "piece": null, "available": false},
                        {"environment": "water", "piece": null, "available": false},
                        {"environment": null, "piece": null, "available": false},
                        {"environment": "water", "piece": null, "available": false},
                        {"environment": "water", "piece": null, "available": false},
                        {"environment": null, "piece": null, "available": false}
                    ],
                    [
                        {"environment": null, "piece": null, "available": false},
                        {"environment": "water", "piece": null, "available": false},
                        {"environment": "water", "piece": null, "available": false},
                        {"environment": null, "piece": null, "available": false},
                        {"environment": "water", "piece": null, "available": false},
                        {"environment": "water", "piece": null, "available": false},
                        {"environment": null, "piece": null, "available": false}
                    ],
                    [
                        {"environment": null, "piece": "b8", "available": false},
                        {"environment": null, "piece": null, "available": true},
                        {"environment": null, "piece": "b3", "available": false},
                        {"environment": null, "piece": null, "available": false},
                        {"environment": null, "piece": "b5", "available": false},
                        {"environment": null, "piece": null, "available": false},
                        {"environment": null, "piece": "b1", "available": false}
                    ],
                    [
                        {"environment": null, "piece": null, "available": true},
                        {"environment": null, "piece": "b2", "available": true},
                        {"environment": null, "piece": null, "available": true},
                        {"environment": "trap", "piece": null, "available": false},
                        {"environment": null, "piece": null, "available": false},
                        {"environment": null, "piece": "b4", "available": false},
                        {"environment": null, "piece": null, "available": false}
                    ],
                    [
                        {"environment": null, "piece": "b6", "available": false},
                        {"environment": null, "piece": null, "available": true},
                        {"environment": "trap", "piece": null, "available": false},
                        {"environment": "den", "piece": null, "available": false},
                        {"environment": "trap", "piece": null, "available": false},
                        {"environment": null, "piece": null, "available": false},
                        {"environment": null, "piece": "b7", "available": false}
                    ]
                ],
                "winner": "",
                "startTime": "",
                "endTime": ""
            },

        // common rest request configuration
        apiConfig:{
            url:'http://129.82.44.122:8081',
            headers: {
                'Content-Type': 'application/text',
                'Access-Control-Allow-Methods': 'GET,PUT,POST,DELETE,PATCH,OPTIONS',
            }
        }
    };

    // This method is responsible for making game state requests, and updating the gameState fields when users click the game board
    postExample(data) {
        var self = this;
        console.log("Making request");
        axios.post(this.state.apiConfig.url,
            data,
            {
                headers: {
                    'Content-Type': 'application/json',
                    'Access-Control-Allow-Methods': 'GET,PUT,POST,DELETE,PATCH',
                }
            })
            .then(function (response) {
                console.log(response.data);
                console.log("Setting game state");
                self.setState({
                    gameState: response.data
                })
            })
            .catch()
    }

    // This is a general request that will take arguments in a payload, and return the data contained in the server response.
    // Ideally this should be called from a seperate method that updates the state with the response from this.
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

    // This is repeated code that needs to be refactored to use the "HandleGeneralRequest", which is functionally identical.
    async handleAcceptInvite(url, payload, headers) {
        console.log("URL: " +url);
        console.log("Payload: " +payload);
        console.log("header: " +headers);
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

    // This is repeated code that needs to be refactored to use the "HandleGeneralRequest", which is functionally identical.
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

    updateLoginValue(loginVal, username, password, invites) {
        console.log(username);
        console.log(invites);
        this.setState({
            loggedIn: loginVal,
            username: username,
            password: password,
            invites: invites,
        });
    }

    updateInvites(invites) {
        this.setState({
            invites: invites
        });
    }

    // It appears this method never sets the game state. What was expected here? There is already another method to set the game state...
    setGameState(gameId) {
        console.log(gameId);
        console.log("calling api for game state...");
        axios.post(
            this.state.apiConfig.url,
            "action=ViewGame&gameID=1234",
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

    // handleLogin(username, password) {
    //     var self = this;
    //     console.log("calling api for game state...");
    //     axios.post(
    //
    //         'http://localhost:8080',
    //
    //         "action=Login&username=" + username + "&password=" + password,
    //         {
    //             headers: {
    //                 'Content-Type': 'application/json',
    //                 'Access-Control-Allow-Methods': 'GET,PUT,POST,DELETE,PATCH',
    //             }
    //         }
    //     ).then(function (response) {
    //         // const loggedIn = response.data.loggedIn;
    //         self.setState({
    //             loggedIn: response.data
    //         })
    //     })
    // }

    render() {
        // This section should be reserved for rendering pagewide components, complex code should be factored into child components, or new components.
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
                                   username={this.state.username}
                                   password={this.state.password}
                            />
                        </TabPanel>
                        <TabPanel><GameRules/></TabPanel>
                        <TabPanel><History /></TabPanel>
                        <TabPanel><Invite apiConfig={this.state.apiConfig} handleGeneralRequest={this.handleGeneralRequest}
                                          handleAcceptInvite={this.handleAcceptInvite}
                                          invites={this.state.invites}
                                          username={this.state.username}
                                          password={this.state.password}
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
            // Tabs that show up if user is not logged in.

            return (
                <div className='menu navigation-menu'>
                    <Tabs>
                        <TabList>
                            <Tab>Login</Tab>
                            <Tab>Register</Tab>
                        </TabList>
                        <TabPanel>
                            <Login apiConfig={this.state.apiConfig} handleGeneralRequest={this.handleGeneralRequest}
                                         loggedIn={this.state.loggedIn}
                                         updateLoginValue={this.updateLoginValue}/>
                        </TabPanel>
                        <TabPanel><Register users={this.state.users} passwords={this.state.passwords}/></TabPanel>

                    </Tabs>
                </div>
            )
        }
    }
}
