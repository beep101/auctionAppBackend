import React,{Component}  from 'react';
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
            <div >
                <label class="inputLabel">Email</label><br/>
                <input class="inputField" id="email" name="email" onChange={this.onChange} value={this.state.email}/><br/>
                
                <label class="inputLabel">Password</label><br/>
                <input class="inputField" id="passwd" name="password" type="password"  onChange={this.onChange} value={this.state.password}/><br/>
                
                <button class="confirmButton" onClick={this.loginReq}>Log In</button>
                <p class="messageText">{this.state.msg}</p>
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