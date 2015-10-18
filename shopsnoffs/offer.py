from flask import Flask, render_template, json, request
#from flask.ext.mysql import MySQL
from constants import _Const
import MySQLdb as db
import sys
import zlib
import time

class Offer(object):
    def __init__(self, item_id, shop_id, name, description, percent, end_time):
		self.id 			= -1
		self.shop_id 		= shop_id
		self.item_id 		= item_id
		self.name 			= name
		self.description 	= description
		self.percent		= percent
		self.end_time		= end_time