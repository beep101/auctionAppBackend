import React, { useContext, useEffect, useRef, useState } from 'react';
import ReactDOM from "react-dom";
import { getClientTokens } from '../apiConsumer/payPalIntegrationConsumer';
import AuthContext from '../context';
import PulseLoader from "react-spinners/PulseLoader";
import { css } from "@emotion/core";

export default function PayMenu(props){
    const context=useContext(AuthContext);

    const [ready,setReady]=useState(false);
    const [msg,setMsg]=useState('Not ready yet')

    const PayButton=useRef();
    useEffect(()=>{
        getClientTokens(context.jwt,(success,data)=>{
            if(success){
                const script = document.createElement("script");
                script.src=`https://www.paypal.com/sdk/js?&client-id=${data.client_id}&merchant-id=${data.client_merchant_id}`
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
        return fetch(`/api/items/${props.item.id}/order`,{
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${context.jwt}`
            },
            body: '{}'
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
            if (res.ok) {
                props.setPaid();
            }else{
                alert('Something went wrong');
            }
        });
    }
    
    return(
        !ready?
        <div className="loadingDotsContainer">
            <PulseLoader color="#8367D8" css={css} size={10}/>
        </div>:
            <PayButton.current
            createOrder={(data, actions) => createOrder(data, actions)}
            onApprove={(data, actions) => onApprove(data, actions)}
        />
    )
}