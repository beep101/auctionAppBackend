import React from 'react'
import AddItemStep1 from './addItemStep1'
import AddItemStep2 from './addItemStep2';
import AddItemStep3 from './addItemStep3';

class AddItem extends React.Component{
    constructor(props){
        super(props);
        //if load data from storage
        //set state
        //else
        this.state={
            step:1,
            data:{
                name:"",
                description:"",
                startingPrice:0.01,
                startdate:"",
                enddate:"",
                subcategory:null,
                address:null,
                imageFiles:[]
            }
        }
    }

    next=(data)=>{
        console.log(data);
        for (const val in data) {
            this.setState({['data']:{[val]:data[val]}});
        }
        this.setState({['step']:this.state.step+1});
    }
    back=(data)=>{
        for (const val in data) {
            this.setState({[val]:data[val]});
        }
        this.setState({['step']:this.state.step-1});
    }
    render(){
        if(this.state.step==1)
            return(<AddItemStep1 next={this.next} back={this.back} data={this.state.data}/>)
        else if (this.state.step==2)
            return(<AddItemStep2 next={this.next} back={this.back} data={this.state.data}/>)
        else if(this.state.step==3)
            return(<AddItemStep3 next={this.next} back={this.back} data={this.state.data}/>)
        else
            <div></div>
    }
}

export default AddItem