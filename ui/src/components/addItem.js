import React, { useReducer } from 'react'
import { addItem } from '../apiConsumer/itemPost';
import AuthContext from '../context';
import AddItemStep1 from './addItemStep1'
import AddItemStep2 from './addItemStep2';
import AddItemStep3 from './addItemStep3';

class AddItem extends React.Component{
    constructor(props){
        super(props);
        if(localStorage.getItem('addItemData')&&localStorage.getItem('addItemStep')){
            this.state={
                step:localStorage.getItem('addItemStep'),
                request:"Waiting for response"
            }
            this.data=JSON.parse(localStorage.getItem('addItemData'));
        }else{
            this.state={
                step:1,
                requestStatus:"Waiting for response",
                requestStatusStyle:"neutralMessage"
            }
            this.data={
                name:"",
                description:null,
                startingPrice:0.01,
                startDate:null,
                endDate:null,
                subcategory:null,
                address:null,
                imageFiles:[],
                images:[]
            }
        }

    }

    next=(data)=>{
        for (const val in data) {
            this.data[val]=data[val];
        }
        localStorage.setItem('addItemStep',this.state.step+1);
        localStorage.setItem('addItemData',JSON.stringify(this.data));
        if(this.state.step===3){
            this.sendRequest();
        }
        this.setState({['step']:this.state.step+1});
    }
    back=(data)=>{
        for (const val in data) {
            this.data[val]=data[val];
        }
        localStorage.setItem('addItemStep',this.state.step-1);
        this.setState({['step']:this.state.step-1});
    }

    sendRequest=()=>{
        localStorage.removeItem('addItemStep');
        localStorage.removeItem('addItemData');
        let imgFiles=[];
        for(const img in this.data.imageFiles){
            let imgSplit=this.data.imageFiles[img].split(",");
            imgFiles.push(imgSplit[imgSplit.length-1])
        }
        let data={
            seller: {id: parseInt(this.context.user.jti)},
            subcategory: {id:this.data.subcategory.id},
            name: this.data.name,
            description: JSON.stringify(this.data.description),
            startingprice: this.data.startingPrice,
            starttime: this.data.startDate,
            endtime: this.data.endDate,
            address: this.data.address,
            imageFiles:imgFiles
        }
        addItem(data,this.context.jwt,(success,data)=>{
            if(success){
                this.setState({['requestStatusStyle']:"successMessage"});
                this.setState({['requestStatus']:"Item posted successefully"});
            }else{
                this.setState({['requestStatusStyle']:"warningMessage"});
                this.setState({['requestStatus']:"Error on item post"});
            }
        });
    }

    render(){
        if(this.state.step==1)
            return(<AddItemStep1 next={this.next} back={this.back} data={this.data}/>)
        else if (this.state.step==2)
            return(<AddItemStep2 next={this.next} back={this.back} data={this.data}/>)
        else if(this.state.step==3)
            return(<AddItemStep3 next={this.next} back={this.back} data={this.data}/>)
        else
            return(
                <div className="formContainer">
                    <p className={this.state.requestStatusStyle}>{this.state.requestStatus}</p>
                </div>
            )
    }
}

AddItem.contextType=AuthContext;

export default AddItem