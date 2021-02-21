import React,{Component}  from 'react';
import axios from 'axios';
import PropTypes from "prop-types";

class Register extends Component{

    constructor(props){
        super(props);
        this.state={
            firstName:'',
            lastName:'',
            email:'',
            password:'',
            msg:''
        }
    }

    render(){
        const labelStyle={
            'font-family': 'sans-serif',
            padding:'2px',
            margin:'4px',
            'font-size':'12px',
        };

        const inputStyle={
            width:'250px',
            'font-family': 'sans-serif',
            'font-size':'15px',
            padding:'2px',
            margin:'4px',
        }

        const buttonStyle={
            width:'250px',
            border: 'none',
            'font-family': 'sans-serif',
            'background-color':'#52307c',
            'font-size':'15px',
            padding:'5px',
            margin:'4px',
            color:'#ffffff',
            cursor: 'pointer'
        }
        const msgStyle={
            'font-family': 'sans-serif',
            margin:'6px',
            color:'#8e1600'
        }
        return (
            <div>
                <label for="firstName" style={labelStyle}>First Name</label><br/>
                <input  id="fname" name="firstName" onChange={this.onChange} style={inputStyle}/><br/>

                <label for="lastName" style={labelStyle}>Last Name</label><br/>
                <input  id="lname" name="lastName" onChange={this.onChange} style={inputStyle}/><br/>

                <label for="email" style={labelStyle}>Email</label><br/>
                <input  id="email" name="email" onChange={this.onChange} style={inputStyle}/><br/>

                <label for="password" style={labelStyle}>Password</label><br/>
                <input id="passwd" type="password" name="password" onChange={this.onChange} style={inputStyle}/><br/>

                <button onClick={this.signupReq} style={buttonStyle}>Sign Up</button>
                <p style={msgStyle}>{this.state.msg}</p>
            </div>
        );
    }

    onChange=(e)=>{
        this.setState({[e.target.name]:e.target.value})
    }

    signupReq=(e)=>{
        let userData={email:this.state.email,
                        password:this.state.password,
                        firstName:this.state.firstName,
                        lastName:this.state.lastName};
        axios.post("/api/signup",userData)
            .then((response)=>{
                if(response.status==200){
                    this.props.signupSuccess(response.data);
                    this.setState({
                        email:'',
                        password:'',
                        firstName:'',
                        lastName:'',
                        msg:''
                    })
                }else{
                    this.setState({
                        email:'',
                        password:'',
                        firstName:'',
                        lastName:'',
                        msg:'Bad email or password'
                    })
                }},
                (error)=>{
                this.setState({
                    email:'',
                    password:'',
                    firstName:'',
                    lastName:'',
                    msg:'Somethong went wrong, please try again'
                })
        });
    }


}

Register.propTyoes = {
    signupSuccess:PropTypes.func.isRequired,
}


export default Register;