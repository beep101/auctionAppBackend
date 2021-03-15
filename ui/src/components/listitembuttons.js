import React from 'react';

class ListItemButtons extends React.Component{
    render(){
        return(
            <div className="listItemButtonContainer">
                <div className="listItemButton">
                    Watchlist
                    <img className="socialImg" src="/images/watchlist.svg"/>
                </div>
                <div className="listItemButton">
                    Bid
                    <img className="socialImg" src="/images/bid.svg"/>
                </div>
            </div>
        )
    }
}

export default ListItemButtons