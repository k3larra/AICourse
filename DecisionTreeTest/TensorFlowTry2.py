from __future__ import absolute_import
from __future__ import division
from __future__ import print_function
import tensorflow as tf
import matplotlib.pyplot as plt
import numpy as np
import pandas as pd
print(tf.VERSION)

training_set = tf.contrib.learn.datasets.base.load_csv_without_header(
    filename='trainingData.txt',
    target_dtype=np.int,
    features_dtype=np.float32,
    target_column=0)

test_set = tf.contrib.learn.datasets.base.load_csv_without_header(
    filename='testData.txt',
    target_dtype=np.int,
    features_dtype=np.float32,
    target_column=0)

print("The training set set: ")
def input_fn_train():  # method used to deliver the training data
    x = tf.convert_to_tensor(training_set.data)
    y = tf.convert_to_tensor(training_set.target)
    return x, y

print("Evaluate using a test set: ")
def input_fn_test():
    x = tf.convert_to_tensor(test_set.data)
    y = tf.convert_to_tensor(test_set.target)
    return x,y

# Specify that all features have real-value data
#feature_columns = [tf.feature_column.numeric_column("x", shape=[8])]
feature_columns = [tf.contrib.layers.real_valued_column("", dimension=4)]
classifier = tf.contrib.learn.DNNClassifier(feature_columns=feature_columns,
                                         hidden_units=[24,50,24],
                                         n_classes=6,
                                         model_dir="/tmp/a4_model")



#Fit model
classifier.fit(input_fn=input_fn_train,max_steps=20000)

evaluate = classifier.evaluate(input_fn=input_fn_test,steps=1)
print(evaluate)
print(evaluate["accuracy"])

print("Test with 2 values and predicition: ")
def new_samples():
  x = np.array([[0,1,1,1,2,4,2,4],[1,0,1,0,4,0,5,0]], dtype=np.float32)
  return x
y = list(classifier.predict_classes(input_fn=new_samples))
print('Predictions: {}'.format(str(y)))