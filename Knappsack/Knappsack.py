from __future__ import absolute_import
from __future__ import division
from __future__ import print_function

from tempfile import TemporaryFile
import os
import matplotlib.pyplot as plt
import numpy as np
import pandas as pd

# Values most will be overridden.... bellow..
numberItems = 10
itemValuesLow = 1
itemValuesHigh = 50
itemValues = np.random.randint(itemValuesLow, itemValuesHigh, size=(1, numberItems))
itemWeightsLow = 1
itemWeightsHigh = 10
itemWeights = np.random.randint(itemWeightsLow, itemWeightsHigh, size=(1, numberItems))
numberKnappsacks = 2
knappsacksWeightsLow = 20
knappsacksWeightsHigh = 45
knappsackWeightConstraints = np.random.randint(20, 45, size=(numberKnappsacks, 1))
X = np.zeros((numberKnappsacks, numberItems), dtype=np.int)


# returns a random knappsack index that can hold an item returns -1 if no knapsack kan hold the item
def getRandomKnappsackIndexThatCanHoldItem(weight):
    shuffledIndex = np.random.permutation(numberKnappsacks)
    for i in range(0, numberKnappsacks):
        # Check if a knappsack kan hold the item
        knapsackIndex = shuffledIndex[i]
        placeLeft = knappsackWeightConstraints[knapsackIndex] - getWeightsForKnappsacks()[knapsackIndex]
        if weight <= placeLeft[0]:
            return knapsackIndex
    return -1  # Then there are no knappsack that can hold the item


# Sums all items in the individual knappsacks return as an array
def getValuesForKnappsacks():
    _Values = np.zeros((knappsackWeightConstraints.shape[0], 1), dtype=np.int)
    for i in range(0, numberKnappsacks):
        _Values[i, 0] = np.sum(itemValues * X[i, :], axis=1, keepdims=True)
    return _Values


# Sums all weights in the individual knappsacks return as an array
def getWeightsForKnappsacks():
    _Weight = np.zeros((knappsackWeightConstraints.shape[0], 1), dtype=np.int)
    for i in range(0, numberKnappsacks):
        # Sum for one knappsack
        sum_it = np.sum(itemWeights * X[i, :], axis=1, keepdims=True)
        _Weight[i, 0] = sum_it
    return _Weight


def getIndexForMostValuableItemNotPacked():
    xor = 1 * np.logical_xor(np.sum(X, axis=0, keepdims=True), np.ones((1, numberItems), dtype=np.int))
    mostValuableItem = np.divide(itemValues, itemWeights) * xor
    return np.argmax(mostValuableItem)

def getMaxWeightForItemNotPacked():
    xor = 1 * np.logical_xor(np.sum(X, axis=0, keepdims=True), np.ones((1, numberItems), dtype=np.int))
    maxWeight = itemWeights * xor
    return np.argmax(maxWeight)

def getMinWeightForItemNotPacked():
    xor = 1 * np.logical_xor(np.sum(X, axis=0, keepdims=True), np.ones((1, numberItems), dtype=np.int))
    minWeight = itemWeights * xor
    return np.argmin(minWeight)

def getIndexArrayForItemNotPackedInValueOrder():
    xor = 1 * np.logical_xor(np.sum(X, axis=0, keepdims=True), np.ones((1, numberItems), dtype=np.int))
    mostValuableItem = np.divide(itemValues, itemWeights) * xor
    orderArray = np.zeros(mostValuableItem.shape[1], dtype=np.int)
    for j in range(0, mostValuableItem.shape[1]):
        indexForMostValuableItem = np.argmax(mostValuableItem)
        if mostValuableItem[0, indexForMostValuableItem] != 0:
            orderArray[j] = indexForMostValuableItem
        else:
            orderArray[j] = -1
        mostValuableItem[0, indexForMostValuableItem] = -1
    return orderArray

