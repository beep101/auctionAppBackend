alter table users add column pushNotifications boolean default true;
update users set pushNotifications=true;