package com.yubaraj.s3.file.copy;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;

/**
 * <p>
 * Initializes AWS credentialis. To initialize aws credentials you must have
 * <code>aws_access_key_id</code> and <code>aws_secret_access_kay</code>. Your
 * credential file should look like following:
 * </p>
 * 
 * <pre class="code">
 * [default]
 * aws_access_key_id= yourAccessKeyId
 * aws_secret_access_key= yourSecretAccessKey
 * </pre>
 * 
 * Move this credentials file to (~/.aws/credentials) after you fill in your
 * access and secret keys in the default profile.
 * <p>
 * 
 * WARNING: To avoid accidental leakage of your credentials, DO NOT keep this
 * file in your source directory.
 * 
 * @author Yuba Raj Kalathoki
 * @version 1.0.0, Dec 31, 2016
 * @since 1.0.0
 */
public class AWSInitializer {
    /**
     * Defined object.
     */
    AmazonS3 amazonS3;
    AmazonS3Client amazonS3Client;

    /**
     * It initializes S3 with default methodology.
     * 
     * @author Yuba Raj Kalathoki
     * @since 1.0.0
     */
    boolean init() {
	/**
	 * credentials object identifying user for authentication user must have
	 * AWSConnector and AmazonS3FullAccess for this example to work
	 */

	/**
	 * Use following method if you want to use .aws folder with credentials
	 * that located in your user/ location.
	 */

	AWSCredentials awsCredentials = new ProfileCredentialsProvider().getCredentials();

	/**
	 * Use following method if you want to provide your secret keys
	 * manually.
	 */

	// AWSCredentials awsCredentials = new
	// BasicAWSCredentials("YourAccessKeyID", "YourSecretAccessKey");

	// create a client connection based on credentials
	amazonS3 = new AmazonS3Client(awsCredentials);
	amazonS3Client = new AmazonS3Client(awsCredentials);
	return true;
    }

    public AmazonS3Client getAmazonS3Client() {
	return amazonS3Client;
    }
}
