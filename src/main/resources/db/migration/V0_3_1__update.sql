CREATE TABLE subcategories(
	id SERIAL PRIMARY KEY, 
	name VARCHAR NOT NULL,
	category INTEGER NOT NULL,
	FOREIGN KEY (category) REFERENCES categories(id)
);

INSERT INTO subcategories(id,NAME, category) VALUES(0,'Other',0);
INSERT INTO subcategories(NAME, category) VALUES('Other',1);
INSERT INTO subcategories(NAME, category) VALUES('Other',2);
INSERT INTO subcategories(NAME, category) VALUES('Other',3);
INSERT INTO subcategories(NAME, category) VALUES('Other',4);
INSERT INTO subcategories(NAME, category) VALUES('Other',5);
INSERT INTO subcategories(NAME, category) VALUES('Other',6);
INSERT INTO subcategories(NAME, category) VALUES('Other',7);

ALTER TABLE items ADD COLUMN subcategory INTEGER;
ALTER TABLE items ADD FOREIGN KEY (subcategory) REFERENCES subcategories(id);

UPDATE items SET subcategory=category;
ALTER TABLE items DROP COLUMN category;
ALTER TABLE items ALTER COLUMN subcategory SET NOT NULL;