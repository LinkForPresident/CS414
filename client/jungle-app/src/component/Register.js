import React from 'react';
class Register extends React.Component {
    constructor(props) {
        super(props);
        this.state = {username: '', password: '', users: this.props.users, passwords: this.props.passwords};

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

    handleSubmit(event) {
        event.preventDefault();
        // let users = this.state.users;
        // let passwords = this.state.passwords;
        this.state.users.add(this.state.username);
        this.state.passwords.add(this.state.password);
        event.preventDefault();
    }

    render() {
        return (
            <form onSubmit={this.handleSubmit}>
                <label>
                    Name:
                    <input type="text" value={this.state.username} onChange={this.handleNameChange} />
                    Password:
                    <input type="password" value={this.state.password} onChange={this.handlePasswordChange} />
                </label>
                <input type="submit" value="Submit" />
            </form>
        );
    }
}
export default Register;