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

    //handles appropriately changing the state of the page whenever the username is changed
    handleNameChange(event) {
        event.preventDefault();
        this.setState({username: event.target.value});
    }

    //handles appropriately changing the state of the page whenever the password is changed
    handlePasswordChange(event) {
        event.preventDefault();
        this.setState({password: event.target.value});
    }

    //Handles the submission of a login request to the server
    async handleLoginSubmit(event, url, payload, headers){
        event.preventDefault();
        this.props.handleGeneralRequest(event, url, payload, headers)
            .then(response => this.props.updateLoginValue(response.loggedIn, response.username, response.password, response.incomingInvites, response.outgoingInvites));
    }

    //Input: Previous props state
    //Automatically compares against the previous props state and runs a function if props changes
    componentDidUpdate(prevProps) {
        if (prevProps !== this.props) {
            this.failedLogin();
        }
    }

    //sets the local state if a login fails
    failedLogin(){
        this.setState({loginFail: true});
    }

    //Returns the appropriate error message based on the state. Default has no error.
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