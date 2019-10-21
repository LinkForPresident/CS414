import React from "react";
class GameRules extends React.Component {
    // Static page with description
    render() {
        return (
            <div className={'GaneRules'}>
                <p>Rules of the Game of Jungle</p>

                <p>Each player has eight pieces, different animals, with different degrees of power. Here are the pieces, their English names, and their relative powers, indicated by number:</p>

                <p>the pieces of dou shou qi and their powers (the jungle game or Chinese animal chess)</p>
                <p>The Animals depicted on the pieces shown here are very interesting abstractions. Your set may look like this, or may have the same animals depicted in a different style — and the same animals may be depicted differently on the board as well. You may want to pencil the pieces’ value numbers, 1 through 8, on the underside of each piece, or keep this chart on hand, to help you become acquainted with the pieces as you play.</p>

                <p>Object of the Game</p>

                <p> To win the game, one player must successfully move any animal into the Den of the opponent. (see Den in the diagram above</p>

                <p>Movement of the Pieces</p>

                <p>The black (or darker) pieces have the first move. All pieces have the same basic move, although some have special powers (described below). The basic move is just one space either forward, backward, left or right. The pieces never move diagonally.</p>

                <p>Captures</p>

                <p>An animal is captured (or “eaten”) by an opposing animal moving onto its square, as in chess or Stratego. But the attacking animal must be of equal or higher power than the one being captured. For instance, the Tiger (6) can capture the Tiger (6), Leopard (5) or Dog (4), but the Dog can not capture the Leopard or Tiger.</p>

                <p>Special Powers</p>

                <p>1) The Rat, although it is the least powerful piece, has the power to capture the Elephant. The Elephant can not capture the Rat. It is said that this is because the rat can creep in under the Elephant’s ear and eat his brain (!).</p>

                <p>2) The Rat, and no other animal, can move freely in the water. It can not, however, attack the Elephant from the water.</p>

                <p>3) Both the Lion and the Tiger can jump over the water, moving from one bank straight forward, backward, left or right (like a rook in chess) to the first square of dry land on the other side. They may capture in this move as well. The Lion and Tiger may not, however, jump over a rat if it is in the way, in the water.</p>

                <p> The Traps</p>

                <p>Each side has three Trap squares surrounding its Den. A player may move on and off of his own Trap squares with no effect. If, however, a player moves onto the opponent’s trap square, that piece loses all of its power, and may be captured by any of the defending pieces.</p>

                <p>The Den</p>

                <p> Animals are not allowed to move into their own Dens. When an animal moves into the opponent’s Den, it has won the game.</p>
            </div>
        )
    }
}
export default GameRules;