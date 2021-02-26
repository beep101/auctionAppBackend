import React from 'react';
import {Link} from 'react-router-dom';
import '../styles/styles.css'

function StaticLinks(){
    return(
        <ul class="footerLinkSet">
            <li class="footerText font20">Auction Purple</li>
            <Link to="/about"><li class="footerText font12">About Us</li></Link>
            <Link to="/termsandconditions"><li class="footerText font12">Terms and Conditions</li></Link>
            <Link to="/privacypolicy"><li class="footerText font12">Privacy Policy</li></Link>
        </ul>
    )
}

export default StaticLinks