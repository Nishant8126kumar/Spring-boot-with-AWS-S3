package com.awsS3.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.security.SecureRandom;

@Component
@Slf4j
public class AwsUtil {


    @Value("${AWS.secret.key}")
    private String secretKey;
    @Value("${AWS.access.key}")
    private String accessKay;

    public AmazonS3 initializeAmazon() {
        AmazonS3 s3Client;
        Regions regions = Regions.US_EAST_1;
        AWSCredentials credentials = new BasicAWSCredentials(accessKay, secretKey);
        s3Client = new AmazonS3Client(credentials);
        s3Client.setRegion(Region.getRegion(regions));
        return s3Client;
    }

    public String createFolderInS3Bucket() {
        try {
            String SUFFIX = "/";
            String folderName = "marketing-file" + SUFFIX + "customer" + new SecureRandom().nextInt(900000) + SUFFIX;
            AmazonS3 s3Client = this.initializeAmazon();
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(0);
            InputStream emptyContent = new ByteArrayInputStream(new byte[0]);
            PutObjectRequest putObjectRequest = new PutObjectRequest("insee-data-nishant", folderName, emptyContent, objectMetadata);
            s3Client.putObject(putObjectRequest);
            return folderName;
        } catch (Exception ex) {
            log.info("Exception occurred while create folder in bucket. " + ex.getMessage());
        }
        return null;
    }

    public String uploadFileOnS3Aws(String fileName, SXSSFWorkbook workbook) {
        log.info("Request for upload the file on S3. ");
        try {
            String folderName = this.createFolderInS3Bucket();
            File file = new File(String.format("%s.%s", fileName, "xlsx"));
            FileOutputStream fOut = new FileOutputStream(file);
            workbook.write(fOut);
            fOut.close();
            workbook.close();
            AmazonS3 s3Client = this.initializeAmazon();
            String keyName = folderName + file.getName();
            s3Client.putObject(new PutObjectRequest("insee-data-nishant", keyName, file));
            return "File has been uploaded successfully.";
        } catch (Exception ex) {
            log.info("Exception :" + ex);
            return "Some error occurred .";
        }
    }
}


