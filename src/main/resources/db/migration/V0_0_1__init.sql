create table users(
	id serial primary key,
	email varchar unique,
	passwd varchar,
	name varchar,
	surname varchar
);

create table items(
	id serial primary key,
	name varchar,
	description text,
	startingPrice numeric(12,2),
	startTime timestamp,
	endTime timestamp,
	seller integer,
	foreign key (seller) references users(id)
);

create table bids(
	id serial primary key,
	bidder integer,
	item integer,
	attempt integer,
	amount numeric(12,2),
	time timestamp,
	foreign key (bidder) references users(id),
	foreign key (item) references items(id)
)