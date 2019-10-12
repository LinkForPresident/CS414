import React from 'react';

class Board extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            background_src: "./images/dou_shou_qi_jungle_game-board.jpg",
            selected_piece: null,
        };
    }

    setSelectedPiece(e) {
        console.log("making request to the server for game");
        this.setState({
            selected_piece: e.target.value
        })
    }

    render() {
        if (this.props.loggedIn) {
            const gameBoard = this.props.gameState.board.map((game, row_index) =>
                <li className={'game-row'}>
                    {game.map((piece, column_index) =>
                        <button
                            id={row_index.toString() + "," + column_index.toString()}
                            value={row_index.toString() + "," + column_index.toString()}
                            className={"game-buttons"}
                            onClick={() => {
                                this.props.postExample("action=move_piece&gameID=1234&username=" + this.props.username + "&password=iforgot123&row=" + row_index + "&column=" + column_index)
                            }}
                            // onClick={this.props.postExample()}
                        >
                            {piece}
                        </button>
                    )}</li>
            );

            return (
                <div className={'Board'}>
                    <p>Put the actual board here for game ({this.props.selectedGame})</p>
                    <ul className={"board-ul"}>
                        {gameBoard}
                    </ul>
                    <img src={this.state.background_src} alt={"board Image"}/>
                </div>
            )
        }
        else {
            return("");
        }
    }
}
export default Board;