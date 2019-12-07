import React from 'react';
class Register extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            username: '',
            password: '',
            email: '',
            registerSuccess: null,
            unregisterSuccess: null
        };

        this.handleNameChange = this.handleNameChange.bind(this);
        this.handlePasswordChange = this.handlePasswordChange.bind(this);
        this.handleEmailChange = this.handleEmailChange.bind(this);
        this.handleRegistrationSubmit = this.handleRegistrationSubmit.bind(this);
    }

    handleNameChange(event) {
        this.setState({username: event.target.value});
    }

    handlePasswordChange(event) {
        this.setState({password: event.target.value});
    }

    handleEmailChange(event) {
        this.setState({email: event.target.value});
    }

    async handleRegistrationSubmit(event, url, payload, headers) {
        var self = this;
        event.preventDefault();
        this.props.handleGeneralRequest(event, url, payload, headers)
            .then(function(response) {
                self.setState({
                    registerSuccess: response.wasSuccessful
                })
                }
            )
    }


    render() {
        let successMessage;
        if (this.state.registerSuccess === false)
        {
            successMessage = <p style={{color:"red"}}>Registration was unsuccessful!</p>
        }
        else if (this.state.registerSuccess === true)
        {
            successMessage = <p style={{color:"green"}}>Registration was successful!</p>
        }

        return (
            <div>
                <form onSubmit={(e) => this.handleRegistrationSubmit(e, this.props.apiConfig.url,
                    "action=Register&username=" + this.state.username + "&password=" + this.state.password + "&email=" + this.state.email,
                    this.props.apiConfig.headers)}>
                    <label>
                        Name:
                        <input type="text" value={this.state.username} onChange={this.handleNameChange} />
                        Password:
                        <input type="password" value={this.state.password} onChange={this.handlePasswordChange} />
                        Email:
                        <input type="text" value={this.state.email} onChange={this.handleEmailChange} />
                        <input type="text" hidden />
                    </label>
                    <input type="submit" value="Submit" />
                </form>
                {successMessage}
            </div>
        );
    }
}
export default Register;