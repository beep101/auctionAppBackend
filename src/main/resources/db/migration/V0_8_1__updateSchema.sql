alter table users add column deactivated boolean not null default false;

insert into users(id,email,passwd,name,surname) values(0,'deleted','deleted','deleted','deleted');