import React, { useContext, useEffect, useState } from 'react'
import { Link } from 'react-router-dom';
import { getBiddedItems } from '../apiConsumer/itemFetchConsumer';
import AuthContext from '../context';
import { timeDiffTodayToDateString } from '../utils/functions';

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
            <tr className="accountSellerTableMediumTr">
                <th className="accountSellerTableTh">Item</th>
                <th className="accountSellerTableTh left">Name</th>
                <th className="accountSellerTableTh">Time left</th>
                <th className="accountSellerTableTh">Your price</th>
                <th className="accountSellerTableTh">No.bids</th>
                <th className="accountSellerTableTh">Highest bid</th>
                <th className="accountSellerTableTh"></th>
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
        return timeDiffTodayToDateString(props.item.endtime);
    }

    return(
        <tr className="accountSellerTableTr">
            <td className="accountSellerTableTd center"><div className="accountSellerTableImgContainer"><img className="accountSellerTableImg" src={props.item.images[0]}></img></div></td>
            <td className="accountSellerTableTd left">{props.item.name}</td>
            <td className="accountSellerTableTd">{timeLeft}</td>
            <td className={maxBid?"accountSellerTableTd green":"accountSellerTableTd"}>${usersBid}</td>
            <td className="accountSellerTableTd">{props.item.bids.length}</td>
            <td className={maxBid?"accountSellerTableTd green":"accountSellerTableTd blue"}>${props.item.bids[props.item.bids.length-1].amount}</td>
            <td className="accountSellerTableTd"><Link className="accountSellerTableLinkButton" to={`/item?id=${props.item.id}`}>VIEW</Link></td>
        </tr>
    )
}

export default AccountBids