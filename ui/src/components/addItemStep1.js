import React, { useEffect, useState } from 'react';
import {getAllCategories} from '../apiConsumer/categoryConsumer';
import {SORTING_SELECT_STYLES, SORTING_SELECT_THEME} from '../utils/constants';
import Select from 'react-select';

function AddItemStep1(props){
    const [data]=useState({
        name:props.data.name,
        description:props.data.description,
        subcategory:props.data.subcategory,
        imageFiles:props.data.imageFiles
    });

    const [categories,setCategories]=useState([]);
    const [subcategories,setSubcateogories]=useState([]);
    const [msg,setMsg]=useState({});

    useEffect(
        ()=>{getAllCategories((success,data)=>{
            if(success){
                let adaptedCategories=data.slice(1).map(element=>{return {value:element.id,label:element.name,subs:element.subcategories}});
                setCategories(adaptedCategories);
            }else{
                console.log("Cannot fetch categories")
            }
        });
    },[]);

    const onChange=(e)=>{
        data[e.target.name]=e.target.value;
    }

    const selectCategory=(category)=>{
        setSubcateogories(category.subs.map(element=>{return {value:element.id,label:element.name}}));
    }

    const selectSubcategory=(subcategory)=>{
        data['subcategory']={id:subcategory.value};
    }

    const onNext=()=>{
        //verify data
        props.next(data);
    }

    return(
        <div className="formContainer">
            <div className="inputFieldContainer">
                <label className="inputLabel">What do you sell?</label><br/>
                <input className="inputFieldWide" id="name" name="name" onChange={onChange}/>
                {msg.name&&<div className="warningMessageInputLabel">{msg.name}</div>}
            </div>
            <div className="inputFieldContainer">
                <Select options={categories} isSearchable={false} name="categories" onChange={selectCategory}
                    styles={SORTING_SELECT_STYLES} theme={SORTING_SELECT_THEME}/>
                <Select options={subcategories} isSearchable={false} name="subcategories" onChange={selectSubcategory}
                    styles={SORTING_SELECT_STYLES} theme={SORTING_SELECT_THEME}/>
                {msg.cats&&<div className="warningMessageInputLabel">{msg.cats}</div>}
            </div>
            <div className="inputFieldContainer">
                <label className="inputLabel">Description</label><br/>
                <input className="inputFieldWide" id="description" name="description" onChange={onChange} />
                {msg.name&&<div className="warningMessageInputLabel">{msg.name}</div>}
            </div>
            <div>
                <input className="uploadImages" type="file" />
            </div>
            <div>
                <div className="bidButton" onClick={onNext}>Next</div>
            </div>
        </div>
    );
}

export default AddItemStep1