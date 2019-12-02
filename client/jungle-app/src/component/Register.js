import React from 'react';
class Register extends React.Component {
    constructor(props) {
        super(props);
        this.state = {username: '', password: ''};

        this.handleNameChange = this.handleNameChange.bind(this);
        this.handlePasswordChange = this.handlePasswordChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleNameChange(event) {
        this.setState({username: event.target.value});
    }

    handlePasswordChange(event) {
        this.setState({password: event.target.value});
    }

    async handleSubmit(event, url, payload, headers) {
        event.preventDefault();
        this.props.handleGeneralRequest(event, url, payload, headers)
    }

    render() {
        return (
            <form onSubmit={(e) => this.handleSubmit(e, this.props.apiConfig.url,
                "action=Register&username=" + this.state.username + "&password=" + this.state.password,
                this.props.apiConfig.headers)}>
                <label>
                    Name:
                    <input type="text" value={this.state.username} onChange={this.handleNameChange} />
                    Password:
                    <input type="password" value={this.state.password} onChange={this.handlePasswordChange} />
                    <input type="text" hidden />
                </label>
                <input type="submit" value="Submit" />
            </form>
        );
    }
}
export default Register;