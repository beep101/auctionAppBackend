import React, { useEffect, useRef, useState } from 'react';
import Autocomplete from 'react-autocomplete';
import { MAPS_API_KEY } from '../utils/apiAccess';
import { AUTOCOMPLETE_MENU_STYLE } from '../utils/constants';
import citiesAndCountries from '../utils/countriesAndCities';
import {Loader} from 'google-maps'

function AddressEditor(props){
    const [msg,setMsg]=useState({});
    const countriesAutocomplete=useRef(Object.keys(citiesAndCountries).map(x=>({id:x,label:x})));
    const [citiesAutocomplete,setCitiesAutocomplete]=useState([]);

    const [address,setAddress]=useState(props.data.address?props.data.address:'');
    const [city,setCity]=useState(props.data.city?props.data.city:'');
    const [country,setCountry]=useState(props.data.country?props.data.country:'');
    const [zip,setZip]=useState(props.data.zip?props.data.zip:'');
    const [phone,setPhone]=useState(props.data.phone?props.data.phone:'');

    const data=useRef(props.data);
    const loader=useRef(new Loader(MAPS_API_KEY,{
        version: "weekly",
        libraries: ["places"]
    }));

    const onChange=(e)=>{
        data.current[e.target.name]=e.target.value;
        switch(e.target.name){
            case 'address':
                setAddress(e.target.value);
                break;
            case 'phone':
                setPhone(e.target.value);
                break;
            case 'zip':
                setZip(e.target.value);
                break;
            
        }
        props.change(data.current);
    };

    useEffect(()=>{
        setMsg(props.msg);
    },[props.msg]);

    useEffect(()=>{
        const addressField=document.querySelector('#address');
        
        loader.current.load().then((google)=>{
            let autocomplete = new google.maps.places.Autocomplete(addressField, {
                fields: ["address_components", "geometry"],
                types: ["address"],
            });

            const fillData=()=>{
                const place = autocomplete.getPlace();
                data.current.address="";
                for (const component of place.address_components){
                    const compType=component.types[0];
                    switch (compType) {
                        case "street_number": {
                            const newAddress=`${data.current.address} ${component.long_name}`;
                            setAddress(newAddress);
                            data.current.address=newAddress;
                            break;
                        }
                        case "route": {
                            const newAddress=`${component.short_name} ${data.current.address}`;
                            setAddress(newAddress);
                            data.current.address=newAddress;
                            break;
                        }
                        case "postal_code": {
                            setZip(component.long_name);
                            data.current.zip=component.long_name;
                            break;
                        }
                        case "locality":{
                            setCity(component.long_name);
                            data.current.city=component.long_name;
                            break;
                        }
                        case "country":{
                            setCountry(component.long_name);
                            data.current.country=component.long_name;
                            break;
                        }
                    }
                }
            }
            props.change(data.current);
            autocomplete.addListener("place_changed", fillData);
        });
    },[])

    const changeCountry=(e)=>{
        data.current['country']=e.target.value;
        setCountry(e.target.value);
        props.change(data.current);
    }

    const changeCity=(e)=>{
        data.current['city']=e.target.value;
        setCity(e.target.value);
        props.change(data.current);
    }

    const renderAutocompleteItem=(item, isHighlighted) =>
        <div className={isHighlighted ? 'autocompleteItem highlightBackgound' : 'autocompleteItem' }>
            {item.label}
        </div>

    return(
        <div className="formContainer" >
            <div className="inputFieldContainer">
                <label className="inputLabel">Address</label><br/>
                <input className="inputFieldWide" id="address" name="address" onChange={onChange} value={address}/>
                {msg.address&&<div className="warningMessageInputLabel">{msg.address}</div>}
            </div>
            <div className="inputFieldContainer">
                <div className="dateInputContainer">
                    <div className="inputFieldContainer verticalFlex">
                        <label className="inputLabel">Country</label><br/>
                        <Autocomplete
                            className="inputFieldWide narrowed"
                            id="country"
                            name="country"
                            
                            inputProps={{className:"inputFieldWide narrowed"}}
                            wrapperProps={{className:"autocompleteWrapper"}}
                            menuStyle={AUTOCOMPLETE_MENU_STYLE}
                            
                            shouldItemRender={(item, value) => item.label.toLowerCase().startsWith(value.toLowerCase())}

                            getItemValue={item=>item.label}
                            renderItem={renderAutocompleteItem}
                            onChange={changeCountry}
                            value={country}
                            items={countriesAutocomplete.current}
                            onSelect={(value)=>{
                                data.current.country=value;
                                setCountry(value)
                                setCitiesAutocomplete(citiesAndCountries[value].map(x=>({id:x,label:x})));
                            }}
                        />
                    </div>
                    <div className="inputFieldContainer verticalFlex">
                        <label className="inputLabel">City</label><br/>
                        <Autocomplete
                            className="inputFieldWide narrowed"
                            id="city"
                            name="city"

                            inputProps={{className:"inputFieldWide narrowed"}}
                            wrapperProps={{className:"autocompleteWrapper"}}
                            menuStyle={AUTOCOMPLETE_MENU_STYLE}

                            shouldItemRender={(item, value) => item.label.toLowerCase().startsWith(value.toLowerCase())}

                            getItemValue={(item)=>item.label}
                            renderItem={renderAutocompleteItem}
                            onChange={changeCity}
                            value={city}
                            items={citiesAutocomplete}
                            onSelect={(value)=>{
                                data.current.city=value;
                                setCity(value);
                            }}
                        />
                    </div>
                </div>
                {msg.city&&<div className="warningMessageInputLabel">{msg.city}</div>}
                {msg.country&&<div className="warningMessageInputLabel">{msg.country}</div>}
            <div className="inputFieldContainer">
                <label className="inputLabel">ZIP code</label><br/>
                <input className="inputFieldWide" id="zip" name="zip" onChange={onChange} value={zip}/>
                {msg.zip&&<div className="warningMessageInputLabel">{msg.zip}</div>}
            </div>
            </div>
            <div className="inputFieldContainer">
                <label className="inputLabel">Phone</label><br/>
                <input className="inputFieldWide" id="phone" name="phone" onChange={onChange} value={phone}/>
                {msg.phone&&<div className="warningMessageInputLabel">{msg.phone}</div>}
            </div>
        </div>
    )
}

export default AddressEditor