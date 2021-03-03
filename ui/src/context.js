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
                searchText:"",
                searchCallback:()=>{console.log('No callback set')}
            }
        }else{
            this.state={
                jwt:"",
                user:{},
                searchText:"",
                searchCallback:()=>{console.log('No callback set')}
            }
        }
    }

    login=(jwt)=>{
        this.setState({
            jwt:jwt,
            user:jwtDecode(jwt),
            searchText:"",
            searchCallback:()=>{console.log('No callback set')}
        })
    }

    logout=()=>{
        this.setState({
            jwt:"",
            user:{},
            searchText:"",
            searchCallback:()=>{console.log('No callback set')}
        })
    }

    setSearchText=(text)=>{
        this.setState({['searchText']:text});
        this.state.searchCallback(text);
    }

    setSearchCallback=(callback)=>{
        this.setState({['searchCallback']:callback})
    }


    render(){
        const {jwt,user,searchText}=this.state;
        const {login,logout,setSearchText,setSearchCallback}=this;
        return(
            <AuthContext.Provider value={{jwt,user,searchText,login,logout,setSearchText,setSearchCallback}}>
                {this.props.children}
            </AuthContext.Provider>
        )
    }
}

export default AuthContext;