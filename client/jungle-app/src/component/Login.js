import React from 'react';

class Login extends React.Component {
    constructor(props) {
        super(props);
        this.state = {username: '', password: '', apiConfig: this.props.apiConfig, loggedIn: this.props.loggedIn, loginFail: null};
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
        //console.log("test1");
        //const response = await fetch("http://localhost:8080"); //Await test, appears to never work
        //console.log("test2");
        //TODO: This is checked before server response. Should check after server response.
        // if(response.ok && this.props.loggedIn === false){
        //     console.log("test3");
        //     this.setState({loginFail: true});
        // }
    }

    componentDidUpdate(prevProps) {
        if (prevProps !== this.props) {
            this.failedLogin();
        }
    }

    failedLogin(){
        this.setState({loginFail: true});
    }

    loginError(){
        if (this.state.loginFail === true) {
            return "Error: Incorrect username or password";
        }
        return "";
    }

    render() {
        return (
            <div>
                <form onSubmit={(e) => this.handleSubmit(e, this.state.apiConfig.url,
                    "action=Login&username=" + this.state.username + "&password=" + this.state.password,
                    this.props.apiConfig.headers)}>
                    <label>
                        Username:
                        <input type="text" value={this.state.username} onChange={this.handleNameChange}/>
                        Password:
                        <input type="password" value={this.state.password} onChange={this.handlePasswordChange}/>
                        <input type="text" value="login" hidden/>
                    </label>
                    <input type="submit" value="Submit"/>
                </form>
                <p style={{color: "red"}}>{this.loginError()}</p>
            </div>
        );
    }
}

export default Login;