import React from 'react';
import {AuthConsumer} from '../context';

class LoginSignupAccountButtons extends React.Component{
    constructor(props){
        super(props);
    }
    
    render(){
        const buttonStyle={
            background: 'none',
            border: 'none',
            'font-family': 'sans-serif',
            color: '#ffffff',
            cursor: 'pointer'
        }
        return(
            <AuthConsumer>
                {context=>{
                    if(context.jwt==""){
                        return (
                        <span>
                            <span onClick={this.props.loginClick} style={buttonStyle}>
                                Log In
                            </span>
                            <span style={{color:'#ffffff', 'font-family':'sans-serif','margin-left':'10px','margin-right':'10px'}}> or </span>
                            <span onClick={this.props.signupClick} style={buttonStyle}>
                                Sign Up
                            </span>
                        </span>
                        );
                    }else{
                        return(
                        <span onClick={this.props.accountClick} style={buttonStyle}>
                            {context.user.sub}
                        </span>
                        );
                    }
                }}

            </AuthConsumer>
            
        );
    }


}

export default LoginSignupAccountButtons