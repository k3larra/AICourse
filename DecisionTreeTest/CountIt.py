import os
import matplotlib.pyplot as plt
import numpy as np
import pandas as pd
##Some code for counting gain
infoD = -2/6*np.log2(2/6)-2/6*np.log2(2/6)-1/6*np.log2(1/6)-1/6*np.log2(1/6)
print(infoD)
data = pd.read_csv('katjong.txt', header = None)
trainingTuples = data[0]
print(trainingTuples)
accuracy = data[1]
print(accuracy)
numberNodes = data[2]
print(numberNodes)
depth = data[3]
print(depth)
plt.plot(trainingTuples[0:100],accuracy[0:100])
plt.xlabel('Training set size')
plt.ylabel('Accuracy')
plt.show()
plt.plot(trainingTuples,numberNodes)
plt.plot(trainingTuples,depth)
plt.xlabel('Training set size')
plt.ylabel('NumberNodes')
plt.show()