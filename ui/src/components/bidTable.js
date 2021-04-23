import React, { useEffect, useState } from 'react';

function BidTable(props){
    const [bids,setBids]=useState([]);

    useEffect(()=>{
        if(props.bids)
            setBids(props.bids);
        else
            setBids([]);
    },[props.bids])

    return(
        <table className="bidTable">
            <col className="bidTableColumn1"/>
            <col className="bidTableColumn2"/>
            <col className="bidTableColumn3"/>
            <thead/>
            <tr className="bidTableTr">
                <th className="bidTableTh">Name</th>
                <th className="bidTableTh">Date</th>
                <th className="bidTableTh">Amount</th>
            </tr>
            {bids.map((bid)=><BidLine bid={bid}/>)}
        </table>
    )
}


function BidLine(props){
    const [name,setName]=useState("");
    const [time,setTime]=useState("");
    const [amount,setAmount]=useState("")

    useEffect(()=>{
        const timeString = props.bid.time.split(/[-T:.]/);
        const timeObj = new Date(Date.UTC(timeString[0], timeString[1]-1, timeString[2], timeString[3], timeString[4], timeString[5]));
        
        const year = new Intl.DateTimeFormat('en', { year: 'numeric' }).format(timeObj);
        const month = new Intl.DateTimeFormat('en', { month: 'long' }).format(timeObj);
        const day = new Intl.DateTimeFormat('en', { day: 'numeric' }).format(timeObj);
        
        setName(`${props.bid.bidder.firstName} ${props.bid.bidder.lastName}`);
        setTime(`${day}. ${month}. ${year}.`);
        setAmount(`$ ${props.bid.amount}`)
    },[props.bid])
    

    return(
        <tr className="bidTableTr">
            <td className="bidTableTd">{name}</td>
            <td className="bidTableTd">{time}</td>
            <td className="bidTableTd">{amount}</td>
        </tr>
    )
}

export default BidTable