def rotateItems():
    done = False
    fromPack = 0
    itemNbr = 0
    indexForKnapsackWithMostSpaceLeft = np.argmax(knappsackWeightConstraints - getWeightsForKnappsacks(),axis=0)[0]
    weightLeft = knappsackWeightConstraints[indexForKnapsackWithMostSpaceLeft] - getWeightsForKnappsacks()[indexForKnapsackWithMostSpaceLeft]
    if(weightLeft[0]>0):
        itemsWeightInKnapsack = itemWeights * X[indexForKnapsackWithMostSpaceLeft, :]
        itemsWeightInKnapsack = itemsWeightInKnapsack[itemsWeightInKnapsack > 0]
        itemWeightsToSearchFor = np.unique(itemsWeightInKnapsack) # Those are the unique weights in the knapsack we want to remove from
        itemsWeightsAllKnapsack = itemWeights * X
        itemsWeightsAllKnapsack[indexForKnapsackWithMostSpaceLeft,:] = 0 # empty the row we are moving item from
        for i in range(0,itemWeightsToSearchFor.shape[0]):
            exists = np.where(itemsWeightsAllKnapsack==itemWeightsToSearchFor[i]+weightLeft) # remove exact weight Test other solutions!!
            if (exists[0].shape[0]>0): # found at least one item to swap with
                throwAwayFromSack = itemWeights * X
                throwAwayFromSack = throwAwayFromSack[indexForKnapsackWithMostSpaceLeft,:]
                removeThis = np.where( throwAwayFromSack == itemWeightsToSearchFor[i])
                if removeThis[0].shape[0]>0:
                    X[indexForKnapsackWithMostSpaceLeft, removeThis[0][0]] = 0 #Throw away this item
                    X[exists[0][0], exists[1][0]] = 0 # move this item from one knapsack to
                    X[indexForKnapsackWithMostSpaceLeft, exists[1][0]] = 1 #This knapsack
                    fromPack = indexForKnapsackWithMostSpaceLeft
                    itemNbr = removeThis[0][0]
                    done = True
                break
    else:
        done=False
    return [done,fromPack,itemNbr]

# Sum of value all items in knappsacks
def utility():
    return np.sum(getValuesForKnappsacks())


# Are all items picked
def allItemsPicked():
    if np.sum(X) < numberKnappsacks * numberItems:
        return False
    else:
        return True

def printDetails(info,knappsack,item):
    itemWeight = itemWeights[0, item]
    itemValue = itemValues[0, item]
    print(info+" weight: " + str(itemWeight) + " with value: " + str(itemValue)+" knapsack "+ str(knappsack)+" value/weight: "+str(itemValue/itemWeight))

def putItemInKnappsack(knappsack, itemIndex, ):
    X[knappsack, itemIndex] = 1


# Plots the items in colors based on which knappsack they are in, items not in knappsacks are black spots
def plotTheItems():
    for a in range(0, numberKnappsacks):
        packWeights = X[a, :] * itemWeights[0, :]
        packValues = X[a, :] * itemValues[0, :]
        packValues = packValues[packValues > 0]
        packWeights = packWeights[packWeights > 0]
        c = np.random.random(3)
        plt.scatter(packWeights, packValues, color=c)
    indexArrayForNotPacked = getIndexArrayForItemNotPackedInValueOrder()
    indexes = indexArrayForNotPacked[indexArrayForNotPacked >= 0]
    restWeights = np.take(itemWeights[0, :], indexes)
    restValues = np.take(itemValues[0, :], indexes)
    d2 = {'Weights2': restWeights,
          'Values2': restValues}
    df2 = pd.DataFrame(d2)
    plt.scatter(df2['Weights2'], df2['Values2'], color='black')
    plt.xlabel("Weights")
    plt.ylabel("Values")
    plt.show()


