import React from 'react';

class Square extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            background_src: "./images/dou_shou_qi_jungle_game-board.jpg",
            environment: null
        }
    }

    render() {
        return(
            <button
                id={this.props.row + "," + this.props.column}
                value={this.props.row + "," + this.props.column}
                className={"game-buttons " + (this.props.available ? "availableSpace" : "notAvailableSpace")}
                onClick={() => {
                    this.props.postExample("action=move_piece&gameID=1234&username=" + this.props.username + "&password=iforgot123&row=" + this.props.row + "&column=" + this.props.column)
                }}
                // onClick={this.props.postExample()}
            >|{this.props.piece}{this.props.environment}{this.props.available}|</button>
        );
    }
}
export default Square;