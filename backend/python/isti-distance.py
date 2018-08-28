#from dtaidistance import dtw
#from dtaidistance import dtw_visualisation as dtwvis
import array
s1 = array.array('d', [0, 0, 1, 2, 1, 0, 1, 0, 0])
s2 = array.array('d', [0, 1, 2, 0, 0, 0, 0, 0, 0])
#d, paths= dt.warping_paths(s1, s2, window=25, psi=2)
#best_path = dtw.best_path(paths)
#dtwvis.plot_warpingpaths(s1, s2, paths, best_path)

from dtaidistance import dtw
from dtaidistance import dtw_visualisation as dtwvis
#from matplotlib import pyplot as plt
#import numpy as np
#x = np.arange(0, 20, .5)
#s1 = np.sin(x)
#s2 = np.sin(x - 1)
d, paths = dtw.warping_paths(s1, s2, window=25, psi=2)
best_path = dtw.best_path(paths)
print(d)
#plt.figure()
dtwvis.plot_warpingpaths(s1, s2, paths, best_path, "image.png")