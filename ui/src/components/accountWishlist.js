import React, { useContext, useEffect, useState } from 'react'
import { Link } from 'react-router-dom';
import { getAllWishes } from '../apiConsumer/wishlistConsumer';
import AuthContext from '../context';
import { timeDiffTodayToDateString } from '../utils/functions';

function AccountWishlist(props){
    const [wishes,setWishes]=useState([]);
    const context=useContext(AuthContext)

    useEffect(()=>{
        getAllWishes(context.jwt,(success,data)=>{
            if(success)
                setWishes(data);
        })
    },[])

    return(
        <table className="accountSellerTable">
            <tr className="accountSellerTableMediumTr">
                <th className="accountSellerTableTh">Item</th>
                <th className="accountSellerTableTh left">Name</th>
                <th className="accountSellerTableTh">Time left</th>
                <th className="accountSellerTableTh">Highest bid</th>
                <th className="accountSellerTableTh">Status</th>
                <th className="accountSellerTableTh"></th>
            </tr>
            {wishes.map(wish=>(<WishItem wish={wish}/>))}
        </table>
    )
}

function WishItem(props){
    const [timeLeft, setTimeLeft]=useState("");
    const [isActive,setIsActive]=useState(true);

    useEffect(()=>{
        setTimeLeft(timeDiff())
        setIsActive(checkIfActive())
    },[props.wish.item])

    const timeDiff=()=>{
        return timeDiffTodayToDateString(props.wish.item.endtime);
    }

    const checkIfActive=()=>{
        let dateComponent = props.wish.item.endtime.split(/[-T:.]/);
        let endtime = new Date(Date.UTC(dateComponent[0], dateComponent[1]-1, dateComponent[2],
                                        dateComponent[3], dateComponent[4], dateComponent[5]));
        if(props.wish.item.sold||endtime.getTime()<(new Date()).getTime())
            return false;
        return true;
    }

    return(
        <tr className="accountSellerTableTr">
            <td className="accountSellerTableTd center"><div className="accountSellerTableImgContainer"><img className="accountSellerTableImg" src={props.wish.item.images[0]}></img></div></td>
            <td className="accountSellerTableTd left">{props.wish.item.name}</td>
            <td className="accountSellerTableTd">{timeLeft}</td>
            <td className="accountSellerTableTd">{props.wish.item.bids.length?`$${props.wish.item.bids[props.wish.item.bids.length-1].amount}`:'None'}</td>
            <td className="accountSellerTableTd"><div className={isActive?"openWish":"closedWish"}>{isActive?'OPEN':'CLOSED'}</div></td>
            <td className="accountSellerTableTd"><Link className="accountSellerTableLinkButton" to={`/item?id=${props.wish.item.id}`}>View</Link></td>
        </tr>
    )
}

export default AccountWishlist