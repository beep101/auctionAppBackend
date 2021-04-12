import React, { useContext, useEffect, useState } from 'react'
import { Link } from 'react-router-dom';
import { getActiveItems, getInactiveItems } from '../apiConsumer/itemFetchConsumer';
import AuthContext from '../context';

function AccountSeller(props){
    const [active, setActive]=useState([]);
    const [inactive,setInactive]=useState([]);
    const [activeSelected,setActiveSelected]=useState(true);

    const context=useContext(AuthContext)

    useEffect(()=>{
        const token=context.jwt;
        getActiveItems(token,(success,data)=>{
            if(success){
                setActive(data);
            }else{
                setActive([]);
            }
        });
        getInactiveItems(token,(success,data)=>{
            if(success){
                setInactive(data);
            }else{
                setInactive([]);
            }
        });
    },[])

    if(active.length==0&&inactive.length==0)
        return(
            <div className="notSellerContainer">
                <div className="notSellerTitle">SELL</div>
                <img className="notSellerImg" src="images/bag.svg"></img>
                <div className="notSellerLabel">You do not have any scheduled items for sale.</div>
                <Link className="notSellerButton" to="/addItem">Become seller</Link>
            </div>
        )
    else
        return(
            <div>
                <div className="accountSellerButtonBar">
                    <div>
                        <div className={activeSelected?'accountSellerButtonSelected':'accountSellerButton'} onClick={()=>setActiveSelected(true)}>Active</div>
                        <div className={activeSelected?'accountSellerButton':'accountSellerButtonSelected'} onClick={()=>setActiveSelected(false)}>Sold</div>
                    </div>
                    <Link to="/addItem" className='accountSellerButtonSelected'><img className="accountNavbarIcon" src="images/add_white.svg"></img><span>Add Item</span></Link>
                </div>
                {activeSelected?<ActiveItems items={active}/>:<InactiveItems items={inactive}/>}
                
            </div>
        )
}

function ActiveItems(props){
    const [items,setItems]=useState([]);

    useEffect(()=>{
        setItems(props.items);
    },[props.items]);

    if(items.length)
        return(
            <table className="accountSellerTable">
                <tr className="accountSellerTableMedium">
                    <th className="accountSellerTable">Item</th>
                    <th className="accountSellerTable left">Name</th>
                    <th className="accountSellerTable">Time left</th>
                    <th className="accountSellerTable">Starting price</th>
                    <th className="accountSellerTable">No.bids</th>
                    <th className="accountSellerTable">Highest bid</th>
                    <th className="accountSellerTable"></th>
                </tr>
                {items.map(item=><ActiveItem item={item}></ActiveItem>)}
            </table>
        )
    else
        return(
            <div className="accountSellerNoContent">No items currently active</div>
        )

}

function InactiveItems(props){
    const [items,setItems]=useState([]);

    useEffect(()=>{
        setItems(props.items);
    },[props.items]);

    if(items.length)
        return(
            <table className="accountSellerTable">
                <tr className="accountSellerTableMedium">
                    <th className="accountSellerTable">Item</th>
                    <th className="accountSellerTable left">Name</th>
                    <th className="accountSellerTable">End date</th>
                    <th className="accountSellerTable">Starting price</th>
                    <th className="accountSellerTable">No.bids</th>
                    <th className="accountSellerTable">Highest bid</th>
                    <th className="accountSellerTable"></th>
                </tr>
                {items.map(item=><InactiveItem item={item}></InactiveItem>)}
            </table>
        )
    else
        return(
            <div className="accountSellerNoContent">No sold items yet</div>
        )
}

function ActiveItem(props){
    const [timeLeft, setTimeLeft]=useState("");

    useEffect(()=>{
        setTimeLeft(timeDiff())
    },[props.item.endtime])

    const timeDiff=()=>{
        let t = props.item.endtime.split(/[-T:.]/);
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

    return(
        <tr className="accountSellerTable">
            <td className="accountSellerTable center"><div className="accountSellerTableImgContainer"><img className="accountSellerTable" src={props.item.images[0]}></img></div></td>
            <td className="accountSellerTable left">{props.item.name}</td>
            <td className="accountSellerTable">{timeLeft}</td>
            <td className="accountSellerTable">${props.item.startingprice}</td>
            <td className="accountSellerTable">{props.item.bids.length}</td>
            <td className="accountSellerTable">{props.item.bids.length?`$${props.item.bids[props.item.bids.length-1].amount}`:'None'}</td>
            <td className="accountSellerTable"><Link className="accountSellerTableLinkButton" to={`/item?id=${props.item.id}`}>View</Link></td>
        </tr>
    )

}

function InactiveItem(props){
    const [enddate,setEnddate]=useState("");

    useEffect(()=>{
        setEnddate(formatDate());
    },[props.item.endtime]);

    const formatDate=()=>{
        const t = props.item.endtime.split(/[-T:.]/);
        const months = [ "January", "February", "March", "April", "May", "June", 
           "July", "August", "September", "October", "November", "December" ];

        return `${Number.parseInt(t[2])}. ${months[Number.parseInt(t[1])-1]} ${t[0]}.`
    }

    return(
        <tr className="accountSellerTable">
            <td className="accountSellerTable center"><div className="accountSellerTableImgContainer"><img className="accountSellerTable" src={props.item.images[0]}></img></div></td>
            <td className="accountSellerTable left">{props.item.name}</td>
            <td className="accountSellerTable">{enddate}</td>
            <td className="accountSellerTable">${props.item.startingprice}</td>
            <td className="accountSellerTable">{props.item.bids.length}</td>
            <td className="accountSellerTable">{props.item.bids.length?`$${props.item.bids[props.item.bids.length-1].amount}`:'None'}</td>
            <td className="accountSellerTable"><Link className="accountSellerTableLinkButton" to={`/item?id=${props.item.id}`}>View</Link></td>
        </tr>
    )

}

export default AccountSeller