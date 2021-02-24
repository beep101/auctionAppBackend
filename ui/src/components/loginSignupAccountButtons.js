import React from 'react';
import { Link } from 'react-router-dom';
import AuthContext from '../context';
import '../styles/styles.css'

class LoginSignupAccountButtons extends React.Component{
    constructor(props){
        super(props);
    }
    
    render(){       
        if(this.context.jwt==""){
            return (
                <span>
                    <Link to="/login">
                        <span class="text link">
                            Log In
                        </span>
                    </Link>
                    <span class="text"> or </span>
                    <Link to="/register">
                        <span class="text link">
                            Register
                        </span>
                    </Link>
                </span>
                );
        }else{
            return(
                <span class="text link">
                    {this.context.user.sub}
                </span>
            );
        }
    }


}

LoginSignupAccountButtons.contextType=AuthContext;

export default LoginSignupAccountButtons