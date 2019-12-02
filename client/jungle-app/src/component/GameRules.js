import React from "react";
import Card from "react-bootstrap/Card";
import CardColumns from "react-bootstrap/CardColumns";
import CardDeck from "react-bootstrap/CardDeck";
class GameRules extends React.Component {
    // Static page with description
    render() {
        return (
            <div className={'GameRules'}>
                <h2>Rules of the Game of Jungle</h2>

                <h3>Pieces</h3>
                <p>Each player has eight pieces, which are different animals, with different degrees of power indicated by number.</p>

                <div className={'pieces'}>
                    <Card className={"pieceDescriptionCard"} bg={"primary"} border={"light"}>
                        <Card.Img variant="top" style={{width: '7em'}} src={require("./images/b1.png")} />
                        <Card.Body>
                            <Card.Title>Rat</Card.Title>
                        </Card.Body>
                    </Card>

                    <Card className={"pieceDescriptionCard"} bg={"primary"} border={"light"}>
                        <Card.Img variant="top" style={{width: '7em'}} src={require("./images/b2.png")} />
                        <Card.Body>
                            <Card.Title>Cat</Card.Title>
                        </Card.Body>
                    </Card>
                    <Card className={"pieceDescriptionCard"} bg={"primary"} border={"light"}>
                        <Card.Img variant="top" style={{width: '7em'}} src={require("./images/b3.png")} />
                        <Card.Body>
                            <Card.Title>Wolf</Card.Title>
                        </Card.Body>
                    </Card>

                    <Card className={"pieceDescriptionCard"} bg={"primary"} border={"light"}>
                        <Card.Img variant="top" style={{width: '7em'}} src={require("./images/b4.png")} />
                        <Card.Body>
                            <Card.Title>Dog</Card.Title>
                        </Card.Body>
                    </Card>
                    <Card className={"pieceDescriptionCard"} bg={"primary"} border={"light"}>
                        <Card.Img variant="top" style={{width: '7em'}} src={require("./images/b5.png")} />
                        <Card.Body>
                            <Card.Title>Panther</Card.Title>
                        </Card.Body>
                    </Card>

                    <Card className={"pieceDescriptionCard"} bg={"primary"} border={"light"}>
                        <Card.Img variant="top" style={{width: '7em'}} src={require("./images/b6.png")} />
                        <Card.Body>
                            <Card.Title>Tiger</Card.Title>
                        </Card.Body>
                    </Card>

                    <Card className={"pieceDescriptionCard"} bg={"primary"} border={"light"}>
                        <Card.Img variant="top" style={{width: '7em'}} src={require("./images/b7.png")} />
                        <Card.Body>
                            <Card.Title>Lion</Card.Title>
                        </Card.Body>
                    </Card>

                    <Card className={"pieceDescriptionCard"} bg={"primary"} border={"light"}>
                        <Card.Img variant="top" style={{width: '7em'}} src={require("./images/b8.png")} />
                        <Card.Body>
                            <Card.Title>Elephant</Card.Title>
                        </Card.Body>
                    </Card>
                </div>
                <br style={{clear: "left"}}/>
                <ul>
                    <li>The rat is the only animal that may go onto a water square, and cannot be killed while in the water.</li>
                    <li>The rat can capture and Elephant, but only from a land square (not a water tile)</li>
                    <li>The Lion and the Tiger can jump over the river horizontally or vertically, landing on the next land tile on the other side. This cannot happen if either color rat is in between the land tiles.</li>
                </ul>


                <h3>Tile Environments</h3>
                <Card className={"tileDescriptionCard"} bg={"dark"} border={"success"}>
                    <Card.Img variant="top" style={{width: '7em', margin:'10px'}} src={require("./images/den.png")} />
                    <Card.Body>
                        <Card.Title>Den</Card.Title>
                        <Card.Text>The first player to get one of their animals into the opposing den wins. Animals cannot move into their own den.</Card.Text>
                    </Card.Body>
                </Card>

                <Card className={"tileDescriptionCard"} bg={"dark"} border={"success"}>
                    <Card.Img variant="top" style={{width: '7em', margin:'10px'}} src={require("./images/trap.png")} />
                    <Card.Body>
                        <Card.Title>Trap</Card.Title>
                        <Card.Text>Three traps surround the Den. Any animal in an opposing trap tile temporarily reduces their rank to 0 until they exit the trap.</Card.Text>
                    </Card.Body>
                </Card>

                <Card className={"tileDescriptionCard"} bg={"dark"} border={"success"}>
                    <Card.Img variant="top" style={{width: '7em', margin:'10px'}} src={require("./images/water.png")} />
                    <Card.Body>
                        <Card.Title>River</Card.Title>
                        <Card.Text>Acts as a tile that cannot be moved through, except by the rat. The Lion and Tiger can also leap over the river.</Card.Text>
                    </Card.Body>
                </Card>

                <br style={{clear: "left"}}/>

                <h2>Object of the Game</h2>
                <p> To win the game, one player must successfully move any animal into the Den of the opponent.</p>

                <h2>Movement of the Pieces</h2>
                <p>Blue pieces have the first move. All pieces can move one space either forward, backward, left or right. The pieces never move diagonally. Some pieces have special moves, as described above.</p>

                <h2>Captures</h2>
                <p>An animal is captured (or “eaten”) by an opposing animal moving onto its square. The attacking animal must be of equal or higher power than the one being captured. For instance, the Tiger (6) can capture the Tiger (6), Leopard (5) or Dog (4), but the Dog can not capture the Leopard or Tiger.</p>

                <h2> The Traps</h2>
                <p>Each side has three Trap squares surrounding its Den. A player may move on and off of his own Trap squares with no effect. If, however, a player moves onto the opponent’s trap square, that piece loses all of its power, and may be captured by any of the defending pieces.</p>

                <h2>The Den</h2>
                <p> Animals are not allowed to move into their own Dens. When an animal moves into the opponent’s Den, it has won the game.</p>
            </div>
        )
    }
}
export default GameRules;