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

                <div>
                    <label className="inputLabel">Password</label><br/>
                    <input className="inputFieldWide" id="passwd" name="password" type="password"  onChange={this.onChange} value={this.state.password}/><br/>
                </div>
                
                <button className="buttonWide" onClick={this.newPassReq}>Change password</button>
                {this.state.msg==""?null:<p className="warningMessage">{this.state.msg}</p>}
            </div>
        )
    }

    newPassReq=(e)=>{
        let token=queryString.parse(this.props.location.search)['token'];
        let passwd=this.state.password;
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
                    msg:'Your request might be expired'
                });
            }
        });
    }
}

export default NewPassword