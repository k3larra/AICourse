from sklearn import neighbors, datasets
import matplotlib.pyplot as plt
import numpy as np
import pandas as pd

traningData= np.array(pd.read_csv('trainingData.txt', header = None))
classTrainingDataY = traningData[:,0]
classTrainingDataY = np.transpose(classTrainingDataY)
trainingTuplesX = traningData[:, 1:]
testData = np.array(pd.read_csv('testData.txt', header = None))
classTestData = testData[:,0]
testTuples = testData[:,1:]

n_neighbors = 50
accuracyResult = np.empty(shape=[0, 2])

#for x in range(100,trainingTuplesX.shape[0],100):
for x in range(100, 10000, 100):
    idx = np.random.randint(trainingTuplesX.shape[0], size=x)
    clf = neighbors.KNeighborsClassifier(n_neighbors, weights = 'distance')
    clf.fit(trainingTuplesX[idx,:], classTrainingDataY[idx])
    y_pred = clf.predict(testTuples)
    correctlabeld = testData.shape[0]-(classTestData != y_pred).sum()
    accuracyResult = np.append(accuracyResult, [[x, correctlabeld/ testData.shape[0]]], axis=0)

print(y_pred)
plt.plot(accuracyResult[:,0],accuracyResult[:,1])
plt.xlabel('Training set size')
plt.ylabel('Accuracy')
plt.show()