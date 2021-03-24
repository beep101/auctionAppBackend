import React from 'react';
import {SHORT_DESCRIPTION_CHAR_COUNT} from '../utils/constants'
import { Link } from 'react-router-dom';

class FeaturedItem extends React.Component{
    render(){
        let description=''
        if(this.props.item.description.length>SHORT_DESCRIPTION_CHAR_COUNT){
            description=`${this.props.item.description.substring(0,SHORT_DESCRIPTION_CHAR_COUNT)}...`;
        }else{
            description=this.props.item.description;
        }
        return(
            <div className="feturedItemContainer">
                <div className="featuredItemTextContainer">
                    <div className="featuredItemName">{this.props.item.name}</div>
                    <div className="featuredItemStartPrice">{`Starts from - $${this.props.item.startingprice}`}</div>
                    <div className="feturedItemDescriptionText">{description}</div>
                    <Link to={`/item?id=${this.props.item.id}`}>
                        <div className="bidButton featuredButton">Bid Now</div>
                    </Link>
                </div>
                <div className="featuredItemImgContainer">
                    <img crossorigin="anonymous" className="gridImg" src={this.props.item.images[0]}/>
                </div>
           </div>

        )
    }
}

export default FeaturedItem