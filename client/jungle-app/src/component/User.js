import React from 'react';
import ToastContext from "react-bootstrap/cjs/ToastContext";
import ButtonGroup from "react-bootstrap/ButtonGroup";
import Button from "react-bootstrap/Button";
import Alert from "react-bootstrap/Alert";

class User extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            show: true,
            unregisterSuccess: null,
        };
        this.handleUnregistrationSubmit = this.handleUnregistrationSubmit.bind(this);
    }

    setShow(bool) {
        this.setState({
            show: bool
        });
    }

    async handleUnregistrationSubmit(event, url, payload, headers) {
        var self = this;
        event.preventDefault();
        this.props.handleGeneralRequest(event, url, payload, headers)
            .then(response => this.props.updateLoginValue(false, "", "", "")
            )
    }


    render() {
        let alert;
        let successMessage;
        if (this.state.unregisterSuccess === false)
        {
            successMessage = <p style={{color:"red"}}>Registration was unsuccessful!</p>
        }
        else if (this.state.unregisterSuccess === true)
        {
            successMessage = <p style={{color:"green"}}>Registration was successful!</p>
        }

        if (this.state.show) {
            alert =
                <Alert variant="dark" onClose={() => this.setShow(false)} dismissible>
                    <Alert.Heading>Welcome {this.props.username}!</Alert.Heading>
                    <p>I hope you've been enjoying our game.</p>
                    <hr/>
                    <p>Although we'd love for you to stick around, you can manage your account here if you'd like to logout or unregister.</p>
                </Alert>
        }
        return (

            <div className={'UserPage'}>
                {alert}
                <ButtonGroup aria-label="Basic example">
                    <Button
                        variant="secondary"
                        onClick={() => {
                            this.props.Logout()
                        }}>Logout</Button>
                    <Button variant="secondary" onClick={(e) => this.handleUnregistrationSubmit(e, this.props.apiConfig.url,
                        "action=Unregister&username=" + this.props.username + "&password=" + this.props.password + "&email=" + this.props.email,
                        this.props.apiConfig.headers)}>Unregister</Button>
                </ButtonGroup>



            </div>
        )
    }
}
export default User;