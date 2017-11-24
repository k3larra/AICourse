
def implicit():
    from google.cloud import storage

    # If you don't specify credentials when constructing the client, the
    # client library will look for credentials in the environment.
    storage_client = storage.Client()

    # Make an authenticated API request
    buckets = list(storage_client.list_buckets())
    print(buckets)
    print("tre0")

    from google.cloud import bigquery
    client = bigquery.Client(project='skanependlaren')

    for dataset in client.list_datasets():
        print(dataset.description)


implicit()