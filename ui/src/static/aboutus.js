import React from 'react';

function About(){
    return(
        <div className="about">
            <h1 className="highlight">About Us</h1>
            <div className="aboutContainer">
                <div className="aboutTextContainer">
                    <p className="textBody">
                        Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed ac convallis leo, volutpat eleifend ex. Maecenas gravida justo sem, quis tempor purus mollis quis. Sed laoreet porta mi, et tincidunt lacus scelerisque a. Praesent interdum mauris a urna congue, eget varius enim vulputate. Proin malesuada scelerisque diam sit amet dapibus. Nulla vitae consectetur eros, pellentesque malesuada est. Fusce pulvinar pretium ante quis convallis. Praesent condimentum, enim vitae hendrerit accumsan, ipsum tortor euismod orci, at elementum arcu felis eu odio. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas sed est vel velit condimentum cursus eu quis dolor. Phasellus elementum lorem nec enim semper feugiat. Fusce feugiat, purus ac commodo dignissim, arcu risus aliquam libero, at imperdiet libero sapien at arcu. Mauris nec ligula quis dolor imperdiet placerat. Donec commodo tristique ligula nec porta. Nullam condimentum neque hendrerit augue varius, ac convallis libero pulvinar. Sed bibendum risus at sagittis bibendum. Integer imperdiet cursus ultrices. Suspendisse potenti. Fusce nulla nisl, porttitor quis augue sit amet, accumsan porta sem. Maecenas elementum nunc vitae nibh eleifend semper. </p>
                    <p className="textBody">
                        Nunc vulputate non nulla quis maximus. Praesent sit amet augue non tortor fringilla consequat. Cras et lorem sem. Nulla quam augue, imperdiet ut nibh eu, ornare viverra nibh. Fusce laoreet diam vitae justo lacinia, in porta tortor imperdiet. Donec lorem orci, tincidunt a ex non, ornare feugiat justo. Quisque eget sem quis lorem malesuada efficitur id vel dolor. Praesent cursus libero sed ex ullamcorper iaculis at ac sapien. Mauris ac ullamcorper eros. Proin sed nibh magna. Donec et ligula mauris. Vivamus consectetur fermentum elit, vel sodales libero ultrices ac.
                    </p>
                </div>
                <div>
                    <img src="/images/pic1.jpg" className="aboutImgBig" />
                    <div>
                        <img src="/images/pic2.jpg" className="aboutImgSmall"/>
                        <img src="/images/pic3.jpg" className="aboutImgSmall"/>
                    </div>
                </div>
            </div>  
        </div>
    )
}

export default About