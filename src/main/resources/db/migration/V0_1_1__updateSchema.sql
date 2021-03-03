create table categories(
	id serial primary key,
	name varchar unique not null
);

alter table items add category int;
alter table items add sold boolean default FALSE;
alter table items add foreign key (category)  references categories(id);

insert into categories(id,name) values(0,'unknown');
update items set category=0;

alter table items 
alter column category set not null;