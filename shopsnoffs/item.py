from flask import Flask, render_template, json, request
#from flask.ext.mysql import MySQL
from constants import _Const
import MySQLdb as db
import sys
import zlib
import time

class Item(object):
    def __init__(self):
        CONST = _Const()
        self.HOST = CONST.MYSQL_HOST_IP
        self.ITEMDB = 'itemdb'
        self.PORT = 3306
        self.USER = CONST.MYSQL_USER
        self.PASSWORD = CONST.MYSQL_PASS