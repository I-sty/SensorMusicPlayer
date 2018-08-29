import pymongo
from pymongo import MongoClient
client = MongoClient('localhost', 27017)
print('Databases: ', client.database_names())
db = client['Tododb']
print('Collections: ', db.collection_names())
print('\n')
coll = db['buffers']

for col in coll.find({}):
  for keys in col.keys(): 
    if keys == "value":
      print ('{', keys, ":" , col[keys] , '}' )
    print('\n')

