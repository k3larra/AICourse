import tensorflow as tf
print(tf.VERSION)
tf.logging.set_verbosity(tf.logging.INFO)

SESS_DICT = {}
def get_session(model_id):
    global SESS_DICT
    config = tf.ConfigProto(allow_soft_placement=True)
    SESS_DICT[model_id] = tf.Session(config=config)
    return SESS_DICT[model_id]

def load_tf_model(model_path):
    sess = get_session(model_path)
    tf.saved_model.loader.load(sess, [tf.saved_model.tag_constants.SERVING], model_path)
    return sess

sess=load_tf_model("tmpen/try4_model/try2/1511968880")

for i in sess.graph.get_operations():
    print i.name
    print i.type

input_x_holder =sess.graph.get_operation_by_name("ParseExample/key_detectedActivityConfidenceINT").outputs[0]

#predictions_holder = sess.graph.get_operation_by_name("dnn/binary_logistic_head/predictions/probabilities").outputs[0]