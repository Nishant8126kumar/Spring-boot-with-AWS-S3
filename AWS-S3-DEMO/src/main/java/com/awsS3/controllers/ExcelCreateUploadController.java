package com.awsS3.controllers;

import com.awsS3.config.AwsUtil;
import com.awsS3.services.ExcelUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

@RestController
@RequestMapping(value = "/excel")
public class ExcelCreateUploadController {


    @Autowired
    AwsUtil awsUtil;

    @Autowired
    ExcelUploadService excelUploadService;

    @GetMapping(value = "/excel-upload")
    public String testApplication() throws IOException, NoSuchFieldException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        excelUploadService.createCustomerDetails();
        return "Hi am from excel application.";
    }


}
