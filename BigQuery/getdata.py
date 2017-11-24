from google.cloud import bigquery
client = bigquery.Client(project="skanependlaren")
dataset = client.dataset("commuting")
table = dataset.table("travelsearch")

def list_tables(dataset_id, project=None):
    """Lists all of the tables in a given dataset.

    If no project is specified, then the currently active project is used.
    """
    bigquery_client = bigquery.Client(project=project)
    dataset_ref = bigquery_client.dataset(dataset_id)

    for table in bigquery_client.list_dataset_tables(dataset_ref):
        print(table.table_id)

def list_rows(dataset_id, table_id, project=None):
    """Prints rows in the given table.

    Will print 25 rows at most for brevity as tables can contain large amounts
    of rows.

    If no project is specified, then the currently active project is used.
    """
    bigquery_client = bigquery.Client(project=project)
    dataset_ref = bigquery_client.dataset(dataset_id)
    table_ref = dataset_ref.table(table_id)

    # Get the table from the API so that the schema is available.
    table = bigquery_client.get_table(table_ref)

    # Load at most 25 results.
    rows = bigquery_client.list_rows(table, max_results=25)

    # Use format to create a simple table.
    format_string = '{!s:<16} ' * len(table.schema)

    # Print schema field names
    field_names = [field.name for field in table.schema]
    print(format_string.format(*field_names))

    for row in rows:
        #encoded_string = *row
        #the_encoding = 'utf-8'  # for the sake of example
        #text = unicode(encoded_string, the_encoding)
        print(row[1])
        #print(format_string.format(*row))


#list_tables("commuting","skanependlaren")

#list_rows()

def query_rows():
    query_job = client.query("""
        #standardSQL
        SELECT * FROM commuting.travelsearch WHERE 
        detectedActivity!='' LIMIT 100""")

    results = query_job.result()  # Waits for job to complete.
    frame=results.to_dataframe()
    for row in results:
        print("{}: {}".format(row.uid, row.startStation.encode('utf8')))

#query_rows()

def query_again():
    QUERY = (""" SELECT * FROM commuting.travelsearch WHERE 
        detectedActivity!='' LIMIT 100""")
    TIMEOUT = 30  # in seconds
    query_job = client.query(QUERY)  # API request - starts the query
    assert query_job.state == 'RUNNING'

    # Waits for the query to finish
    iterator = query_job.result(timeout=TIMEOUT)
    for row in iterator:
        print("{}: {}: {}".format(row.uid, row.startStation.encode('utf8'), row.detectedActivity.encode('utf8')))
    #rows = list(iterator)

    #assert query_job.state == 'DONE'
    #assert len(rows) == 10
    #row = rows[0]
    #assert row[0] == row.uid == row['uid']



#Delete everything
def delete_all():
    TIMEOUT = 30  # in seconds
    QUERY=("""DELETE FROM commuting.travelsearch WHERE true""")
    query_job = client.query(QUERY)  # API request - starts the query
    results = query_job.result(timeout=TIMEOUT)  # Waits for job to complete.
    for row in results:
        print("{}: {}: {}".format(row.uid, row.startStation.encode('utf8'),row.detectedActivity.encode('utf8')))

#Delete specific
def delete_specific():
    TIMEOUT = 30  # in seconds
    QUERY=("""DELETE FROM commuting.travelsearch WHERE startStation!='Ystads station'""")
    query_job = client.query(QUERY)  # API request - starts the query
    results = query_job.result(timeout=TIMEOUT)  # Waits for job to complete.
    for row in results:
        print("{}: {}".format(row.uid, row.startStation.encode('utf8')))

query_again()
##delete_all()  /Not possible over a Streaming buffer
#delete_specific()
query_again()