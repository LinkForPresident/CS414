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
        this.updateIncomingInvites = this.updateIncomingInvites.bind(this);
        this.updateOutgoingInvites = this.updateOutgoingInvites.bind(this);
        this.updateActiveGames = this.updateActiveGames.bind(this);
        this.updateCompletedGames = this.updateCompletedGames.bind(this);
        this.setSelectedGame = this.setSelectedGame.bind(this);
        this.handleForfeitGame = this.handleForfeitGame.bind(this);
        this.ViewGameState = this.ViewGameState.bind(this);
        this.postExample = this.postExample.bind(this);
        this.Logout = this.Logout.bind(this);
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
        incomingInvites: " ",
        outgoingInvites: " ",
        gameState:
            {

            },

        // common rest request configuration
        apiConfig:{
            url:'localhost:8080',
            headers: {
                'Content-Type': 'application/text',
                'Access-Control-Allow-Methods': 'GET,PUT,POST,DELETE,PATCH,OPTIONS',
            }
        }
    };

    Logout() {
        var self = this;
        axios.post(this.state.apiConfig.url,
            "action=Logout&username=" + this.state.username + "&password=" + this.state.password, {
            headers: {
                'Content-Type': 'application/text',
                'Access-Control-Allow-Methods': 'GET,PUT,POST,DELETE,PATCH,OPTIONS',
            }
        }
    ).then(
            function (response) {
                console.log(response.data);
                self.setState({
                    loggedIn: false,
                    username: null,
                    password: null,
                })
            }
        )
    }

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
                    console.log("General Request response: ");
                    console.log(response);
                    return response;
                }
            )
            .catch();
        return resp.data;
    }

    // This is repeated code that needs to be refactored to use the "HandleGeneralRequest", which is functionally identical.
        async handleUpdateInvites(url, payload, headers) {
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

    // This is repeated code that needs to be refactored to use the "HandleGeneralRequest", which is functionally identical.
    async handleForfeitGame(url, payload, headers) {
        var self=this;
        var resp = await axios.post(url,
            payload,
            headers,
        )
            .then(function (response) {
                console.log(response.data);
                    self.setState({
                        // gameState: response.data
                        gameState: {}
                    });
                }
            )
            .catch();
    }

    updateLoginValue(loginVal, username, password, incomingInvites, outgoingInvites) {
            this.setState({
                loggedIn: loginVal,
                username: username,
                password: password,
                incomingInvites: incomingInvites,
                outgoingInvites: outgoingInvites,
            });
    }

    updateIncomingInvites(incomingInvites) {
        console.log("Updating incoming invites");
        this.setState({
            incomingInvites: incomingInvites
        });
    }

    updateOutgoingInvites(outgoingInvites) {
        console.log("Updating outgoing invites");
        this.setState({
            outgoingInvites: outgoingInvites
        });
    }

    ViewGameState(gameId) {
        var self = this;
        console.log("calling api for game state...");
        axios.post(
            this.state.apiConfig.url,
            "action=ViewGame&gameID=" + gameId + "&username=" + this.state.username + "&password=" + this.state.password,
            {headers: this.state.apiConfig.headers}
        ).then(function (response) {
            console.log(response.data);
            console.log("Setting game state");
            self.setState({
                gameState: response.data
            })
        }).catch();
        console.log("Game State set.")
    }

    async getGames(url, payload, headers) {
        console.log("making request to the server for player games");
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

    updateActiveGames(games) {
        this.setState({
            activeGames: games
        });
    }

    updateCompletedGames(games) {
        this.setState({
            completedGames: games
        });
    }

    setSelectedGame(e) {
        console.log("setSelectedGame: " + e.target.value);
        this.setState({
            selectedGame: e.target.value
        });
        this.setState({
            gameID: e.target.value
        });


        this.ViewGameState(e.target.value);
    }

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
                            <Tab>User</Tab>
                        </TabList>
                        <TabPanel><Home/></TabPanel>

                        <TabPanel>
                            {this.state.gameState.gameID != null ? <h1 style={{textAlign: 'left'}}>#{this.state.gameState.gameID}: {this.state.username === this.state.gameState.playerOne ? <span className={"bluePlayer"}>{this.state.gameState.playerOne}</span>: <span className={"redPlayer"}>{this.state.gameState.playerTwo}</span> } VS {this.state.username === this.state.gameState.playerOne ? <span className={"redPlayer"}>{this.state.gameState.playerTwo}</span>: <span className={"bluePlayer"}>{this.state.gameState.playerOne}</span> } </h1>: <h1 style={{textAlign: 'left'}}>Select a Game</h1>}
                            <Games
                                activeGames={this.state.activeGames}
                                setSelectedGame={this.setSelectedGame}
                                loggedIn={this.state.loggedIn}
                                apiConfig={this.state.apiConfig}
                                handleGeneralRequest={this.handleGeneralRequest}
                                username={this.state.username}
                                password={this.state.password}
                                getGames={this.getGames}
                                updateActiveGames={this.updateActiveGames}
                            />
                            <Board selectedGame={this.state.selectedGame}
                                   gameState={this.state.gameState}
                                   postExample={this.postExample}
                                   loggedIn={this.state.loggedIn}
                                   username={this.state.username}
                                   ViewGameState={this.ViewGameState}
                                   password={this.state.password}
                                   handleForfeitGame={this.handleForfeitGame}
                                   selectedGame={this.state.selectedGame}
                                   updateActiveGames={this.updateActiveGames}
                                   apiConfig={this.state.apiConfig}
                                   getGames={this.getGames}
                            />
                        </TabPanel>
                        <TabPanel><GameRules/></TabPanel>
                        <TabPanel><History
                                completedGames={this.state.completedGames}
                                loggedIn={this.state.loggedIn}
                                apiConfig={this.state.apiConfig}
                                handleGeneralRequest={this.handleGeneralRequest}
                                username={this.state.username}
                                password={this.state.password}
                                getGames={this.getGames}
                                updateCompletedGames={this.updateCompletedGames}
                        /></TabPanel>
                        <TabPanel><Invite apiConfig={this.state.apiConfig}
                                            handleGeneralRequest={this.handleGeneralRequest}
                                            handleUpdateInvites={this.handleUpdateInvites}
                                            updateLoginValue={this.updateLoginValue}
                                          handleAcceptInvite={this.handleAcceptInvite}
                                          incomingInvites={this.state.incomingInvites}
                                          outgoingInvites={this.state.outgoingInvites}
                                          username={this.state.username}
                                          password={this.state.password}
                                          handleDeclineInvite={this.handleDeclineInvite}
                                          updateIncomingInvites={this.updateIncomingInvites}
                                          updateOutgoingInvites={this.updateOutgoingInvites}/></TabPanel>
                        <TabPanel>
                            <User
                                Logout={this.Logout}
                                username={this.state.username}
                                password={this.state.password}
                                apiConfig={this.state.apiConfig}
                                handleGeneralRequest={this.handleGeneralRequest}
                                loggedIn={this.state.loggedIn}
                                updateLoginValue={this.updateLoginValue}
                            />
                        </TabPanel>
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
                        <TabPanel><Register apiConfig={this.state.apiConfig} handleGeneralRequest={this.handleGeneralRequest}/></TabPanel>

                    </Tabs>
                </div>
            )
        }
    }
}
