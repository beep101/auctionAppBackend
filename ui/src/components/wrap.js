import React, {Component, useContext} from 'react';
import Login from './login';
import Register from './register';
import AuthContext, { AuthProvider } from '../context';
import SocialMediaLinks from "../static/socialMediaLink";
import NavBar from './navbar';
import LoginSignupAccountButtons from './loginSignupAccountButtons';
import {Route, BrowserRouter as Router, Link} from 'react-router-dom';
import About from '../static/aboutus';
import TermsAndConditions from '../static/termsandconditions';
import PrivacyPolicy from '../static/privacypolicy';
import Home from './home';
import Shop from './shop';
import Account from './account';
import "../styles/styles.css"
import StaticLinks from '../static/staticLinks';
import Contacts from '../static/contact';

class Wrap extends Component{

    render(){
        return(
                <Router>
                    <div id="header"  class="headerBar">

                        <span class="floatLeft marginLeft15">
                            <SocialMediaLinks />
                        </span>

                        <span class="floatRight marginRight30">
                            <LoginSignupAccountButtons />
                        </span>

                    </div>

                    <NavBar />

                    <div id="content">
                        <Route path="/" exact component={Home} />
                        <Route path="/login" exact component={ Login }/>
                        <Route path="/register" exact component={Register} />
                        <Route path="/about" exact component={ About } />
                        <Route path="/termsandconditions" exact component={ TermsAndConditions } />
                        <Route path="/privacypolicy" exact component={ PrivacyPolicy } />
                        <Route path="/shop" exact component={Shop}/>
                        <Route path="/account" exact component={Account} />
                    </div>

                    <div id="footer"  class="footerBar">
                        <span class="inlineBlock">
                            <StaticLinks />
                        </span>
                        <span class="inlineBlock">
                            <Contacts />
                        </span>
                    </div>
                </Router>
        );    
    }

}

Wrap.contextType=AuthContext;

export default Wrap;