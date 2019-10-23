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
        //Line 23: {this.props.piece}.{(this.props.environment != "empty" ? this.props.environment : "")}.{this.props.available}
        return(
            <button
                id={this.props.row + "," + this.props.column}
                value={this.props.row + "," + this.props.column}
                className={"game-buttons " + (this.props.available ? "availableSpace" : "notAvailableSpace")}
                onClick={() => {
                    this.props.postExample("action=MovePiece&gameID=1234&username=" + this.props.username + "&password=iforgot123&row=" + this.props.row + "&column=" + this.props.column)
                }}
                // onClick={this.props.postExample()}
            >
                <img src={require("./images/"+this.props.environment+".png")} alt="r1" height="95%" style={{position: "absolute", zIndex: "10"}}/>
                <img src={require("./images/"+this.props.piece+".png")} alt="r1" height="100%" style={{zIndex: "20"}}/>
            </button>
        );
    }
}
export default Square;