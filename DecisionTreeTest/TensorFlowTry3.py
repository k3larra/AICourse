from __future__ import absolute_import
from __future__ import division
from __future__ import print_function

import tensorflow as tf
from sklearn.ensemble import forest
from tensorflow import feature_column as fc
import matplotlib.pyplot as plt
import numpy as np
import pandas as pd
print(tf.VERSION)
tf.logging.set_verbosity(tf.logging.INFO)
#init = tf.global_variables_initializer
def main():
    training_set = tf.contrib.learn.datasets.base.load_csv_without_header(
        filename='trainingData.txt',
        target_dtype=np.int,
        features_dtype=np.int,
        target_column=0)

    test_set = tf.contrib.learn.datasets.base.load_csv_without_header(
        filename='testData.txt',
        target_dtype=np.int,
        features_dtype=np.int,
        target_column=0)

    def input_fn_train():  # method used to deliver the training data
        x = {
                "blinkyE":tf.convert_to_tensor(training_set.data),
                 "inkyE": tf.convert_to_tensor(training_set.data),
                 "pinkyE": tf.convert_to_tensor(training_set.data),
                 "sueE": tf.convert_to_tensor(training_set.data),
                 "blinkyDist": tf.convert_to_tensor(training_set.data),
                 "inkyDist": tf.convert_to_tensor(training_set.data),
                 "pinkyDist": tf.convert_to_tensor(training_set.data),
                 "sueDist": tf.convert_to_tensor(training_set.data)
             }
        y = tf.convert_to_tensor(training_set.target)
        return x, y

    def input_fn_test():
        x = {
            "blinkyE": tf.convert_to_tensor(test_set.data),
            "inkyE": tf.convert_to_tensor(test_set.data),
            "pinkyE": tf.convert_to_tensor(test_set.data),
            "sueE": tf.convert_to_tensor(test_set.data),
            "blinkyDist": tf.convert_to_tensor(test_set.data),
            "inkyDist": tf.convert_to_tensor(test_set.data),
            "pinkyDist": tf.convert_to_tensor(test_set.data),
            "sueDist": tf.convert_to_tensor(test_set.data)
        }
        y = tf.convert_to_tensor(test_set.target)
        return x,y

    #Describe the feature columns
    a1 =fc.embedding_column(fc.categorical_column_with_identity(key="blinkyE",num_buckets=2,default_value=0),dimension=1)
    b1 =fc.embedding_column(fc.categorical_column_with_identity(key="inkyE",num_buckets=2,default_value=0),1)
    c1 =fc.embedding_column(fc.categorical_column_with_identity(key="pinkyE",num_buckets=2,default_value=0),1)
    d1 =fc.embedding_column(fc.categorical_column_with_identity(key="sueE",num_buckets=2,default_value=0),1)
    e1 =fc.embedding_column(fc.categorical_column_with_identity(key="blinkyDist",num_buckets=5,default_value=0),1)
    f1 =fc.embedding_column(fc.categorical_column_with_identity(key="inkyDist",num_buckets=5,default_value=0),1)
    g1 =fc.embedding_column(fc.categorical_column_with_identity(key="pinkyDist",num_buckets=5,default_value=0),1)
    h1 =fc.embedding_column(fc.categorical_column_with_identity(key="sueDist",num_buckets=5,default_value=0),1)

    categorical_columns = set([a1,b1,c1,d1,e1,f1,g1,h1])
    estimator = tf.estimator.DNNClassifier(feature_columns=categorical_columns,
                                             hidden_units=[24,50,24],
                                             n_classes=6,
                                             model_dir="/tmp/a8_model"
                                            )

    #Fit model
    print("Train: ")
    estimator.train(input_fn=input_fn_train,max_steps=20000)
    print("Fitted! : ")
    print("Evaluate using a test set: ")
    evaluate = estimator.evaluate(input_fn=input_fn_test,steps=1)
    print(evaluate)

    sess = tf.Session()
    sess.run(tf.global_variables_initializer())

    # save the model
    feature_spec = tf.feature_column.make_parse_example_spec(categorical_columns)
    tfrecord_serving_input_fn = tf.estimator.export.build_parsing_serving_input_receiver_fn(feature_spec)
    path=estimator.export_savedmodel(export_dir_base="/tmp/a8_model/saved", serving_input_receiver_fn = tfrecord_serving_input_fn, as_text=True)
    print(path)
if __name__ == "__main__":
    main()