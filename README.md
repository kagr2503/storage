# storage


<b>Elasticsearch:</b>

Install Elasticsearch on local machine and run on 9200 port.
or 
If Elasticsearch host on another server then replace the host & port.

#
<b>SQS: </b>
SQS queue define and the set permission. so, the able to received the event from the s3.

Replace the AWS Key & SQS Queue url

#
<b>S3:</b>

File data save on S3, If we add any new file or remove then we received the message from sqs and take the action accordingly.

#
File data parsing & Elasticsearch indexing we are doing in background jobs.
Service Designed as distributed, Stateless and scale able.

