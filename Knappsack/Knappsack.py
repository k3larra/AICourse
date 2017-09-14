from __future__ import print_function
from __future__ import division
from __future__ import absolute_import

import pandas as pd
import numpy as np
import matplotlib.pyplot as plt


#Value pi
Values = np.array([[24, 34, 56, 84, 2, 3,5,4,3,2,34,3,2,4,67,8,7,6,1,1]])

#Weight wi
ItemWeights = np.array([[3, 2, 4, 2, 6, 6,3,2,3,4,30,3,20,40,67,80,7,600,1,1]],dtype=np.int8)

#Weight capacity Wj for the knappsacks (2 in this case)
WeightConstraints = np.array([[20], [35]])
numberKnappsacs = WeightConstraints.shape[0]
numberItems = Values.shape[1]
#df.plot(style=['o','rx'])
#df.
#Decision matrix
X = np.zeros((numberKnappsacs,numberItems),dtype=np.int)
#Create Value matrix for knappsacks



##returns the first knappsack that can hold an item returns -1 if no knapsack kan hold the item
def getKnappsackIndexThatCanHoldItem(weight):
    for i in range(0,numberKnappsacs):
        #Check if a knappsack kan hold the item
        placeLeft = WeightConstraints[i]-getWeightsForKnappsacks()[i]
        if weight <= placeLeft[0]:
            return i
    return -1

##Sums all items in the individual knappsacks
def getValuesForKnappsacks():
    _Values = np.zeros((WeightConstraints.shape[0], 1), dtype=np.int)
    for i in range(0, numberKnappsacs):
        _Values[i, 0] = np.sum(Values * X[i, :], axis=1, keepdims=True)
    return _Values

##Sums all items in the individual knappsacks
def getWeightsForKnappsacks():
    _Weight = np.zeros((WeightConstraints.shape[0], 1), dtype=np.int)
    for i in range(0, numberKnappsacs):
        #Sum for one knappsack
        sum = np.sum(ItemWeights * X[i, :], axis=1, keepdims=True)
        _Weight[i, 0] = sum
    return _Weight

def getIndexForMostValubleItemNotPacked():
    xor =1* np.logical_xor(np.sum(X, axis=0, keepdims=True),np.ones((1,numberItems),dtype=np.int))
    mostValuableItem= np.divide(Values, ItemWeights) * xor
    return np.argmax(mostValuableItem)

def getIndexArrayForItemNotPackedInWeightOrder():
    xor =1* np.logical_xor(np.sum(X, axis=0, keepdims=True),np.ones((1,numberItems),dtype=np.int))
    mostValuableItem= np.divide(Values, ItemWeights) * xor
    orderArray = np.zeros(mostValuableItem.shape[1], dtype=np.int8)
    for j in range(0,mostValuableItem.shape[1]):
        indexForMostValubleItem = np.argmax(mostValuableItem)
        orderArray[j] = indexForMostValubleItem
        mostValuableItem[0,indexForMostValubleItem]=-1
    return orderArray

##Sum of value all items in knappsacs
def utility():
    return np.sum(getValuesForKnappsacks())

def printTheItems():
    d = {'Weights': ItemWeights[0, :],
         'Values': Values[0, :]}
    df = pd.DataFrame(d)
    plt.scatter(df['Weights'], df['Values'])
    plt.xlabel("Weights")
    plt.ylabel("Values")
    plt.show()

def allItemsPicked():
    if(np.sum(X)<numberKnappsacs*numberItems):
        return False
    else:
        return True

def putItemInKnappsack(knappsack,itemIndex,):
    X[knappsack,itemIndex] = 1

def printAll(verbose):
    if(verbose):
        print("************************")
        print("Value")
        print(Values)
        print("ItemWeights")
        print(ItemWeights)
        print("Values/ItemWeights")
        print(np.divide(Values, ItemWeights))
        print("X")
        print(X)
        print("Knappsacks weightleft")
        print(WeightConstraints-getWeightsForKnappsacks())
    print("Value for the sacks")
    print(getValuesForKnappsacks())
    print("Total value (utility)")
    print(utility())
    print("************************")


def greedyAlgorithm():
    # F
    i=0
    while ((not allItemsPicked())and(i<50)):
        indexMostValubleItem = getIndexArrayForItemNotPackedInWeightOrder()
        for j in range(0,indexMostValubleItem.shape[0]):
          weightPickedItem = ItemWeights[0, indexMostValubleItem[j]]
          indexKnappsackToPutItemIn = getKnappsackIndexThatCanHoldItem(weightPickedItem)
          if(indexKnappsackToPutItemIn>-1):
            putItemInKnappsack(indexKnappsackToPutItemIn,indexMostValubleItem[j])
            break
        i=i+1
    #return 1
#program starts
#print(getIndexForMostValubleItemNotPacked())
#print(getValuesForKnappsacks())
#print(utility())
#print(allItemsPicked())
#print(numberKnappsacs*numberItems)
#print(X)
#printTheItems()
printAll(True)
greedyAlgorithm()
printAll(True)
#print(getIndexArrayForItemNotPackedInWeightOrder())
#print(np.ones((numberKnappsacs,numberItems),dtype=np.int))
#print(np.logical_xor(X,np.ones((numberKnappsacs,numberItems),dtype=np.int)))
#print(1* np.logical_xor(X,np.ones((numberKnappsacs,numberItems),dtype=np.int)))
#getIndexForMostValubleItemNotPacked()