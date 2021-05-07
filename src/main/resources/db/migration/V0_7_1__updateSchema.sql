alter table users add column merchantid varchar not null default '';
alter table items add column completed boolean not null default false;