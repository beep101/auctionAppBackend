CREATE TABLE notifications(
	id SERIAL PRIMARY KEY,
	title VARCHAR not null,
	body VARCHAR not null, 
	userid INT not null,
	time timestamp not null,
	FOREIGN KEY (userid) REFERENCES users(id) 
)