import React from 'react';

class BidLine extends React.Component{
    render(){
        const timeString = this.props.bid.time.split(/[-T:.]/);
        const timeObj = new Date(Date.UTC(timeString[0], timeString[1]-1, timeString[2], timeString[3], timeString[4], timeString[5]));
        
        const year = new Intl.DateTimeFormat('en', { year: 'numeric' }).format(timeObj);
        const month = new Intl.DateTimeFormat('en', { month: 'long' }).format(timeObj);
        const day = new Intl.DateTimeFormat('en', { day: 'numeric' }).format(timeObj);
        return(
            <tr className="bidTable">
                <td className="bidTable">{this.props.bid.bidder.firstName} {this.props.bid.bidder.lastName}</td>
                <td className="bidTable">{day} {month} {year}</td>
                <td className="bidTable">$ {this.props.bid.amount}</td>
            </tr>
        )
    }
}

export default BidLine