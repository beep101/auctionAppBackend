import React from 'react';
import jwtDecode from 'jwt-decode';

const AuthContext = React.createContext();

export class AuthProvider extends React.Component{

    constructor(props){
        super(props);
        this.searchCallback=(text)=>{console.log('No callback set')};
        this.searchText="";
        let token=localStorage.getItem('token');
        if(token){
            this.state={
                jwt:token,
                user:jwtDecode(token)
            }
        }else{
            this.state={
                jwt:"",
                user:{}
            }
        }
    }

    login=(jwt)=>{
        this.setState({
            jwt:jwt,
            user:jwtDecode(jwt),
            searchCallback:()=>{console.log('No callback set')}
        })
    }

    logout=()=>{
        this.setState({
            jwt:"",
            user:{},
            searchCallback:()=>{console.log('No callback set')}
        })
    }

    search=(text)=>{
        if(text!=null){
            this.searchText=text;
        }
        this.searchCallback(this.searchText);
    }

    setSearchCallback=(callback)=>{
        this.searchCallback=callback;
    }

    removeSearchCallback=()=>{
        this.searchCallback=(text)=>{console.log('No callback set')};
    }


    render(){
        const {jwt,user}=this.state;
        const {login,logout,search,setSearchCallback,removeSearchCallback}=this;
        return(
            <AuthContext.Provider value={{jwt,user,login,logout,search,setSearchCallback,removeSearchCallback}}>
                {this.props.children}
            </AuthContext.Provider>
        )
    }
}

export default AuthContext;