import React from 'react';
import { Link } from 'react-router-dom';
import AuthContext from '../context';
import '../styles/styles.css'
import { withRouter } from "react-router-dom";

class NavBar extends React.Component{

    constructor(props){
        super(props);
        this.searchText="";
    }

    onChange=(e)=>{
        this.searchText=e.target.value;
    }

    onEnter=(e)=>{
        if(e.key==='Enter'){
            this.context.search(this.searchText);
            this.props.history.push("/search");
        }
    }

    shopClick=()=>{
        this.context.search("");
        this.props.history.push("/shop");
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
                    <input onKeyDown={this.onEnter} onChange={this.onChange} className="searchBoxInput" name="searchBox" />
                </div>
                <div className="navLinks">
                    <Link to="/">
                        <span className="linkStyle">
                            Home
                        </span>
                    </Link>
                    <span className="linkStyle" onClick={this.shopClick}>
                        Shop
                    </span>
                    <Link to="/account">
                        <span className="navbarDropdownContainer">
                            <div className="linkStyle">My Account</div>
                            <div className="navbarDropdownItemsContainer">
                                <Link className="navbarDropdownItem" to='/account?tab=profile'>Profile</Link>
                                <Link className="navbarDropdownItem" to='/account?tab=seller'>Seller</Link>
                                <Link className="navbarDropdownItem" to='/account?tab=bids'>Bids</Link>
                                <Link className="navbarDropdownItem" to='/account?tab=watchlist'>Watchlist</Link>
                                <Link className="navbarDropdownItem" to='/account?tab=settings'>Settings</Link>
                            </div>
                        </span>
                    </Link>
                </div>
            </div>
        );
    }
}

NavBar.contextType=AuthContext;
export default withRouter(NavBar)