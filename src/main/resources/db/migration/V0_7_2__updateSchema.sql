alter table items add column winner int;
alter table items add column paid boolean not null default false;
alter table items add foreign key (winner) references users(id);