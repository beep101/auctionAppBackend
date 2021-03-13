import React from 'react';
import { Link } from 'react-router-dom';
import ListItemButtons from './listitembuttons';

class ItemElement extends React.Component{

    constructor(props){
        super(props);
    }
    render(){
        let itemClass,imageClass,textClass,nameClass,priceClass,imgContClass;
        if(this.props.type==="grid"){
            itemClass="gridItem";
            imgContClass="gridItemImgContainer";
            imageClass="gridImg";
            textClass="gridText"
            nameClass="gridName";
            priceClass="gridPrice";
        }else if(this.props.type==="list"){
            itemClass="listItem";
            imgContClass="listItemImgContainer";
            imageClass="listImg";
            textClass="listText"
            nameClass="listName";
            priceClass="listPrice";
        }
        return(
            <div className={itemClass}>
                <Link to={this.props.link} className={imgContClass}>
                    <img crossorigin="anonymous" className={imageClass} src={this.props.item.images[0]}/>
                </Link>
                <div className={textClass}>
                    <span className={nameClass}>{this.props.item.name}</span>
                    {this.props.type==="list"&&<div className="listDescription">{this.props.item.description}</div>}
                    <span className={priceClass}>Starts From ${this.props.item.startingprice}</span>
                    {this.props.type==="list"&&<ListItemButtons/>}
                </div>
            </div>
        );
    }
} 

export default ItemElement;