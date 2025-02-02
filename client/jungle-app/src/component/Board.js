import React from 'react';
import Square from './Square';

class Board extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            selected_piece: null,
        };
        this.handleForfeitGame = this.handleForfeitGame.bind(this);
    }
    componentDidMount() {
        // console.log
        this.props.ViewGameState(this.props.gameState.gameID);
    }

    handleForfeitGame(){
        var self = this;
        console.log("User" + this.props.username + "is forfeiting game.");
        this.props.handleForfeitGame(this.props.apiConfig.url, "action=ForfeitGame&username=" + this.props.username + "&password=" + this.props.password + "&gameID=" + this.props.selectedGame, this.props.apiConfig.headers)
        this.props.getGames(this.props.apiConfig.url, "action=ViewUserGames&username=" + this.props.username + "&password=" + this.props.password, this.props.apiConfig.headers)
            .then(
                function (response) {
                    self.props.updateActiveGames(response.activeGames)
                }
            )
    }

    // Constructing the board involves mapping the 2 dimensional array into "Squares" where piece, and environment logic and display specifics exist.
    render() {
        if (this.props.loggedIn &&  this.props.gameState.board != null) {
            const gameBoard = this.props.gameState.board.map((game, row_index) =>
                <li className={'game-row col-12 col-lg-8'}>
                    {game.map((space, column_index) =>
                        <Square row={row_index} column={column_index} username={this.props.username} gameID={this.props.gameState.gameID} password={this.props.password} postExample={this.props.postExample} environment={space.environment} piece={space.piece} available={space.available} />
                        )}</li>
            );

            let winning = "";
            if(this.props.gameState.winner !== "") {
                winning = "Player " + this.props.gameState.winner + " Wins!";
            }

            return (
                // bootstrapped grid container with rows
                <div className={'Board container'}>
                    <h3 style={{color: "lightgreen"}}>{winning}</h3>
                    <ul className={"board-ul row"}>
                        {gameBoard}
                    </ul>
                    <button onClick={() => this.handleForfeitGame()}>Forfeit &#9872;</button>
                </div>
            )
        }
        else {
            return("");
        }
    }
}
export default Board;