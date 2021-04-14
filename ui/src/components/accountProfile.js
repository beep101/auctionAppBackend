import React, { useContext, useRef, useState } from 'react';
import AuthContext from '../context';
import {  PHONE_REGEX_PATTERNS } from '../utils/constants';
import {validateEmail} from '../utils/functions';
import {ONE_YEAR_MILIS} from '../utils/constants';
import UserEditor from './accountProfileUserEditor';
import PayMethodEditor from './accountProfilePayMethodEditor';
import AddressEditor from './accountProfileAddressEditor'
import { addUserAddress, addUserPayMethod, modUserAddress, modUserData, modUserPayMethod } from '../apiConsumer/accountEditor';
import {refresh} from '../apiConsumer/accountConsumer'

function AccountProfile(props){
    const context=useContext(AuthContext);

    const payEmpty=useRef(context.user.user.payMethod?false:true);
    const addressEmpty=useRef(context.user.user.address?false:true);

    const userData=useRef({
        email:context.user.user.email,
        firstName:context.user.user.firstName,
        lastName:context.user.user.lastName,
        birthday:context.user.user.birthday?new Date(context.user.user.birthday):null,
        gender:context.user.user.gender
    });
    const [userMsg,setUserMsg]=useState({});

    const addressData=useRef(context.user.user.address?context.user.user.address:{
        address:"",
        city:"",
        country:"",
        phone:"",
        zip:""
    });
    const [addressMsg,setAddressMsg]=useState({});

    const payMethodData=useRef(context.user.user.payMethod?context.user.user.payMethod:{
        onCardName:"",
	    cardNumber:"",
	    cvccw:"",
	    expMonth:"",
	    expYear:""
    });
    const [payMethodMsg,setPayMethodMsg]=useState({});

    const changeUserData=(data)=>{
        userData.current=data;
        let msg={}
        if(!userData.current.firstName)
            msg.firstName="First name cannot be empty";
        if(!userData.current.lastName)
            msg.lastName="Last name cannot be empty";
        if(!userData.current.email)
            msg.email="Email can't be empty";
        if(!validateEmail(userData.current.email))
            msg.email="Email must be in valid format";
        if(userData.current.gender)
            if(userData.current.gender.toUpperCase()!='M'&&userData.current.gender.toUpperCase()!='F')
                msg.gender="Gender must be male or female";
        let lowerBound=new Date();
        lowerBound.setTime(lowerBound-100*ONE_YEAR_MILIS)
        if(userData.current.birthday.getTime()<lowerBound.getTime())
            msg.birthday="User must be younger than 100 years";
        let upperBound=new Date();
        upperBound.setTime(upperBound-13*ONE_YEAR_MILIS)
        if(userData.current.birthday.getTime()>upperBound.getTime())
            msg.birthday="User must be older than 13 years";
        setUserMsg(msg);
    }

    const changeAddressData=(data)=>{
        addressData.current=data;
        let msg={}
        let allEmpty=true;
        if(!addressData.current.address)
            msg.address="Address can't be empty";
        else
            allEmpty=false;
        if(!addressData.current.city)
            msg.city="City name can't be empty";
        else
            allEmpty=false;
        if(!addressData.current.country)
            msg.country="Country name can't be empty";
        else
            allEmpty=false;
        if(!addressData.current.zip)
            msg.zip="ZIP code can't be empty";
        else
            allEmpty=false;
        if(addressData.current.phone){
            allEmpty=false;
            let isPhoneValid=false;
            for(const pattern in PHONE_REGEX_PATTERNS){
                let regex=new RegExp(pattern);
                if(regex.test(addressData.current.phone))
                    isPhoneValid=true;
            }
            if(!isPhoneValid){
                msg.phone="Phone number format is invalid";
            }
        }
        if(allEmpty){
            setAddressMsg({});
            addressEmpty.current=true;
            return;
        }else{
            addressEmpty.current=false;
        }
        setAddressMsg(msg);
    }

    const changePayMethodData=(data)=>{
        payMethodData.current=data;
        let msg={}
        let allEmpty=true;
        if(!payMethodData.current.onCardName)
            msg.onCardName="Name on card cannot be empty";
        else
            allEmpty=false;
        if(!payMethodData.current.cardNumber)
            msg.cardNumber="Card number cannot be empty";
        else
            allEmpty=false;
        if(!payMethodData.current.cvccw)
            msg.cvccw="CVC/CW cannot be empty";
        else
            allEmpty=false;
        if(!payMethodData.current.expMonth)
            msg.expDate="Expire date cannot be empty";
        else
            allEmpty=false;
        if(!payMethodData.current.expYear)
            msg.expDate="Expire date cannot be empty";
        else
            allEmpty=false;
        const crrDate=new Date();
        if(crrDate.getFullYear()>payMethodData.current.expYear
            ||(crrDate.getFullYear()==payMethodData.current.expYear
            &&crrDate.getMonth()>=payMethodData.current.expMonth-1))
            msg.expDate="Card cannot be expired";
        if(allEmpty){
            setPayMethodMsg({})
            payEmpty.current=true;
            return;
        }else{
            payEmpty.current=false;
        }
        setPayMethodMsg(msg)
    }

    const save=()=>{
        modUserData(userData.current,context.jwt,(success,data)=>{
            if(success){
                refresh(context.jwt,(success,data)=>{
                    if(success){
                        context.login(data);
                        localStorage.removeItem('token');
                        localStorage.setItem('token',data);
                    }
                })
            }
        })

        if(!addressEmpty.current){
            if(context.user.user.address){
                addressData.current.id=context.user.user.address.id;
                modUserAddress(addressData.current,context.jwt,(success,data)=>{
                    if(success){
                        refresh(context.jwt,(success,data)=>{
                            if(success){
                                context.login(data);
                                localStorage.removeItem('token');
                                localStorage.setItem('token',data);
                            }
                        })
                    }
                })
            }else{
                addUserAddress(addressData.current,context.jwt,(success,data)=>{
                    if(success){
                        refresh(context.jwt,(success,data)=>{
                            if(success){
                                context.login(data);
                                localStorage.removeItem('token');
                                localStorage.setItem('token',data);
                            }
                        })
                    }
                })
            }
        }else{
            count+=1;
        }
        if(!payEmpty.current){
            if(context.user.user.payMethod){
                payMethodData.current.id=context.user.user.payMethod.id;
                modUserPayMethod(payMethodData.current,context.jwt,(success,data)=>{
                    if(success){
                        refresh(context.jwt,(success,data)=>{
                            if(success){
                                context.login(data);
                                localStorage.removeItem('token');
                                localStorage.setItem('token',data);
                            }
                        })
                    }
                })
            }else{
                addUserPayMethod(payMethodData.current,context.jwt,(success,data)=>{
                    if(success){
                        refresh(context.jwt,(success,data)=>{
                            if(success){
                                context.login(data);
                                localStorage.removeItem('token');
                                localStorage.setItem('token',data);
                            }
                        })
                    }
                })
            }
        }

    }
    return(
        <div>
            <UserEditor data={userData.current} msg={userMsg} change={changeUserData}/>
            <AddressEditor data={addressData.current} msg={addressMsg} change={changeAddressData}/>
            <PayMethodEditor data={payMethodData.current} msg={payMethodMsg} change={changePayMethodData}/>
            <div className="accountProfileButtonContainer">
                <div className="bidButton width10vw" onClick={save}>Save Info</div>
            </div>
        </div>
    )
}

export default AccountProfile