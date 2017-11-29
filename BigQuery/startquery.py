import tensorflow as tf
import time
import numpy as np
from tensorflow.contrib.cloud.python.ops import bigquery_reader_ops
print(tf.VERSION)
tf.logging.set_verbosity(tf.logging.INFO)
PROJECT="skanependlaren"
DATASET="commuting"
TABLE="travelsearch"
TIME= int(round(time.time() * 1000))
NUM_PARTITIONS=1
#TIME=3600000*24 ##Read more here https://cloud.google.com/bigquery/table-decorators
 ##Read more here https://cloud.google.com/bigquery/table-decorators
#https://www.tensorflow.org/api_docs/python/tf/contrib/cloud/BigQueryReader
#https://stackoverflow.com/questions/45795761/reading-data-with-bigqueryreader-in-tensorflow
#https://stackoverflow.com/questions/44580308/tensorflow-hangs-with-high-cpu-usage-for-training-a-very-small-forest
#client = bigquery.Client(project="skanependlaren")
#dataset = client.dataset("commuting")
#table = dataset.table("travelsearch")
sess = tf.Session() #can be removed when estimator#

def input_fn_from_bigquery():
    features = dict(
        uid=tf.FixedLenFeature([1], tf.string),
        startStation=tf.FixedLenFeature([1], tf.string),
        endStation=tf.FixedLenFeature([1], dtype=tf.string, default_value="USA"),
        longitude=tf.FixedLenFeature([1], tf.string),
        latitude=tf.FixedLenFeature([1], tf.string),
        locationAccuracy=tf.FixedLenFeature([1], tf.string),
        timeLocationDetected=tf.FixedLenFeature([1], tf.string),
        detectedActivity=tf.FixedLenFeature([1], tf.string),
        detectedActivityINT=tf.FixedLenFeature([1], tf.string),
        detectedActivityINT2=tf.FixedLenFeature([1], tf.int64, default_value=0),
        locationAccuracyINT2=tf.FixedLenFeature([1], tf.int64,default_value=1),
    )
    # Create the parse_examples list of features.
    training_data = dict(
        startStation=tf.FixedLenFeature([1], dtype=tf.string),
        endStation=tf.FixedLenFeature([1], dtype=tf.string, default_value="USA"),
        longitude=tf.FixedLenFeature([1], tf.string),
        latitude=tf.FixedLenFeature([1], tf.string),
        locationAccuracy=tf.FixedLenFeature([1], tf.string),
        timeLocationDetected=tf.FixedLenFeature([1], tf.string),
        detectedActivityINT=tf.FixedLenFeature([1],tf.string),
        detectedActivityINT2 = tf.FixedLenFeature([1], tf.int64, default_value=2),
        locationAccuracyINT2 = tf.FixedLenFeature([1], tf.int64, default_value=3),
    )

    # Create the parse_examples list of features.
    label = dict(
        #endStation=tf.FixedLenFeature([1], tf.string),
        #endStation=tf.FixedLenFeature([1], tf.string),
        locationAccuracyINT2=tf.FixedLenFeature([1], tf.int64, default_value=3),
    )

    # Create a Reader.
    reader = bigquery_reader_ops.BigQueryReader(project_id=PROJECT,
                                                dataset_id=DATASET,
                                                table_id=TABLE,
                                                timestamp_millis=TIME,
                                                num_partitions=NUM_PARTITIONS,
                                                features=features)
    # Populate a queue with the BigQuery Table partitions.

    queue = tf.train.string_input_producer(reader.partitions())

    tf.train.start_queue_runners(sess) ##Remove later
    row_id, examples_serialized = reader.read_up_to(queue, 100)  ##OK then we get a vector
    features = tf.parse_example(examples_serialized, features=training_data)
    labels = tf.parse_example(examples_serialized, features=label)
    return features, labels["locationAccuracyINT2"]


features1, labels1 = input_fn_from_bigquery()
print(sess.run(features1["locationAccuracy"]))
print(sess.run(features1["locationAccuracyINT2"]))
print(sess.run(features1["detectedActivityINT"]))
print(sess.run(features1["detectedActivityINT2"]))
#print(sess.run(features1["startStation"]))
#print(sess.run(labels1))
print(sess.run(labels1))




