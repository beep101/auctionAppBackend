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
            <div class={itemClass}>
                <div class={imgContClass}>
                    <img class={imageClass} src={this.props.item.images[0]}/>
                </div>
                {this.props.type=="grid"?<hr class="gridBreak"></hr>:null}
                <div class={textClass}>
                    <span class={nameClass}>{this.props.item.name}</span>
                    <span class={priceClass}>from {this.props.item.startingprice}$</span>
                </div>
            </div>
        );
    }
} 

export default ItemElement;