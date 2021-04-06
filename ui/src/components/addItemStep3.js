import React from 'react'
import {Loader} from 'google-maps'
import {MAPS_API_KEY} from '../utils/apiAccess'
import {PHONE_REGEX_PATTERNS} from '../utils/constants'
import citiesAndCountries from '../utils/countriesAndCities';
import Autocomplete from 'react-autocomplete';
import {AUTOCOMPLETE_MENU_STYLE} from '../utils/constants'

class AddItemStep3 extends React.Component{

    constructor(props){
        super(props)
        this.state={
            address:props.data.address?props.data.address.address:'',
            city:props.data.address?props.data.address.city:'',
            country:props.data.address?props.data.address.country:'',
            zip:props.data.address?props.data.address.zip:'',
            phone:props.data.address?props.data.address.phone:'',
            msg:{},
            countriesAutocomplete:Object.keys(citiesAndCountries).map(x=>({id:x,label:x})),
            citiesAutocomplete:[]
        }
        console.log(props);
        console.log(this.state);
    }
        
    componentDidMount=()=>{
        this.addressField=document.querySelector('#address')
        const loader=new Loader(MAPS_API_KEY,{
            version: "weekly",
            libraries: ["places"]
        });

        loader.load().then((google)=>{
            let autocomplete = new google.maps.places.Autocomplete(this.addressField, {
                fields: ["address_components", "geometry"],
                types: ["address"],
            });

            const fillData=()=>{
                const place = autocomplete.getPlace();
                this.state.address="";
                for (const component of place.address_components){
                    const compType=component.types[0];
                    switch (compType) {
                        case "street_number": {
                            this.setState({['address']:`${this.state.address} ${component.long_name}`});
                            break;
                        }
                        case "route": {
                            this.setState({['address']:`${component.short_name} ${this.state.address}`});
                            break;
                        }
                        case "postal_code": {
                            this.setState({['zip']:component.long_name});
                            break;
                        }
                        case "locality":{
                            this.setState({['city']:component.long_name});
                            break;
                        }
                        case "country":{
                            this.setState({['country']:component.long_name});
                            break;
                        }
                    }
                }
            }

            autocomplete.addListener("place_changed", fillData);
        });
    }

    onChange=(e)=>{
        this.setState({[e.target.name]:e.target.value});
    }

    onNext=()=>{
        let valid=true;
        let msg={};
        if(!this.state.address){
            valid=false;
            msg.address="Address name can't be empty";
        }
        if(!this.state.city){
            valid=false;
            msg.city="City name can't be empty";
        }
        if(!this.state.zip){
            valid=false;
            msg.zip="ZIP code can't be empty";
        }
        if(!this.state.country){
            valid=false;
            msg.country="Country name can't be empty";
        }
        if(this.state.phone){
            let isPhoneValid=false;
            for(const pattern in PHONE_REGEX_PATTERNS){
                let regex=new RegExp(pattern);
                if(regex.test(this.state.phone))
                    isPhoneValid=true;
            }
            if(!isPhoneValid){
                valid=false;
                msg.phone="Phone number format is invalid";
            }
        }
        this.setState({['msg']:msg});
        let data={
            address:{
                address:this.state.address,
                city:this.state.city,
                country:this.state.country,
                zip:this.state.zip,
                phone:this.state.phone
            }
        }
        if(valid){
            this.props.next(data);
        }
    }

    onBack=()=>{
        let data={
            address:{
                address:this.state.address,
                city:this.state.city,
                country:this.state.country,
                zip:this.state.zip,
                phone:this.state.phone
            }
        }
        this.props.back(data)
    }

    renderAutocompleteItem=(item, isHighlighted) =>
        <div className={isHighlighted ? 'autocompleteItem highlightBackgound' : 'autocompleteItem' }>
            {item.label}
        </div>

    render(){
        return(
            <div className="formContainer" >
                <div className="inputFieldContainer">
                    <label className="inputLabel">Address</label><br/>
                    <input className="inputFieldWide" id="address" name="address" onChange={this.onChange} value={this.state.address}/>
                    {this.state.msg.address&&<div className="warningMessageInputLabel">{this.state.msg.address}</div>}
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
                                renderItem={this.renderAutocompleteItem}
                                onChange={(e)=>this.setState({['country']:e.target.value})}
                                value={this.state.country}
                                items={this.state.countriesAutocomplete}
                                onSelect={(value)=>{
                                    this.setState({['country']:value});
                                    this.setState({['citiesAutocomplete']:citiesAndCountries[value].map(x=>({id:x,label:x}))});
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
                                renderItem={this.renderAutocompleteItem}
                                onChange={(e)=>this.setState({['city']:e.target.value})}
                                value={this.state.city}
                                items={this.state.citiesAutocomplete}
                                onSelect={(value)=>this.setState({['city']:value})}
                            />
                        </div>
                    </div>
                    {this.state.msg.city&&<div className="warningMessageInputLabel">{this.state.msg.city}</div>}
                    {this.state.msg.country&&<div className="warningMessageInputLabel">{this.state.msg.country}</div>}
                <div className="inputFieldContainer">
                    <label className="inputLabel">ZIP code</label><br/>
                    <input className="inputFieldWide" id="zip" name="zip" onChange={this.onChange} value={this.state.zip}/>
                    {this.state.msg.zip&&<div className="warningMessageInputLabel">{this.state.msg.zip}</div>}
                </div>
                </div>
                <div className="inputFieldContainer">
                    <label className="inputLabel">Phone</label><br/>
                    <input className="inputFieldWide" id="phone" name="phone" onChange={this.onChange} value={this.state.phone}/>
                    {this.state.msg.phone&&<div className="warningMessageInputLabel">{this.state.msg.phone}</div>}
                </div>
                <div className="inputFieldContainer categorySelectsInline">
                    <span className="categorySelectContainer">
                        <div className="bidButton lightGrayBorder" onClick={this.onBack}>
                            Back
                        </div>
                    </span>
                    <span className="categorySelectContainer">
                        <div className="bidButton" onClick={this.onNext}>
                            Done
                        </div>
                    </span>
                </div>
            </div>
        );
    }

}

export default AddItemStep3