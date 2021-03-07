import React from 'react'
import { forgotPassword } from '../apiConsumer/accountConsumer';

class ForgotPassword extends React.Component{

    constructor(props){
        super(props);
        this.state={
            email:'',
            success:false,
            msg:''
        }
    }

    onChange=(e)=>{
        this.setState({[e.target.name]:e.target.value})
    }

    render(){
        if(!this.state.success){
            return(
                <div className="formContainer">
                    <div>
                        <label className="inputLabel">Email</label><br/>
                        <input className="inputFieldWide" id="email" name="email" onChange={this.onChange} value={this.state.email}/>
                    </div>
                    
                    <button className="buttonWide" onClick={this.forgotReq}>Forgot password</button>
                    {this.state.msg==""?null:<p className="warningMessage">{this.state.msg}</p>}
                </div>
            )
        }else{
            return(
                <div className="formContainer">
                    <p className="successMessage">{this.state.msg}</p>
                </div>    
            )         
        }

    }

    forgotReq=(e)=>{
        let email=this.state.email;
        forgotPassword(email,(success,message)=>{
            if(success){
                this.setState({
                    email:'',
                    success:true,
                    msg:'Request successeful, please check your email'
                });
            }
            else{
                this.setState({
                    email:'',
                    success:false,
                    msg:"Please enter valid email address"
                });
            }
        });
    }


}

export default ForgotPassword