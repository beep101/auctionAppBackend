import React, { useContext, useEffect, useState } from 'react'
import { Link } from 'react-router-dom';
import { getBiddedItems } from '../apiConsumer/itemFetchConsumer';
import AuthContext from '../context';

function AccountBids(props){
    const [bids,setBids]=useState([]);
    
    const context=useContext(AuthContext);

    useEffect(()=>{
        const token=context.jwt;

        getBiddedItems(token,(success,data)=>{
            if(success)
                setBids(data);
            else
                setBids([]);
        })
    },[])

    if(bids.length)
        return(
            <BiddedItems items={bids} />
        )
    else
        return(
            <div className="notSellerContainer">
                <div className="notSellerTitle">No bids found</div>
                <img className="notSellerImg" src="images/bid_purple.svg"></img>
                <Link className="notSellerButton" to="/shop">Go to shop</Link>
            </div>
        )
}

function BiddedItems(props){
    const [items,setItems]=useState([]);

    useEffect(()=>{
        setItems(props.items);
    },[props.items]);

    return(
        <table className="accountSellerTable">
            <tr className="accountSellerTableMedium">
                <th className="accountSellerTable">Item</th>
                <th className="accountSellerTable left">Name</th>
                <th className="accountSellerTable">Time left</th>
                <th className="accountSellerTable">Your price</th>
                <th className="accountSellerTable">No.bids</th>
                <th className="accountSellerTable">Highest bid</th>
                <th className="accountSellerTable"></th>
            </tr>
            {items.map(item=><BiddedItem item={item}></BiddedItem>)}
        </table>
    )
}

function BiddedItem(props){
    const [timeLeft, setTimeLeft]=useState("");
    const [usersBid,setUsersBid]=useState(0);
    const [maxBid,setMaxBid]=useState(false);

    const context=useContext(AuthContext);

    useEffect(()=>{
        setTimeLeft(timeDiff())
    },[props.item.endtime])
 
    useEffect(()=>{
        const bids=props.item.bids.filter(x=>x.bidder.id==context.user.jti).map(x=>x.amount);
        let bid=bids[0];
        if(bids.length>1)
            bid=bids.reduce((a,b)=>(Math.max(a,b)));
        setUsersBid(bid);
        if(bid==props.item.bids[props.item.bids.length-1].amount)
            setMaxBid(true);
    },[props.item.bids])   

    const timeDiff=()=>{
        let t = props.item.endtime.split(/[-T:.]/);
        let endtime = new Date(Date.UTC(t[0], t[1]-1, t[2], t[3], t[4], t[5]));
        if(endtime.getTime()<(new Date()).getTime())
            return "Ended";
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

    return(
        <tr className="accountSellerTable">
            <td className="accountSellerTable center"><div className="accountSellerTableImgContainer"><img className="accountSellerTable" src={props.item.images[0]}></img></div></td>
            <td className="accountSellerTable left">{props.item.name}</td>
            <td className="accountSellerTable">{timeLeft}</td>
            <td className={maxBid?"accountSellerTable green":"accountSellerTable"}>${usersBid}</td>
            <td className="accountSellerTable">{props.item.bids.length}</td>
            <td className={maxBid?"accountSellerTable green":"accountSellerTable blue"}>${props.item.bids[props.item.bids.length-1].amount}</td>
            <td className="accountSellerTable"><Link className="accountSellerTableLinkButton" to={`/item?id=${props.item.id}`}>VIEW</Link></td>
        </tr>
    )
}

export default AccountBids