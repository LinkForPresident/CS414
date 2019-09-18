
import React from "react";
import {Tabs, TabList, Tab, TabPanel} from 'react-tabs';

export default class App extends React.Component {
    state = {
        total: null,
        next: null,
        operation: null,
    };

    handleClick = buttonName => {

    };

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
            <div className='navigation'>
                <Tabs>
                    <TabList>
                        <Tab>Game</Tab>
                        <Tab>Home</Tab>
                        <Tab>Invite</Tab>
                        <Tab>Login</Tab>
                        <Tab>User</Tab>
                    </TabList>
                    <TabPanel><Game/></TabPanel>
                    <TabPanel><Home/></TabPanel>
                    <TabPanel><Invite/></TabPanel>
                    <TabPanel><Login/></TabPanel>
                    <TabPanel><User/></TabPanel>
                </Tabs>
            </div>
        )
    }
}

class Game extends React.Component {
    render() {
        return (
            <div className={'Game'}>
                <p>Game Goes here</p>
            </div>
        )
    }
}

class Home extends React.Component {
    render() {
        return (
            <div className={'Game'}>
                <p>Home Goes here</p>
            </div>
        )
    }
}

class Invite extends React.Component {
    render() {
        return (
            <div className={'Game'}>
                <p>Invite Goes here</p>
            </div>
        )
    }
}

class Login extends React.Component {
    render() {
        return (
            <div className={'Game'}>
                <p>Login Goes here</p>
            </div>
        )
    }
}

class User extends React.Component {
    render() {
        return (
            <div className={'Game'}>
                <p>User Goes here</p>
            </div>
        )
    }
}