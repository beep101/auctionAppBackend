import React, { useContext, useState } from 'react';
import { switchPushNotificationsAllowed } from '../apiConsumer/accountEditor';
import AuthContext from '../context';

export default function AccountSettings(props){

    return(
        <div>
            <LogoutButton />
            <DeactivateButton />
            <NotificationsSelection />
        </div>
    )
}

function LogoutButton(){
    const context=useContext(AuthContext);

    const logout=()=>{
        context.logout();
        localStorage.removeItem('token');
    }

    return(
        <div className="formContainer" >
            <div className="formContainerTitle">Logout</div>
            <div className="inputFieldContainer selfAlignLeft">
                <label className="notSellerLabel paddingBtm20 paddingTop10">
                    Press the button below to logout from the device.
                </label><br/>
                <div className="bidButton width10vw" onClick={logout}>
                    Logout
                </div>
            </div>
        </div>
    )
}

function DeactivateButton(){
    const context=useContext(AuthContext);

    const deactivate=()=>{
        
    }

    return(
        <div className="formContainer" >
            <div className="formContainerTitle">Deactivate</div>
            <div className="inputFieldContainer selfAlignLeft">
                <label className="notSellerLabel paddingBtm20 paddingTop10">
                    Do you want to deactivate the account?
                </label><br/>
                <div className="bidButton width10vw" onClick={deactivate}>
                    Deactivate
                </div>
            </div>
        </div>
    )
}

function NotificationsSelection(){
    const context=useContext(AuthContext);

    const [notifications,setNotifications]=useState(context.user.user.pushNotifications);

    const change=()=>{
        switchPushNotificationsAllowed(context.jwt,(success,data)=>{
            if(success)
                setNotifications(data.pushNotifications);
        })
    }
    console.log(context)
    return(
        <div className="formContainer" >
            <div className="formContainerTitle">Policy and Community</div>
            <div className="inputFieldContainer selfAlignLeft">
                <label className="notSellerLabel paddingTop10">
                    Receive updates on bids and offers. 
                </label>
                <label className="notSellerLabel paddingBtm20 ">
                    Stay informed trough.
                </label><br/>
                <label className="notSellerLabel">
                    <input type="checkbox"
                        checked={notifications}
                        onChange={change}
                    />
                    Push notifications
                </label>
            </div>
        </div>
    )
}