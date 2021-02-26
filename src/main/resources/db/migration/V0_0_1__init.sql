create table users(
	id serial primary key,
	email varchar unique not null,
	passwd varchar not null,
	name varchar not null,
	surname varchar not null
);

create table items(
	id serial primary key,
	name varchar not null,
	description text not null,
	startingPrice numeric(12,2) not null,
	startTime timestamp not null,
	endTime timestamp not null,
	seller integer not null,
	foreign key (seller) references users(id)
);

create table bids(
	id serial primary key,
	bidder integer not null,
	item integer not null,
	attempt integer not null,
	amount numeric(12,2) not null,
	time timestamp not null,
	foreign key (bidder) references users(id),
	foreign key (item) references items(id)
)