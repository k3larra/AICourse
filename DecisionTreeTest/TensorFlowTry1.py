from __future__ import absolute_import
from __future__ import division
from __future__ import print_function
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
df=pd.read_csv('trainingData.txt',usecols = [1,2,3,4,5,6,7,8],header=None)
d = df.values

lf = pd.read_csv('trainingData.txt',usecols = [0] ,header=None)
l = lf.values

dataset = np.array(d,'str')
labels = np.array(l,'int64')

# Specify that all features have real-value data
#feature_columns = [tf.feature_column.numeric_column("x", shape=[8])]
#feature_columns = [tf.contrib.layers.real_valued_column("", dimension=8)]

a = tf.contrib.layers.sparse_column_with_keys(column_name="a",keys=["0", "1"])
b = tf.contrib.layers.sparse_column_with_keys(column_name="b",keys=["0", "1"])
c = tf.contrib.layers.sparse_column_with_keys(column_name="c",keys=["0", "1"])
d = tf.contrib.layers.sparse_column_with_keys(column_name="d",keys=["0", "1"])
e = tf.contrib.layers.sparse_column_with_keys(column_name="e",keys=["0", "1","2","3","4","5"])
f = tf.contrib.layers.sparse_column_with_keys(column_name="f",keys=["0", "1","2","3","4","5"])
g = tf.contrib.layers.sparse_column_with_keys(column_name="g",keys=["0", "1","2","3","4","5"])
h = tf.contrib.layers.sparse_column_with_keys(column_name="h",keys=["0", "1","2","3","4","5"])
a1 = tf.contrib.layers.embedding_column(a,dimension=8)
b1 = tf.contrib.layers.embedding_column(b,dimension=8)
c1 = tf.contrib.layers.embedding_column(c,dimension=8)
d1 = tf.contrib.layers.embedding_column(d,dimension=8)
e1 = tf.contrib.layers.embedding_column(e,dimension=8)
f1 = tf.contrib.layers.embedding_column(f,dimension=8)
g1 = tf.contrib.layers.embedding_column(g,dimension=8)
h1 = tf.contrib.layers.embedding_column(h,dimension=8)
categorical_columns = [a1,b1,c1,d1,e1,f1,g1,h1]
# Build 3 layer DNN with 10, 20, 10 units respectively.
classifier = tf.estimator.DNNClassifier(feature_columns=categorical_columns,
                                        hidden_units=[10, 20, 10],
                                        n_classes=10,
                                        model_dir="/tmp/model-a")


# Define the training inputs
train_input_fn = tf.estimator.inputs.numpy_input_fn(
    x={"a":np.array(dataset),"b":np.array(dataset),"c":np.array(dataset),"d":np.array(dataset),
       "e": np.array(dataset),"f":np.array(dataset),"g":np.array(dataset),"h":np.array(dataset)},
    y=labels,
    num_epochs=None,
    shuffle=True)

# Train model.
classifier.train(input_fn=train_input_fn, steps=2000)

mymat = tf.Variable([["0","1","0","1","3","4","5","5"],["0","1","0","1","3","4","5","5"]], tf.string)
def new_samples():
    return np.array([["0","1","0","1","3","4","5","5"],["0","1","0","1","3","4","5","5"]],'str')
y = list(classifier.predict(input_fn=new_samples))
print('Predictions: {}'.format(str(y)))
#tensorflow
#x = tf.placeholder(tf.float32,shape=(20931,9))
#x = data
#w = tf.random_normal([100,20931],mean=0.0, stddev=1.0, dtype=tf.float32)
#y = tf.nn.softmax(tf.matmul(w,x))
#with tf.Session() as sess:
#    print(sess.run(y))