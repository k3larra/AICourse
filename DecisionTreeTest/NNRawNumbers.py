from sklearn.neural_network import MLPClassifier
import matplotlib.pyplot as plt
import numpy as np
import pandas as pd
#Using Multilayer Perceptron NN Cooleas and best
traningData= np.array(pd.read_csv('rawTrainingNumbers.txt', header = None))
classTrainingDataY = traningData[:,0]
classTrainingDataY = np.transpose(classTrainingDataY)
trainingTuplesX = traningData[:, 1:]
testData = np.array(pd.read_csv('rawTestNumbers.txt', header = None))
classTestData = testData[:,0]
testTuples = testData[:,1:]

n_neighbors = 50
accuracyResult = np.empty(shape=[0, 2])

for x in range(20000,trainingTuplesX.shape[0],1000):
    idx = np.random.randint(trainingTuplesX.shape[0], size=x)
    clf = MLPClassifier(solver='adam', activation = "relu", alpha=1e-5, hidden_layer_sizes=(1293,1293), random_state=1)
    clf.fit(trainingTuplesX[idx,:], classTrainingDataY[idx])
    y_pred = clf.predict(testTuples)
    print(clf.get_params(deep=True))
    correctlabeld = testData.shape[0]-(classTestData != y_pred).sum()
    accuracyResult = np.append(accuracyResult, [[x, correctlabeld/ testData.shape[0]]], axis=0)
    print("accuracyResult: ")
    print(accuracyResult)

print(y_pred)
plt.plot(accuracyResult[:,0],accuracyResult[:,1])
plt.xlabel('Training set size')
plt.ylabel('Accuracy')
plt.show()