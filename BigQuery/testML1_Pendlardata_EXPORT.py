import tensorflow as tf
import time
import numpy as np
import os
import sys

from tensorflow.contrib.cloud.python.ops import bigquery_reader_ops
print(tf.VERSION)
tf.logging.set_verbosity(tf.logging.INFO)
PROJECT="skanependlaren"
DATASET="commuting"
TABLE="travelsearch"
TIME= int(round(time.time() * 1000))
NUM_PARTITIONS=1

def input_fn_from_bigquery():
    features = dict(
        uid=tf.FixedLenFeature([1], tf.string),
        startStation=tf.FixedLenFeature([1], tf.string),
        endStation=tf.FixedLenFeature([1], dtype=tf.string, default_value="USA"),
        longitude=tf.FixedLenFeature([1], tf.string),
        latitude=tf.FixedLenFeature([1], tf.string),
        locationAccuracy=tf.FixedLenFeature([1], tf.string),
        locationAccuracyINT2=tf.FixedLenFeature([1], tf.int64),
        timeLocationDetected=tf.FixedLenFeature([1], tf.string),
        detectedActivity=tf.FixedLenFeature([1], tf.string),
        detectedActivityINT=tf.FixedLenFeature([1], tf.string),
        detectedActivityINT2=tf.FixedLenFeature([1], tf.int64, default_value=0),
        longitudeFloat=tf.FixedLenFeature([1], tf.float32,default_value=12.0),
        latitudeFloat=tf.FixedLenFeature([1], tf.float32, default_value=14.0),
        detectedActivityConfidenceINT=tf.FixedLenFeature([1], tf.int64, default_value=5),
        timeLocationDetectedINT=tf.FixedLenFeature([1], tf.int64, default_value=6),
    )
    # Create the parse_examples list of features.
    training_data = dict(
        locationAccuracyINT2=tf.FixedLenFeature([1], tf.int64, default_value=3),
        longitudeFloat=tf.FixedLenFeature([1], tf.float32, default_value=12.0),
        latitudeFloat=tf.FixedLenFeature([1], tf.float32, default_value=14.0),
        detectedActivityConfidenceINT=tf.FixedLenFeature([1], tf.int64, default_value=5),
        timeLocationDetectedINT=tf.FixedLenFeature([1], tf.int64, default_value=6),
    )
    # Create the parse_examples list of features.
    label = dict(
        detectedActivityINT2=tf.FixedLenFeature([1], tf.int64, default_value=2)
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
    row_id, examples_serialized = reader.read_up_to(queue, 50)  ##OK then we get a vector
    features = tf.parse_example(examples_serialized, features=training_data)
    labels = tf.parse_example(examples_serialized, features=label)
    return features, labels["detectedActivityINT2"]
#
#
#
feature_columns = [tf.feature_column.numeric_column("locationAccuracyINT2", shape=[1]),
                    tf.feature_column.numeric_column("longitudeFloat", shape=[1]),
                    tf.feature_column.numeric_column("latitudeFloat", shape=[1]),
                    tf.feature_column.numeric_column("detectedActivityConfidenceINT", shape=[1]),
                   tf.feature_column.numeric_column("timeLocationDetectedINT", shape=[1])]

estimator = tf.estimator.DNNClassifier(feature_columns=feature_columns,
                                       hidden_units=[10,10],
                                       n_classes=5,
                                       model_dir="tmpen/try4_model")

def save_tf_learn_model(estimator, model_name, export_dir, feature_columns, ):
    #feature_spec = tf.contrib.layers.create_feature_spec_for_parsing(feature_columns)
    feature_spec = {"locationAccuracyINT2": tf.FixedLenFeature(dtype=tf.int64, shape=[1], default_value=5),
                      "longitudeFloat": tf.FixedLenFeature(dtype=tf.float32, shape=[1], default_value=5),
                      "latitudeFloat": tf.FixedLenFeature(dtype=tf.float32, shape=[1], default_value=5),
                      "detectedActivityConfidenceINT": tf.FixedLenFeature(dtype=tf.int64, shape=[1], default_value=5),
                      "timeLocationDetectedINT": tf.FixedLenFeature(dtype=tf.int64, shape=[1], default_value=5)}
    serving_input_fn = tf.estimator.export.build_parsing_serving_input_receiver_fn(feature_spec)
    export_dir = os.path.join(export_dir, model_name)
    estimator.export_savedmodel(export_dir, serving_input_fn)
    print("Done exporting tf.learn model to " + export_dir + "!")


save_tf_learn_model(estimator,"try2","tmpen/try4_model",feature_columns)

# SESS_DICT = {}
# def get_session(model_id):
#     global SESS_DICT
#     config = tf.ConfigProto(allow_soft_placement=True)
#     SESS_DICT[model_id] = tf.Session(config=config)
#     return SESS_DICT[model_id]
#
# def load_tf_model(model_path):
#     sess = get_session(model_path)
#     tf.saved_model.loader.load(sess, [tf.saved_model.tag_constants.SERVING], model_path)
#     return sess
#
#
# estimator = load_tf_model("tmpen/try4_model")


##Here train models for all individual users.
##If they are new users perhaps use a pretrained model
print("Train")
estimator.train(input_fn=input_fn_from_bigquery,max_steps=50)

#Export all models trained in this batch

##
print("Evaluate accuracy (Create test set).")
#Here the model has to be evaluated against real data.
accuracy_score = estimator.evaluate(input_fn=input_fn_from_bigquery,steps=2)["accuracy"]
print("\nTest Accuracy: {0:f}\n".format(accuracy_score))
#

# #Export
# tf.app.flags.DEFINE_integer('model_version', 1, 'version number of the model.')
# tf.app.flags.DEFINE_string('work_dir', '/tmp/exportmodel', 'Working directory.')
# FLAGS = tf.app.flags.FLAGS
# print(FLAGS.model_version)
# print(FLAGS.work_dir)
#
# export_path_base = sys.argv[-1]
# export_path = os.path.join(
#       tf.compat.as_bytes(export_path_base),
#       tf.compat.as_bytes(str(FLAGS.model_version)))
# print 'Exporting trained model to', export_path
#
# #builder = tf.saved_model.builder.SavedModelBuilder(export_path)
#
# def serving_input_receiver_fn():
#     feature_spec = {INPUT_TENSOR_NAME: tf.FixedLenFeature(dtype=tf.float32, shape=[4])}
#     return tf.estimator.export.build_parsing_serving_input_receiver_fn(feature_spec)()
#

#https://github.com/tensorflow/serving/issues/488
# INPUT_TENSOR_NAME = 'inputs'
#
#
# def estimator(model_path):
#     feature_columns = [tf.feature_column.numeric_column(INPUT_TENSOR_NAME, shape=[4])]
#     return tf.estimator.DNNClassifier(feature_columns=feature_columns,
#                                       hidden_units=[10, 20, 10],
#                                       n_classes=3,
#                                       model_dir=model_path)
#
#
# def serving_input_receiver_fn():
#     feature_spec = {INPUT_TENSOR_NAME: tf.FixedLenFeature(dtype=tf.float32, shape=[4])}
#     return tf.estimator.export.build_parsing_serving_input_receiver_fn(feature_spec)()
#############
# def save_tf_learn_model(estimator, model_name, export_dir, feature_columns, ):
#     feature_spec = create_feature_spec_for_parsing(feature_columns)
#     serving_input_fn = input_fn_utils.build_parsing_serving_input_fn(feature_spec)
#     export_dir = os.path.join(export_dir, model_name)
#     estimator.export_savedmodel(export_dir, serving_input_fn)
#     print("Done exporting tf.learn model to " + export_dir + "!")
#
# SESS_DICT = {}
# def get_session(model_id):
#     global SESS_DICT
#     config = tf.ConfigProto(allow_soft_placement=True)
#     SESS_DICT[model_id] = tf.Session(config=config)
#     return SESS_DICT[model_id]
#
# def load_tf_model(model_path):
#     sess = get_session(model_path)
#     tf.saved_model.loader.load(sess, [tf.saved_model.tag_constants.SERVING], model_path)
#     return sess

