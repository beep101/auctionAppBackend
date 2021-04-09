import React, { useEffect, useState } from 'react';
import queryString from 'query-string';
import { Link } from 'react-router-dom';

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
                <Link className={tab=='profile'?"accountNavbarButtonSelected":"accountNavbarButton"} to='/account?tab=profile'>Profile</Link>
                <Link className={tab=='seller'?"accountNavbarButtonSelected":"accountNavbarButton"} to='/account?tab=seller'>Seller</Link>
                <Link className={tab=='bids'?"accountNavbarButtonSelected":"accountNavbarButton"} to='/account?tab=bids'>Bids</Link>
                <Link className={tab=='watchlist'?"accountNavbarButtonSelected":"accountNavbarButton"} to='/account?tab=watchlist'>Watchlist</Link>
                <Link className={tab=='settings'?"accountNavbarButtonSelected":"accountNavbarButton"} to='/account?tab=settings'>Settings</Link>
            </div>
            <div>
                {tab}
            </div>
        </div>
    )

}

export default Account