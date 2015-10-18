from _dbutil import Connection
import psycopg2


class DBUtil(object):
    def __init__(self):
        self.conn = Connection()

    def fix_string(self, s):
       return str(s).replace("'", "\\'")

    def register_customer(self, data):
        query = """
            INSERT INTO customers (name, surname, login, password, email) VALUES (%s);
        """
        params = ", ".join([(" '%s' ") % x for x in data])
        query = query % params
        res = self._run_query(query)
        if res:
            return res

    def login_customer(self, login, password):
        query = """
            SELECT * FROM customers WHERE login = '%s' and password = '%s'
        """
        query = query % (login, password)
        return self._run_query(query, True)

    def _run_query(self, query, flag=False):
        cur = self.conn.cursor()
        try:
            cur.execute(query)
            if flag:
                rows = cur.fetchall()
        except psycopg2.Error as e:
            self.conn.rollback()
            return e
        finally:
            self.conn.commit()
            cur.close()
            self.conn.close()
        if flag:
            return rows

    def get_category(self, name):
        query = """
             SELECT * FROM categories WHERE name = '%s';
        """
        query = query % name
        return self._run_query(query, True)

    def create_category(self, name):
        query = """
             INSERT INTO  categories (name) VALUES ('%s');
        """
        query = query % name
        return self._run_query(query)

    def add_item(self, data, categories):
        query = """
            INSERT INTO items (shop_id, name, description, price, image_url) VALUES
            (%s) RETURNING id;
        """
        params = ", ".join([(" '%s' ") % x for x in data])
        query = query % params
        id_item = self._run_query(query, True)[0][0]
        for category in categories:
            if not id_item:
                continue
            id_cat = self.get_category(category)[0][0]
            query = """
                INSERT INTO items_to_categories (category_id, item_id) VALUES
                (%s, %s);
            """
            query = query % (id_cat, id_item)
            self._run_query(query)

    def get_item(self, name, shop_id):
        query = """
             SELECT * FROM items WHERE name = '%s' and shop_id = %s ;
        """
        query = query % (name, shop_id)
        return self._run_query(query, True)

    def get_items_by_shop_id(self, shop_id):
        query = """
             SELECT * FROM items WHERE shop_id = %s ;
        """
        query = query % (shop_id)
        return self._run_query(query, True)

    def add_shop(self, name, lat, lon, descr, image_url):
        query = """
             INSERT INTO  shops (name, latitude, longitude, position, description,
             image_url) VALUES ('%s', %s, %s, point(%s, %s), '%s', '%s');
        """
        name = self.fix_string(name)
        query = query % (name, lat, lon, lat, lon, descr, image_url)
        return self._run_query(query)

    def get_shop_by_name(self, name, lat, lon):
        query = """
             SELECT * FROM shops WHERE name = '%s' and latitude <= %s and latitude >= %s
             and longitude <= %s and longitude >= %s;
        """
        eps = 0.000005
        lat_d = float(lat) - eps
        lat_u = float(lat) + eps
        lon_d = float(lon) - eps
        lon_u = float(lon) + eps
        query = query % (name, lat_u, lat_d, lon_u, lon_d)
        return self._run_query(query, True)

    def search_items_by_name(self, name):
        query = """
            SELECT * FROM items WHERE name %% '%s';
        """
        query = query % name
        return self._run_query(query, True)

    def search_shops_by_name(self, name):
        query = """
            SELECT * FROM shops WHERE name %% '%s';
        """
        query = query % name
        return self._run_query(query, True)

    def search_offers_by_name(self, name):
        query = """
            SELECT * FROM offers WHERE name %% '%s';
        """
        query = query % name
        return self._run_query(query, True)

    def search_items_by_description(self, descr_query):
        query = """
            SELECT * FROM items WHERE description @@ '%s';
        """
        query = query % descr_query
        return self._run_query(query, True)

    def search_shops_by_description(self, descr_query):
        query = """
            SELECT * FROM shops WHERE description @@ '%s';
        """
        query = query % descr_query
        return self._run_query(query, True)

    def search_offers_by_description(self, descr_query):
        query = """
            SELECT * FROM offers WHERE description @@ '%s';
        """
        query = query % descr_query
        return self._run_query(query, True)

    # items = [item_name, item_name]
    def add_offer(self, data, items, shop_id):
        query = """
            INSERT INTO offers (shop_id, name, description, percent, end_time) VALUES
            (%s) RETURNING id;
        """
        params = ", ".join([(" '%s' ") % x for x in data])
        query = query % params
        id_offer = self._run_query(query, True)[0][0]

        for item in items:
            if not id_offer:
                continue
            id_item = self.get_item(item, shop_id)[0][0]
            query = """
                INSERT INTO offers_to_items (offer_id, item_id) VALUES
                (%s, %s);
            """
            query = query % (id_offer, id_item)
            self._run_query(query)

    def add_offer_to_store(self, data, items_ids, shop_id):
        query = """
            INSERT INTO offers (shop_id, name, description, percent, end_time) VALUES
            (%s) RETURNING id;
        """
        params = ", ".join([(" '%s' ") % x for x in data])
        query = query % params
        id_offer = self._run_query(query, True)[0][0]

        for item in items_ids:
            if not id_offer:
                continue
            query = """
                INSERT INTO offers_to_items (offer_id, item_id) VALUES
                (%s, %s);
            """
            query = query % (id_offer, item)
            self._run_query(query)

    def get_shop_id_and_name_by_login(self, login):
        query = """
             SELECT shops.id, shops.name FROM shops INNER JOIN merchants ON
             shops.id = merchants.shop_id WHERE merchants.login = '%s';
        """
        query = query % login
        return self._run_query(query, True)

    def get_shops_around(self, latitude, longitude, distance, limit):
        query = """
            SELECT *
            FROM
                (SELECT id, name, latitude, longitude, description, image_url,
                        round((position <@> point(%s, %s))::numeric, 3) as distance
                    FROM shops
                        ORDER BY position <-> point(%s, %s)
            ) tmp
            WHERE tmp.distance < %s
            LIMIT %s;
        """
        query = query % (latitude, longitude, latitude, longitude, distance, limit)
        return self._run_query(query, True)

    def search_item_in_zone(self, text, lat, lon, dist):
        query = """
            SELECT * FROM
                (SELECT items.id, items.name, shops.name, shops.latitude, shops.longitude,
                    items.description, items.price, items.image_url,
                    round((position <@> point(%s, %s))::numeric, 3) as distance
                FROM shops INNER JOIN items on shops.id=items.shop_id
                WHERE items.name %% '%s' or items.description @@ '%s'
                       or shops.name %% '%s' or shops.description @@ '%s'
                ORDER BY position <-> point(%s, %s)
                ) tmp
            WHERE tmp.distance < %s;
        """
        query = query % (lat, lon, text, text, text, text, lat, lon, dist)
        return self._run_query(query, True)

    def get_items_by_category(self, category_name):
        query = """
            SELECT items_to_categories.item_id FROM items_to_categories INNER JOIN categories ON
                items_to_categories.category_id = categories.id WHERE categories.name = '%s';
        """
        query = query % category_name
        return self._run_query(query, True)

    def get_item_by_id(self, it_id):
        query = """
            SELECT items.id, items.name, shops.name, shops.latitude, shops.longitude,
                    items.description, items.price, items.image_url
            FROM items INNER JOIN shops on shops.id = items.shop_id WHERE items.id = %s;
        """
        query = query % it_id
        return self._run_query(query, True)

if __name__ == "__main__":
    db = DBUtil()
    print db.get_shops_around(53.091347, 8.780611, 3, 10)
    print db.search_item_in_zone('osteria', 53.07544, 8.80382, 3)
    print db.get_shop_by_name('WESER Schiff der Jugendherberge Bremen', 53.0795953, 8.7968021)
    print db.get_items_by_shop_id(613)
    print db.get_items_by_category('gift')
    print db.get_item_by_id(49)
