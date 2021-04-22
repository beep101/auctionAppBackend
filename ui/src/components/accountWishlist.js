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
            <tr className="accountSellerTableMedium">
                <th className="accountSellerTable">Item</th>
                <th className="accountSellerTable left">Name</th>
                <th className="accountSellerTable">Time left</th>
                <th className="accountSellerTable">Highest bid</th>
                <th className="accountSellerTable">Status</th>
                <th className="accountSellerTable"></th>
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
        <tr className="accountSellerTable">
            <td className="accountSellerTable center"><div className="accountSellerTableImgContainer"><img className="accountSellerTable" src={props.wish.item.images[0]}></img></div></td>
            <td className="accountSellerTable left">{props.wish.item.name}</td>
            <td className="accountSellerTable">{timeLeft}</td>
            <td className="accountSellerTable">{props.wish.item.bids.length?`$${props.wish.item.bids[props.wish.item.bids.length-1].amount}`:'None'}</td>
            <td className="accountSellerTable"><div className={isActive?"openWish":"closedWish"}>{isActive?'OPEN':'CLOSED'}</div></td>
            <td className="accountSellerTable"><Link className="accountSellerTableLinkButton" to={`/item?id=${props.wish.item.id}`}>View</Link></td>
        </tr>
    )
}

export default AccountWishlist