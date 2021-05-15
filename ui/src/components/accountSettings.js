import React, { useContext, useState } from 'react';
import ReactModal from 'react-modal';
import { deactivateAccount, switchPushNotificationsAllowed } from '../apiConsumer/accountEditor';
import AuthContext from '../context';
import { MODAL_CONTENT, MODAL_OVERLAY } from '../styles/modelStyles';

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

    const [modalActive,setModalActive]=useState(false);

    const logout=()=>{
        context.logout();
        localStorage.removeItem('token');
    }

    return(
        <div className="formContainer" >
            <ReactModal isOpen={modalActive} style={{overlay:MODAL_OVERLAY,content: MODAL_CONTENT}}>
                <div className="formContainerTitle">Logout</div>
                <div className="notSellerLabel">
                    <p>Are you sure you want to log out?</p>
                </div>
                <div className="modalButtons">
                    <span className="bidButton" onClick={logout}>Yes</span>
                    <span className="bidButton" onClick={()=>setModalActive(false)}>No</span>
                </div>
            </ReactModal>
            <div className="formContainerTitle">Logout</div>
            <div className="inputFieldContainer selfAlignLeft">
                <label className="notSellerLabel paddingBtm20 paddingTop10">
                    Press the button below to logout from the device.
                </label><br/>
                <div className="bidButton width10vw" onClick={()=>setModalActive(true)}>
                    Logout
                </div>
            </div>
        </div>
    )
}

function DeactivateButton(){
    const SOFT=false;
    const PERMANENT=true;

    const context=useContext(AuthContext);

    const [modalActive,setModalActive]=useState(false);
    const [errorModalActive,setErrorModalActive]=useState(false);

    const deactivate=(permanent)=>{
        deactivateAccount(context.jwt,context.user.jti,permanent,(success,data)=>{
            setModalActive(false);
            if(success){
                context.logout();
                localStorage.removeItem('token');
            }else{
                console.log('error');
                setErrorModalActive(true);
            }
        });
    }

    return(
        <div className="formContainer" >
            <ReactModal isOpen={modalActive} style={{overlay:MODAL_OVERLAY,content: MODAL_CONTENT}}>
                <div className="formContainerTitle">Deactivate account</div>
                <div className="notSellerLabel">
                    <p>Deactivate will deactivate account temporarly</p>
                    <p>Delete will deactivate account permanently</p>
                    <p>In any case all active bids will be discarded</p>
                </div>
                <div className="modalButtons">
                    <span className="bidButton" onClick={()=>deactivate(SOFT)}>Deactivate</span>
                    <span className="bidButton" onClick={()=>deactivate(PERMANENT)}>Delete</span>
                    <span className="bidButton" onClick={()=>setModalActive(false)}>Close</span>
                </div>
            </ReactModal>
            <ReactModal isOpen={errorModalActive} style={{overlay:MODAL_OVERLAY,content: MODAL_CONTENT}}>
                <div className="formContainerTitle">Delete unsuccessful</div>
                <div className="notSellerLabel">
                    <p>Deactivation did not succeed</p>
                    <p>Check if you have active or unpaid items</p>
                </div>
                <div className="modalButtons">
                    <span className="bidButton" onClick={()=>setErrorModalActive(false)}>Close</span>
                </div>
            </ReactModal>
            <div className="formContainerTitle">Deactivate</div>
            <div className="inputFieldContainer selfAlignLeft">
                <label className="notSellerLabel paddingBtm20 paddingTop10">
                    Do you want to deactivate the account?
                </label><br/>
                <div className="bidButton width10vw" onClick={()=>setModalActive(true)}>
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