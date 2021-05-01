import React, {Component, useContext} from 'react';
import Login from './login';
import Register from './register';
import AuthContext from '../context';
import SocialMediaLinks from "../static/socialMediaLink";
import NavBar from './navbar';
import LoginSignupAccountButtons from './loginSignupAccountButtons';
import {Route, BrowserRouter as Router, Link} from 'react-router-dom';
import About from '../static/aboutus';
import TermsAndConditions from '../static/termsandconditions';
import PrivacyPolicy from '../static/privacypolicy';
import Home from './home';
import Account from './account';
import "../styles/styles.css"
import StaticLinks from '../static/staticLinks';
import Contacts from '../static/contact';
import Item from './item';
import Search from './search';
import ForgotPassword from './forgotPassword';
import NewPassword from './newPassword';
import AllCategories from './allCategories';
import AddItem from './addItem';
import Notifications from './notifications';

class Wrap extends Component{

    render(){
        return(
                <Router>
                    <div id="header"  className="headerBar">

                        <span className="floatLeft marginLeft15">
                            <SocialMediaLinks />
                        </span>

                        <span className="floatRight marginRight30">
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
                        <Route path="/shop" exact component={Search}/>
                        <Route path="/account" exact component={Account} />
                        <Route path="/item" exact component={Item}/>
                        <Route path="/search" exact component={Search}/>
                        <Route path="/forgotPassword" exact component={ForgotPassword} />
                        <Route path="/newPassword" exact component={NewPassword} />
                        <Route path="/allCategories" exact component={AllCategories} />
                        <Route path="/addItem" exact component={AddItem}/>
                        <Route path="/notifications" exact component={Notifications}/>
                    </div>

                    <div id="footer"  className="footerBar">
                        <span className="inlineBlock">
                            <StaticLinks />
                        </span>
                        <span className="inlineBlock">
                            <Contacts />
                        </span>
                    </div>
                </Router>
        );    
    }

}

Wrap.contextType=AuthContext;

export default Wrap;