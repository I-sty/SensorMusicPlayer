import numpy as np
import sys
from dtaidistance import dtw
from pymongo import MongoClient

'''
matrix - The matrix with the items
index - Which column need to check
rows - The number of rows
'''


def calcMinimum(matrix, index, rows):
    # print('\ncalcMinimum: ', matrix , ' ', index, ' ', rows)
    minimum = 8
    minIndex = -1
    for x in range(0, rows):
        if matrix[x][index] < minimum:
            minimum = matrix[x][index]
            minIndex = x
    if (minIndex != -1):
        matrix[minIndex][1] += 1


def getShapeId(matrix, rows):
    # print('\ngetShapeId: ', matrix , ' ', rows)
    minimum = 1000
    minIndex = -1
    for x in range(0, rows):
        if matrix[x][1] < minimum:
            minimum = matrix[x][1]
            minIndex = x
    # print('minIndex')
    # print(minIndex)
    if (minIndex > -1 and minimum > 0):
        return matrix[minIndex][0]
    else:
        return '0'


client = MongoClient('localhost', 27017)
# print('Databases: ', client.database_names())
db = client['Tododb']
# print('Collections: ', db.collection_names())
coll = db['shapes']
colls = coll.find({})
size = colls.count()

# Create a matrix to store results
n = 6
# shape_type | winner |  x  |  y  |  z  | sum
#  circle (1)|    1   |     |     | min | 
#  circle (1)|    2   |     | min |     | min
#  circle (1)|    1   | min |     |     | 
#  square (2)|    0   |     |     |     | 
#  square (2)|    0   |     |     |     | 
#  square (2)|    0   |     |     |     | 

matrix = [0] * size
for i in range(size):
    matrix[i] = [0] * n

k = 0

for col in coll.find({}):
    if (col["shape"] == "circle"):
        matrix[k][0] = 1
    elif (col["shape"] == "square"):
        matrix[k][0] = 2
    k += 1

# print('\n\nempty matrix: ', matrix)

k = 0

'''
Create an array.

Parameters:	

object : An array, any object exposing the array interface, an object whose __array__ method returns an array, or any (nested) sequence.

Returns:
An array object satisfying the specified requirements.
'''
x2 = np.fromstring(sys.argv[1], dtype=float, sep=',')
y2 = np.fromstring(sys.argv[2], dtype=float, sep=',')
z2 = np.fromstring(sys.argv[3], dtype=float, sep=',')

for col in coll.find({}):
    x1 = np.array(col["x"])
    y1 = np.array(col["y"])
    z1 = np.array(col["z"])
    dx, matrixX = dtw.warping_paths(x1, x2, window=25, psi=2)
    dy, matrixY = dtw.warping_paths(y1, y2, window=25, psi=2)
    dz, matrixZ = dtw.warping_paths(z1, z2, window=25, psi=2)
    matrix[k][2] = dx
    matrix[k][3] = dy
    matrix[k][4] = dz
    matrix[k][5] = dx + dy + dz
    k += 1

for i in range(2, 6):
    calcMinimum(matrix, i, size)

# print('\n\ncalculated matrix: \n', matrix)
print(getShapeId(matrix, size))

'''
Dynamic Time Warping.

The full matrix of all warping paths is build.

Parameters:	

s1 – First sequence
s2 – Second sequence
window – Only allow for maximal shifts from the two diagonals smaller than this number. It includes the diagonal, meaning that an Euclidean distance is obtained by setting weight=1.
max_dist – Stop if the returned values will be larger than this value
max_step – Do not allow steps larger than this value
max_length_diff – Return infinity if length of two series is larger
penalty – Penalty to add if compression or expansion is applied
psi – Psi relaxation parameter (ignore start and end of matching). Useful for cyclical series.

Returns:	
(DTW distance, DTW matrix)
'''
# dx, matrixX = dtw.warping_paths(x1, x2, window=25, psi=2)
# dy, matrixY = dtw.warping_paths(y1, y2, window=25, psi=2)
#dz, matrixZ = dtw.warping_paths(z1, z2, window=25, psi=2)
#print('DTW distance = ', d)
# print(dx)
# print(dy)
#print(dz)
#print('DTW matrix = ', matrix)

'''
Compute the optimal path from the n*m warping paths matrix.
'''
#best_path = dtw.best_path(matrix)

'''
Plot the warping paths matrix.

Parameters:	
s1 – Series 1
s2 – Series 2
paths – Warping paths matrix
path – Path to draw (typically this is the best path)
filename – Filename for the image (optional)
shownumbers – Show distances also as numbers
'''
#dtwvis.plot_warpingpaths(s1, s2, matrix, best_path, "image.png")
