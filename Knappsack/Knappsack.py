from __future__ import print_function
from __future__ import division
from __future__ import absolute_import

from random import shuffle

import pandas as pd
import numpy as np
import matplotlib.pyplot as plt


#Value pi
#Values = np.array([[24, 34, 56, 84, 2, 3,5,4,3,2,34,3,2,4,67,8,7,6,1,1]],dtype=np.int8)
Values = np.random.randint(1,100,size=(1,10000))
#Weight wi
#ItemWeights = np.array([[3, 2, 4, 2, 6, 6,3,2,3,4,30,3,20,40,67,80,7,600,1,1]],dtype=np.int8)
ItemWeights= np.random.randint(1,10,size=(1,10000))

#Weight capacity Wj for the knappsacks (2 in this case)
#WeightConstraints = np.array([[20],[35],[50],[100],[45],[69],[80],[40],[30],[28]])
WeightConstraints = np.random.randint(20,45,size=(500,1))
numberKnappsacs = WeightConstraints.shape[0]
numberItems = Values.shape[1]
#df.plot(style=['o','rx'])
#df.
#Decision matrix
X = np.zeros((numberKnappsacs,numberItems),dtype=np.int)
#Create Value matrix for knappsacks



##returns a random knappsack index that can hold an item returns -1 if no knapsack kan hold the item
def getRandomKnappsackIndexThatCanHoldItem(weight):
    shuffledIndex = np.arange(numberKnappsacs)
    np.random.shuffle(shuffledIndex)
    for i in range(0,numberKnappsacs):
        #Check if a knappsack kan hold the item
        knapsackIndex = shuffledIndex[i]
        placeLeft = WeightConstraints[knapsackIndex]-getWeightsForKnappsacks()[knapsackIndex]
        if weight <= placeLeft[0]:
            return knapsackIndex
    return -1 ##Then there are no knappsack that can hold the item

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
    orderArray = np.zeros(mostValuableItem.shape[1], dtype=np.int)
    for j in range(0,mostValuableItem.shape[1]):
        indexForMostValubleItem = np.argmax(mostValuableItem)##Kolla om -1
        if (mostValuableItem[0,indexForMostValubleItem] != 0):
            orderArray[j] = indexForMostValubleItem
        else:
            orderArray[j] = -1
        mostValuableItem[0,indexForMostValubleItem]=-1
    return orderArray

##Sum of value all items in knappsacs
def utility():
    return np.sum(getValuesForKnappsacks())

def plotTheItems():
    d = {'Weights': ItemWeights[0, :],
         'Values': Values[0, :]}

    df = pd.DataFrame(d)
    plt.scatter(df['Weights'], df['Values'],color='blue')
    indexArrayForNotPacked = getIndexArrayForItemNotPackedInWeightOrder()
    indexes = indexArrayForNotPacked[indexArrayForNotPacked >= 0]
    restWeitghts = np.take(ItemWeights[0, :], indexes)
    restValues = np.take(Values[0, :], indexes)

    d2 = {'Weights2': restWeitghts,
          'Values2': restValues}
    df2 = pd.DataFrame(d2)
    plt.scatter(df2['Weights2'], df2['Values2'], color='red')
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
        print("ItemsNotPicked")
        indexArrayMostValubleItems = getIndexArrayForItemNotPackedInWeightOrder()
        s=""
        for j in range(0,indexArrayMostValubleItems.shape[0]):
            if (indexArrayMostValubleItems[j] < 0):  ##All items picked
                break
            s=s+","+str(ItemWeights[0, indexArrayMostValubleItems[j]])
        print("Weights not picked: "+s)
    print("Value for the sacks")
    print(getValuesForKnappsacks())
    print("Total value (utility)")
    print(utility())
    print("************************")


def greedyAlgorithm():
    allItemsPossibleToPackPacked = False
    i=0
    while ((not allItemsPicked()and not allItemsPossibleToPackPacked)):
        indexArrayMostValubleItems = getIndexArrayForItemNotPackedInWeightOrder()
        for j in range(0,indexArrayMostValubleItems.shape[0]):
          if(indexArrayMostValubleItems[j]<0): ##All items picked
              allItemsPossibleToPackPacked = True
              break
          weightPickedItem = ItemWeights[0, indexArrayMostValubleItems[j]]
          indexKnappsackToPutItemIn = getRandomKnappsackIndexThatCanHoldItem(weightPickedItem)
          if(indexKnappsackToPutItemIn>-1): #Then to hevy for any knappsack try next
            putItemInKnappsack(indexKnappsackToPutItemIn,indexArrayMostValubleItems[j])#Add it and break
            break
        i=i+1


greedyAlgorithm()
printAll(False)
plotTheItems()

