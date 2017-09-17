from __future__ import print_function
from __future__ import division
from __future__ import absolute_import

from random import shuffle
import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
#Values most will be overridden.... bellow..
numberItems = 10
itemValuesLow = 1
itemValuesHigh = 50
itemValues = np.random.randint(itemValuesLow, itemValuesHigh, size=(1, numberItems))
itemWeightsLow = 1
itemWeightsHigh = 10
itemWeights= np.random.randint(itemWeightsLow, itemWeightsHigh, size=(1, numberItems))
numberKnappsacks = 2
knappsacksWeightsLow = 20
knappsacksWeightsHigh = 45
knappsackWeightConstraints = np.random.randint(20, 45, size=(numberKnappsacks, 1))
X = np.zeros((numberKnappsacks, numberItems), dtype=np.int)

##returns a random knappsack index that can hold an item returns -1 if no knapsack kan hold the item
def getRandomKnappsackIndexThatCanHoldItem(weight):
    #shuffledIndex = np.arange(numberKnappsacs)
    #np.random.shuffle(shuffledIndex)
    shuffledIndex = np.random.permutation(numberKnappsacks)
    for i in range(0, numberKnappsacks):
        #Check if a knappsack kan hold the item
        knapsackIndex = shuffledIndex[i]
        placeLeft = knappsackWeightConstraints[knapsackIndex] - getWeightsForKnappsacks()[knapsackIndex]
        if weight <= placeLeft[0]:
            return knapsackIndex
    return -1 ##Then there are no knappsack that can hold the item

##Sums all items in the individual knappsacks return as an array
def getValuesForKnappsacks():
    _Values = np.zeros((knappsackWeightConstraints.shape[0], 1), dtype=np.int)
    for i in range(0, numberKnappsacks):
        _Values[i, 0] = np.sum(itemValues * X[i, :], axis=1, keepdims=True)
    return _Values

##Sums all weights in the individual knappsacks return as an array
def getWeightsForKnappsacks():
    _Weight = np.zeros((knappsackWeightConstraints.shape[0], 1), dtype=np.int)
    for i in range(0, numberKnappsacks):
        #Sum for one knappsack
        sum = np.sum(itemWeights * X[i, :], axis=1, keepdims=True)
        _Weight[i, 0] = sum
    return _Weight

def getIndexForMostValubleItemNotPacked():
    xor =1* np.logical_xor(np.sum(X, axis=0, keepdims=True),np.ones((1,numberItems),dtype=np.int))
    mostValuableItem= np.divide(itemValues, itemWeights) * xor
    return np.argmax(mostValuableItem)

def getIndexArrayForItemNotPackedInWeightOrder():
    xor =1* np.logical_xor(np.sum(X, axis=0, keepdims=True),np.ones((1,numberItems),dtype=np.int))
    mostValuableItem= np.divide(itemValues, itemWeights) * xor
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

#Are all items picked
def allItemsPicked():
    if(np.sum(X)<numberKnappsacks*numberItems):
        return False
    else:
        return True

def putItemInKnappsack(knappsack,itemIndex,):
    X[knappsack,itemIndex] = 1

#Plots the items in colors based on wich knappsack they are in, items not in knappsacks are black spots
def plotTheItems():
    for a in range(0, numberKnappsacks):
        packWeights = X[a,:] * itemWeights[0, :]
        packValues = X[a,:] * itemValues[0, :]
        packValues = packValues[packValues>0]
        packWeights = packWeights[packWeights>0]
        c=np.random.random(3)
        plt.scatter(packWeights,packValues,color=c)
    indexArrayForNotPacked = getIndexArrayForItemNotPackedInWeightOrder()
    indexes = indexArrayForNotPacked[indexArrayForNotPacked >= 0]
    restWeitghts = np.take(itemWeights[0, :], indexes)
    restValues = np.take(itemValues[0, :], indexes)
    d2 = {'Weights2': restWeitghts,
          'Values2': restValues}
    df2 = pd.DataFrame(d2)
    plt.scatter(df2['Weights2'], df2['Values2'], color='black')
    plt.xlabel("Weights")
    plt.ylabel("Values")
    plt.show()

def printAll(verbose):
    if(verbose):
        print("************************")
        print("Value")
        print(itemValues)
        print("ItemWeights")
        print(itemWeights)
        print("Values/ItemWeights")
        print(np.divide(itemValues, itemWeights))
        print("X")
        print(X)
        print("Knappsacks weightleft")
        print(knappsackWeightConstraints - getWeightsForKnappsacks())
        print("ItemsNotPicked")
        indexArrayMostValubleItems = getIndexArrayForItemNotPackedInWeightOrder()
        s=""
        for j in range(0,indexArrayMostValubleItems.shape[0]):
            if (indexArrayMostValubleItems[j] < 0):  ##All items picked
                break
            s=s+","+str(itemWeights[0, indexArrayMostValubleItems[j]])
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
          weightPickedItem = itemWeights[0, indexArrayMostValubleItems[j]]
          indexKnappsackToPutItemIn = getRandomKnappsackIndexThatCanHoldItem(weightPickedItem)
          if(indexKnappsackToPutItemIn>-1): #Then to hevy for any knappsack try next
            putItemInKnappsack(indexKnappsackToPutItemIn,indexArrayMostValubleItems[j])#Add it and break
            break
        i=i+1

