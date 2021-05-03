self.addEventListener('activate', event => event.waitUntil(clients.claim()));

self.addEventListener('push', event => event.waitUntil(handlePushEvent(event)));

self.addEventListener('notificationclick', event => event.waitUntil(handleNotificationClick(event)));

async function handlePushEvent(event) {
    const msg = event.data.json();
    self.registration.showNotification(msg.title, {
        body: msg.body
    });
}

async function handleNotificationClick(event) {
    await clients.openWindow("/notifications");
    event.notification.close();
}
