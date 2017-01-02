package com.yubaraj.s3.file.copy;
import java.io.IOException;

import org.json.JSONObject;

import com.yubaraj.s3.file.copy.CopyUtils;

public class CustomTester {
    public static void main(String[] args) {
	 String source =
	 "s3://bucket/file_keys3/bu/cket/file_keys3/buck/et/file_key.json";
	 System.out.println("Source: " + source);
	 String bucketName = source.substring(5);
	 int keyStartIndex = bucketName.indexOf('/');
	 bucketName = bucketName.substring(0, keyStartIndex);
	 System.out.println("Bucket: " + bucketName);
	
	 String[] tokens = source.split(bucketName);
	 String keyString = tokens[1].substring(1);
	 System.out.println("keys: " + keyString);

//	try {
//	    String contentString = CopyUtils.readFile("books.xml");
//	    JSONObject jsonObject = CopyUtils.getJSONObject(contentString);
//	    System.out.println(jsonObject.toString());
////	    System.out.println(contentString);
//	} catch (IOException e) {
//	    e.printStackTrace();
//	}
	
//	try {
//	    String contentString = CopyUtils.readFile("books.json");
//	    System.out.println(contentString);
//	    String xml = CopyUtils.getXml(contentString);
//	    System.out.println(xml);
//	} catch (IOException e) {
//	    e.printStackTrace();
//	}

    }
}
