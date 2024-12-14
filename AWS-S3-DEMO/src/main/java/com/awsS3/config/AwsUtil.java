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
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.security.SecureRandom;

@Component
@Slf4j
public class AwsUtil {


    public String createFolderInS3Bucket() {
        try {
            String SUFFIX = "/";
            String folderName = "marketing-file" + SUFFIX + "customer" + new SecureRandom().nextInt(900000) + SUFFIX;
            AmazonS3 s3Client = this.initializeAmazon();
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(0);
            InputStream emptyContent = new ByteArrayInputStream(new byte[0]);
            PutObjectRequest putObjectRequest = new PutObjectRequest("my-first-bucket-81", folderName, emptyContent, objectMetadata);
            s3Client.putObject(putObjectRequest);
            return folderName;
        } catch (Exception ex) {
            log.info("Exception occurred while create folder in bucket. " + ex.getMessage());
        }
        return null;
    }

    public AmazonS3 initializeAmazon() {
        AmazonS3 s3Client;
        Regions regions = Regions.US_EAST_2;
        AWSCredentials credentials = new BasicAWSCredentials("AKIAZ24ISRUKO4BPTVUF", "IM6ZZ4Ks9U51riWK+db1hA1vZLVCUwP9sH0TQXw6");
        s3Client = new AmazonS3Client(credentials);
        s3Client.setRegion(Region.getRegion(regions));
        return s3Client;
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
            s3Client.putObject(new PutObjectRequest("my-first-bucket-81", keyName, file));
            return "File has been uploaded successfully.";
        } catch (Exception ex) {
            log.info("Exception :" + ex);
            return "Some error occurred .";
        }
    }
}


