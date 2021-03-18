import React,{Component}  from 'react';
import { Link } from 'react-router-dom';
import {login} from '../apiConsumer/accountConsumer'
import AuthContext from '../context';
import '../styles/styles.css';
import {validateEmail} from '../utils/functions';

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
        return (
            <div className="formContainer">
                <div className="inputFieldContainer">
                    <label className="inputLabel">Email</label><br/>
                    <input className="inputFieldWide" id="email" name="email" onChange={this.onChange} value={this.state.email}/>
                    {this.state.msg&&this.state.msg.email&&<div className="warningMessageInputLabel">{this.state.msg.email}</div>}
                </div>
                
                <div className="inputFieldContainer"> 
                    <label className="inputLabel">Password</label><br/>
                    <input className="inputFieldWide" id="passwd" name="password" type="password"  onChange={this.onChange} value={this.state.password}/><br/>
                    {this.state.msg&&this.state.msg.password&&<div className="warningMessageInputLabel">{this.state.msg.password}</div>}
                </div>
                
                <button className="buttonWide" onClick={this.loginReq}>Log In</button>
                <p className="message">Forgot your password?  <Link to="/forgotPassword" className="highlightMessage">Click here</Link></p>
                {this.state.msg&&this.state.msg.response&&<p className="warningMessage">{this.state.msg.response}</p>}
            </div>
        );
    }

    onChange=(e)=>{
        this.setState({[e.target.name]:e.target.value})
        if(this.state.msg){
            let msg=this.state.msg
            msg[e.target.name]=null;
            this.setState({['msg']:msg})
        }
    }

    loginReq=(e)=>{
        let credentials={email:this.state.email, password:this.state.password};
        if(!this.validate(credentials)){
            return;
        }
        login(credentials,(success,message)=>{
            if(success){
                this.setState({
                    email:'',
                    password:'',
                    msg:{}
                });
                this.context.login(message);
                localStorage.setItem('token',message)
                this.props.history.push("/");
            }
            else{
                if(message.password){
                    this.setState({['password']:''})
                }
                if(message.email){
                    this.setState({['email']:''})
                }
                this.setState({['msg']:message});
            }
        });
    }

    validate=(userData)=>{
        let res=true;
        let msg={}
        if(!validateEmail(userData.email)){
            msg['email']="Email format invalid";
            res=false;            
        }
        if(userData.email===""){
            msg['email']="Can't be empty";
            res=false;            
        }
        if(userData.password.length<6){
            msg['password']="Password too short";
            res=false;            
        }
        this.setState({['msg']:msg})
        return res;
    }
}

Login.contextType=AuthContext;

export default Login;