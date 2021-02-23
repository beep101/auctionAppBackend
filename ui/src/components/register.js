import React,{Component}  from 'react';
import axios from 'axios';
import AuthContext from '../context';
import '../styles/header.css'

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
            <div>
                <label class="inputLabel">First Name</label><br/>
                <input class="inputField" name="firstName" onChange={this.onChange} value={this.state.firstName}/><br/>

                <label class="inputLabel">Last Name</label><br/>
                <input class="inputField" name="lastName" onChange={this.onChange} value={this.state.lastName}/><br/>

                <label class="inputLabel">Email</label><br/>
                <input class="inputField" name="email" onChange={this.onChange} value={this.state.email}/><br/>

                <label class="inputLabel">Password</label><br/>
                <input class="inputField" name="password" onChange={this.onChange} type="password" value={this.state.password}/><br/>

                <button class="confirmButton" onClick={this.signupReq}>Sign Up</button>
                <p class="messageText">{this.state.msg}</p>
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
        axios.post("/api/signup",userData)
            .then((response)=>{
                if(response.status==200){
                    this.setState({
                        email:'',
                        password:'',
                        firstName:'',
                        lastName:'',
                        msg:''
                    });
                    this.context.login(response.data)
                    this.props.history.push("/");
                }else{
                    this.setState({
                        email:'',
                        password:'',
                        firstName:'',
                        lastName:'',
                        msg:'Bad email or password'
                    })
                }},
                (error)=>{
                this.setState({
                    email:'',
                    password:'',
                    firstName:'',
                    lastName:'',
                    msg:'Somethong went wrong, please try again'
                })
        });
    }


}

Register.contextType=AuthContext;

export default Register;