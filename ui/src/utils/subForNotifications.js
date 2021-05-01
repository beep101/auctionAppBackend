import { subscribeForPushNotifcations, unsubscribePushNotifications } from '../apiConsumer/pushNotificationonsumer';

"use strict";

export async function subForNotifications(token,notificationsKey){
    await navigator.serviceWorker.register("/sw.js", {scope: "/"});
    const registration=await navigator.serviceWorker.ready;
    const subscription=await registration.pushManager.getSubscription();
    if(!subscription){
        const subData = await registration.pushManager.subscribe({
            userVisibleOnly: true,
            applicationServerKey: notificationsKey
        })
        subscribeForPushNotifcations(token,subData,(success,data)=>{
            if(!success){
                console.log("Cannot subscribe to notifications")
            }
        });
    }
}

export async function unsubNotifications(token){
    const registration = await navigator.serviceWorker.ready;
    const subscription = await registration.pushManager.getSubscription();
    if (subscription) {
        const notificationsLink=subscription.endpoint;
        const successful = await subscription.unsubscribe();
        if (successful) {
            unsubscribePushNotifications(token,notificationsLink,(success,data)=>{})
        }
    }
}