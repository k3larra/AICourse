import tensorflow as tf
import time
from tensorflow.contrib.cloud.python.ops import bigquery_reader_ops
import numpy as np
from numpy import genfromtxt
import pandas as pd
print(tf.VERSION)
tf.logging.set_verbosity(tf.logging.INFO)
def main():


    #def training_set3(file_name_queue):
    file_name_queue = tf.train.string_input_producer(["data.txt"], num_epochs=4, shuffle=False)
    reader = tf.TextLineReader()
    key, value = reader.read(file_name_queue)

    # Default values, in case of empty columns. Also specifies the type of the
    # decoded result.
    record_defaults = [["usa"],["crap"]]
    x,y = tf.decode_csv(value, record_defaults=record_defaults)
    features = tf.stack([x])
    labels = tf.stack([y])

    with tf.Session() as sess:
        sess.run(tf.global_variables_initializer())
        sess.run(tf.local_variables_initializer())
        #     #init_op = tf.group(tf.global_variables_initializer(), tf.local_variables_initializer())
        coord = tf.train.Coordinator()
        threads = tf.train.start_queue_runners(coord=coord)
        a=tf.feature_column.embedding_column(tf.feature_column.categorical_column_with_vocabulary_list(key='startStation', vocabulary_list=('KristianstadC', 'LundC', 'YstadC', 'DegebergaSkolan', 'HelsingborgC'), default_value=0),dimension=1)
        b=tf.feature_column.embedding_column(tf.feature_column.categorical_column_with_identity(key='startStation', num_buckets=4, default_value=0),dimension=1)
        c = tf.feature_column.embedding_column(
            tf.feature_column.categorical_column_with_identity(key='cartStation', num_buckets=4, default_value=0),
            dimension=1)
        categorical_columns=set([a])
        estimator = tf.estimator.DNNClassifier(feature_columns=categorical_columns,
                                                hidden_units=[10,10],
                                                n_classes=4,
                                                model_dir="tmpen/try_model3")
        print("Train")
        estimator.train(input_fn=sess.run([features,labels]),max_steps=2)
        print("done")
        coord.request_stop()
        coord.join(threads)

    #
        # #tf.global_variables_initializer().run()
        # sess.run(tf.global_variables_initializer())
        # sess.run(tf.local_variables_initializer())
        # #init_op = tf.group(tf.global_variables_initializer(), tf.local_variables_initializer())
        # coord = tf.train.Coordinator()
        # threads = tf.train.start_queue_runners(coord=coord)
        # for i in range(1200):   ##Then ndont use epocs
        #     x_data, y_data = sess.run([x, y])
        #     print(x_data, y_data)
        # while True:
        #     try:
        #         x_data, y_data = sess.run([features,y])
        #         print("x:",x_data)
        #         print("y:",y_data)
        #     except tf.errors.OutOfRangeError:
        #         print("out")
        #         break
    #feat = training_set3()
    #print(feat)
    #print(sess.run(feat))


    # training_set = tf.contrib.learn.datasets.base.load_csv_without_header(
    #     filename='data.txt',
    #     target_dtype=np.string_,
    #     features_dtype=np.string_,
    #     target_column=0)

    #print(training_set.target[0])

    # training_set2 = tf.contrib.learn.datasets.base.load_csv_without_header(
    #     filename='data2.txt',
    #     target_dtype=np.int,
    #     features_dtype=np.int,
    #     target_column=0)


    # def input_fn_train():
    #     x={
    #         "startStation":tf.convert_to_tensor(training_set.data)
    #     }
    #     y=tf.convert_to_tensor(training_set.target)
    #     return x,y

    #


if __name__ == "__main__":
    main()



