import React, { useEffect, useRef, useState } from 'react';
import DayPickerInput from 'react-day-picker/DayPickerInput';
import {ONE_DAY_MILIS} from '../utils/constants';
import "react-day-picker/lib/style.css";
import "../styles/styles.css";

function AddItemStep2(props){
    let [disabledStart,setDisabledStart]=useState(new Date());
    useEffect(()=>{
        let ds=new Date();
        ds.setHours(12,0,0,0)
        setDisabledStart(ds);
    },[]);

    let [disabledEnd,setDisabledEnd]=useState(new Date());
    useEffect(()=>{
        let de=new Date();
        de.setTime(de+ONE_DAY_MILIS);
        de.setHours(12,0,0,0);
        setDisabledEnd(de)
    },[]);


    const [startDate,setStartDate]=useState(new Date());
    useEffect(()=>{
        if(props.data.startDate)
            if(typeof props.data.startDate === 'string' || props.data.startDate instanceof String)
                startDateChange(new Date(props.data.startDate));
            else
                startDateChange(props.data.startDate);
        else{
            let sd=new Date();
            sd.setHours(12,0,0,0);
            setStartDate(sd);
        }
    },[]);

    const [endDate,setEndDate]=useState(new Date());
    useEffect(()=>{
        if(props.data.endDate){
            if(typeof props.data.endDate === 'string' || props.data.endDate instanceof String)
                endDateChange(new Date(props.data.endDate));
            else
                endDateChange(props.data.endDate);
        }else{
            let ed=new Date();
            ed.setTime(ed.getTime()+5*ONE_DAY_MILIS);
            ed.setHours(12,0,0,0);
            setEndDate(ed);
        }
    },[]);

    const [startingPrice,setStartingPrice]=useState(props.data.startingPrice?props.data.startingPrice:0.01)
    const [msg,setMsg]=useState({})

    const onChange=(e)=>{
        if(e.target.name=="startingPrice")
            setStartingPrice(e.target.value);
        let newMsg={
            startingPrice:undefined,
            startDate:msg.startDate,
            endDate:msg.endDate
        };
        setMsg(newMsg);
    }

    const onNext=()=>{
        let valid=true;
        let msg={}
        if(!startingPrice||startingPrice<0.01){
            valid=false;
            msg.startingPrice="Start price can't be empty";
        }
        if(startDate.getTime()<disabledStart.getTime()){
            valid=false;
            msg.startDate="Invalid start date, date must be today or later";
        }
        if(endDate.getTime()<=startDate.getTime()){
            valid=false;
            msg.endDate="Invalid end date, must be after start day";
        }
        let data={
            startDate:startDate,
            endDate:endDate,
            startingPrice:startingPrice
        }
        setMsg(msg);
        if(valid)
            props.next(data);
    }
    const onBack=()=>{
        let data={
            startDate:startDate,
            endDate:endDate,
            startingPrice:startingPrice
        }
        props.back(data);   
    }

    const startDateChange=(date)=>{
        let newMsg={
            startDate:undefined,
            startingPrice:msg.startingPrice,
            endDate:msg.endDate
        };

        setStartDate(date);
        let de=new Date();
        de.setTime(date.getTime()+ONE_DAY_MILIS);
        setDisabledEnd(de);

        if(date.getTime()>=endDate.getTime()){
            let ed=new Date();
            ed.setTime(date.getTime()+ONE_DAY_MILIS);
            setEndDate(ed);
            newMsg.endDate=undefined;
        }

        if(date.getTime()<disabledStart.getTime()){
            newMsg.startDate="Invalid start date, date must be today or later";
        }
        setMsg(newMsg);
    }

    const endDateChange=(date)=>{
        setEndDate(date);
        let newMsg={
            startDate:msg.startDate,
            startingPrice:msg.startingPrice,
            endDate:undefined
        };
        if(date.getTime()<disabledEnd.getTime()||date.getTime()<=startDate.getTime()){
            newMsg.endDate="Invalid end date, must be after start day";
        }
        setMsg(newMsg);
    }

    return(
        <div className="formContainer">
            <div className="inputFieldContainer verticalFlex">
                <label className="inputLabel">Your start price</label><br/>
                <input className="inputFieldWide" id="startingPrice" name="startingPrice" type="number" onChange={onChange} value={startingPrice}/>
                {msg.startingPrice&&<div className="warningMessageInputLabel">{msg.startingPrice}</div>}
            </div>
            <div className="inputFieldContainer">
                <div className="dateInputContainer">
                    <div className="inputFieldContainer verticalFlex">
                        <label className="inputLabel">Start date</label><br/>
                        <DayPickerInput onDayChange={startDateChange} value={startDate} inputProps={{ className: 'dateInputField' }} disabledDays={[{ before: disabledStart }]}/>
                    </div>
                    <div className="inputFieldContainer verticalFlex">
                        <label className="inputLabel">End date</label><br/>
                        <DayPickerInput onDayChange={endDateChange} value={endDate} inputProps={{ className: 'dateInputField' }} disabledDays={[{ before: disabledEnd }]} />
                    </div>
                </div>
                {msg.startDate&&<div className="warningMessageInputLabel">{msg.startDate}</div>}
                {msg.endDate&&<div className="warningMessageInputLabel">{msg.endDate}</div>}
            </div>
            <div className="auctionDatesMessageContainer">
                The auction will be automatically closed when the end time comes. The highest bid will win the auction.
            </div>
            <div className="inputFieldContainer categorySelectsInline">
                <span className="categorySelectContainer">
                    <div className="bidButton lightGrayBorder" onClick={onBack}>
                        Back
                    </div>
                </span>
                <span className="categorySelectContainer">
                    <div className="bidButton" onClick={onNext}>
                        Next
                    </div>
                </span>
            </div>
        </div>
    )
}

export default AddItemStep2