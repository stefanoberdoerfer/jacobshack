from flask import Flask, render_template, json, request
#from flask.ext.mysql import MySQL
from constants import _Const
import MySQLdb as db
import sys
import zlib
import time

const = _Const()

class Upload(object):
	@staticmethod
	def allowed_file(self, filename):
		return '.' in filename and \
       		filename.rsplit('.')[-1] in conf.ALLOWED_EXTENSIONS

    @staticmethod
    def upload(self):
    	return 