def constant(f):
    def fset(self, value):
        raise TypeError
    def fget(self):
        return f()
    return property(fget, fset)

class _Const(object):
    @constant
    def MYSQL_PASS():
        return "mysqlcriticsangle$8789iivii"
    @constant
    def MYSQL_USER():
        return "ca"
    @constant
    def MYSQL_HOST_IP():
        return "52.24.253.31"
    @constant
    def test():
        return "52.24.253.31"
    @constant
    def DB_CONFIG():
        return "host=52.89.215.121 port=5432 dbname=jacobshack_db user=jacobshack\
                password=jacobshack"
    @constant
    def UPLOAD_FOLDER():
        return './static/images/items'

    @constant
    def ALLOWED_IMG_EXTENSIONS():
        return set(['png', 'jpg', 'jpeg'])
