import matplotlib.pyplot as plt
import os
import shutil
from pymongo import MongoClient

client = MongoClient('localhost', 27017)
print('Databases: ', client.database_names())
db = client['Tododb']
print('Collections: ', db.collection_names())
coll = db['buffers']

dir = 'asimage'
if not os.path.exists(dir):
    os.makedirs(dir)
else:
    shutil.rmtree(dir)
    os.makedirs(dir)
os.chdir(dir)

for col in coll.find({}):
    print("\n", col)
    plt.plot(col["x"])
    plt.plot(col["y"])
    plt.plot(col["z"])
    plt.ylabel('accelerometer value')
    plt.xlabel('sample numbers')
    plt.savefig(str(col["_id"]))
    plt.gcf().clear()
