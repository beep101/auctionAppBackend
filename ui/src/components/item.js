import React from 'react';
import queryString from 'query-string';
import {getItemById} from '../apiConsumer/itemFetchConsumer'
import {getBidsLimited, addBid} from '../apiConsumer/bidConsumer'
import AuthContext from '../context';
import 'react-toastify/dist/ReactToastify.css';
import {ITEM_MSG_DELAY} from '../utils/constants'
import ReactMarkdown from 'react-markdown'
import BidTable from './bidTable';
import RelatedItems from './relatedItems';
import ReactTooltip from 'react-tooltip';
import { addWishToWishlist, delWishFromWishlist } from '../apiConsumer/wishlistConsumer';
import { timeDiffTodayToDateString } from '../utils/functions';

class Item extends React.Component{

    constructor(props){
        super(props);
        let params = queryString.parse(this.props.location.search)
        if(params['id']){
            this.state={
                id:params['id'],
                item:{},
                bids:[],
                bidAmount:0,
                msg:"",
                msgType:"itemMsg",
                isOwner:false,
                expired:false,
                isWish:false
            }
        }else{
            this.state={
                id:"",
                item:{},
                bids:[],
                bidAmount:0,
                msg:"",
                msgType:"itemMsg",
                isOwner:false,
                isExpired:false,
                isWish:false
            }
        }
    }

    onChange=(e)=>{
        this.setState({[e.target.name]:e.target.value});
    }

    componentDidMount=()=>{
        getItemById(this.state.id,(success,data)=>{
            if(success){
                this.setState({['item']:data});
                this.setState({['displayedImage']:data.images[0]});
                this.setState({['isOwner']:data.seller.id==this.context.user.jti});
                this.isExpired();
                this.isWish(data);
                if(data.bids.length&&data.bids[0].bidder.id==this.context.user.jti)
                    this.setBanner('You are the highest bidder','itemMsg successItemMsg');
            }else{
                this.setState({['msg']:'Cannot load item',['msgType']:'itemMsg warningItemMsg'});
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

    isExpired=()=>{
        let t = this.state.item.endtime.split(/[-T:.]/);
        let endtime = new Date(Date.UTC(t[0], t[1]-1, t[2], t[3], t[4], t[5]));
        if(endtime.getTime()<=(new Date()).getTime()||this.state.item.sold)
            this.setState({['isExpired']:true});
        else
            this.setState({['isExpired']:false});
    }

    timeDiff=()=>{
        if(this.state.isExpired)
            return "Expired";
        return timeDiffTodayToDateString(this.state.item.endtime)
    }

    placeBid=()=>{
        if(this.context.jwt===""){
            this.setMsg('Have to be logged in to place bids','itemMsg warningItemMsg');
            return;
        }
        if(this.state.isOwner){
            this.setMsg('Cannot place bid on your own item','itemMsg warningItemMsg');
            return;
        }
        let bidAmount=parseFloat(this.state.bidAmount)
        if(!bidAmount){
            this.setMsg('Bid amount value not valid','itemMsg warningItemMsg');
            return;
        }
        let bid={
            amount: bidAmount,
            bidder:{id:parseInt(this.context.user.jti)},
            item:{id:this.state.id}
        }
        addBid(bid,this.context.jwt,(success,data)=>{
            if(success){
                this.setMsg('Bid placed successefuly','itemMsg successItemMsg');
                this.loadBids();
            }else{
                if(data.errors){
                    this.setMsg(data.errors['User'],'itemMsg errorItemMsg');
                    this.loadBids();
                }else{
                    this.setMsg('Bid failed, bid amount is too low','itemMsg errorItemMsg');
                }
            }
        })
    }

    setMsg=(msg,type)=>{
        this.setState({['msg']:msg,['msgType']:type});
        setTimeout(()=>{this.setState({['msg']:''});},ITEM_MSG_DELAY);
    }

    setBanner=(msg,type)=>{
        this.setState({['msg']:msg,['msgType']:type});
    }

    isWish=(data)=>{
        this.setState({['isWish']:this.context.wishes.some(w => w.item.id === data.id)});
    }

    onWishClick=()=>{
        if(this.state.isWish){
            let wishes=this.context.wishes.filter(w=>w.item.id===this.state.item.id);
            delWishFromWishlist({id:wishes[0].id},this.context.jwt,(success,data)=>{
                if(success){
                    this.context.removeWish(data);
                    this.setState({['isWish']:false});
                }
            })
        }else{
            addWishToWishlist({item:{id:this.state.item.id}},this.context.jwt,(success,data)=>{
                if(success){
                    this.context.addWish(data);
                    this.setState({['isWish']:true});
                }                
            })
        }
    }

    render(){
        if(this.state.item.id){
            return(
            <div className="itemPage">
                <div className={this.state.msgType}>{this.state.msg}</div>
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
                            {(!this.context.jwt||this.state.isOwner||this.state.isExpired)&&
                                <ReactTooltip place="top" type="dark" effect="float">
                                    <span className="reactTooltipText">{!this.context.jwt?'Log in to place bid':
                                        this.state.isOwner?'Cannot place bids on your own items':
                                        'Cannot place bids on expired items'}
                                    </span>
                                </ReactTooltip>
                            }
                            <div className="bidContainer" data-tip="React-tooltip">
                                <input name="bidAmount" onChange={this.onChange} className="bidInput" disabled={!this.context.jwt||this.state.isOwner||this.state.isExpired}/>
                                <span onClick={!this.context.jwt||this.state.isOwner||this.state.isExpired?()=>{}:this.placeBid}
                                    className={!this.context.jwt||this.state.isOwner||this.state.isExpired?"bidButtonDisabled":"bidButton"}>Place Bid</span>
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
                                {this.state.item.seller.id==this.context.user.jti?  
                                    <div className="itemWishlistButton">
                                        Wishlist
                                        <img className="socialImg" src={this.state.isWish?"/images/watchlist_ppl.svg":"/images/watchlist.svg"}/>
                                    </div>:
                                    <div className={this.state.isWish?"itemWishlistButtonSelected pointer":"itemWishlistButton pointer"}
                                        onClick={this.onWishClick}>
                                        Wishlist
                                        <img className="socialImg" src={this.state.isWish?"/images/watchlist_ppl.svg":"/images/watchlist.svg"}/>
                                    </div>
                                }
                            </div>
                                
                        </div>

                        <div>
                            <div className="itemStartPrice color5a5a5aMarginLeft">Details</div>
                            
                            <ReactMarkdown className="itemDescriptionText">{this.state.item.description}</ReactMarkdown>
                        </div>
                    </div>
                    
                </div>
                {this.state.isOwner&&<BidTable bids={this.state.bids}/>}
                {!this.state.isOwner&&<RelatedItems itemId={this.state.item.id} category={this.state.item.subcategory.category.id}/>}
            </div>
            )
        }else{
            return(
                <div className="itemPage"></div>
            );
        }
    }
}

Item.contextType=AuthContext;

export default Item;