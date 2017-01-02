package com.yubaraj.s3.file.copy;

import java.io.File;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.s3.AmazonS3Client;

/**
 * Starter class from where application starts.
 * 
 * @author Yuba Raj Kalathoki
 * @version 1.0.0
 * @since 1.0.0, Dec 31, 2016
 */
public class Application {
    private static final Logger LOG = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
	AWSInitializer initializer = new AWSInitializer();
	if (initializer.init()) {
	    if (args.length >= 3) {
		String sourcePath = args[0];
		String destinationFolder = args[1];
		String destinationFileFormat = args[2];

		AmazonS3Client s3Client = initializer.getAmazonS3Client();

		String sourceBucketName = CopyUtils.getBucketName(sourcePath);
		String sourceKey = CopyUtils.getKey(sourcePath, sourceBucketName);

		System.out.println("Source bucket: " + sourceBucketName);
		System.out.println("sourceKey: " + sourceKey);

		String destinationBucketName = CopyUtils.getBucketName(destinationFolder);
		String targetKey = CopyUtils.getKey(destinationFolder);

		// String targetKey = CopyUtils.getTargetKey(sourceKey);

		System.out.println("Target bucket: " + destinationBucketName);
		System.out.println("TargetKey: " + targetKey);

		String sourceBucket = CopyUtils.getBucketName(sourcePath);
		String key = CopyUtils.getKey(sourcePath);

		String fileContent = CopyUtils.getObjectContent(s3Client, sourceBucket, key);

		String targetFileName = getTargetFileName(sourcePath, destinationFileFormat);

		String fileObject = targetKey + "/" + targetFileName;

		String newFileContent = "";
		if (destinationFileFormat.equalsIgnoreCase(Extension.JSON.name())) {
		    newFileContent = CopyUtils.getJSONObject(fileContent).toString();
		} else if (destinationFileFormat.equalsIgnoreCase(Extension.XML.name())) {
		    newFileContent = CopyUtils.getXml(fileContent);
		} else {
		    try {
			throw new InvalidTargetFormatException(
				"Invalid target format. The valid target formats are:" + Extension.values());
		    } catch (InvalidTargetFormatException e) {
			e.printStackTrace();
		    }
		}
		File file = CopyUtils.writeFile(key, newFileContent);
		System.out.println(file.getAbsolutePath());
		CopyUtils.upload(s3Client, destinationBucketName, fileObject, file);
		LOG.info("Copied to: " + destinationBucketName + "/" + fileObject);
		if (file != null) {
		    file.deleteOnExit();
		}
	    } else {
		LOG.info("Invalid commandline argument.");
	    }
	} else {
	    LOG.info("Invalid AWS credentials. Please verify your credentials (~/.aws/credentials)");
	}
    }

    /**
     * Returns target file name with given extension.
     * 
     * @param extension
     * @author Yuba Raj Kalathoki
     * @since 1.0.0
     */
    private static String getTargetFileName(String sourceKey, String extension) {
	String fileNameWithExtension = new File(sourceKey).getName();
	String fileNameWithoutExtension = FilenameUtils.removeExtension(fileNameWithExtension);
	return fileNameWithoutExtension; // This is modified version. Which does
					 // not include file extension.
	// String targetFileName = "";
	// if (extension.equalsIgnoreCase(Extension.JSON.name())) {
	// targetFileName = fileNameWithoutExtension + "." +
	// Extension.JSON.name().toLowerCase();
	// } else if (extension.equalsIgnoreCase(Extension.XML.name())) {
	// targetFileName = fileNameWithoutExtension + "." +
	// Extension.XML.name().toLowerCase();
	// } else {
	// try {
	// throw new InvalidTargetFormatException(
	// "Invalid target format. The valid target formats are:" +
	// Extension.values());
	// } catch (InvalidTargetFormatException e) {
	// e.printStackTrace();
	// }
	// }
	// return targetFileName;
    }
}
