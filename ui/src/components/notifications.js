import React, { useContext, useEffect, useRef, useState } from 'react'
import { Link } from 'react-router-dom';
import { getAllPushNotifications } from '../apiConsumer/pushNotificationonsumer';
import AuthContext from '../context';

export default function Notifications(props){
    const boxRef=useRef(null);
    const [notifications,setNotifications]=useState([]);

    const context = useContext(AuthContext);

    useEffect(()=>{
        getAllPushNotifications(context.jwt,(success,data)=>{
            if(success)
                setNotifications(data);
        })
    },[]);

    useEffect(() => {
        document.addEventListener('mousedown', handleClickOutside);
        return () => {
          document.removeEventListener('mousedown', handleClickOutside);
        };
    }, []);

    const handleClickOutside=(e)=>{
        if (boxRef.current && !boxRef.current.contains(e.target)) {
            props.close();
        }
    }

    return(
        <div ref={boxRef} className="notificationsBox">
            {notifications.map(n=><Notification notification={n}/>)}
        </div>
    )
}

function Notification(props){
    return(
        <Link className="notificationContainer" to={props.notification.link}>
            <div className="notificationTitle">{props.notification.title}</div>
            <div className="notificationBody">{props.notification.body}</div>
        </Link>
    )
}