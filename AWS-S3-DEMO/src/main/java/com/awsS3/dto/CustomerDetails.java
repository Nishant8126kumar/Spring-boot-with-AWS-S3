package com.awsS3.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerDetails {

    private String name;
    private String mobileNo;
    private Integer age;
    private String passportNo;
    private String  address;
}

