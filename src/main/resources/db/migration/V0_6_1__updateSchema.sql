CREATE TABLE pushSubs(
	id SERIAL PRIMARY KEY,
	url VARCHAR not null,
	auth VARCHAR not null,
	userid INT not null,
	FOREIGN KEY (userid) REFERENCES users(id)
)