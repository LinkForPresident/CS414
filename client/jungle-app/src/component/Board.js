import React from 'react';
import Square from './Square';

class Board extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            selected_piece: null,
        };
    }

    // Constructing the board involves mapping the 2 dimensional array into "Squares" where piece, and environment logic and display specifics exist.
    render() {

        if (this.props.loggedIn) {
            const gameBoard = this.props.gameState.board.map((game, row_index) =>
                <li className={'game-row col-12 col-lg-8'}>
                    {game.map((space, column_index) =>
                        <Square row={row_index} column={column_index} username={this.props.username} postExample={this.props.postExample} environment={space.environment} piece={space.piece} available={space.available} />
                        )}</li>
            );

            return (
                // bootstrapped grid container with rows
                <div className={'Board container'}>

                    <ul className={"board-ul row"}>
                        {gameBoard}
                    </ul>
                </div>
            )
        }
        else {
            return("");
        }
    }
}
export default Board;