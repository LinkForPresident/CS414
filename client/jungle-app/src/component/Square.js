import React from 'react';

class Square extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            // background_src: "./images/dou_shou_qi_jungle_game-board.jpg",
            environment: null
        }
    }

    render() {
        let environmentImg;
        let pieceImg;
        if (this.props.environment !== null && this.props.environment !== "empty") {
            environmentImg = <img src={require("./images/"+this.props.environment+".png")} alt="r1" height="95%" style={{position: "relative", zIndex: "10"}}/>
        }
        if (this.props.piece !== null && this.props.piece !== undefined) {
            pieceImg = <img src={require("./images/"+this.props.piece+".png")} alt="r1" height="100%" style={{position: "relative", zIndex: "20"}}/>
        }
        return(
            // While buttons seem to be working well here, any event driven component would work here. All the logic needed should have been passed via a prop.
            <button
                id={this.props.row + "," + this.props.column}
                value={this.props.row + "," + this.props.column}
                className={"game-buttons " + (this.props.available ? "availableSpace" : "notAvailableSpace")}
                onClick={() => {
                    this.props.postExample("action=MovePiece&gameID=1234&username=" + this.props.username + "&password=iforgot123&row=" + this.props.row + "&column=" + this.props.column)
                }}
                // onClick={this.props.postExample()}
            >
                {'.'}
                {environmentImg}
                {pieceImg}
            </button>
        );
    }
}
export default Square;