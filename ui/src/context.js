import React from 'react';
import jwtDecode from 'jwt-decode';
import { getAllWishes } from './apiConsumer/wishlistConsumer';

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
                user:jwtDecode(token),
                wishes:[]
            }
            getAllWishes(token,(success,data)=>{
                if(success)
                    this.setState({['wishes']:data});
            })
        }else{
            this.state={
                jwt:"",
                user:{},
                wishes:[]
            }
        }
    }

    login=(jwt)=>{
        this.setState({
            jwt:jwt,
            user:jwtDecode(jwt),
            searchCallback:()=>{console.log('No callback set')},
            wishes:[]
        })
        getAllWishes(jwt,(success,data)=>{
            if(success)
                this.setState({['wishes']:data});
        })
    }

    logout=()=>{
        this.setState({
            jwt:"",
            user:{},
            searchCallback:()=>{console.log('No callback set')},
            wishes:[]
        })
    }

    addWish=(wish)=>{
        let wishes=[...this.state.wishes];
        wishes.push(wish);
        this.setState({['wishes']:wishes});
    }

    removeWish=(wish)=>{
        let wishes=[...this.state.wishes];
        wishes=wishes.filter(w=>(wish.id!=w.id));
        this.setState({['wishes']:wishes});
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

    setUser=(user)=>{
        let newUser={...this.state.user};
        newUser.user=user;
        this.setState({['user']:newUser});
    }


    render(){
        const {jwt,user,wishes}=this.state;
        const {login,logout,search,setSearchCallback,removeSearchCallback,setUser,addWish,removeWish}=this;
        return(
            <AuthContext.Provider value={{jwt,user,wishes,login,logout,search,setSearchCallback,removeSearchCallback,setUser,addWish,removeWish}}>
                {this.props.children}
            </AuthContext.Provider>
        )
    }
}

export default AuthContext;