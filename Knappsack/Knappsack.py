from __future__ import print_function
from __future__ import division
from __future__ import absolute_import

import pandas as pd
import numpy as np
#Value pi
Value = np.array([[23,34,56,3,2,3]])
numberItems = Value.shape[1]
print(Value)
#Weight wi
ItemWeights = np.array([[3, 2, 4, 5, 6, 6]])
print(ItemWeights)
#Weight capacity Wj for the knappsacks (2 in this case)
WeightConstraints = np.array([[20], [35]])
numberKnappsacs = WeightConstraints.shape[0]
d = {'one' : np.random.rand(10),
     'two' : np.random.rand(10)}

df = pd.DataFrame(d)

df.plot(style=['o','rx'])
#Decision matrix
X = np.zeros((numberKnappsacs,numberItems),dtype=np.int)
X[1,0]=1
X[0,3]=1
X[1,1]=1
print("X")
print(X)
#Value matrix for knappsacks
Values = np.zeros((WeightConstraints.shape[0], 1), dtype=np.int)
print(Values)
for i in range(0,numberKnappsacs):
    Values[i,0] = np.sum(Value * X[i, :], axis=1, keepdims=True)

print("Result of packing")
print(Values)

def greedyAlgorithm():
    #
    return 1

def getIndexForMostValubleItem():
    return 1