from random import randint
from flask import Flask,  session, render_template, json, request
import sys
import re
from offer import Offer
from util import Util
from constants import _Const
from dbutil import DBUtil

const = _Const()
_DBUtil = DBUtil()

app = Flask(__name__)
app.config['UPLOAD_FOLDER'] = const.UPLOAD_FOLDER

@app.route('/')
def homepage():
	return render_template("index.html")

@app.route('/submitoffer')
def submitoffer():
    return render_template("submitoffer.html")

@app.route('/login', methods=['GET', 'POST'])
def login():
    if request.method == 'POST':
        session['username'] = request.form['username']
        return redirect(url_for('index'))
    return '''
        <form action="" method="post">
            <p><input type=text name=username>
            <p><input type=submit value=Login>
        </form>
    '''

@app.route('/logout')
def logout():
    # remove the username from the session if it's there
    session.pop('username', None)
    return redirect(url_for('index'))

@app.route('/getoffers', methods=['GET', 'POST'])
def getoffers():
    s = ''
    offers = []
    offers.append({"name":"iPhone6", "url":"http://images.apple.com/v/iphone/home/r/home/images/accessories_for_iphone_light_small.jpg", \
    		'location':[53.09289, 8.64706], 'store_name':"Apple", 'discount':35, \
    		'orig_price':600, 'exp_time': 1445175979})
    offers.append({"name":"Pizza", "url":"https://upload.wikimedia.org/wikipedia/en/thumb/d/d2/Pizza_Hut_logo.svg/1088px-Pizza_Hut_logo.svg.png", \
    		'location':[53.08271, 8.78706], 'store_name':"Pizza Hut", 'discount':30, \
    		'orig_price':30, 'exp_time': 1445175902})
    offers.append({"name":"Coffee", "url":"http://www.printawallpaper.com/upload/designs/hot_coffee_cup_image1.jpg", \
    		'location':[53.0787, 8.79008], 'store_name':"Cafe", 'discount':30, \
    		'orig_price':8, 'exp_time': 1445175390})
    offers.append({"name":"Beck Beers", "url":"http://www.hot-dog.org/sites/all/themes/hotdogs/images/beer-sausage.jpg", \
    		'location':[53.0628187, 8.787674], 'store_name':"Becks", 'discount':20, \
    		'orig_price':12, 'exp_time': 1445175239})
    offers.append({"name":"Lacoste", "url":"https://fortunedotcom.files.wordpress.com/2011/10/lacoste_crocodile.jpg", \
            'location':[53.0822704, 8.7870184], 'store_name':"Lacoste", 'discount':30, \
            'orig_price':120, 'exp_time': 1445175390})
    return json.dumps(offers)


@app.route('/getoffersbydistance', methods=['GET', 'POST'])
def getoffersbydistance():
    lati = 0.0
    longi = 0.0
    dist = 0.0
    response = []
    if request.method == "POST" or request.method == "GET" :
        lati  = request.args.get('lati')
        longi = request.args.get('longi')
        dist  = request.args.get('dist')
        shops = _DBUtil.get_shops_around(lati, longi, dist, 100) 
        if  shops:
            print >>sys.stderr, str('shops:') + str(shops)
            for shop in shops:
                response.append({"store_name":shop[1], 'location':[shop[2], shop[3]], "url":shop[5]})
    return json.dumps(response)


@app.route('/getoffersbyname', methods=['GET', 'POST'])
def getoffersbyname():
    lati = 0.0
    longi = 0.0
    dist = 0.0
    response = []
    if request.method == "POST" or request.method == "GET" :
        query  = request.args.get('query')
        lati  = request.args.get('lati')
        longi = request.args.get('longi')
        dist  = request.args.get('dist')
        shops = _DBUtil.search_item_in_zone(query, lati, longi, dist) 
        if  shops:
            print >>sys.stderr, str('shops:') + str(shops)
            for shop in shops:
                response.append({"item_name":shop[1], "store_name":shop[2], \
                    'location':[shop[3], shop[4]], "url":shop[7], "discount":randint(0,40)})
    return json.dumps(response)

@app.route('/getoffersbycategory', methods=['GET', 'POST'])
def getoffersbycategory():
    lati = 0.0
    longi = 0.0
    dist = 0.0
    response = []
    if request.method == "POST" or request.method == "GET" :
        category  = request.args.get('query')
        lati  = request.args.get('lati')
        longi = request.args.get('longi')
        dist  = request.args.get('dist')
        itemids = _DBUtil.get_items_by_category(category)
        doneshops = []
        for itemid in itemids:
            itemdata = _DBUtil.get_item_by_id(itemid[0])[0]
            if itemdata and itemdata[2] not in doneshops:
                if(Util.distance(float(lati), float(longi), float(itemdata[3]), float(itemdata[4])) <= float(dist)):
                    doneshops.append(itemdata[2])
                    response.append({"item_name":itemdata[1], "store_name":itemdata[2], \
                        'location':[itemdata[3], itemdata[4]], "url":itemdata[7], "discount":randint(0,40)})
    return json.dumps(response)


@app.route('/upload_file', methods=['GET', 'POST'])
def upload_file():
    if request.method == "POST" or request.method == "GET" :
        file = request.files['file']
        if not file :
            return json.dumps(["Fail: NO file"])
        merch_id = Util.getVal(request.json, 'merch_id')
        shop_id = Util.getVal(request.json, 'shop_id')
        name = Util.getVal(request.json, 'name')
        description = Util.getVal(request.json, 'description') 
        percent = Util.getVal(request.json, 'percent')
        end_time = Util.getVal(request.json, 'end_time')
        item_id = Util.getVal(request.json, 'item_id')
        if file and Util.allowedFileName(file.filename):
            filename = secure_filename(file.filename)
            path = os.path.join(app.config['UPLOAD_FOLDER'], merch_id, shop_id, filename)
            print >>sys.stderr, str('Uploading file to path:') + path
            file.save(path)
            '''
            offer = Offer(item_id, shop_id, name, description, percent, end_time)
            Util.saveOffer(offer)
            '''
            return json.dumps(["Success"]) 
    return json.dumps(["Fail"])

if __name__ == "__main__":
    app.run()

