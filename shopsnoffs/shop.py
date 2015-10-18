from flask import Flask, render_template, json, request
#from flask.ext.mysql import MySQL
from constants import _Const
import MySQLdb as db
import sys
import zlib
import time

class Shop(object):
    def __init__(self):
        self.id		= -1
		self.name	= ""
		self.position = []
		self.desctiption = ""