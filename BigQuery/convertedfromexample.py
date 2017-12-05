from __future__ import absolute_import
from __future__ import division
from __future__ import print_function

import os
from six.moves.urllib.request import urlopen

import numpy as np
import tensorflow as tf

# Data sets
IRIS_TRAINING = "data.txt"

IRIS_TEST = "data2.txt"

def main():

  # Load datasets.
  training_set = tf.contrib.learn.datasets.base.load_csv_with_header(
      filename=IRIS_TRAINING,
      target_dtype=np.int,
      features_dtype=np.int)
  test_set = tf.contrib.learn.datasets.base.load_csv_with_header(
      filename=IRIS_TEST,
      target_dtype=np.int,
      features_dtype=np.int)

  # Specify that all features have real-value data
  feature_columns = [tf.feature_column.numeric_column("x", shape=[1])]

  # Build 3 layer DNN with 10, 20, 10 units respectively.
  estimator = tf.estimator.DNNClassifier(feature_columns=feature_columns,
                                          hidden_units=[10, 20, 10],
                                          n_classes=3,
                                          model_dir="/tmp/iris4_model")
  # Define the training inputs
  train_input_fn = tf.estimator.inputs.numpy_input_fn(
      x={"x": np.array(training_set.data)},
      y=np.array(training_set.target),
      num_epochs=None,
      shuffle=True)

  # Train model.
  estimator.train(input_fn=train_input_fn, steps=2000)

  # Define the test inputs
  test_input_fn = tf.estimator.inputs.numpy_input_fn(
      x={"x": np.array(test_set.data)},
      y=np.array(test_set.target),
      num_epochs=1,
      shuffle=False)

  # Evaluate accuracy.
  accuracy_score = estimator.evaluate(input_fn=test_input_fn)["accuracy"]
  #
  print("\nTest Accuracy: {0:f}\n".format(accuracy_score))
  #
  # Classify two new flower samples.
  new_samples = np.array(
      [[2],
       [2],
       [3]], dtype=np.float32)
  predict_input_fn = tf.estimator.inputs.numpy_input_fn(
      x={"x": new_samples},
      num_epochs=1,
      shuffle=False)
  #
  predictions = list(estimator.predict(input_fn=predict_input_fn))
  predicted_classes = [p["classes"] for p in predictions]

  for i in predictions:
      print(i["classes"][0])

  print(
          "New Samples, Class Predictions:    {}\n"
          .format(predicted_classes))

if __name__ == "__main__":
    main()