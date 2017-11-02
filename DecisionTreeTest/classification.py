import matplotlib.pyplot as plt
import numpy as np
import pandas as pd
from sklearn import linear_model
from sklearn.linear_model import LogisticRegression
import graphviz
# X = [[0, 0], [1, 1]]
# Y = [0, 1]
# clf = tree.DecisionTreeClassifier()
# clf = clf.fit(X, Y)
#
# result = clf.predict([[2., 2.]])
# print(result[0])
#sklearn.preprocessing.
traningData= np.array(pd.read_csv('trainingData.txt', header = None))
classTrainingData = traningData[:,0]
classTrainingData = np.transpose(classTrainingData)
trainingTuples = traningData[:, 1:]
testData = np.array(pd.read_csv('testData.txt', header = None))
classTestData = testData[:,0]
testTuples = testData[:,1:]
#accuracyResult = np.array[[]]
#i=0

accuracyResult = np.empty(shape=[0, 3])

clf = LogisticRegression(solver='sag', max_iter=100, random_state=42,
                         multi_class="ovr").fit(trainingTuples, classTrainingData)
print("Accuracy score : %.3f (%s)" % (clf.score(testTuples, classTestData), "ovr"))
print("Score on training data : %.3f (%s)" % (clf.score(trainingTuples, classTrainingData), "ovr"))
#for x in range(10,trainingTuples.shape[0],100):
# for x in range(10, 2000, 100):
#     idx = np.random.randint(trainingTuples.shape[0],size=x)
#     clf = linear_model.l
#     clf = clf.fit(trainingTuples[idx,:], classTrainingData[idx])
#     correct = 0;
#     for row in testData:
#       result = clf.predict([row[1:]])
#       if result[0]==row[0]:
#           correct=correct+1
#     accuracyResult=np.append(accuracyResult,[[x,correct/testData.shape[0],clf.tree_.node_count]],axis=0)
#
# dot_data = tree.export_graphviz(clf,out_file=None)
# graph = graphviz.Source(dot_data)
# graph.render("iris")
# plt.plot(accuracyResult[:,0],accuracyResult[:,1])
# plt.xlabel('Training set size')
# plt.ylabel('Accuracy')
# plt.show()
#
# plt.plot(accuracyResult[:,0],accuracyResult[:,2])
# plt.xlabel('Training set size')
# plt.ylabel('Number nodes')
# plt.show()
#
# result = clf.predict([trainingTuples[600,:]])
# print(result[0])
# print(classTrainingData[600])
# result = clf.predict([trainingTuples[1,:]])
# print(result[0])
# print(classTrainingData[1])
#
#Evaluate




