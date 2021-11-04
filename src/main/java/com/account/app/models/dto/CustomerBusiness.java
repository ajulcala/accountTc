package com.account.app.models.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
@Getter
@Setter
public class CustomerBusiness {
    private String id;
    private String ruc;
    private String business_name; //razon social
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createAt; //fecha de creacion
    private String address;
    private String type; //tipo de persona
}
