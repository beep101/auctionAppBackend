import React from 'react'
import { forgotPassword } from '../apiConsumer/accountConsumer';
import {validateEmail} from '../utils/functions';

class ForgotPassword extends React.Component{

    constructor(props){
        super(props);
        this.state={
            email:'',
            success:false,
            msg:{}
        }
    }

    onChange=(e)=>{
        this.setState({[e.target.name]:e.target.value})
    }

    render(){
        if(!this.state.success){
            return(
                <div className="formContainer">
                    <div className="inputFieldContainer">
                        <label className="inputLabel">Email</label><br/>

                        <input className="inputFieldWide" id="email" name="email"
                        onChange={this.onChange} value={this.state.email}/>

                        {this.state.msg&&this.state.msg.email&&
                        <div className="warningMessageInputLabel">{this.state.msg.email}</div>}

                    </div>
                    
                    <button className="buttonWide" onClick={this.forgotReq}>Forgot password</button>
                    {this.state.msg && this.state.msg.response && <p className="warningMessage">{this.state.msg.response}</p>}
                </div>
            )
        }else{
            return(
                <div className="formContainer">
                    <p className="successMessage">{this.state.msg.response}</p>
                </div>    
            )         
        }

    }

    forgotReq=(e)=>{
        let email=this.state.email;
        if(!validateEmail(email)){
            let msg={email:"Please enter valid email"};
            this.setState({['msg']:msg})
            return;
        }
        forgotPassword(email,(success,message)=>{
            if(success){
                this.setState({
                    email:'',
                    success:true,
                    msg:message
                });
            }
            else{
                this.setState({
                    email:'',
                    success:false,
                    msg:message
                });
            }
        });
    }


}

export default ForgotPassword