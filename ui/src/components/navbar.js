import React from 'react';

class NavBar extends React.Component{
    render(){
        const linkStyle={
            margin:'20px',
            'font-family': 'sans-serif',
            'font-size':'20px',
            color: '#252525',
            cursor: 'pointer'
        }

        const searchBoxStyle={
            width:'400px',
            'font-family': 'sans-serif',
            'font-size':'20px',
        }
        return(
            <div>
                <div style={{display:"inline-block",float:'left','margin-left': '150px' ,color:'#52307c', 'font-size':'25px',}}>
                    Auction Purple
                </div>
                <div style={{display:"inline-block",'margin-left': '200px'}}>
                    <input id="searchBox" name="searchBox" style={searchBoxStyle}>
                    </input>
                </div>
                <div style={{display:"inline-block",float:'right','margin-right': '50px'}}>
                    <span style={linkStyle}>
                        Home
                    </span>
                    <span style={linkStyle}>
                        Shop
                    </span>
                    <span style={linkStyle}>
                        My Account
                    </span>
                </div>
            </div>
        );
    }
}

export default NavBar