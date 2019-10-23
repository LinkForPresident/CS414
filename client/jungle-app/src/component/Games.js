import React from "react";
import {Button} from "react-bootstrap";

class Games extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            activeGames: this.props.activeGames,
            completedGames: this.props.completedGames,
        };

    }

    render() {
        // This is where active and inactive games can be chosen for either viewing or playing.
        if (this.props.loggedIn) {

            const activeGamesList = this.state.activeGames.map((game) =>
                <li className={'list-group-item list-group-item-dark'}>
                    <Button onClick={this.props.setSelectedGame} value={game}
                    >
                        Game Id: {game}
                    </Button> request json</li>);
            const completedGamesList = this.state.completedGames.map((game) =>
                <li className={'list-group-item list-group-item-dark'}>
                    <Button onClick={this.props.setSelectedGame} value={game}
                    >
                        Game Id: {game}
                    </Button> request json</li>);
            return (
                <div className={'GamesPage'}>
                    <h2>Active Games</h2>
                    <ul className={'list-group list-group-horizontal'}>
                        {activeGamesList}
                    </ul>
                    <h2>Completed Games</h2>
                    <ul className={'list-group list-group-horizontal'}>
                        {completedGamesList}
                    </ul>

                </div>
            )
        }
        else {
            return("");
        }
    }
}

export default Games;
