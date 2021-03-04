import React from 'react';

class ItemElement extends React.Component{

    constructor(props){
        super(props);
    }
    render(){
        let itemClass,imageClass,textClass,nameClass,priceClass,imgContClass;
        if(this.props.type=="grid"){
            itemClass="gridItem";
            imgContClass="gridItemImgContainer";
            imageClass="gridImg";
            textClass="gridText"
            nameClass="gridName";
            priceClass="gridPrice";
        }
        return(
            <div className={itemClass}>
                <div className={imgContClass}>
                    <img className={imageClass} src={this.props.item.images[0]}/>
                </div>
                {this.props.type=="grid"?<hr className="gridBreak"></hr>:null}
                <div className={textClass}>
                    <span className={nameClass}>{this.props.item.name}</span>
                    <span className={priceClass}>from {this.props.item.startingprice}$</span>
                </div>
            </div>
        );
    }
} 

export default ItemElement;