import React, { useRef, useState } from 'react'
import {Loader,LoaderOptions} from 'google-maps'
import {MAPS_API_KEY} from '../utils/apiAccess'

function AddItemStep3(props){
    let data=useRef({
        
    });

    const [address,setAddress]=useState(props.address?props.address.address:'');
    const [city,setCity]=useState(props.address?props.address.city:'');
    const [country,setCountry]=useState(props.address?props.address.country:'');
    const [zip,setZip]=useState(props.zip?props.address.zip:'');
    const [phone,setPhone]=useState(props.phone?props.address.phone:'');
    const [msg,setMsg]=useState({});

    let addressField=document.querySelector('#address')

    const loader=new Loader(MAPS_API_KEY,{
        version: "weekly",
    });
    let google=await loader.load();
    Promise.allSettled([]).then(success=>google=success,error=>console.log("err "+error));
    console.log(google);
    let autocomplete = new google.maps.places.Autocomplete(addressField, {
        fields: ["address_components", "geometry"],
        types: ["address"],
    });
    autocomplete.addListener("place_changed", fillData);

    const fillData=()=>{
        const place = autocomplete.getPlace();
        for (const component of place.address_components){
            const compType=component.types[0];

            let address;
            let city;
            let country;
            let zip;

            switch (compType) {
                case "street_number": {
                    setAddress(`${component.long_name}`);
                    break;
                }
                case "route": {
                    setCity(component.short_name);
                    break;
                }
                case "postal_code": {
                    setZip(component.long_name);
                    break;
                }
                case "locality":{
                    setPhone(component.long_name);
                    break;
                }
                case "country":{
                    setCountry(component.long_name);
                    break;
                }
            }
          
        }
    }


    const onChange=(e)=>{

    }

    const onNext=()=>{
        
    }

    return(
        <div className="formContainer" >
            <div className="inputFieldContainer">
                <label className="inputLabel">Address</label><br/>
                <input className="inputFieldWide" id="address" name="address" onChange={onChange} value={address}/>
                {msg.address&&<div className="warningMessageInputLabel">{msg.address}</div>}
            </div>
            <div className="inputFieldContainer">
                <label className="inputLabel">City</label><br/>
                <input className="inputFieldWide" id="city" name="city" onChange={onChange} value={city}/>
                {msg.city&&<div className="warningMessageInputLabel">{msg.city}</div>}
            </div>
            <div className="inputFieldContainer">
                <label className="inputLabel">ZIP code</label><br/>
                <input className="inputFieldWide" id="zip" name="zip" onChange={onChange} value={zip}/>
                {msg.zip&&<div className="warningMessageInputLabel">{msg.zip}</div>}
            </div>
            <div className="inputFieldContainer">
                <label className="inputLabel">Country</label><br/>
                <input className="inputFieldWide" id="country" name="country" onChange={onChange} value={country}/>
                {msg.country&&<div className="warningMessageInputLabel">{msg.country}</div>}
            </div>
            <div className="inputFieldContainer">
                <label className="inputLabel">Phone</label><br/>
                <input className="inputFieldWide" id="phone" name="phone" onChange={onChange} value={phone}/>
                {msg.phone&&<div className="warningMessageInputLabel">{msg.phone}</div>}
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

export default AddItemStep3