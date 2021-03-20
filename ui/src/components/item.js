import React from 'react';
import queryString from 'query-string';
import {getItemById} from '../apiConsumer/itemFetchConsumer'
import {getBidsLimited, addBid} from '../apiConsumer/bidConsumer'
import BidLine from './bidLine';
import AuthContext from '../context';
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import { DEFAULT_TOAST_CONFIG} from "../utils/constants"

class Item extends React.Component{

    constructor(props){
        super(props);
        let params = queryString.parse(this.props.location.search)
        if(params['id']){
            this.state={
                id:params['id'],
                item:{},
                bids:[],
                bidAmount:0
            }
        }else{
            this.state={
                id:"",
                item:{},
                bids:[],
                bidAmount:0
            }
        }
    }

    onChange=(e)=>{
        this.setState({[e.target.name]:e.target.value})
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
        this.loadBids();
    }

    loadBids=()=>{
        getBidsLimited(this.state.id,10,(success,data)=>{
            if(success){
                this.setState({['bids']:data});
            }else{
                console.log("Cannot load bids");
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

    placeBid=()=>{
        if(this.context.jwt===""){
            this.setState({['msg']:'Have to be logged in to place bids',['msgType']:'warningItemMsg'});
            return;
        }
        let bidAmount=parseFloat(this.state.bidAmount)
        if(!bidAmount){
            this.setState({['msg']:'Bid amount value not valid',['msgType']:'warningItemMsg'});
            return;
        }
        let bid={
            amount: bidAmount,
            bidder:{id:parseInt(this.context.user.jti)},
            item:{id:this.state.id}
        }
        addBid(bid,this.context.jwt,(success,data)=>{
            if(success){
                this.setState({['msg']:'Bid placed successefuly',['msgType']:'successItemMsg'});
                this.loadBids();
            }else{
                this.setState({['msg']:'Bid failed, bid amount is too low',['msgType']:'errorItemMsg'});
            }
        })
    }

    render(){
        if(this.state.item.id){
            return(
            <div className="itemPage">
                {this.state.msg&&<div className={this.state.msgType}>{this.state.msg}</div>}
                <div className="itemContainer">
                    <div className="itemImageContainer">
                        <div className="itemImageMainFrame">
                            <img crossorigin="anonymous" className="itemImageMain" src={this.state.displayedImage}/>
                        </div>
                        <div className="itemThumbsBar">
                            {this.state.item.images.map((image)=>
                                <span onClick={()=>this.changeDisplayedImage(image)} className="itemImageThumbFrame" >
                                    <img crossorigin="anonymous" className="itemThumbImage" src={image} />
                                </span>)}
                        </div>
                    </div>
                    <div className="itemInformationsContainer">
                        <div className="itemDataContainer">
                            <div className="itemName">{this.state.item.name}</div>
                            <div className="itemStartPrice">Starts from - ${this.state.item.startingprice}</div>
                            <div className="bidContainer">
                                <input name="bidAmount" onChange={this.onChange} className="bidInput" disabled={this.context.jwt===""}/>
                                <span onClick={this.context.jwt===""?()=>{}:this.placeBid}
                                      className={this.context.jwt===""?"bidButtonDisabled":"bidButton"}>Place Bid</span>
                                <div className="bidMinMessage">Enter ${this.state.bids.length===0?this.state.item.startingprice:this.state.bids[0].amount} or more</div>
                            </div>
                            <div className="width10vw">
                                <div className="messageS">
                                    <span >Highes bid: ${this.state.bids.length===0?this.state.item.startingprice:this.state.bids[0].amount}</span>
                                </div>
                                <div className="messageS">
                                    <span>Bid count: {this.state.bids.length}</span>
                                </div>
                                <div className="messageS">
                                    <span>Time left: {this.timeDiff()}</span>
                                </div>

                                <br/>
                                <div className="listItemButton">
                                    Watchlist
                                    <img className="socialImg" src="/images/watchlist.svg"/>
                                </div>
                            </div>
                                
                        </div>

                        <div>
                            <div className="itemStartPrice color5a5a5aMarginLeft">Details</div>
                            
                            <div className="itemDescriptionText">{this.state.item.description}</div>
                        </div>
                    </div>
                    
                </div>
                <table className="bidTable">
                    <col className="bidTableColumn1"/>
	                <col className="bidTableColumn2"/>
	                <col className="bidTableColumn3"/>
	                <thead/>
                    <tr className="bidTable">
                        <th className="bidTable">Name</th>
                        <th className="bidTable">Date</th>
                        <th className="bidTable">Amount</th>
                    </tr>
                    {this.state.bids.map((bid)=><BidLine bid={bid}/>)}
                </table>
            </div>
            )
        }else{
            return(
                <div className="warningItemMsg">
                    No data
                </div>
            );
        }
    }
}

Item.contextType=AuthContext;

export default Item;