def setup(_numberKnappsacks,_knappsacksWeightsLow, _knappsacksWeightsHigh,
    _numberItems, _itemValuesLow, _itemValuesHigh, _itemWeightsLow, _itemWeightsHigh):
     global numberItems,itemValues,itemWeights, knappsackWeightConstraints,X,numberKnappsacks
     numberItems = _numberItems
     numberKnappsacks = _numberKnappsacks
     itemValues = np.random.randint(_itemValuesLow, _itemValuesHigh, size=(1, _numberItems))
     itemWeights = np.random.randint(_itemWeightsLow, _itemWeightsHigh, size=(1, _numberItems))
     knappsackWeightConstraints = np.random.randint(_knappsacksWeightsLow, _knappsacksWeightsHigh, size=(_numberKnappsacks, 1))
     X = np.zeros((_numberKnappsacks, _numberItems), dtype=np.int)

print ('Knappsack problem')
print ('0: Greedy Algoritm 25 knappsacks 30-60kg 1000 items 2-10kg value 1-100 + plot packed items (takes around 10sec)')
print ('1: Greedy Algoritm 5 knappsacks 30-60kg 100 items value 1-100, 2-10 kg + plot packed items')
print ('2: Greedy Algoritm 5 knappsacks 30-60kg 10 items value 1-100, 2-10 kg + plot packed items (All items fit in packs)')
print ('3: Greedy Algoritm 5 knappsacks 13,17,23,29,31,37,41,43,47,53 kg 200 items value 1-100, 7-11 kg + plot packed items')
print ('4: Greedy Algoritm like 0 but run 10 times and plot histogram over values (takes around a minute)')
print ('5: Greedy Algoritm like 1 but run 100 times and plot histogram over values')
print ('6: Greedy Algoritm like 2 but run 100 times and plot histogram over values (All items fit in packs)')
print ('7: Greedy Algoritm like 3 but run 100 times and plot histogram over values')
opt = int(input('Select startvalues: '))
if opt==0:
    # # Value p
    setup(25, 30, 60,1000, 1, 100, 2, 10)
    greedyAlgorithm()
    printAll(False)
    plotTheItems()
if opt==1:
    setup(5,30,60,100,1,100,2,10)
    greedyAlgorithm()
    printAll(False)
    plotTheItems()
if opt==2:
    setup(5,30,60,10,1,100,2,10)
    greedyAlgorithm()
    printAll(False)
    plotTheItems()
if opt==3:
    setup(10,1,2,200,1,100,7,12)
    knappsackWeightConstraints = np.array([[13], [17], [23], [29], [31], [37], [41], [43], [47], [53]])
    greedyAlgorithm()
    printAll(False)
    plotTheItems()
if opt==4:
    setup(25, 30, 60,1000, 1, 100, 2, 10)
    numberRuns = 10
    result = np.zeros(numberRuns)
    for b in range(0,numberRuns):
        X = np.zeros((numberKnappsacks, numberItems), dtype=np.int)
        greedyAlgorithm()
        printAll(False)
        result[b]=utility()
    print(result)
    plt.hist(result)
    plt.show()
if opt==5:
    setup(5, 30, 60, 100, 1, 100, 2, 10)
    numberRuns = 100
    result = np.zeros(numberRuns)
    for b in range(0,numberRuns):
        X = np.zeros((numberKnappsacks, numberItems), dtype=np.int)
        greedyAlgorithm()
        printAll(False)
        result[b]=utility()
    print(result)
    plt.hist(result)
    plt.show()
if opt==6:
    setup(5, 30, 60, 10, 1, 100, 2, 10)
    numberRuns = 100
    result = np.zeros(numberRuns)
    for b in range(0,numberRuns):
        X = np.zeros((numberKnappsacks, numberItems), dtype=np.int)
        greedyAlgorithm()
        printAll(False)
        result[b]=utility()
    print(result)
    plt.hist(result)
    plt.show()
if opt==7:
    setup(10, 1, 2, 200, 1, 100, 3, 13)
    knappsackWeightConstraints = np.array([[13], [17], [23], [29], [31], [37], [41], [43], [47], [53]])
    numberRuns = 100
    result = np.zeros(numberRuns)
    for b in range(0,numberRuns):
        X = np.zeros((numberKnappsacks, numberItems), dtype=np.int)
        greedyAlgorithm()
        printAll(False)
        result[b]=utility()
    print(result)
    plt.hist(result)
    plt.show()
