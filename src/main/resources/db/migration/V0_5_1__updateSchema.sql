CREATE TABLE wishlist(
	id serial primary key,
	userId INT,
	itemId INT,
	FOREIGN KEY (userId) REFERENCES users(id),
	FOREIGN KEY (itemId) REFERENCES items(id)
)