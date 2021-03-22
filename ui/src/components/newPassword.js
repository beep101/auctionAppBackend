import React from 'react'
import { newPassword } from '../apiConsumer/accountConsumer';
import queryString from 'query-string';

class NewPassword extends React.Component{
    
    constructor(props){
        super(props);
        this.state={
            password:'',
            msg:''
        }
    }

    onChange=(e)=>{
        this.setState({[e.target.name]:e.target.value})
    }
    
    render(){
        return(
            <div className="formContainer">

                <div className="inputFieldContainer">
                    <label className="inputLabel">Password</label><br/>
                    
                    <input className="inputFieldWide" id="passwd" name="password"
                    type="password" onChange={this.onChange} value={this.state.password}/>

                    {this.state.msg&&this.state.msg.password&&
                    <div className="warningMessageInputLabel">{this.state.msg.password}</div>}
                
                </div>
                
                <button className="buttonWide" onClick={this.newPassReq}>Change password</button>
                {this.state.msg && this.state.msg.response && <p className="warningMessage">{this.state.msg}</p>}
            </div>
        )
    }

    newPassReq=(e)=>{
        const token=queryString.parse(this.props.location.search)['token'];
        let passwd=this.state.password;
        if(passwd.length<6){
            let msg={password:"Password too short"};
            this.setState({['msg']:msg})
            return;
        }
        newPassword(token,passwd,(success,message)=>{
            if(success){
                this.setState({
                    password:'',
                    msg:''
                });
                this.props.history.push("/login");
            }
            else{
                this.setState({
                    password:'',
                    msg:message
                });
            }
        });
    }
}

export default NewPassword