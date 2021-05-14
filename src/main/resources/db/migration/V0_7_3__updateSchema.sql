create table orders(
	orderId varchar primary key,
	item int not null,
	successeful boolean not null default false,
	foreign key (item) references items(id)
);