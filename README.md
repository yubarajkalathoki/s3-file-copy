# s3-file-copy
Copies files between Amazon S3 buckets.


This application accepts commandline argument to copy Amazon S3 bucket objects from source to distination after converting objects into given format. Target format accepts either `json` or `xml`.

Sample format to run application.  

`java s3-file-copy-1.0.0.jar s3://<source_bucket>/<file_key> s3://<target_bucket>/<file_key> json/xml`