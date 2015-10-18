import sys
from random import randint
sys.path.insert(0,"/var/www/shopsnoffs/shopsnoffs/")
from  dbutil import DBUtil

import psycopg2

items = []
basepirce = 259
itemname = "Lloyd Hedin Plain Toe Shoes Black"
description = "Beautiful plain toe shoes featuring brogue detailing, rubber sole. Made in Germany."
image_url = "http://www.mensdesignershoe.com/avactis-images/lloyd-hedin-plain-toe-shoes-black.png"
items.append([itemname, basepirce, description, image_url])

basepirce = 267
itemname = "Spider Suede Boots Black"
description = "Beautiful suede chukka boots with contrast color rubber soles. Made in Germany. Runs large, order 1/2 size down."
image_url = "http://www.mensdesignershoe.com/avactis-images/lloyd-spider-suede-boots-black.png"
items.append([itemname, basepirce, description, image_url])

basepirce = 280
itemname = "Varello Split Toe Gore-Tex Boots TD Moro"
description = "Split toe boots featuring Gore-Tex, and shearling fur lining. Rubber super grip sole. International shipping available only to limited countries, contact us prior to shipping to see if we can ship to your country. "
image_url = "http://www.mensdesignershoe.com/avactis-images/lloyd-mens-varello-split-toe-boots-td-moro.png"
items.append([itemname, basepirce, description, image_url])

basepirce = 100
itemname = "Saphir Antiqued Lace Up Boots Saddle"
description = "Fine antiqued lace up boots, featuring three eyelet design, and rubber sole. Made in Germany"
image_url = "http://www.mensdesignershoe.com/avactis-images/lloyd-saphir-antiqued-lace-up-boots-saddle_0.png"
items.append([itemname, basepirce, description, image_url])

basepirce = 280
itemname = "Valdez Pebble Grain Split Toe Gore-Tex Shoes Amber"
description = "Beautiful grained leather split toe shoes featuring Gore-Tex. Rubber super grip sole. International shipping available only to limited countries, contact us prior to shipping to see if we can ship to your country."
image_url = "http://www.mensdesignershoe.com/avactis-images/lloyd-mens-valdez-pebble-grain-gore-shoes-amber.png"
items.append([itemname, basepirce, description, image_url])

basepirce = 279
itemname = "Pebble Grain Split Toe Gore-Tex Shoes Amber"
description = "Beautiful grained leather split toe shoes featuring Gore-Tex. Rubber super grip sole. International shipping available only to limited countries, contact us prior to shipping to see if we can ship to your country."
image_url = "http://www.mensdesignershoe.com/avactis-images/lloyd-mens-valdez-pebble-grain-gore-shoes-amber.png"
items.append([itemname, basepirce, description, image_url])

basepirce = 240
itemname = "Hel Suede Lace Up Shoes Ocean"
description = "Beautiful soft suede lace up shoes, featuring fine stitch detailing, and rubber sole. Made in Germany."
image_url = "http://www.mensdesignershoe.com/avactis-images/lloyd-hel-suede-lace-up-shoes-ocean_0.png"
items.append([itemname, basepirce, description, image_url])

select_category  = "shoes"
_DBUtil = DBUtil()
shopids = []
for line in open('/home/ubuntu/dbdata/bremenshops.csv','r').readlines():
        parts = line.split(',')
        shopname = parts[0]
        lat = parts[1]
        longi = parts[2]
        image_url = parts[3]
        category = parts[4].strip().lower().replace("&", " ")
        #_DBUtil.add_shop(shopname, lat, longi, "", image_url)
        #print("category:" + category)
        categories = category.split(" ");
        for category in categories:
        	if len(category) > 0:
       			if category == select_category:
       				#print(_DBUtil.get_shop_by_name(shopname, float(lat), float(longi))[0][0])
       				shopids.append(_DBUtil.get_shop_by_name(shopname, (lat), (longi))[0][0])
shopids = set(shopids)
category = select_category
for shop_id in shopids:
	for item in items:
		price = item[1] + float(randint(10,80))/100
		print(str([shop_id, item[0], item[2], price, item[3]]))
		_DBUtil.add_item([shop_id, item[0], item[2], price, item[3]], [category,  "shoes", "gift"])

