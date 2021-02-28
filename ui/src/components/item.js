import React from 'react';
import queryString from 'query-string';
import {getItemById} from '../apiConsumer/itemFetchConsumer'

class Item extends React.Component{

    constructor(props){
        super(props);
        let params = queryString.parse(this.props.location.search)
        if(params['id']){
            this.state={
                id:params['id'],
                item:{}
            }
        }else{
            this.state={
                id:"",
                item:{}
            }
        }
    }

    componentDidMount=()=>{
        getItemById(this.state.id,(success,data)=>{
            if(success){
                this.setState({['item']:data});
                this.setState({['displayedImage']:data.images[0]})
            }else{
                console.log("Cannot load item");
            }
        });
    }

    changeDisplayedImage=(image)=>{
        this.setState({['displayedImage']:image})
    }

    timeDiff=()=>{
        var t = this.state.item.endtime.split(/[-T:.]/);
        var endtime = new Date(Date.UTC(t[0], t[1]-1, t[2], t[3], t[4], t[5]));
        var secs=(endtime.getTime()-Date.now())/1000;

        if(secs/60>1){
            var mins=secs/60;
            if(mins/60>1){
                var hrs=mins/60;
                if(hrs/24>1){
                    return Math.round(hrs/24)+' days';
                }else{
                    return Math.round(hrs)+' hours';
                }
            }else{
                return Math.round(mins/60)+' minutes';
            }
        }else{
            return Math.round(secs/60)+' seconds';
        }
    }

    render(){
        if(this.state.item.id){
            return(
                <div class="itemContainer">
                    <div class="itemImageContainer">
                        <div class="itemImageMainFrame">
                            <img class="itemImageMain" src={this.state.displayedImage}/>
                        </div>
                        <div class="itemThumbsBar">
                            {this.state.item.images.map((image)=><span onClick={()=>this.changeDisplayedImage(image)} class="itemImageThumbFrame" ><img style={{'max-height': '95%','max-width': '95%'}} src={image}/></span>)}
                        </div>
                    </div>
                    <div class="itemInformationsContainer">
                        <div class="itemDataContainer">
                            <div class="itemName">{this.state.item.name}</div>
                            <div class="itemStartPrice">Starts from {this.state.item.startingprice}$</div>
                            <div>
                                <input class="bidInput"/>
                                <span class="bidButton">Place Bid</span>
                            </div>
                            <div class="width10vw">
                                <div class="messageS flexEnd"><span >Highes bid</span><span>***$</span></div>
                                <div class="messageS flexEnd"><span>Bid count</span><span>N</span></div>
                                <div class="messageS flexEnd"><span>Time left</span><span>{this.timeDiff()}</span></div>
                            </div>
                                
                        </div>
                        <div>
                            <div class="itemStartPrice">Description</div>
                            
                            <div class="itemDescriptionText">{this.state.item.description}</div>
                        </div>
                    </div>
                </div>
            )
        }else{
            return(
                <div>
                    No data
                </div>
            );
        }
    }
}

export default Item;