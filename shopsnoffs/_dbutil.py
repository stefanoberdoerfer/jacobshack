import sys
import time

try:
    import psycopg2
except ImportError, e:
    print "Error loading psycopg2 module: %s" % e
    sys.exit(5)
from psycopg2.extras import DictCursor
from psycopg2.pool import ThreadedConnectionPool


class Database:
    def __init__(self, connect_param):
        self.__connect_param = connect_param
        self.__pool = ThreadedConnectionPool(0, 10, self.__connect_param)
        # get cursor and test it
        # cur = self.cursor()
        # cur.execute('SHOW transaction_read_only')
        # standby = cur.fetchone()
        # cur.close()

    def get_connection(self):
        return self.__pool.getconn()

    def put_connection(self, connection):
        self.__pool.putconn(connection)


class Connection:
    # reconnections limit
    reconnects = 5

    def __init__(self):
        db_config = "host=52.89.215.121 port=5432 dbname=jacobshack_db user=jacobshack\
        password=jacobshack"
        self.__db = Database(db_config)

    def cursor(self):
        # try to check if we have connection already
        # if not self.isConnected():
        self.connect()
        # get cursor and check if we haven't lost connection
        for i in xrange(self.reconnects):
            try:
                # if it is not first loop - try to connect
                if i:
                    self.connect(self.__connect_param)
                # get cursor and test it
                cur = self._cursor()
                cur.execute('SELECT 1')
                # return cursor as soon as it is ok
                return cur
            except Exception, e:
                time.sleep(2)
        raise e

    def _cursor(self):
        return self.__connection.cursor(cursor_factory=psycopg2.extras.DictCursor)

    def is_connected(self):
        if not self.__connection:
            return False
        return True

    def connect(self):
        self.__connection = self.__db.get_connection()
        self.__closed = False

    def commit(self):
        self.__connection.commit()

    def rollback(self):
        self.__connection.rollback()

    def close(self):
        if self.__connection:
            self.__connection.close()
        self.__db.put_connection(self.__connection)
        self.__connection = None
        self.__closed = True
