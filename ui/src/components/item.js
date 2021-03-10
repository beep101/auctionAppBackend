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
            toast.error('Have to be logged in to place bids', DEFAULT_TOAST_CONFIG);
            return;
        }
        let bidAmount=parseFloat(this.state.bidAmount)
        if(!bidAmount){
            toast.error('Bid amount value not valid', DEFAULT_TOAST_CONFIG);
            return;
        }
        let bid={
            amount: bidAmount,
            bidder:{id:parseInt(this.context.user.jti)},
            item:{id:this.state.id}
        }
        addBid(bid,this.context.jwt,(success,data)=>{
            if(success){
                toast.success('Bid placed successefuly', DEFAULT_TOAST_CONFIG);
                this.loadBids();
            }else{
                toast.error('Bid failed, bid amount is too low', DEFAULT_TOAST_CONFIG);
            }
        })
    }

    render(){
        if(this.state.item.id){
            return(
            <div class="itemPage">
                <ToastContainer
                    position="top-center"
                    autoClose={3000}
                    hideProgressBar={true}
                    newestOnTop
                    closeOnClick
                    rtl={false}
                    pauseOnFocusLoss
                    draggable={false}
                    pauseOnHover={false}
                />
                <div class="itemContainer">
                    <div class="itemImageContainer">
                        <div class="itemImageMainFrame">
                            <img crossorigin="anonymous" class="itemImageMain" src={this.state.displayedImage}/>
                        </div>
                        <div class="itemThumbsBar">
                            {this.state.item.images.map((image)=>
                                <span onClick={()=>this.changeDisplayedImage(image)} class="itemImageThumbFrame" >
                                    <img crossorigin="anonymous" class="itemThumbImage" src={image} />
                                </span>)}
                        </div>
                    </div>
                    <div className="itemInformationsContainer">
                        <div className="itemDataContainer">
                            <div className="itemName">{this.state.item.name}</div>
                            <div className="itemStartPrice">Starts from {this.state.item.startingprice}$</div>
                            <div>

                                <input name="bidAmount" onChange={this.onChange} class="bidInput"/>
                                <span onClick={this.placeBid} class="bidButton">Place Bid</span>
                            </div>
                            <div class="width10vw">
                                <div class="messageS flexEnd">
                                    <span >Highes bid</span>
                                    <span>$ {this.state.bids.length===0?
                                        this.state.item.startingprice:this.state.bids[0].amount}
                                    </span>
                                </div>
                                <div class="messageS flexEnd">
                                    <span>Bid count</span>
                                    <span>{this.state.bids.length}</span></div>
                                <div class="messageS flexEnd">
                                    <span>Time left</span>
                                    <span>{this.timeDiff()}</span>
                                </div>

                            </div>
                                
                        </div>
                        <div>
                            <div className="itemStartPrice">Description</div>
                            
                            <div className="itemDescriptionText">{this.state.item.description}</div>
                        </div>
                    </div>
                    
                </div>
                <table className="bidTable">
                    <col className="bidTableColumn1"/>
	                <col className="bidTableColumn2"/>
	                <col className="bidTableColumn3"/>
	                <thead/>
                    <tr class="bidTable">
                        <th class="bidTable">Name</th>
                        <th class="bidTable">Date</th>
                        <th class="bidTable">Amount</th>
                    </tr>
                    {this.state.bids.map((bid)=><BidLine bid={bid}/>)}
                </table>
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

Item.contextType=AuthContext;

export default Item;