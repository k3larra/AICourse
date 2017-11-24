import numpy as np
import pandas as pd
traningData= np.array(pd.read_csv('trainingData.txt', header = None))
classTrainingData = traningData[:,0]
classTrainingData = np.transpose(classTrainingData)
trainingTuples = traningData[:, 1:]
testData = np.array(pd.read_csv('testData.txt', header = None))
classTestData = testData[:,0]
testTuples = testData[:,1:]
a ="ture"
correct = 0;
for i in range(0,trainingTuples.shape[0]):
    if not(1 <= trainingTuples[i][4] <= 3) and not(1 <= trainingTuples[i][5] <= 3) and not (1 <= trainingTuples[i][6] <= 3) and not (1 <= trainingTuples[i][7] <= 3):  ##All goast far away
        if(classTrainingData[i]==2):
            correct = correct + 1
        else:
            print("All ghosts far away (2): %i" % (classTrainingData[i]))  # //0,4 or 5 on all distances gives 2
            print(trainingTuples[i])
    elif (trainingTuples[i][0]==1) and (trainingTuples[i][1]==1)and (trainingTuples[i][2]==1)and(trainingTuples[i][3]==1): ##All goast editable
        if (classTrainingData[i] == 1):
            correct = correct + 1
        else:
            print(trainingTuples[i])
            print("All ghosts edible (1): %i" % (classTrainingData[i]))  # // One on all editable gives 1 attack
    elif (1<=trainingTuples[i][4]<=2) or (1<=trainingTuples[i][5]<=2) or (1<=trainingTuples[i][6]<=2) or (1<=trainingTuples[i][7]<=2): ##Any ghost close
        if (classTrainingData[i] == 4):
            correct = correct + 1
        else:
            print("Any ghost close (4): %i" % (classTrainingData[i]))  # // 1 or 2 on distances gives 3
            print(trainingTuples[i])
    else:
        if (classTrainingData[i] == 5):
            correct = correct + 1
        else:
            print("All other (5): %i" % (classTrainingData[i]))  # // 1 or 2 on distances gives 3
            print(trainingTuples[i])

print("Possible Accuracy trainingData: %.3f" % (correct/trainingTuples.shape[0]))
correct = 0
for i in range(0,testTuples.shape[0]):
    if not(1 <= testTuples[i][4] <= 3) and not(1 <= testTuples[i][5] <= 3) and not (1 <= testTuples[i][6] <= 3) and not (1 <= testTuples[i][7] <= 3):  ##All goast far away
        if(classTestData[i]==2):
            correct = correct + 1
        else:
            print("All ghosts far away (2): %i" % (classTestData[i]))  # //0,4 or 5 on all distances gives 2
            print(testTuples[i])
    elif (testTuples[i][0]==1) and (testTuples[i][1]==1)and (testTuples[i][2]==1)and(testTuples[i][3]==1): ##All goast editable
        if (classTestData[i] == 1):
            correct = correct + 1
        else:
            print(testTuples[i])
            print("All ghosts edible (1): %i" % (classTestData[i]))  # // One on all editable gives 1 attack
    elif (1<=testTuples[i][4]<=2) or (1<=testTuples[i][5]<=2) or (1<=testTuples[i][6]<=2) or (1<=testTuples[i][7]<=2): ##Any ghost close
        if (classTestData[i] == 4):
            correct = correct + 1
        else:
            print("Any ghost close (4): %i" % (classTestData[i]))  # // 1 or 2 on distances gives 3
            print(testTuples[i])
    else:
        if (classTestData[i] == 5):
            correct = correct + 1
        else:
            print("All other (5): %i" % (classTestData[i]))  # // 1 or 2 on distances gives 3
            print(testTuples[i])

print("Possible Accuracy testData: %.3f" % (correct/testTuples.shape[0]))


