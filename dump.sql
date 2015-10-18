# Should be run manually under super user
# CREATE EXTENSION cube;
# CREATE EXTENSION earthdistance;

CREATE SEQUENCE customers_id_seq;
CREATE TABLE customers (
	id smallint NOT NULL DEFAULT nextval('customers_id_seq'),
	name text,
	surname text,
	login text UNIQUE,
	password text,
	email text
);
ALTER SEQUENCE customers_id_seq OWNED BY customers.id;

CREATE SEQUENCE shops_id_seq;
CREATE TABLE shops (
	id smallint NOT NULL DEFAULT nextval('shops_id_seq'),
	name text UNIQUE,
	position point,
	desctiption text

);

ALTER SEQUENCE shops_id_seq OWNED BY shops.id;
ALTER TABLE shops ADD CONSTRAINT shop_id_unique UNIQUE (id);

CREATE SEQUENCE merchants_id_seq;
CREATE TABLE merchants (
	id smallint NOT NULL DEFAULT nextval('customers_id_seq'),
	name text,
	surname text,
	login text UNIQUE,
	password text,
	email text,
	shop_id integer REFERENCES shops(id) NOT NULL
);
ALTER SEQUENCE merchants_id_seq OWNED BY merchants.id;


CREATE SEQUENCE offers_id_seq;
CREATE TABLE offers (
	id smallint NOT NULL DEFAULT nextval('offers_id_seq'),
	shop_id integer REFERENCES shops(id) NOT NULL,
	item_id integer REFERENCES offers(id) NOT NULL,
	name text,
	description text,
	percent float,
	date_time varchar(10)
);

ALTER SEQUENCE offers_id_seq OWNED BY offers.id;
ALTER TABLE offers ADD CONSTRAINT offer_id_unique UNIQUE (id);

CREATE SEQUENCE items_id_seq;
CREATE TABLE items (
	id smallint NOT NULL DEFAULT nextval('items_id_seq'),
	shop_id integer REFERENCES shops(id) NOT NULL,
	category_id integer REFERENCES categories(id) NOT NULL,
	name text,
	description text,
	price float,
	image_url varchar(250)
);

ALTER SEQUENCE items_id_seq OWNED BY items.id;

CREATE SEQUENCE categories_id_seq;
CREATE TABLE categories (
	id smallint NOT NULL DEFAULT nextval('categories_id_seq'),
	name text
);

ALTER SEQUENCE categories_id_seq OWNED BY categories.id;


CREATE INDEX items_shops_id ON items (shop_id);
CREATE INDEX items_offers_id ON items (offer_id);
CREATE INDEX offers_shops_id ON offers (shop_id);

ALTER TABLE items ADD CONSTRAINT item_shop_unique UNIQUE (shop_id);
ALTER TABLE items ADD CONSTRAINT item_offer_unique UNIQUE (offer_id);
ALTER TABLE offers ADD CONSTRAINT offer_shop_unique UNIQUE (shop_id);
