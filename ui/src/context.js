import React from 'react';
import jwtDecode from 'jwt-decode';

const AuthContext = React.createContext();

export class AuthProvider extends React.Component{

    constructor(props){
        super(props);
        let token=localStorage.getItem('token');
        if(token){
            this.state={
                jwt:token,
                user:jwtDecode(token),
                searchText:""
            }
        }else{
            this.state={
                jwt:"",
                user:{},
                searchText:""
            }
        }
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

    setSearchText=(text)=>{
        this.setState({['searchText']:text})
    }


    render(){
        const {jwt,user,searchText}=this.state;
        const {login,logout,setSearchText}=this;
        return(
            <AuthContext.Provider value={{jwt,user,searchText,login,logout,setSearchText}}>
                {this.props.children}
            </AuthContext.Provider>
        )
    }
}

export default AuthContext;