import React from 'react';

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
        this.props.handleGeneralRequest(event, url, payload, headers)
            .then(response => this.props.updateLoginValue(response.loggedIn, response.username, response.invites));

    }


    render() {
        return (
            <form onSubmit={(e) => this.handleSubmit(e, this.state.apiConfig.url,
                "action=Login&username=" + this.state.username + "&password=" + this.state.password,
                this.props.apiConfig.headers)}>
                <label>
                    Username:
                    <input type="text" value={this.state.username} onChange={this.handleNameChange} />
                    Password:
                    <input type="password" value={this.state.password} onChange={this.handlePasswordChange} />
                    <input type="text" value="login" hidden />
                </label>
                <input type="submit" value="Submit"/>
            </form>
        );
    }
}

export default Login;