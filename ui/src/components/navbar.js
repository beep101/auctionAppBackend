import React from 'react';
import { Link } from 'react-router-dom';
import AuthContext from '../context';
import '../styles/styles.css'

class NavBar extends React.Component{

    onChange=(e)=>{
        this.context.setSearchText(e.target.value);
    }
    
    render(){

        return(
            <div className="navBar">
                <div className="logoStyle">
                    <Link to="/">
                        <img className="logoImg" src="/images/logo.svg"/>
                    </Link>
                </div>
                <div className="searchBox">
                    <Link to="/search">
                        <input onChange={this.onChange} className="searchBoxInput" name="searchBox" />
                    </Link>
                </div>
                <div className="navLinks">
                    <Link to="/">
                        <span className="linkStyle">
                            Home
                        </span>
                    </Link>
                    <Link to="/shop">
                        <span className="linkStyle">
                            Shop
                        </span>
                    </Link>
                    <Link to="/account">
                        <span className="linkStyle">
                            My Account
                        </span>
                    </Link>
                </div>
            </div>
        );
    }
}

NavBar.contextType=AuthContext;
export default NavBar