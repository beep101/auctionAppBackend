import React, { useReducer } from 'react'
import { addItem } from '../apiConsumer/itemPost';
import AuthContext from '../context';
import AddItemStep1 from './addItemStep1'
import AddItemStep2 from './addItemStep2';
import AddItemStep3 from './addItemStep3';
import { css } from "@emotion/core";
import PulseLoader from "react-spinners/PulseLoader";
import { withRouter } from "react-router-dom";
import { draftToMarkdown } from 'markdown-draft-js';

class AddItem extends React.Component{
    constructor(props,context){
        super(props);
        if(localStorage.getItem('addItemData')&&localStorage.getItem('addItemStep')){
            this.state={
                step:parseInt(localStorage.getItem('addItemStep')),
                success:true
            }
            this.data=JSON.parse(localStorage.getItem('addItemData'));
            console.log(this.state);
            console.log(this.data)
        }else{
            this.state={
                step:1,
                success:true
            }
            this.data={
                name:"",
                description:null,
                startingPrice:0.01,
                startDate:null,
                endDate:null,
                subcategory:null,
                address:context.user.address?context.user.address:null,
                images:[]
            }
        }
        console.log(this.data)
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
        console.log(this.data)
        let imgFiles=[];
        for(const img in this.data.images){
            let imgSplit=this.data.images[img].data.split(",");
            imgFiles.push(imgSplit[imgSplit.length-1])
        }
        let startDate=new Date();
        if(this.data.startDate.getDate()!=startDate.getDate()){
            startDate=this.data.startDate;
        }
        let data={
            seller: {id: parseInt(this.context.user.jti)},
            subcategory: {id:this.data.subcategory.id},
            name: this.data.name,
            description: draftToMarkdown(this.data.description),
            startingprice: this.data.startingPrice,
            starttime: startDate,
            endtime: this.data.endDate,
            address: this.data.address,
            imageFiles:imgFiles
        }
        console.log(data)
        addItem(data,this.context.jwt,(success,data)=>{
            if(success){
                this.setState({['requestStatusStyle']:"successMessage"});
                this.props.history.push(`/item?id=${data.id}`);

            }else{
                this.setState({['success']:false});
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
            return(this.state.success?<div className="fullSize"><PulseLoader color="#8367D8" css={css} size={50}/></div>
                    :<div className="fullSize"><div className="warningMessage">Error on item post</div></div>)

    }
}

AddItem.contextType=AuthContext;

export default withRouter(AddItem)