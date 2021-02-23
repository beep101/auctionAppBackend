import React from 'react';
import jwtDecode from 'jwt-decode';

const AuthContext = React.createContext();

export class AuthProvider extends React.Component{
    state={
        jwt:"",
        user:{}
    }

    login=(jwt)=>{
        this.setState({
            jwt:jwt,
            user:jwtDecode(jwt)
        })
    }

    logout=()=>{
        this.setState({
            jwt:"",
            user:{}
        })
    }


    render(){
        const {jwt,user}=this.state;
        const {login,logout}=this;
        return(
            <AuthContext.Provider value={{jwt,user,login,logout}}>
                {this.props.children}
            </AuthContext.Provider>
        )
    }
}

export default AuthContext;