import React from 'react';
import SocialMediaLinks from './socialMediaLink' 
import "../styles/styles.css"

function Contacts(){
    return(
    <ul className="contactList">
        <li className="footerText font20">Get in Touch</li>
        <li className="footerText font12">Call Us at +123 45 678 789</li>
        <li className="footerText font12">support@auctionpurple.com</li>
        <li className="footerText font12 paddingTop10" ><SocialMediaLinks/></li>
    </ul>
    )
}

export default Contacts
