import pymongo
from pymongo import MongoClient
client = MongoClient('localhost', 27017)
client.database_names()
db = client['<db_name>']
db.collection_names()
coll = db['<collection_name>']

for col in coll.find({}):
    for keys in col.keys(): 
        print ('{', keys, ":" , col[keys] , '}' )

