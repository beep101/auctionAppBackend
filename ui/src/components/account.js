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
                <Link className={tab=='profile'?"accountNavbarButtonSelected":"accountNavbarButton"} to='/account?tab=profile'>
                    <img className="accountNavbarIcon" src={tab=='profile'?"images/profile_white.svg":"images/profile_darker.svg"}></img>
                    <span>Profile</span>
                </Link>
                <Link className={tab=='seller'?"accountNavbarButtonSelected":"accountNavbarButton"} to='/account?tab=seller'>
                    <img className="accountNavbarIcon" src={tab=='seller'?"images/list_icon_white.svg":"images/list_icon_darker.svg"}></img>
                    <span>Seller</span>
                </Link>
                <Link className={tab=='bids'?"accountNavbarButtonSelected":"accountNavbarButton"} to='/account?tab=bids'>
                    <img className="accountNavbarIcon" src={tab=='bids'?"images/bid_white.svg":"images/bid_darker.svg"}></img>
                    <span>Bids</span>
                </Link>
                <Link className={tab=='watchlist'?"accountNavbarButtonSelected":"accountNavbarButton"} to='/account?tab=watchlist'>
                    <img className="accountNavbarIcon" src={tab=='watchlist'?"images/wishlist_packet_white.svg":"images/wishlist_packet_darker.svg"}></img>
                    <span>Watchlist</span>
                </Link>
                <Link className={tab=='settings'?"accountNavbarButtonSelected":"accountNavbarButton"} to='/account?tab=settings'>
                    <img className="accountNavbarIcon" src={tab=='settings'?"images/cog_white.svg":"images/cog_darker.svg"}></img>
                    <span>Settings</span>
                </Link>
            </div>
            <div>
                {tab=="seller"&&<AccountSeller/>}
                {tab=="bids"&&<AccountBids/>}
            </div>
        </div>
    )

}

export default Account