import React from 'react';
import { Link } from 'react-router-dom';

class NavBar extends React.Component{
    render(){

        return(
            <div>
                <div class="logoStyle">
                    Auction Purple
                </div>
                <div class="searchBox">
                    <input class="searchBox" name="searchBox">
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