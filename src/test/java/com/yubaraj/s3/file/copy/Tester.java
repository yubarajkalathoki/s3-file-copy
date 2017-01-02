package com.yubaraj.s3.file.copy;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.s3.AmazonS3Client;

/**
 * Tests various methods.
 * 
 * @author Yuba Raj Kalathoki
 * @version 1.0.0
 * @since 1.0.0, Dec 31, 2016
 */
public class Tester {
    private static final Logger LOG  = LoggerFactory.getLogger(Tester.class);
    AmazonS3Client s3Client;

    @Before
    public void init() {
	AWSInitializer initializer = new AWSInitializer();
	initializer.init();
	s3Client = initializer.amazonS3Client;
    }

    //@Test
    public void testToReadObjectContent() {
	String sourceFilePath = "s3://bucket/key/object";
	String sourceBucket=CopyUtils.getBucketName(sourceFilePath);
	String key = CopyUtils.getKey(sourceFilePath);
	String objectContent=CopyUtils.getObjectContent(s3Client, sourceBucket, key);
	LOG.info(objectContent);
    }
    
    @Test
    public void testToGetBucketName(){
	String targetpath="s3://bucket/key/object";
	LOG.info(CopyUtils.getBucketName(targetpath));
    }
}
