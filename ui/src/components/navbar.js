import React from 'react';
import { Link } from 'react-router-dom';
import '../styles/styles.css'

class NavBar extends React.Component{
    render(){

        return(
            <div class="navBar">
                <div class="logoStyle">
                    Auction Purple
                </div>
                <div class="searchBox">
                    <input class="searchBoxInput" name="searchBox">
                    </input>
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

export default NavBar