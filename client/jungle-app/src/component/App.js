
import React from "react";
import {Tabs, TabList, Tab, TabPanel} from 'react-tabs';
import { Button } from 'react-bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css';

export default class App extends React.Component {
    state = {
        total: null,
        next: null,
        operation: null,
    };

    // handleClick = buttonName => {
    //
    // };

    render() {
        return (
            <div className="component-app">
                <Header />
            </div>
        );
    }
}

class Header extends React.Component {

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
                    <TabPanel><Game/></TabPanel>
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

class Game extends React.Component {
    render() {
        return (
            <div className={'GamePage'}>
                <p>Game Goes here</p>
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