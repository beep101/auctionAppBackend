import React from 'react';
import SocialMediaLinks from './socialMediaLink' 
import "../styles/styles.css"

function Contacts(){
    return(
    <ul style={{'list-style-type': 'none'}}>
        <li class="footerText font25">Get in Touch</li>
        <li class="footerText">Call Us at +123 45 678 789</li>
        <li class="footerText">support@auctionpurple.com</li>
        <li><SocialMediaLinks/></li>
    </ul>
    )
}

export default Contacts
