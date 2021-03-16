import React,{Component}  from 'react';
import {signup, singup} from '../apiConsumer/accountConsumer'
import AuthContext from '../context';
import {Link} from 'react-router-dom';
import '../styles/styles.css';

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
        
        return (
            <div className="formContainer">
                <div>
                    <label className="inputLabel">First Name</label><br/>
                    <input className="inputFieldWide" name="firstName" onChange={this.onChange} value={this.state.firstName}/><br/>
                </div>
                
                <div>
                    <label className="inputLabel">Last Name</label><br/>
                    <input className="inputFieldWide" name="lastName" onChange={this.onChange} value={this.state.lastName}/><br/>
                </div>
                
                <div>
                    <label className="inputLabel">Email</label><br/>
                    <input className="inputFieldWide" name="email" onChange={this.onChange} value={this.state.email}/><br/>
                </div>
                
                <div>
                    <label className="inputLabel">Password</label><br/>
                    <input className="inputFieldWide" name="password" onChange={this.onChange} type="password" value={this.state.password}/><br/>
                </div>
                
                <button className="buttonWide" onClick={this.signupReq}>Sign Up</button>
                <p className="message">Already have an account?  <Link to="/login" className="highlightMessage">Login</Link></p>
                {this.state.msg.split(",").map(msg=><p className="warningMessage">{msg}</p>)}
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
        signup(userData,(success,message)=>{
            if(success){
                this.setState({
                    email:'',
                    password:'',
                    firstName:'',
                    lastName:'',
                    msg:''
                });
                this.context.login(message);
                localStorage.setItem('token',message)
                this.props.history.push("/");
            }else{
                this.setState({
                    email:'',
                    password:'',
                    firstName:'',
                    lastName:'',
                    msg:message
                });
            }
        });
    }


}

Register.contextType=AuthContext;

export default Register;