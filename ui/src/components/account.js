import React, { useEffect, useState } from 'react';
import queryString from 'query-string';
import { Link } from 'react-router-dom';
import AccountSeller from './accountSeller';
import AccountBids from './accountBids';

function Account(props){

    const [tab,setTab]=useState('profile');

    useEffect(()=>{
        let tab=queryString.parse(props.location.search)['tab'];
        if(!tab)
            tab='profile'
        setTab(tab);
    },[props.location]);

    return(
        <div className='accountContainer'>
            <div className='accountNavbar'>
                <AccountTabButton tab={tab} name='profile' img='profile' value='Profile'/>
                <AccountTabButton tab={tab} name='seller' img='list_icon' value='Seller'/>
                <AccountTabButton tab={tab} name='bids' img='bid' value='Bids'/>
                <AccountTabButton tab={tab} name='watchlist' img='wishlist_packet' value='Watchlist'/>
                <AccountTabButton tab={tab} name='settings' img='cog' value='Settings'/>
            </div>
            <div>
                {tab=="seller"&&<AccountSeller/>}
                {tab=="bids"&&<AccountBids/>}
            </div>
        </div>
    )

}

function AccountTabButton(props){
    const [tab,setTab]=useState(props.tab)
    
    useEffect(()=>{
        setTab(props.tab);
    },[props.tab]);

    return(
        <Link className={tab===props.name?'accountNavbarButtonSelected':'accountNavbarButton'} to={`/account?tab=${props.name}`}>
            <img className="accountNavbarIcon" src={tab===props.name?`images/${props.img}_white.svg`:`images/${props.img}_darker.svg`}></img>
            <span>{props.value}</span>
        </Link>
    )

}

export default Account