import React, { useCallback, useContext, useState } from 'react';
import { Link } from 'react-router-dom';
import AuthContext from '../context';
import '../styles/styles.css'
import Notifications from './notifications';

export default function LoginSignupAccountButtons(props){
    const context=useContext(AuthContext);

    const [isOpened,setOpened]=useState(false);

    const openClose=()=>{
        setOpened(!isOpened);
    }
    
    const closeNotifications=()=>{
        setOpened(false);
    }

    if(!context.jwt)
        return (
            <span>
                <Link to="/login">
                    <span className="textHeaderBar link">
                        Log In
                    </span>
                </Link>
                <span className="textHeaderBar"> or </span>
                <Link to="/register">
                    <span className="textHeaderBar link">
                        Register
                    </span>
                </Link>
            </span>
        );
    else
        return(
            <span className="notifiationsIcon">
                <img className="socialImg" onClick={openClose} src={isOpened?"/images/bell_purple.svg":"/images/bell.svg"}/>
                {isOpened&&<Notifications close={closeNotifications}/>}
            </span>
        );
}