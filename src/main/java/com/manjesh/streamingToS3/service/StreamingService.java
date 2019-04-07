package com.manjesh.streamingToS3.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class StreamingService {

  @Value("${AWSaccesskey}")
  private String amazonS3AccessKey;

  @Value("${AWSsecretkey}")
  private String amazonS3SecretKey;

  @Value("${AWSbucketName}")
  private String amazonS3BucketName;

  private AmazonS3 amazonS3;

  public Map<String, String> uploadToS3(MultipartFile multipartFile) {
    byte[] bytes= null;
    try {
      bytes = multipartFile.getBytes();
    } catch (IOException e) {
      e.printStackTrace();
    }
    configAwsCredentials();
    String fileName = multipartFile.getOriginalFilename() + "--" + System.currentTimeMillis() + "."
        + multipartFile.getContentType();
    String folderName = "streamingToS3";
    String domainName = "https://s3.ap-south-1.amazonaws.com";
    InputStream fis = new ByteArrayInputStream(bytes);
    ObjectMetadata metadata = new ObjectMetadata();
    metadata.setContentLength(bytes.length);
    metadata.setContentType(multipartFile.getContentType());
    metadata.setCacheControl("public, max-age=31536000");
    amazonS3.putObject(this.amazonS3BucketName, folderName + "/" + fileName, fis, metadata);
    amazonS3.setObjectAcl(this.amazonS3BucketName, folderName + "/" + fileName,
        CannedAccessControlList.PublicRead);
    Map<String, String> url = new HashMap<>();
    url.put("url", domainName + "/" + this.amazonS3BucketName + "/" + folderName + "/" + fileName);
    return url;
  }

  private void configAwsCredentials() {
    BasicAWSCredentials awsCredentials = new BasicAWSCredentials(amazonS3AccessKey,
        amazonS3SecretKey);
    amazonS3 = AmazonS3ClientBuilder.standard().withRegion(Regions.AP_SOUTH_1)
        .withCredentials(new AWSStaticCredentialsProvider(awsCredentials)).build();
  }

}
