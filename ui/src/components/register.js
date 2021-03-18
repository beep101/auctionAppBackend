import React,{Component}  from 'react';
import {signup, singup} from '../apiConsumer/accountConsumer'
import AuthContext from '../context';
import {Link} from 'react-router-dom';
import '../styles/styles.css';
import {validateEmail} from '../utils/functions'

class Register extends Component{

    constructor(props){
        super(props);
        this.state={
            firstName:'',
            lastName:'',
            email:'',
            password:''
        }
    }

    render(){
        
        return (
            <div className="formContainer">
                <div className="inputFieldContainer">
                    <label className="inputLabel">First Name</label><br/>
                    <input className="inputFieldWide" name="firstName" onChange={this.onChange} value={this.state.firstName}/><br/>
                    {this.state.msg&&this.state.msg.firstName&&<div className="warningMessageInputLabel">{this.state.msg.firstName}</div>}
                </div>
                
                <div className="inputFieldContainer">
                    <label className="inputLabel">Last Name</label><br/>
                    <input className="inputFieldWide" name="lastName" onChange={this.onChange} value={this.state.lastName}/><br/>
                    {this.state.msg&&this.state.msg.lastName&&<div className="warningMessageInputLabel">{this.state.msg.lastName}</div>}
                </div>
                
                <div className="inputFieldContainer">
                    <label className="inputLabel">Email</label><br/>
                    <input className="inputFieldWide" name="email" onChange={this.onChange} value={this.state.email}/><br/>
                    {this.state.msg&&this.state.msg.email&&<div className="warningMessageInputLabel">{this.state.msg.email}</div>}
                    </div>
                
                <div className="inputFieldContainer">
                    <label className="inputLabel">Password</label><br/>
                    <input className="inputFieldWide" name="password" onChange={this.onChange} type="password" value={this.state.password}/><br/>
                    {this.state.msg&&this.state.msg.password&&<div className="warningMessageInputLabel">{this.state.msg.password}</div>}
                </div>
                
                <button className="buttonWide" onClick={this.signupReq}>Sign Up</button>
                <p className="message">Already have an account?  <Link to="/login" className="highlightMessage">Login</Link></p>
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

    signupReq=(e)=>{
        let userData={email:this.state.email,
                    password:this.state.password,
                    firstName:this.state.firstName,
                    lastName:this.state.lastName};
        if(!this.validate(userData)){
            return;
        }
        signup(userData,(success,message)=>{
            if(success){
                this.setState({
                    email:'',
                    password:'',
                    firstName:'',
                    lastName:''
                });
                this.context.login(message);
                localStorage.setItem('token',message)
                this.props.history.push("/");
            }else{
                console.log(message);
                if(message.password){
                    this.setState({['password']:''})
                }
                if(message.email){
                    this.setState({['email']:''})
                }
                if(message.lastName){
                    this.setState({['lastName']:''})
                }
                if(message.firstName){
                    this.setState({['firstName']:''})
                }
                this.setState({['msg']:message});
            }
        });
    }

    validate=(userData)=>{
        let res=true;
        let msg={}
        if(!validateEmail(userData.email)){
            msg['email']="Must be valid email";
            res=false;            
        }
        if(userData.email===""){
            msg['email']="Can't be empty";
            res=false;            
        }
        if(userData.password.length<6){
            msg['password']="At least 6 characters";
            res=false;            
        }
        if(userData.firstName===""){
            msg['firstName']="Can't be empty";
            res=false;
        }
        if(userData.lastName===""){
            msg['lastName']="Can't be empty";
            res=false;
        }
        this.setState({['msg']:msg})
        return res;
    }

}

Register.contextType=AuthContext;

export default Register;