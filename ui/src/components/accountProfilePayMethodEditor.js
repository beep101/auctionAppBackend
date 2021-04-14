import React, { useEffect, useRef, useState } from 'react';
import Select from 'react-select';
import { SORTING_SELECT_STYLES, SORTING_SELECT_THEME } from '../utils/constants';

function PayMethodEditor(props){
    const [msg,setMsg]=useState({});
    const data=useRef(props.data);
    const expMonth=useRef(props.data.expMonth?{value:props.data.expMonth,label:props.data.expMonth}:null);
    const expYear=useRef(props.data.expYear?{value:props.data.expYear,label:props.data.expYear}:null);
    const [months,setMonths]=useState([]);
    const [years,setYears]=useState([]);

    useEffect(()=>{
        const months=[]
        for(let i=1;i<=12;i++)
            months.push({value:i,label:i})
        const years=[]
        for(let i=(new Date()).getFullYear();i<=(new Date()).getFullYear()+10;i++)
            years.push({value:i,label:i})
        setMonths(months);
        setYears(years);
    },[])

    useEffect(()=>{
        setMsg(props.msg);
    },[props.msg]);
    
    const selectMonth=(month)=>{
        data.current.expMonth=month.value;
        props.change(data.current);
    }

    const selectYear=(year)=>{
        data.current.expYear=year.value;
        props.change(data.current);
    }

    const onChange=(e)=>{
        data.current[e.target.name]=e.target.value;
        props.change(data.current);
    };

    return(
        <div className="formContainer" >
            <div className="inputFieldContainer categorySelectsInline">
                <span className="categorySelectContainer">
                    <label className="inputLabel">Name on card</label><br/>
                    <input className="inputFieldWide" id="onCardName" name="onCardName" onChange={onChange} defaultValue={data.current.onCardName}/>
                    {msg.onCardName&&<div className="warningMessageInputLabel">{msg.onCardName}</div>}
                </span>
                <span className="categorySelectContainer">
                    <label className="inputLabel">Card number</label><br/>
                    <input className="inputFieldWide" id="cardNumber" name="cardNumber" onChange={onChange} defaultValue={data.current.cardNumber}/>
                    {msg.cardNumber&&<div className="warningMessageInputLabel">{msg.cardNumber}</div>}
                </span>
            </div>
            <div className="inputFieldContainer categorySelectsInline">
                <span className="categorySelectContainer">
                    <label className="inputLabel">Expiration date</label><br/>
                    <Select options={months} isSearchable={false} name="categories" onChange={selectMonth} placeholder="Month"
                    styles={SORTING_SELECT_STYLES} theme={SORTING_SELECT_THEME} defaultValue={expMonth.current}/>
                </span>
                <span className="categorySelectContainer">
                    <Select options={years} isSearchable={false} name="categories" onChange={selectYear} placeholder="Year"
                    styles={SORTING_SELECT_STYLES} theme={SORTING_SELECT_THEME} defaultValue={expYear.current}/>
                    {msg.expDate&&<div className="warningMessageInputLabel">{msg.expDate}</div>}
                </span>
                <span className="categorySelectContainer">
                    <label className="inputLabel">CV/CCW</label><br/>
                    <input className="inputFieldWide" id="cvccw" name="cvccw" onChange={onChange} defaultValue={data.current.cvccw}/>
                    {msg.cvccw&&<div className="warningMessageInputLabel">{msg.cvccw}</div>}
                </span>
            </div>
        </div>
    )
}

export default PayMethodEditor