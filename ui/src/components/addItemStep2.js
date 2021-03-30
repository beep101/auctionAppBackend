import React, { useRef, useState } from 'react';
import DayPickerInput from 'react-day-picker/DayPickerInput';
import "react-day-picker/lib/style.css";
import "../styles/styles.css";

function AddItemStep2(props){
    let disabledStart=new Date();
    disabledStart.setHours(12,0,0,0);
    let disabledEnd=new Date();
    disabledEnd.setHours(12,0,0,0);
    disabledEnd.setTime(disabledStart.getTime()+24*60*60*1000);

    let sd=new Date();
    sd.setHours(12,0,0,0);
    const [startDate,setStartDate]=useState(sd);
    let ed=new Date();
    ed.setTime(startDate.getTime()+5*24*60*60*1000);
    ed.setHours(12,0,0,0);
    const [endDate,setEndDate]=useState(ed);

    let data=useRef({
        starttime:props.starttime,
        endtime:props.endtime,
        startingprice:props.startingprice
    });
    
    const [msg,setMsg]=useState({})

    const onChange=(e)=>{
        data.current[e.target.name]=e.target.value;
        let newMsg={
            startingprice:undefined,
            startDate:msg.startDate,
            endDate:msg.endDate
        };
        setMsg(newMsg);
    }

    const onNext=()=>{
        let valid=true;
        let msg={}
        if(!data.current.startingprice||data.current.startingprice<0.01){
            valid=false;
            msg.startingprice="Start price can't be empty";
        }
        if(startDate.getTime()<disabledStart.getTime()){
            valid=false;
            newMsg.startDate="Invalid start date, date must be today or later";
        }
        if(endDate.getTime()<=startDate.getTime()){
            valid=false;
            newMsg.endDate="Invalid end date, must be after start day";
        }
        setMsg(msg);
        if(valid)
            props.next(data.current);
    }

    const startDateChange=(date)=>{
        let newMsg={
            startDate:undefined,
            startingprice:msg.startingprice,
            endDate:msg.endDate
        };

        setStartDate(date);
        disabledEnd.setTime(date.getTime()+24*60*60*1000);
        if(date.getTime()>=endDate.getTime()){
            let ed=new Date();
            ed.setTime(date.getTime()+24*60*60*1000);
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
            startingprice:msg.startingprice,
            endDate:undefined
        };
        if(date.getTime()<disabledEnd.getTime()||date.getTime()<=startDate.getTime()){
            newMsg.endDate="Invalid end date, must be after start day";
        }
        setMsg(newMsg);
    }

    return(
        <div className="formContainer">
            <div className="inputFieldContainer">
                <label className="inputLabel">Your start price</label><br/>
                <input className="inputFieldWide" id="startingprice" name="startingprice" type="number" onChange={onChange}/>
                {msg.startingprice&&<div className="warningMessageInputLabel">{msg.startingprice}</div>}
            </div>
            <div className="inputFieldContainer">
                <DayPickerInput onDayChange={startDateChange} value={startDate} disabledDays={[{ before: disabledStart }]}/>
                <DayPickerInput onDayChange={endDateChange} value={endDate} disabledDays={[{ before: disabledEnd }]} />
                {msg.startDate&&<div className="warningMessageInputLabel">{msg.startDate}</div>}
                {msg.endDate&&<div className="warningMessageInputLabel">{msg.endDate}</div>}
            </div>
            <div className="inputFieldContainer">
                <div className="bidButton" onClick={()=>{props.back(data)}}>
                    Back
                </div>
                <div className="bidButton" onClick={onNext}>
                    Next
                </div>
            </div>
        </div>
    )
}

export default AddItemStep2