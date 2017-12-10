from __future__ import absolute_import
from __future__ import division
from __future__ import print_function

import os

import numpy as np
import tensorflow as tf

# Data sets
IRIS_TRAINING = "iris_training.csv"

ROOT_DIR = "ml_pendlaren"
MODEL_NAME = "v01"

def main():
  print(tf.VERSION)
  #tf.logging.set_verbosity(tf.logging.INFO)

  # Load datasets.
  training_set = tf.contrib.learn.datasets.base.load_csv_with_header(
      filename=IRIS_TRAINING,
      target_dtype=np.int,
      features_dtype=np.float32)

  # Specify that all features have real-value data
  feature_columns = [tf.feature_column.numeric_column(key="inputs",default_value=[[0,0,0,0]], shape=(1,4),dtype=tf.float32)]
  # Build 3 layer DNN with 10, 20, 10 units respectively.
  classifier = tf.contrib.learn.DNNClassifier(feature_columns=feature_columns,
                                              hidden_units=[10, 20, 10],
                                              n_classes=3,
                                              model_dir="tmp/iris_model")

  # Define the training inputs
  train_input_fn = tf.estimator.inputs.numpy_input_fn(
      x={"inputs": np.array(training_set.data)},
      y=np.array(training_set.target),
      num_epochs=None,
      shuffle=True)

  # Fit model.
  classifier.fit(input_fn=train_input_fn, steps=2000)


  def serving_input_fn():
      inputs = {"inputs": tf.placeholder(dtype=tf.float32,name="inputs_placeholder",shape=(1,4))}
      return tf.contrib.learn.InputFnOps(features=inputs, labels=None, default_inputs=inputs)

  export_dir = os.path.join(ROOT_DIR, MODEL_NAME)
  classifier.export_savedmodel(export_dir_base=export_dir, serving_input_fn=serving_input_fn)

if __name__ == "__main__":
    main()