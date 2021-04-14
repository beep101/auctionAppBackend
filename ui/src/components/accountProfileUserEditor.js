import React, { useEffect, useRef, useState } from 'react';
import Select from 'react-select';
import { SORTING_SELECT_STYLES, SORTING_SELECT_THEME } from '../utils/constants';


function UserEditor(props){
    const [msg,setMsg]=useState(props.msg)
    const data=useRef({...props.data,...{birthday:props.data.birthday?new Date(props.data.birthday):new Date()}});

    const [bDay,setbDay]=useState(null);
    const [bMonth,setbMonth]=useState(null);
    const [bYear,setbYear]=useState(null);
    const [days,setDays]=useState([]);
    const [months,setMonths]=useState([]);
    const [years,setYears]=useState([]);

    const gender=useRef(props.data.gender?{value:props.data.gender,label:props.data.gender=='M'?'Male':'Female'}:null);
    const genders=useRef([{value:'M',label:'Male'},{value:'F',label:'Female'}]);

    useEffect(()=>{
        if(props.data.birthday){
            const bdaydate=props.data.birthday
            updateBdaySelectorValues(bdaydate);
        }
    },[])

    useEffect(()=>{
        setMsg(props.msg);
    },[props.msg]);

    useEffect(()=>{
        const days=[]
        for(let i=1;i<=31;i++)
            days.push({value:i,label:i})
        const months=[]
        for(let i=0;i<=11;i++)
            months.push({value:i,label:i+1})
        const years=[]
        for(let i=1900;i<=(new Date()).getFullYear();i++)
            years.push({value:i,label:i})
        setDays(days);
        setMonths(months);
        setYears(years);
    },[])

    const selectGender=(gender)=>{
        data.current.gender=gender.value;
        props.change(data.current);
    }

    const updateBdaySelectorValues=(bdaydate)=>{
        setbDay({value:bdaydate.getDate()-1,label:bdaydate.getDate()});
        setbMonth({value:bdaydate.getMonth(),label:bdaydate.getMonth()+1});
        setbYear({value:bdaydate.getFullYear(),label:bdaydate.getFullYear()});
    }

    const selectDay=(day)=>{
        data.current.birthday.setDate(day.value);
        updateBdaySelectorValues(data.current.birthday);
        props.change(data.current);
    }

    const selectMonth=(month)=>{
        data.current.birthday.setMonth(month.value);
        updateBdaySelectorValues(data.current.birthday);
        props.change(data.current);
    }

    const selectYear=(year)=>{
        data.current.birthday.setFullYear(year.value);
        updateBdaySelectorValues(data.current.birthday);
        props.change(data.current);
    }

    const onChange=(e)=>{
        data.current[e.target.name]=e.target.value;
        props.change(data.current);
    };

    return(
        <div className="formContainer" >
            <div>
            <div className="inputFieldContainer">
                <label className="inputLabel">First name</label>
                <input className="inputFieldWide" id="firstName" name="firstName" onChange={onChange} defaultValue={data.current.firstName}/>
                {msg.firstName&&<div className="warningMessageInputLabel">{msg.firstName}</div>}
            </div>
            <div className="inputFieldContainer">
                <label className="inputLabel">Last name</label>
                <input className="inputFieldWide" id="lastName" name="lastName" onChange={onChange} defaultValue={data.current.lastName}/>
                {msg.lastName&&<div className="warningMessageInputLabel">{msg.lastName}</div>}
            </div>
            <div className="inputFieldContainer">
                <label className="inputLabel">I am</label>
                <span className='width200px'>
                    <Select options={genders.current} isSearchable={false} name="categories" onChange={selectGender} placeholder="Gender"
                        styles={SORTING_SELECT_STYLES} theme={SORTING_SELECT_THEME} defaultValue={gender.current}/>
                </span>
                {msg.gender&&<div className="warningMessageInputLabel">{msg.gender}</div>}
            </div>
            <div className="inputFieldContainer">
                <label className="inputLabel">Date of Birth</label>
                <div className="categorySelectsInline">
                    <span className="categorySelectContainer width5vw">
                        <Select options={days} isSearchable={false} name="categories" onChange={selectDay} placeholder="Day"
                            styles={SORTING_SELECT_STYLES} theme={SORTING_SELECT_THEME} value={bDay}/>
                    </span>
                    <span className="categorySelectContainer width5vw">
                        <Select options={months} isSearchable={false} name="categories" onChange={selectMonth} placeholder="Month"
                            styles={SORTING_SELECT_STYLES} theme={SORTING_SELECT_THEME} value={bMonth}/>
                    </span>
                    <span className="categorySelectContainer">
                        <Select options={years} isSearchable={false} name="categories" onChange={selectYear} placeholder="Year"
                            styles={SORTING_SELECT_STYLES} theme={SORTING_SELECT_THEME} value={bYear}/>
                    </span>
                </div>
                {msg.birthday&&<div className="warningMessageInputLabel">{msg.birthday}</div>}
            </div>
            <div className="inputFieldContainer">
                <label className="inputLabel">Email address</label><br/>
                <input className="inputFieldWide" id="email" name="email" onChange={onChange} defaultValue={data.current.email}/>
                {msg.email&&<div className="warningMessageInputLabel">{msg.email}</div>}
            </div>
            </div>
        </div>
    )
}

export default UserEditor