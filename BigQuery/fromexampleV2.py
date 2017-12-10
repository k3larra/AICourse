from __future__ import absolute_import
from __future__ import division
from __future__ import print_function

import os
from six.moves.urllib.request import urlopen

import numpy as np
import tensorflow as tf

# Data sets
IRIS_TRAINING = "iris_training.csv"
IRIS_TEST = "iris_test.csv"
ROOT_DIR = "tmp/iris_new_model"
MODEL_NAME = "try8"
INPUT_TENSOR_NAME = 'inputs'


def train_input_fn(training_dir, training_file_name):
    training_set = tf.contrib.learn.datasets.base.load_csv_with_header(
        filename=os.path.join(training_dir, training_file_name),
        target_dtype=np.int,
        features_dtype=np.float32)

    return tf.estimator.inputs.numpy_input_fn(
        x={INPUT_TENSOR_NAME: np.array(training_set.data)},
        y=np.array(training_set.target),
        num_epochs=None,
        shuffle=True)


def test_input_fn(training_dir, test_file_name):
    training_set = tf.contrib.learn.datasets.base.load_csv_with_header(
        filename=os.path.join(training_dir, test_file_name),
        target_dtype=np.int,
        features_dtype=np.float32)

    return tf.estimator.inputs.numpy_input_fn(
        x={INPUT_TENSOR_NAME: np.array(training_set.data)},
        y=np.array(training_set.target),
        num_epochs=2,
        shuffle=True)


def estimatorCreator(model_path):
    feature_columns = [tf.feature_column.numeric_column(INPUT_TENSOR_NAME, shape=[4])]
    return tf.estimator.DNNClassifier(feature_columns=feature_columns,
                                      hidden_units=[10, 20, 10],
                                      n_classes=3,
                                      model_dir=model_path)


def serving_input_receiver_fn():
    feature_spec = {INPUT_TENSOR_NAME: tf.FixedLenFeature(dtype=tf.float32, shape=[4])}
    return tf.estimator.export.build_parsing_serving_input_receiver_fn(feature_spec)()


def main():
    print(tf.VERSION)
    tf.logging.set_verbosity(tf.logging.INFO)
    estimator = estimatorCreator(ROOT_DIR)
    print("Train model")
    estimator.train(input_fn=train_input_fn(ROOT_DIR,IRIS_TRAINING), steps=2000)
    print("Evaluate model")
    accuracy_score = estimator.evaluate(input_fn=test_input_fn(ROOT_DIR,IRIS_TEST))["accuracy"]
    print("Test Accuracy: {0:f}".format(accuracy_score))
    print("Make prediction")
    # Classify two new flower samples.
    new_samples = np.array(
        [[6.4,3.2,4.5,1.5],
         [5.8,3.1,5.0,1.7],
         [8.8,9.1,3.0,1.1],
         [5.4,3.4,1.7,0.2]], dtype=np.float32)
    predict_input_fn = tf.estimator.inputs.numpy_input_fn(
        x={"inputs": new_samples},
        num_epochs=1,
        shuffle=False)
    predictions = list(estimator.predict(input_fn=predict_input_fn))
    predicted_classes = [p["classes"] for p in predictions]
    print("New Samples, Class Predictions:    {}".format(predicted_classes))
    #Save model
    export_dir = os.path.join(ROOT_DIR, MODEL_NAME)
    estimator.export_savedmodel(export_dir, serving_input_receiver_fn)
    
if __name__ == "__main__":
    main()