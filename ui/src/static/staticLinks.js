import React from 'react';
import {Link} from 'react-router-dom';
import '../styles/styles.css'

function StaticLinks(){
    return(
        <ul className="footerLinkSet">
            <li className="footerText font20">Auction Purple</li>
            <Link to="/about"><li className="footerText font12">About Us</li></Link>
            <Link to="/termsandconditions"><li className="footerText font12">Terms and Conditions</li></Link>
            <Link to="/privacypolicy"><li className="footerText font12">Privacy Policy</li></Link>
        </ul>
    )
}

export default StaticLinks