def printAll(verbose):
    if verbose:
        print("************************")
        print("Value")
        print(itemValues)
        print("ItemWeights")
        print(itemWeights)
        print("Values/ItemWeights")
        print(np.divide(itemValues, itemWeights))
        print("X")
        print(X)
        print("Knappsacks weight left")
        print(knappsackWeightConstraints - getWeightsForKnappsacks())
        print("ItemsNotPicked")
        indexArrayMostValuableItems = getIndexArrayForItemNotPackedInValueOrder()
        s = ""
        for j in range(0, indexArrayMostValuableItems.shape[0]):
            if indexArrayMostValuableItems[j] < 0:  # All items picked
                break
            s = s + "," + str(itemWeights[0, indexArrayMostValuableItems[j]])
        print("Weights not picked: " + s)
    print("Value for the sacks")
    print(getValuesForKnappsacks())
    print("Total value (utility)")
    print(utility())
    print("************************")


def greedyAlgorithm():
    allItemsPossibleToPackPacked = False
    i = 0
    while not allItemsPicked() and not allItemsPossibleToPackPacked:
        indexArrayMostValuableItems = getIndexArrayForItemNotPackedInValueOrder()
        for j in range(0, indexArrayMostValuableItems.shape[0]):
            if indexArrayMostValuableItems[j] < 0:  # All items picked
                allItemsPossibleToPackPacked = True
                break
            weightPickedItem = itemWeights[0, indexArrayMostValuableItems[j]]
            indexKnappsackToPutItemIn = getRandomKnappsackIndexThatCanHoldItem(weightPickedItem)
            if indexKnappsackToPutItemIn > -1:  # Then to heavy for any knappsack try next
                putItemInKnappsack(indexKnappsackToPutItemIn, indexArrayMostValuableItems[j])  # Add it and break
                break
        i = i + 1

def nearestNeighborhood():
    global numberItems, itemValues, itemWeights, knappsackWeightConstraints, X, numberKnappsacks,\
        lastAddedknappsack,lastaddedItem
    greedyAlgorithm()
    startValue = utility()
    breakIt = False
    lastAddedknappsack = -1
    lastaddedItem = -1
    while (not breakIt):
         remove = rotateItems()
         if(remove[0]):
             removedfromPack = remove[1]
             removedItemNbr = remove[2]
             indexForKnapsackWithMostSpaceLeft = np.argmax(knappsackWeightConstraints - getWeightsForKnappsacks(), axis=0)[0]
             weightLeft = knappsackWeightConstraints[indexForKnapsackWithMostSpaceLeft] - getWeightsForKnappsacks()[indexForKnapsackWithMostSpaceLeft]
             if(weightLeft[0]>0):
                 # Fill the empty space in all knappsacks possible
                 for k in range(0, getIndexArrayForItemNotPackedInValueOrder().shape[0]): # Dont fill in the same
                     index = getIndexArrayForItemNotPackedInValueOrder()[k]
                     if (index == -1): break # All packed items packed
                     if (itemWeights[0,index] <= weightLeft):
                         # Check so I dont add the same as I removed
                         if (removedfromPack != lastAddedknappsack) and (removedItemNbr!=lastaddedItem):
                            lastAddedknappsack = indexForKnapsackWithMostSpaceLeft
                            lastaddedItem = index
                            X[indexForKnapsackWithMostSpaceLeft,index]=1
                            # print(" neigbourhood: "+str(utility()-startValue))
                            break
             else:breakIt=True #There is no space in
         else:breakIt = True
    greedyAlgorithm()
    #print("Started with utility: "+ str(startValue) + " ended with : "+str(utility())+" so added value "+str(utility()-startValue))
    return utility()-startValue


def setup(_numberKnappsacks, _knappsacksWeightsLow, _knappsacksWeightsHigh,
          _numberItems, _itemValuesLow, _itemValuesHigh, _itemWeightsLow, _itemWeightsHigh):
    global numberItems, itemValues, itemWeights, knappsackWeightConstraints, X, numberKnappsacks
    numberItems = _numberItems
    numberKnappsacks = _numberKnappsacks
    itemValues = np.random.randint(_itemValuesLow, _itemValuesHigh, size=(1, _numberItems))
    itemWeights = np.random.randint(_itemWeightsLow, _itemWeightsHigh, size=(1, _numberItems))
    knappsackWeightConstraints = np.random.randint(_knappsacksWeightsLow, _knappsacksWeightsHigh,
                                                   size=(_numberKnappsacks, 1))
    X = np.zeros((_numberKnappsacks, _numberItems), dtype=np.int)

