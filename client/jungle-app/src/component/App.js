
import React from "react";
import {Tabs, TabList, Tab, TabPanel} from 'react-tabs';
import {Button} from 'react-bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css';

export default class App extends React.Component {
    state = {
        loggedIn: true,
        activeGames : ["0001", "0002", "0005"],
        completedGames: ["0003", "0004"],
    };

    // handleClick = buttonName => {
    //
    // };

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
                    <TabPanel><Games activeGames={this.state.activeGames} completedGames={this.state.completedGames}/></TabPanel>
                    <TabPanel><History/></TabPanel>
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
            <li className={'list-group-item list-group-item-dark'}><Button>Game Id: {game}</Button> request json</li>);
        const completedGamesList = this.state.completedGames.map((game) =>
            <li className={'list-group-item list-group-item-dark'}><Button>Game Id: {game}</Button> request json</li>);
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

                <Board gameId={this.state.selectedGame} />
            </div>
        )
    }
}


class Board extends Games {
    constructor(props) {
        super(props);
        this.state = {
            gameId: this.props.gameId,
        }
    }
    render() {
        return(
            <div className={'Board'}>
                <p>Put the actual board here for game ({this.state.gameId})</p>
            </div>
        )
    }
}

class History extends React.Component {
    render() {
        return (
            <div className={'HistoryPage'}>
                <p>History Goes here</p>
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