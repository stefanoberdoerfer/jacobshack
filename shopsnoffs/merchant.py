from flask import Flask, render_template, json, request
#from flask.ext.mysql import MySQL
from constants import _Const
import MySQLdb as db
import sys
import zlib
import time

class Merchant(object):
    def __init__(self):
        self.id = ""
		self.name = ""
		self.surname = ""
		self.login = ""
		self.password = ""
		self.email = ""
		self.shop_id = -1