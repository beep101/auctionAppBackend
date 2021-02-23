import React, {Component} from 'react';
import Login from './login';
import Register from './register';
import AuthContext, { AuthProvider } from '../context';
import SocialMediaLinks from "./socialMediaLink";
import NavBar from './navbar';
import LoginSignupAccountButtons from './loginSignupAccountButtons';
import {Route, BrowserRouter as Router, Link} from 'react-router-dom';
import About from '../static/aboutus';
import TermsAndConditions from '../static/termsandconditions';
import PrivacyPolicy from '../static/privacypolicy';
import Home from './home';
import Shop from './shop';
import Account from './account';


class Wrap extends Component{
 
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
            <AuthProvider>
                <Router>
                    <div id="header"  style={headerStyle}>

                        <span style={{float:'left', 'margin-left':'5px'}}>
                            <SocialMediaLinks />
                        </span>

                        <span style={{float:'right', 'margin-right':'15px'}}>
                            <LoginSignupAccountButtons />
                        </span>

                    </div>

                    <div style={navBarStyle}>
                        <NavBar />
                    </div>

                    <div id="content" style={{display: 'flex','justify-content': 'center'}}>
                        <Route path="/" exact component={Home} />
                        <Route path="/login" exact component={ Login }/>
                        <Route path="/register" exact component={Register} />
                        <Route path="/about" exact component={ About } />
                        <Route path="/termsandconditions" exact component={ TermsAndConditions } />
                        <Route path="/privacypolicy" exact component={ PrivacyPolicy } />
                        <Route path="/shop" exact component={Shop}/>
                        <Route path="/account" exact component={Account} />
                    </div>

                    <div id="footer"  style={footerStyle}>
                        <span style={{display:"inline-block",float:'left', 'margin-left':'350px'}}>
                            <ul style={{'list-style-type': 'none'}}>
                                <li style={{...footerTextStyle,...{fontSize:'25px'}}}>Auction Purple</li>
                                <Link to="/about"><li style={footerTextStyle}>About Us</li></Link>
                                <Link to="/termsandconditions"><li style={footerTextStyle}>Terms and Conditions</li></Link>
                                <Link to="/privacypolicy"><li style={footerTextStyle}>Privacy Policy</li></Link>
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
                </Router>
            </AuthProvider>
        );    
    }

}

export default Wrap;