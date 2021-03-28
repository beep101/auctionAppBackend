import React, { useState } from 'react';

function AddItemStep2(props){
    let data={
        starttime:props.startingprice,
        endtime:props.endtime,
        startingprice:props.startingprice
    }
    const [msg,setMsg]=useState({})

    const onChange=()=>{

    }

    const onNext=()=>{
        //verify data
        props.next(data);
    }

    return(
        <div className="formContainer">
            <div className="inputFieldContainer">
                <label className="inputLabel">Your start price</label><br/>
                <input className="inputFieldWide" id="startingprice" name="startingprice" onChange={onChange}/>
                {msg.name&&<div className="warningMessageInputLabel">{msg.startingprice}</div>}
            </div>
            <div className="inputFieldContainer">
                <input className="inputFieldWide" id="starttime" name="starttime" type="date" onChange={onChange}/>
                <input className="inputFieldWide" id="endtime" name="endtime" type="date" onChange={onChange}/>
                {msg.name&&<div className="warningMessageInputLabel">{msg.date}</div>}
            </div>
            <div>
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