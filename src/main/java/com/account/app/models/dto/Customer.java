package com.account.app.models.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
@Getter
@Setter
public class Customer {
    private String id;
    private String dni;
    private String name; //nombre
    private String apep;//apellido paterno
    private String apem;// apellido materno
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birth_date; //fecha nacimiento
    private String address;
    private String type; //tipo de persona
}