print('Knapsack problem')
print('0: Greedy Algorithm 25 knapsacks 30-60kg 1000 items 2-10kg value 1-100 '
      '+ plot packed items (takes around 10sec)')
print('1: Greedy Algorithm 5 knapsacks 30-60kg 100 items value 1-100, 2-10 kg + plot packed items')
print('2: Greedy Algorithm 5 knapsacks 30-60kg 10 items value 1-100, '
      '2-10 kg + plot packed items (All items fit in packs)')
print('3: Greedy Algorithm 5 knapsacks 13,17,23,29,31,37,41,43,47,53 kg 200 items value 1-100, '
      '7-11 kg + plot packed items')
print('4: Greedy Algorithm like 0 but run 10 times and plot histogram over values (takes around a minute)')
print('5: Greedy Algorithm like 1 but run 100 times and plot histogram over values')
print('6: Greedy Algorithm like 2 but run 100 times and plot histogram over values (All items fit in packs)')
print('7: Greedy Algorithm like 3 but run 100 times and plot histogram over values')
print('8: Greedy Algorithm run X times with a predifined set of items and same packs as in 3')
print('9: same as 8 but with Nearest neighborhood')
print('10: Same as 5 but with Nearest neighborhood')
opt = int(input('Select alternatives: '))
#opt = 9
if opt == 0:
    # # Value p
    setup(25, 30, 60, 1000, 1, 100, 2, 10)
    greedyAlgorithm()
    printAll(False)
    plotTheItems()
if opt == 1:
    setup(5, 30, 60, 100, 1, 100, 2, 10)
    greedyAlgorithm()
    printAll(False)
    plotTheItems()
if opt == 2:
    setup(5, 30, 60, 10, 1, 100, 2, 10)
    greedyAlgorithm()
    printAll(False)
    plotTheItems()
if opt == 3:
    setup(10, 1, 2, 200, 1, 100, 7, 12)
    knappsackWeightConstraints = np.array([[13], [17], [23], [29], [31], [37], [41], [43], [47], [53]])
    greedyAlgorithm()
    printAll(False)
    plotTheItems()
if opt == 4:
    setup(25, 30, 60, 1000, 1, 100, 2, 10)
    numberRuns = 10
    result = np.zeros(numberRuns)
    for b in range(0, numberRuns):
        X = np.zeros((numberKnappsacks, numberItems), dtype=np.int)
        greedyAlgorithm()
        printAll(False)
        result[b] = utility()
    print(result)
    plt.hist(result)
    plt.show()
if opt == 5:
    setup(5, 30, 60, 100, 1, 100, 2, 10)
    numberRuns = 100
    result = np.zeros(numberRuns)
    for b in range(0, numberRuns):
        X = np.zeros((numberKnappsacks, numberItems), dtype=np.int)
        greedyAlgorithm()
        printAll(False)
        result[b] = utility()
    print(result)
    plt.hist(result)
    plt.show()
if opt == 6:
    setup(5, 30, 60, 10, 1, 100, 2, 10)
    numberRuns = 100
    result = np.zeros(numberRuns)
    for b in range(0, numberRuns):
        X = np.zeros((numberKnappsacks, numberItems), dtype=np.int)
        greedyAlgorithm()
        printAll(False)
        result[b] = utility()
    print(result)
    plt.hist(result)
    plt.show()
if opt == 7:
    setup(10, 1, 2, 200, 1, 100, 3, 13)
    knappsackWeightConstraints = np.array([[13], [17], [23], [29], [31], [37], [41], [43], [47], [53]])
    numberKnappsacks = knappsackWeightConstraints.shape[0]
    X = np.zeros((numberKnappsacks, numberItems), dtype=np.int)
    numberRuns = 10
    result = np.zeros(numberRuns)
    for b in range(0, numberRuns):
        X = np.zeros((numberKnappsacks, numberItems), dtype=np.int)
        greedyAlgorithm()
        printAll(False)
        result[b] = utility()
    print(result)
    print("Max")
    print(np.max(result))
    plt.hist(result,numberRuns)
    plt.xlabel("Value for "+str(numberKnappsacks)+" knapsacks")
    plt.ylabel("Number of times packed out of "+str(numberRuns)+" ")
    plt.show()
