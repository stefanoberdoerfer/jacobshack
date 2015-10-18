from flask import Flask, render_template, json, request
#from flask.ext.mysql import MySQL
from constants import _Const

import sys
import zlib
import time
from offer import Offer

Const = _Const()

class Util(object):
    @staticmethod
    def doesUserExist( username, password):
        return True

    @staticmethod
    def getVal( dict, key):
        if key in dic:
            return dict[key]
        return None

    @staticmethod
    def registerMerchant( name):
        return True

    @staticmethod
    def registerShop( shopname):
        return True

    @staticmethod
    def doesItemExist( item_name, shop_id):
        return False

    @staticmethod
    def allowedFileName(file_name):
         return '.' in filename and \
           filename.rsplit('.', 1)[1] in Const.ALLOWED_EXTENSIONS

    @staticmethod
    def saveOffer( offer):

        return True

    @staticmethod
    def distance( x1, y1, x2, y2):
        return ((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2))

    @staticmethod
    def getClosestOffers( lati, longi):
        offers = []
        for i in range( 10):
            offers.append( Offer( ))
        return offers


