import tensorflow as tf
import matplotlib.pyplot as plt
import numpy as np
import pandas as pd

# traningData= np.array(pd.read_csv('trainingData.txt', header = None))
# classTrainingDataY = traningData[:,0]
# classTrainingDataY = np.transpose(classTrainingDataY)
# trainingTuplesX = traningData[:, 1:]
# testData = np.array(pd.read_csv('testData.txt', header = None))
# classTestData = testData[:,0]
# testTuples = testData[:,1:]
df=pd.read_csv('trainingData.txt',usecols = [1,2,3,4,5,6,7,8,1],skiprows = [0],header=None)
d = df.values
l = pd.read_csv('trainingData.txt',usecols = [0] ,header=None)
labels = l.values
data = np.array(d,'float32')
labels = np.array(l,'int32')
print(data)
#tensorflow
x = tf.placeholder(tf.float32,shape=(20931,9))
x = data
w = tf.random_normal([100,20931],mean=0.0, stddev=1.0, dtype=tf.float32)
y = tf.nn.softmax(tf.matmul(w,x))
with tf.Session() as sess:
    print(sess.run(y))