import tensorflow as tf
import time
from tensorflow.contrib.cloud.python.ops import bigquery_reader_ops

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
#sess = tf.Session() #can be removed when estimator#

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
    )
    # Create the parse_examples list of features.
    training_data = dict(
        endStation=tf.FixedLenFeature([1], dtype=tf.string, default_value="USA"),
        #longitude=tf.FixedLenFeature([1], tf.string),
        #latitude=tf.FixedLenFeature([1], tf.string),
        #locationAccuracy=tf.FixedLenFeature([1], tf.string),
        #timeLocationDetected=tf.FixedLenFeature([1], tf.string),
    )

    # Create the parse_examples list of features.
    label = dict(
        startStation=tf.FixedLenFeature([1], tf.string)
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

    #tf.train.start_queue_runners(sess) ##Remove later
    row_id, examples_serialized = reader.read_up_to(queue, 100)  ##OK then we get a vector
    examples = tf.parse_example(examples_serialized, features=training_data)
    labels = tf.parse_example(examples_serialized, features=label)
    return examples,labels


#examples1, labels1 = input_fn_from_bigquery()

#print(sess.run(examples1["endStation"]))
#print(sess.run(labels1["startStation"]))

a=tf.feature_column.embedding_column(tf.feature_column.categorical_column_with_vocabulary_list(key='startStation', vocabulary_list=('Malmo C', 'Lund C', 'Ystad C', 'Malmo On', 'Helsingborg C'), default_value=0),dimension=1)
categorical_columns=set([a])
estimator = tf.estimator.DNNClassifier(feature_columns=categorical_columns,
                                       hidden_units=[10,10],
                                       n_classes=6,
                                       model_dir="tmpen/try2_model")
print("Train")
estimator.train(input_fn=input_fn_from_bigquery(),max_steps=10)






