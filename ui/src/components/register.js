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
            <div class="formContainer">
                <div>
                    <label class="inputLabel">First Name</label><br/>
                    <input class="inputFieldWide" name="firstName" onChange={this.onChange} value={this.state.firstName}/><br/>
                </div>
                
                <div>
                    <label class="inputLabel">Last Name</label><br/>
                    <input class="inputFieldWide" name="lastName" onChange={this.onChange} value={this.state.lastName}/><br/>
                </div>
                
                <div>
                    <label class="inputLabel">Email</label><br/>
                    <input class="inputFieldWide" name="email" onChange={this.onChange} value={this.state.email}/><br/>
                </div>
                
                <div>
                    <label class="inputLabel">Password</label><br/>
                    <input class="inputFieldWide" name="password" onChange={this.onChange} type="password" value={this.state.password}/><br/>
                </div>
                
                <button class="buttonWide" onClick={this.signupReq}>Sign Up</button>
                <p class="message">Alredy have an account?  <Link to="/login" class="highlightMessage">Login</Link></p>
                {this.state.msg==""?null:<p class="warningMessage">{this.state.msg}</p>}
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