import React from 'react';
import Square from './Square';

class Board extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            selected_piece: null,
        };
    }

    render() {

        if (this.props.loggedIn) {
            const gameBoard = this.props.gameState.board.map((game, row_index) =>
                <li className={'game-row'}>
                    {game.map((space, column_index) =>
                        <Square row={row_index} column={column_index} username={this.props.username} postExample={this.props.postExample} environment={space.environment} piece={space.piece} available={space.available} />
                        )}</li>
            );

            return (
                <div className={'Board'}>
                    <p>Put the actual board here for game ({this.props.selectedGame})</p>
                    <ul className={"board-ul"}>
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