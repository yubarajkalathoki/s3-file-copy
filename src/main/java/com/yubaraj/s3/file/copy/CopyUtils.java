package com.yubaraj.s3.file.copy;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;

import org.apache.commons.io.FilenameUtils;
import org.json.JSONObject;
import org.json.XML;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;

/**
 * Utility class for copying object between objects.
 * 
 * @author Yuba Raj Kalathoki
 * @version 1.0.0
 * @since 1.0.0, Dec 31, 2016
 */
public class CopyUtils {
    /**
     * Returns the bucket name from given path.
     * 
     * @param path
     * @author Yuba Raj Kalathoki
     * @since 1.0.0
     */
    public static String getBucketName(String path) {
	String bucketName = path.substring(5);
	int keyStartIndex = bucketName.indexOf('/');
	bucketName = bucketName.substring(0, keyStartIndex);
	return bucketName;
    }

    /**
     * Returns the key from path and bucket.
     * 
     * @param path
     * @param bucketName
     * @author Yuba Raj Kalathoki
     * @since 1.0.0
     */
    public static String getKey(String path, String bucketName) {
	String[] tokens = path.split(bucketName);
	String keyString = tokens[1].substring(1);
	return keyString;
    }

    /**
     * Returns the content from given file.
     * 
     * @param file
     * @throws IOException
     * @author Yuba Raj Kalathoki
     * @since 1.0.0
     */
    public static String readFile(File file) throws IOException {
	StringBuilder fileContents = new StringBuilder((int) file.length());
	Scanner scanner = new Scanner(file);
	String lineSeparator = System.getProperty("line.separator");
	try {
	    while (scanner.hasNextLine()) {
		fileContents.append(scanner.nextLine() + lineSeparator);
	    }
	    return fileContents.toString();
	} finally {
	    scanner.close();
	}
    }

    /**
     * Returns json content from xml content.
     * 
     * @param xml
     * @author Yuba Raj Kalathoki
     * @since 1.0.0
     */
    public static JSONObject getJSONObject(String xml) {
	JSONObject soapDatainJsonObject = XML.toJSONObject(xml);
	return soapDatainJsonObject;
    }

    /**
     * Returns xml content from json content.
     * 
     * @param json
     * @author Yuba Raj Kalathoki
     * @since 1.0.0
     */
    public static String getXml(String json) {
	JSONObject jsonObject = new JSONObject(json);
	String xml = XML.toString(jsonObject);
	return xml;
    }

    /**
     * Returns the source file value content from given sourceKey.
     * 
     * @param sourceKey
     * @author Yuba Raj Kalathoki
     * @since 1.0.0
     */
    public static String getTargetFile(String sourceKey) {
	String fileNameWithExtension = new File(sourceKey).getName();
	String fileNameWithoutExtension = FilenameUtils.removeExtension(fileNameWithExtension);
	String extension = FilenameUtils.getExtension(fileNameWithExtension);
	System.out.println("File name: " + fileNameWithoutExtension);
	System.out.println("Extension: " + extension);
	String sourceFile = fileNameWithExtension;
	String targetKey = "";
	if (extension.equalsIgnoreCase(Extension.JSON.name())) {
	    JSONObject jsonObject = getJSONObject(sourceFile);
	    targetKey = fileNameWithoutExtension + "." + Extension.JSON;
	    writeFile(targetKey, jsonObject.toString());
	} else if (extension.equalsIgnoreCase(Extension.XML.name())) {

	} else {
	    try {
		throw new InvalidTargetFormatException(
			"Invalid target format. The valid target formats are:" + Extension.values());
	    } catch (InvalidTargetFormatException e) {
		e.printStackTrace();
	    }
	}
	return null;
    }

    /**
     * Returns the key with given path.
     * 
     * @param path
     * @author Yuba Raj Kalathoki
     * @since 1.0.0
     */
    public static String getKey(String path) {
	String[] tokens = path.split(getBucketName(path));
	String keyString = tokens[1].substring(1);
	return keyString;
    }

    public static void upload(AmazonS3 amazonS3, String targetBucketName, String destLocation, File file) {
	PutObjectRequest putObjectRequest = new PutObjectRequest(targetBucketName, destLocation, file);
	putObjectRequest.withCannedAcl(CannedAccessControlList.Private);
	amazonS3.putObject(putObjectRequest);

    }

    public static String getObjectContent(AmazonS3Client s3Client, String bucketName, String key) {
	InputStream objectData = null;
	File tmp = null;
	try {
	    S3Object object = s3Client.getObject(new GetObjectRequest(bucketName, key));
	    objectData = object.getObjectContent();
	    tmp = File.createTempFile("s3test", "");
	    Files.copy(objectData, tmp.toPath(), StandardCopyOption.REPLACE_EXISTING);
	    return readFile(tmp);
	} catch (Exception e) {
	    e.printStackTrace();
	    return null;
	} finally {
	    if (objectData != null) {
		try {
		    objectData.close();
		} catch (IOException e) {
		    e.printStackTrace();
		}
	    }
	    if (tmp != null) {
		tmp.deleteOnExit();
	    }
	}
    }

    public static File writeFile(String pathName, String fileContent) {
	File file = null;
	try {
	    file = File.createTempFile("s3test", "");
	} catch (IOException e1) {
	    e1.printStackTrace();
	}
	BufferedWriter bw = null;
	FileWriter fw = null;
	try {
	    // if file doesnt exists, then create it
	    if (!file.exists()) {
		file.createNewFile();
	    }
	    // true = append file
	    fw = new FileWriter(file.getAbsoluteFile(), true);
	    bw = new BufferedWriter(fw);
	    bw.write(fileContent);
	} catch (IOException e) {
	    e.printStackTrace();
	} finally {
	    try {
		if (bw != null)
		    bw.close();
		if (fw != null)
		    fw.close();
	    } catch (IOException ex) {
		ex.printStackTrace();
	    }
	}
	return file;
    }
}