if opt == 8:
    numberRunstr = input('Select number runs (DEFAULT: 10): ')
    numberRuns = 10
    if numberRunstr != '': numberRuns = int(numberRunstr)
    # To save new dataset uncomment those
    # setup(10, 1, 2, 200, 1, 100, 3, 13)
    # np.save(os.path.join(os.getcwd(), "itemValues"), itemValues)
    # np.save(os.path.join(os.getcwd(), "itemWeights"), itemWeights)
    itemValues = np.load(os.path.join(os.getcwd(), "itemValues.npy"))
    itemWeights = np.load(os.path.join(os.getcwd(), "itemWeights.npy"))
    numberItems = itemValues.shape[1]
    knappsackWeightConstraints = np.array([[13], [17], [23], [29], [31], [37], [41], [43], [47], [53]])
    numberKnappsacks = knappsackWeightConstraints.shape[0]
    X = np.zeros((numberKnappsacks, numberItems), dtype=np.int)
    result = np.zeros(numberRuns)
    for b in range(0, numberRuns):
        X = np.zeros((numberKnappsacks, numberItems), dtype=np.int)
        greedyAlgorithm()
        #printAll(False)
        result[b] = utility()
    print("Max 5")
    max = np.sort(result)
    for i in range(max.shape[0]-5,max.shape[0]):
        print(max[i])
    plt.hist(result,numberRuns)
    plt.xlabel("Value for "+str(numberKnappsacks)+" knapsacks")
    plt.ylabel("Number of times packed out of "+str(numberRuns)+" ")
    plt.show()

if opt == 9:
    numberRunstr = input('Select number runs (DEFAULT: 10): ')
    numberRuns = 10
    if numberRunstr != '': numberRuns = int(numberRunstr)
    itemValues = np.load(os.path.join(os.getcwd(), "itemValues.npy"))
    itemWeights = np.load(os.path.join(os.getcwd(), "itemWeights.npy"))
    numberItems = itemValues.shape[1]
    knappsackWeightConstraints = np.array([[13], [17], [23], [29], [31], [37], [41], [43], [47], [53]])
    numberKnappsacks = knappsackWeightConstraints.shape[0]
    X = np.zeros((numberKnappsacks, numberItems), dtype=np.int)
    result = np.zeros(numberRuns)
    betterThanGreedy = np.zeros(numberRuns)
    for b in range(0, numberRuns):
        X = np.zeros((numberKnappsacks, numberItems), dtype=np.int)
        betterThanGreedy[b] = nearestNeighborhood()
        result[b] = utility()
    print("Neighborhood made it " + str(np.mean(betterThanGreedy)) + " mean value better over " + str(numberRuns) + " runs")
    print("Max 5")
    max = np.sort(result)
    for i in range(max.shape[0] - 5, max.shape[0]):
        print(max[i])
    plt.hist(result, numberRuns)
    plt.xlabel("Value for " + str(numberKnappsacks) + " knapsacks")
    plt.ylabel("Number of times packed out of " + str(numberRuns) + " ")
    plt.show()

if opt == 10:
    setup(5, 30, 60, 100, 1, 100, 2, 10)
    numberRuns = 100
    result = np.zeros(numberRuns)
    betterThanGreedy = np.zeros(numberRuns)
    for b in range(0, numberRuns):
        X = np.zeros((numberKnappsacks, numberItems), dtype=np.int)
        betterThanGreedy[b] = nearestNeighborhood()
        result[b] = utility()
    print("Neighborhood made it "+str(np.mean(betterThanGreedy))+" mean value better over "+str(numberRuns)+" runs")
    plt.hist(result)
    plt.show()
