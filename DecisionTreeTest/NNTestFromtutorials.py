from __future__ import absolute_import
from __future__ import division
from __future__ import print_function

import tensorflow as tf
import numpy as np

# Data sets
IRIS_TRAINING = "iris_training.csv"
IRIS_TEST = "iris_test.csv"
print(tf.VERSION)
tf.logging.set_verbosity(tf.logging.INFO)

# Load datasets.
training_set = tf.contrib.learn.datasets.base.load_csv_with_header(
    filename=IRIS_TRAINING,
    target_dtype=np.int,
    features_dtype=np.float32)
test_set = tf.contrib.learn.datasets.base.load_csv_with_header(
    filename=IRIS_TEST,
    target_dtype=np.int,
    features_dtype=np.float32)

# Specify that all features have real-value data
feature_columns = [tf.contrib.layers.real_valued_column("", dimension=4)]
print(feature_columns)
for i, val in enumerate(feature_columns):
    print(i)
    print(val)
# Build 3 layer DNN with 10, 20, 10 units respectively.
validation_monitor = tf.contrib.learn.monitors.ValidationMonitor(
    test_set.data,
    test_set.target,
    every_n_steps=50)
classifier = tf.contrib.learn.DNNClassifier(feature_columns=feature_columns,
                                            hidden_units=[10, 20, 10],
                                            n_classes=3,
                                            model_dir="/tmp/q_model",
                                            config=tf.contrib.learn.RunConfig(
                                                save_checkpoints_secs=1)
                                            )
print("TJoHo")
def input_fn_train(): # returns x, y (where y represents label's class index).
    x = tf.convert_to_tensor(training_set.data)
    y = tf.convert_to_tensor(training_set.target)
    return x,y
# Fit model.
print("Klopp")
classifier.fit(x=training_set.data,
               y=training_set.target,
               steps=2000,
               monitors=[validation_monitor])


print("HoHo")
def input_fn_test(): # returns x, y (where y represents label's class index).
    x = tf.convert_to_tensor(test_set.data)
    y = tf.convert_to_tensor(test_set.target)
    return x,y
# Evaluate accuracy.
evaluate = classifier.evaluate(input_fn=input_fn_test,steps=1)
print(evaluate)
print(evaluate["accuracy"])
#accuracy_score = classifier.evaluate(x=test_set.data,y=test_set.target)["accuracy"]

print('Accuracy: {0:f}'.format(evaluate["accuracy"]))

# Classify two new flower samples.
def new_samples():
  return np.array(
    [[6.4, 3.2, 4.5, 1.5],
     [5.8, 3.1, 5.0, 1.7]], dtype=np.float32)

y = list(classifier.predict_classes(input_fn=new_samples))
print('Predictions: {}'.format(str(y)))