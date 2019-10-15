import React from 'react';
class Invite extends React.Component {

    constructor(props){
        super(props);
        this.state = {invitedPlayer: "", apiConfig: this.props.apiConfig};
        this.handleInvitedPlayerChange = this.handleInvitedPlayerChange.bind(this);
        this.handleGeneralRequest = this.handleGeneralRequest.bind(this);
        this.handleAcceptInvite = this.handleAcceptInvite.bind(this);
        this.handleDeclineInvite = this.handleDeclineInvite.bind(this);
    }

    render() {
        console.log(this.props.invites);
        if(typeof this.props.invites !== "undefined"){    // gotta be a better way of doing this...
            var playerInvites = this.props.invites.split(",");
            playerInvites.pop();
            console.log(playerInvites);
            var inviteButtons = playerInvites.map((elem) => <div style={{display:'block'}}>{elem}<button onClick={
                this.handleAcceptInvite.bind(this, elem)}>Accept</button>
                <button onClick={this.handleDeclineInvite.bind(this, elem)}>Decline</button></div>);
            console.log(inviteButtons);
        }

        return (
            <div className={'InvitePage'}>
                <form onSubmit={(e) => this.handleGeneralRequest(e, this.state.apiConfig.url,
                    "action=send_invite&playerOne=" + this.props.username + "&playerTwo=" + this.state.invitedPlayer,
                    this.props.apiConfig.headers)}>
                    <label>
                        Username of player to invite:
                        <input type="text" value={this.state.invitedPlayer} onChange={this.handleInvitedPlayerChange} />
                    </label>
                    <input type="submit" value="Submit" />
                </form>
                {inviteButtons}

            </div>


        )
    }

    handleInvitedPlayerChange(event) {
        event.preventDefault();
        this.setState({invitedPlayer: event.target.value});
    }
    async handleGeneralRequest(event, url, payload, headers){
        event.preventDefault();
        this.props.handleGeneralRequest(event, url, payload, headers)
            .then(response => this.props.updateInvites(response.invites));

    }
    async handleAcceptInvite(playerOne){
        console.log("playerOne is: " + playerOne);
        this.props.handleAcceptInvite(this.state.apiConfig.url, "action=accept_invite&playerOne=" + playerOne + "&playerTwo=" + this.props.username, this.props.apiConfig.headers)
            .then(response => this.props.updateInvites(response.invites));


    }
    async handleDeclineInvite(playerOne){

        this.props.handleAcceptInvite(this.state.apiConfig.url, "action=decline_invite&playerOne=" + playerOne + "&playerTwo=" + this.props.username, this.props.apiConfig.headers)
            .then(response => this.props.updateInvites(response.invites));

    }
}

export default Invite;