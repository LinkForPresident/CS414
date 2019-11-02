import React from 'react';

class User extends React.Component {
    render() {
        return (
            <div className={'UserPage'}>
                <button
                    onClick={() => {
                    this.props.Logout()
                }}>Logout</button>
            </div>
        )
    }
}
export default User;