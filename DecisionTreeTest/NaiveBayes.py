from sklearn import datasets
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

from sklearn.naive_bayes import GaussianNB
gnb = GaussianNB()
accuracyResult = np.empty(shape=[0, 2])

for x in range(10,trainingTuplesX.shape[0],100):
    idx = np.random.randint(trainingTuplesX.shape[0], size=x)
    gnb = GaussianNB()
    gnb.fit(trainingTuplesX[idx,:], classTrainingDataY[idx])
    y_pred = gnb.predict(testTuples)
    correctlabeld = testData.shape[0]-(classTestData != y_pred).sum()
    accuracyResult = np.append(accuracyResult, [[x, correctlabeld/ testData.shape[0]]], axis=0)

plt.plot(accuracyResult[:,0],accuracyResult[:,1])
plt.xlabel('Training set size')
plt.ylabel('Accuracy')
plt.show()
