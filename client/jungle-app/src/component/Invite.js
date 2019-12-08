import React from 'react';
class Invite extends React.Component {

    constructor(props){
        super(props);
        this.state = {
            invitedPlayer: "",
            apiConfig:
            this.props.apiConfig,
            inviteSuccess: null
        };
        this.handleInvitedPlayerChange = this.handleInvitedPlayerChange.bind(this);
        this.handleGeneralRequest = this.handleGeneralRequest.bind(this);
        this.handleAcceptInvite = this.handleAcceptInvite.bind(this);
        this.handleDeclineInvite = this.handleDeclineInvite.bind(this);
    }

    render() {
        if(typeof this.props.incomingInvites !== "undefined"){    // gotta be a better way of doing this...
            var incomingInvites = this.props.incomingInvites.split(",");
            incomingInvites.pop();
            var incomingInviteButtons = incomingInvites.map((elem) => <div style={{display:'block'}}>{elem}<button onClick={
                this.handleAcceptInvite.bind(this, elem)}>Accept</button>
                <button onClick={this.handleDeclineInvite.bind(this, elem)}>Decline</button></div>);
        }
        if(typeof this.props.outgoingInvites !== "undefined"){    // gotta be a better way of doing this...
            var outgoingInvites = this.props.outgoingInvites.split(",");
            outgoingInvites.pop();
            var outgoingInviteButtons = outgoingInvites.map((elem) => <div style={{display:'block'}}>{elem}<button onClick={
                this.handleCancelInvite.bind(this, elem)}>Cancel</button></div>);
        }

        let successMessage;
        if (this.state.inviteSuccess === false)
        {successMessage = <p style={{color:"red"}}>Invite was unsuccessful!</p>}
        else if (this.state.inviteSuccess === true)
        {successMessage = <p style={{color:"green"}}>Invite was successful!</p>}

        return (
            <div className={'InvitePage'}>
                <form onSubmit={(e) => this.handleGeneralRequest(e, this.state.apiConfig.url,
                    "action=SendInvite&playerOne=" + this.props.username + "&username=" + this.props.username + "&password=" + this.props.password + "&playerTwo=" + this.state.invitedPlayer,
                    this.props.apiConfig.headers)}>
                    <label>
                        Username of player to invite:
                        <input type="text" value={this.state.invitedPlayer} onChange={this.handleInvitedPlayerChange} />
                    </label>
                    <input type="submit" value="Submit" />
                </form>
                {successMessage}
                <h2> Outgoing Invites </h2>
                {outgoingInviteButtons}
                <h2> Incoming Invites </h2>
                {incomingInviteButtons}
            </div>


        )
    }
    async componentDidMount(event) {
        var self = this;
        var resp = await this.props.handleUpdateInvites(this.state.apiConfig.url, "action=Login&username=" +
        this.props.username + "&password=" + this.props.password, this.props.apiConfig.headers)
        .then(function(response){
            self.props.updateIncomingInvites(response.incomingInvites);
            self.props.updateOutgoingInvites(response.outgoingInvites);
        })
    }

    handleInvitedPlayerChange(event) {
        event.preventDefault();
        this.setState({invitedPlayer: event.target.value});
    }


    async handleGeneralRequest(event=null, url, payload, headers){
        var self = this;
        event.preventDefault();
        var resp = await this.props.handleGeneralRequest(event, url, payload, headers)
            .then(function(response) {
                    self.setState({
                        inviteSuccess: response.wasSuccessful,
                    });
                self.props.updateIncomingInvites(response.incomingInvites);
                self.props.updateOutgoingInvites(response.outgoingInvites);
                }
            );

    }


    async handleAcceptInvite(playerOne){
        console.log("playerOne is: " + playerOne);
        this.props.handleAcceptInvite(this.state.apiConfig.url, "action=AcceptInvite&playerOne=" + playerOne + "&username=" + this.props.username + "&password=" + this.props.password + "&playerTwo=" + this.props.username, this.props.apiConfig.headers)
            .then(response => this.props.updateIncomingInvites(response.incomingInvites));


    }
    async handleDeclineInvite(playerOne){

        this.props.handleAcceptInvite(this.state.apiConfig.url, "action=DeclineInvite&playerOne=" + playerOne + "&username=" + this.props.username + "&password=" + this.props.password + "&playerTwo=" + this.props.username, this.props.apiConfig.headers)
            .then(response => this.props.updateIncomingInvites(response.incomingInvites));

    }
    async handleCancelInvite(playerTwo){

        this.props.handleAcceptInvite(this.state.apiConfig.url, "action=CancelInvite&playerOne=" + this.props.username + "&username=" + this.props.username + "&password=" + this.props.password + "&playerTwo=" + playerTwo, this.props.apiConfig.headers)
            .then(response => this.props.updateOutgoingInvites(response.outgoingInvites));

    }
}

export default Invite;