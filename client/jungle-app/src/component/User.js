import React from 'react';
import ToastContext from "react-bootstrap/cjs/ToastContext";
import ButtonGroup from "react-bootstrap/ButtonGroup";
import Button from "react-bootstrap/Button";
import Alert from "react-bootstrap/Alert";

class User extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            show: true
        };

    }

    setShow(bool) {
        this.setState({
            show: bool
        });
    }

    render() {
        let alert;
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
                    <Button variant="secondary">Unregister (Unimplemented)</Button>
                </ButtonGroup>



            </div>
        )
    }
}
export default User;