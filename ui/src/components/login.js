import React,{Component}  from 'react';
import { Link } from 'react-router-dom';
import {login} from '../apiConsumer/accountConsumer'
import AuthContext from '../context';
import '../styles/styles.css'

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
                <div>
                    <label className="inputLabel">Email</label><br/>
                    <input className="inputFieldWide" id="email" name="email" onChange={this.onChange} value={this.state.email}/>
                </div>
                
                <div>
                    <label className="inputLabel">Password</label><br/>
                    <input className="inputFieldWide" id="passwd" name="password" type="password"  onChange={this.onChange} value={this.state.password}/><br/>
                </div>
                
                <button className="buttonWide" onClick={this.loginReq}>Log In</button>
                <p className="message">Forgot your password?  <Link to="/forgotPassword" className="highlightMessage">Click here</Link></p>
                {this.state.msg===""?null:<p className="warningMessage">{this.state.msg}</p>}
            </div>
        );
    }

    onChange=(e)=>{
        this.setState({[e.target.name]:e.target.value})
    }

    loginReq=(e)=>{
        let credentials={email:this.state.email, password:this.state.password};
        login(credentials,(success,message)=>{
            if(success){
                this.setState({
                    email:'',
                    password:'',
                    msg:''
                });
                this.context.login(message);
                localStorage.setItem('token',message)
                this.props.history.push("/");
            }
            else{
                this.setState({
                    email:'',
                    password:'',
                    msg:message
                });
            }
        });
    }

}

Login.contextType=AuthContext;

export default Login;