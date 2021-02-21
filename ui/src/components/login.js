import React,{Component}  from 'react';
import axios from 'axios';
import PropTypes from "prop-types";

class Login extends Component{

    constructor(props){
        super(props);
        this.state={
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
            <div style={{display:'inline-block'}}>
            <div >
                <label style={labelStyle}>Email</label><br/>
                <input style={inputStyle} id="email" name="email" onChange={this.onChange}/><br/>
                <label style={labelStyle}>Password</label><br/>
                <input style={inputStyle} id="passwd" name="password" type="password"  onChange={this.onChange}/><br/>
                <button style={buttonStyle} onClick={this.loginReq}>Log In</button>
                <p style={msgStyle}>{this.state.msg}</p>
            </div>
            </div>
        );
    }

    onChange=(e)=>{
        this.setState({[e.target.name]:e.target.value})
    }

    loginReq=(e)=>{
        let credentials={email:this.state.email, password:this.state.password};
        axios.post("/api/login",credentials)
        .then((response)=>{
            if(response.status==200){
                this.setState({
                    email:'',
                    password:'',
                    msg:''
                });
                this.props.loginSuccess(response.data);
            }else{
                this.setState({
                    email:'',
                    password:'',
                    msg:'Bad email or password'
                })
            }},
            (error)=>{
            this.setState({
                email:'',
                password:'',
                msg:'Somethong went wrong, please try again'
            })
        });
    }

}

Login.propTyoes = {
    loginSuccess:PropTypes.func.isRequired,
}

export default Login;