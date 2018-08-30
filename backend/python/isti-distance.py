from dtaidistance import dtw
from dtaidistance import dtw_visualisation as dtwvis
import numpy as np
import sys

'''
Create an array.

Parameters:	

object : An array, any object exposing the array interface, an object whose __array__ method returns an array, or any (nested) sequence.

Returns:
An array object satisfying the specified requirements.
'''
s1 = np.array([0, 1, 2, 1, 0, 2, 1, 0, 0])
s2 = np.fromstring(sys.argv[1], dtype=float, sep=',')

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
d, matrix = dtw.warping_paths(s1, s2, window=25, psi=2)
#print('DTW distance = ', d)
print(d)
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
