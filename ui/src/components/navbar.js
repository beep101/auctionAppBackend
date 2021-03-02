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
            <div class="navBar">
                <div class="logoStyle">
                    Auction Purple
                </div>
                <div class="searchBox">
                    <Link to="/search">
                        <input onChange={this.onChange} class="searchBoxInput" name="searchBox" />
                    </Link>
                </div>
                <div class="navLinks">
                    <Link to="/">
                        <span class="linkStyle">
                            Home
                        </span>
                    </Link>
                    <Link to="/shop">
                        <span class="linkStyle">
                            Shop
                        </span>
                    </Link>
                    <Link to="/account">
                        <span class="linkStyle">
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