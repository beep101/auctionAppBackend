import React from 'react';
import {Link} from 'react-router-dom';
import '../styles/styles.css'

function StaticLinks(){
    return(
        <ul style={{'list-style-type': 'none'}}>
            <li class="footerText font25">Auction Purple</li>
            <Link to="/about"><li class="footerText">About Us</li></Link>
            <Link to="/termsandconditions"><li class="footerText">Terms and Conditions</li></Link>
            <Link to="/privacypolicy"><li class="footerText">Privacy Policy</li></Link>
        </ul>
    )
}

export default StaticLinks