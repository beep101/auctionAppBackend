alter table bids drop constraint bids_bidder_fkey;
alter table bids drop constraint bids_item_fkey;
alter table bids add foreign key (bidder) references users(id) on delete cascade;
alter table bids add foreign key (item) references items(id) on delete cascade;

alter table items drop constraint items_seller_fkey;
alter table items drop constraint items_winner_fkey;
alter table items add foreign key (seller) references users(id) on delete cascade;
alter table items add foreign key (winner) references users(id) on delete cascade;

alter table notifications drop constraint notifications_userid_fkey;
alter table notifications add foreign key (userid) references users(id) on delete cascade;

alter table orders drop constraint orders_item_fkey;
alter table orders add foreign key (item) references items(id) on delete cascade;

alter table pushsubs drop constraint pushsubs_userid_fkey;
alter table pushsubs add foreign key (userid) references users(id) on delete cascade;

alter table wishlist drop constraint wishlist_itemid_fkey;
alter table wishlist drop constraint wishlist_userid_fkey;
alter table wishlist add foreign key (itemid) references items(id) on delete cascade;
alter table wishlist add foreign key (userid) references users(id) on delete cascade;
