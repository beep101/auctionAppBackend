import React from 'react';
import { Link } from 'react-router-dom';
import AuthContext from '../context';
import '../styles/styles.css'

class LoginSignupAccountButtons extends React.Component{
    constructor(props){
        super(props);
    }

    logoutUser=()=>{
        this.context.logout();
        localStorage.removeItem('token');
    }
    
    render(){       
        if(this.context.jwt==""){
            return (
                <span>
                    <Link to="/login">
                        <span className="textHeaderBar link">
                            Log In
                        </span>
                    </Link>
                    <span className="textHeaderBar"> or </span>
                    <Link to="/register">
                        <span className="textHeaderBar link">
                            Register
                        </span>
                    </Link>
                </span>
                );
        }else{
            return(
                <span className="textHeaderBar link" onClick={this.logoutUser}>
                    Logout
                </span>
            );
        }
    }
}

LoginSignupAccountButtons.contextType=AuthContext;

export default LoginSignupAccountButtons