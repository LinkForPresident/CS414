import React from "react";
import {Button} from "react-bootstrap";

class History extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
                completedGames: this.props.completedGames,
        };
    }

    async getGames(url, payload, headers){
        // event.preventDefault();
        this.props.getGames(url, payload, headers)
            .then(response =>
                this.props.updateCompletedGames(response.completedGames)
                // console.log("GET GAMES RESPONSE: " + response.activeGames)
            );
    }

    componentDidMount() {
        // console.log
        this.getGames(this.props.apiConfig.url, "action=ViewMatchHistory&username=" + this.props.username + "&password=" + this.props.password, this.props.apiConfig.headers)
    }


    render() {
        // This is where active and inactive games can be chosen for either viewing or playing.

            const completedGamesList = this.props.completedGames.map((game) =>
                        <tr>
                            <td>
                                {game.split("$")[0]}
                            </td>
                            <td>
                                {game.split("$")[1]}
                            </td>
                            <td>
                                {game.split("$")[2]}
                            </td>
                            <td>
                                {game.split("$")[3]}
                            </td>
                            <td>
                                {game.split("$")[4]}
                            </td>
                            <td>
                                {game.split("$")[5]}
                            </td>
                        </tr>);
            return (
                <div className={'GamesPage'}>
                    <h2>Game History</h2>
                    <table className={'table'} style={{backgroundColor: "skyblue"}}>
                    <tr>
                        <th>Game ID</th>
                        <th>Start time</th>
                        <th>End time</th>
                        <th>Player one</th>
                        <th>Player two</th>
                        <th>Winner</th>
                    </tr>
                        {completedGamesList}
                    </table>
                </div>
            )
    }
}

export default History;