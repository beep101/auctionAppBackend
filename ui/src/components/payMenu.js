import React, { useContext, useEffect, useRef, useState } from 'react';
import ReactDOM from "react-dom";
import { getClientTokens } from '../apiConsumer/payPalIntegrationConsumer';
import AuthContext from '../context';

export default function PayMenu(props){
    const context=useContext(AuthContext);

    const [ready,setReady]=useState(false);
    const [msg,setMsg]=useState('Not ready yet')

    const PayButton=useRef();
    useEffect(()=>{
        getClientTokens(context.jwt,(success,data)=>{
            if(success){
                const merchantId=props.item.seller.merchantId;
                const script = document.createElement("script");
                script.src=`https://www.paypal.com/sdk/js?&client-id=${data.client_id}&merchant-id=${data.client_merchant_id}`
                //script.src = `https://www.paypal.com/sdk/js?components=hosted-fields&client-id=${data.client_id}&merchant-id=${merchantId}&currency=USD&intent=capture`;
                //script.dataset.partnerAttributionId=data.bncode;
                //script.dataset.dataClientToken=data.client_token;
                script.async = false;
                document.body.appendChild(script);
                script.onload = () => { 
                    setReady(true);
                };
            }else{
                setMsg('Service unavaliable')
            }
        })
    },[]);

    if(ready){
        PayButton.current = window.paypal.Buttons.driver("react", { React, ReactDOM });
    }
    const createOrder=(data,actions)=>{
        return fetch('/api/createOrder',{
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${context.jwt}`
            },
            body: JSON.stringify(props.item)
        }).then(function(res){
            return res.json()
        }).then(function(data){
            return data.orderId;
        });
    }

    const onApprove=(data,actions)=>{
        return fetch(`/api/captureOrder/${data.orderID}`,{
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${context.jwt}`
            }
        }).then(function(res) {
            if (!res.ok) {
                alert('Something went wrong');
            }
        });
    }

    return(
        !ready?<div>{msg}</div>:
        <div >
            <PayButton.current
                createOrder={(data, actions) => createOrder(data, actions)}
                onApprove={(data, actions) => onApprove(data, actions)}
            />
        </div>
    )
}