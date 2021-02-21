import React, {Component} from 'react';
import ReactDOM from 'react-dom';
import Login from './login';
import Register from './register';
import Auth, { AuthProvider } from '../context';
import jwtDecode from 'jwt-decode';
import SocialMediaLinks from "./socialMediaLink";
import NavBar from './navbar';
import LoginSignupAccountButtons from './loginSignupAccountButtons';

class Wrap extends Component{

    constructor(props){
        super(props);
        this.state={
            auth:{
                jwt:'',
                user:{}
            }
        }
    }
    loginClick=()=>{
        ReactDOM.render(<Login loginSuccess={this.setAuth}/>,document.getElementById("content"));

    }

    signupClick=()=>{
        ReactDOM.render(<Register signupSuccess={this.setAuth}/>,document.getElementById("content"));

    }

    accountClick=()=>{
        ReactDOM.render(<Account/>,document.getElementById("content"))
    }
    render(){
        const headerStyle={
            padding: '5px',
            'background-color': '#252525',
            position: 'fixed',
            top: '0',
            left: '0',
            width: '100%'
        }; 
        const navBarStyle={
            'margin-top':'35px',
            width:'100%'
        }
        const footerStyle={
            position: 'fixed',
            bottom: '0',
            left: '0',
            width: '100%',
            'background-color': '#252525'
        };
        const footerTextStyle={
            color:'#ffffff',
            'font-family': 'sans-serif',
            padding:'5px'
        }
        return(
            <AuthProvider value={this.state.auth}>
            <div id="header"  style={headerStyle}>
                <span style={{float:'left', 'margin-left':'5px'}}>
                    <SocialMediaLinks />
                </span>
                <span style={{float:'right', 'margin-right':'15px'}}>
                    <LoginSignupAccountButtons loginClick={this.loginClick} signupClick={this.signupClick} accountClick={this.accountClick} />
                </span>
            </div>
            <div style={navBarStyle}>
                <NavBar />
            </div>
            <div id="content" style={{display: 'flex','justify-content': 'center'}}></div>

            <div id="footer"  style={footerStyle}>
                <span style={{display:"inline-block",float:'left', 'margin-left':'350px'}}>
                    <ul style={{'list-style-type': 'none'}}>
                        <li style={{...footerTextStyle,...{fontSize:'25px'}}}>Auction Purple</li>
                        <li style={footerTextStyle}>About Us</li>
                        <li style={footerTextStyle}>Terms and Conditions</li>
                        <li style={footerTextStyle}>Privacy Policy</li>
                    </ul>
                </span>
                
                <span style={{display:"inline-block",float:'right', 'margin-right':'600px'}}>
                    <ul style={{'list-style-type': 'none'}}>
                        <li style={{...footerTextStyle,...{fontSize:'25px'}}}>Get in Touch</li>
                        <li style={footerTextStyle}>Call Us at +123 45 678 789</li>
                        <li style={footerTextStyle}>support@auctionpurple.com</li>
                        <li><SocialMediaLinks/></li>
                    </ul>
                </span>
            </div>
            </AuthProvider>
        );    
    }

    setAuth=(jwt)=>{
        this.setState({
            auth:{
                jwt:jwt,
                user:jwtDecode(jwt)
            }
        });
        ReactDOM.render(<div>{this.state.auth.user.sub}</div> ,document.getElementById("content"))
    }


}

export default Wrap;