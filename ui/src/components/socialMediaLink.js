import React from 'react';

class SocialMediaLinks extends React.Component{
    render(){
        const linkStyle = {
            color: "#ffffff",
            padding: '4px',
            'font-family': 'sans-serif',
            'font-weight': "bold"
          };
        return(
            <span>
                <a href="https://facebook.com"><span  style={linkStyle}>Fb</span></a>
                <a href="https://twitter.com"><span  style={linkStyle}>Tw</span></a>
                <a href="https://instagram.com"><span  style={linkStyle}>Ig</span></a>
                <a href="https://linkedin.com" ><span  style={linkStyle}>In</span></a>
            </span>
        );
    }
}

export default SocialMediaLinks