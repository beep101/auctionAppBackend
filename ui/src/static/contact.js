import React from 'react';
import SocialMediaLinks from './socialMediaLink' 
import "../styles/styles.css"

function Contacts(){
    return(
    <ul style={{'list-style-type': 'none'}}>
        <li class="footerText font20">Get in Touch</li>
        <li class="footerText font12">Call Us at +123 45 678 789</li>
        <li class="footerText font12">support@auctionpurple.com</li>
        <li class="footerText font12 paddingTop10" ><SocialMediaLinks/></li>
    </ul>
    )
}

export default Contacts
