import React from 'react';
import { Link } from 'react-router-dom';

class ListItemButtons extends React.Component{
    render(){
        let container=this.props.mode==="grid"?"gridItemButtonContainer":"listItemButtonContainer";
        let buttons=this.props.mode==="grid"?"gridItemButton":"listItemButton";
        return(
            <div className={container}>
                <div className={buttons}>
                    Watchlist
                    <img className="socialImg" src={this.props.mode==="grid"?"/images/watchlist_dark.svg":"/images/watchlist.svg"}/>
                </div>

                <Link to={this.props.bidLink} className={buttons}>
                    Bid
                    <img className="socialImg" src={this.props.mode==="grid"?"/images/bid_dark.svg":"/images/bid.svg"}/>
                </Link>
            </div>
        )
    }
}

export default ListItemButtons