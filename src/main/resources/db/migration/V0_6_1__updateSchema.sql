CREATE TABLE pushSubs(
	id SERIAL PRIMARY KEY,
	url VARCHAR not null,
	auth VARCHAR not null,
	userid INT,
	FOREIGN KEY (userid) REFERENCES users(id)
)