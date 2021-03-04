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
        let t = this.state.item.endtime.split(/[-T:.]/);
        let endtime = new Date(Date.UTC(t[0], t[1]-1, t[2], t[3], t[4], t[5]));
        let secs=(endtime.getTime()-Date.now())/1000;

        if(secs/60>1){
            let mins=secs/60;
            if(mins/60>1){
                let hrs=mins/60;
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
                <div className="itemContainer">
                    <div className="itemImageContainer">
                        <div className="itemImageMainFrame">
                            <img className="itemImageMain" src={this.state.displayedImage}/>
                        </div>
                        <div class="itemThumbsBar">
                            {this.state.item.images.map((image)=><span onClick={()=>this.changeDisplayedImage(image)} className="itemImageThumbFrame" ><img style={{'max-height': '95%','max-width': '95%'}} src={image}/></span>)}
                        </div>
                    </div>
                    <div className="itemInformationsContainer">
                        <div className="itemDataContainer">
                            <div className="itemName">{this.state.item.name}</div>
                            <div className="itemStartPrice">Starts from {this.state.item.startingprice}$</div>
                            <div>
                                <input className="bidInput"/>
                                <span className="bidButton">Place Bid</span>
                            </div>
                            <div className="width10vw">
                                <div className="messageS flexEnd"><span >Highes bid</span><span>***$</span></div>
                                <div className="messageS flexEnd"><span>Bid count</span><span>N</span></div>
                                <div className="messageS flexEnd"><span>Time left</span><span>{this.timeDiff()}</span></div>
                            </div>
                                
                        </div>
                        <div>
                            <div className="itemStartPrice">Description</div>
                            
                            <div className="itemDescriptionText">{this.state.item.description}</div>
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