import React, { useContext } from 'react';
import AuthContext from '../context';

export default function AccountSettings(props){

    return(
        <div>
            <LogoutButton/>
        </div>
    )
}

function LogoutButton(props){
    const context=useContext(AuthContext);

    const logout=()=>{
        context.logout();
        localStorage.removeItem('token');
    }

    return(
        <div onClick={logout}>
            Logout
        </div>
    )
}