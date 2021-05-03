import { get, post, del, defaultErrorHandler } from './apiConsumer'

export function getPushNotificationPublicKey(token,handler){
    get('pushNotifications/publicKey',{headers:{'Authorization' : 'Bearer '+token}}).then(
        (response)=>{
            handler(true,response.data);
        },
        (error)=>{
            defaultErrorHandler(error,handler);
        }
    );
}

export function getAllPushNotifications(token,handler){
    get('pushNotifications',{headers:{'Authorization' : 'Bearer '+token}}).then(
        (response)=>{
            handler(true,response.data);
        },
        (error)=>{
            defaultErrorHandler(error,handler);
        }
    );
}

export function subscribeForPushNotifcations(token,data,handler){
    post('pushNotifications',data,{headers:{'Authorization' : 'Bearer '+token}}).then(
        (response)=>{
            handler(true,response.data);
        },
        (error)=>{
            defaultErrorHandler(error,handler);
        }
    );
}

export function unsubscribePushNotifications(token,link,handler){
    del(`pushNotifications?link=${link}`,{headers:{'Authorization' : 'Bearer '+token}}).then(
        (response)=>{
            handler(true,response.data);
        },
        (error)=>{
            defaultErrorHandler(error,handler);
        }
    )
}