package com.awsS3.services;

import com.awsS3.config.AwsUtil;
import com.awsS3.dto.CustomerDetails;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class ExcelUploadService {

    @Autowired
    AwsUtil awsUtil;

    public void createCustomerDetails() throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, IOException {
        try {
            List<String> headerList = Arrays.asList("Name", "Mobile No", "Age", "Passport No", "Address");
            List<String> fieldValues = Arrays.asList("name", "age", "address", "mobileNo", "passportNo");
            List<CustomerDetails> customerDetailsList =
                    Arrays.asList(CustomerDetails.builder().name("Nishant Kumar").age(26).address("Aligarh").mobileNo("8126632693").passportNo("AJK-22687").build(),
                            CustomerDetails.builder().name("Aman Sharma").age(22).address("Aligarh").mobileNo("9911617346").passportNo("AJK-2268709809").build(),
                            CustomerDetails.builder().name("Suraj Kumar").age(26).address("Noida").mobileNo("8126632693").passportNo("PKAJK-22687").build(),
                            CustomerDetails.builder().name("Brijesh ").age(30).address("Aligarh").mobileNo("8126632678").passportNo("ZPAJK-22687").build());

            XSSFWorkbook xssfWorkbook = new XSSFWorkbook();
            SXSSFWorkbook wbbs = new SXSSFWorkbook(xssfWorkbook, 100);
            String sheen_name = "USER_DETAILS" + UUID.randomUUID().toString().substring(0, 4).toUpperCase();
            SXSSFSheet excelSheet = wbbs.createSheet(sheen_name);
            String fileName = "USER_DETAILS" + UUID.randomUUID().toString().substring(1, 6);

            int rowCount = 0;
            int columnCount = 0;
            Row row = excelSheet.createRow(rowCount++);
            for (String header : headerList) {
                Cell cell = row.createCell(columnCount++);
                cell.setCellValue(header);
            }
            for (CustomerDetails cuDetails : customerDetailsList) {
                row = excelSheet.createRow(rowCount++);
                columnCount = 0;
                for (String objectField : fieldValues) {
                    Cell cell = row.createCell(columnCount);
                    Class<?> aClass = cuDetails.getClass();
                    Method method = aClass.getDeclaredMethod("get" + StringUtils.capitalize(objectField));
                    String value = method.invoke(cuDetails).toString();
                    cell.setCellValue(value);
                    columnCount++;
                }
            }
            awsUtil.uploadFileOnS3Aws(fileName, wbbs);
        } catch (Exception ex) {
            log.info("Exception occurred :" + ex);
        }
    }
}
