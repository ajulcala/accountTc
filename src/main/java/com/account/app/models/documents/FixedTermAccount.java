package com.account.app.models.documents;

import com.account.app.models.dto.Card;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
@Getter
@Setter
@Document(collection="fixedterm_account")
public class FixedTermAccount {
    @Id
    private String id;
    private String naccount;//numero de cuenta++
    private String currency; //tipo de moneda soles o dolares++
    private String dni; //dni del usuario registrado++
    private String type; // tipo de cuenta palzo fijo++
    private Boolean maintenance; //true o false ++
    private Double mcommission; // comision si que hay mantenimiento
    private Boolean limittransaction; // limete de transacciones++
    private Integer limit; //0 si no hay limite de transacciones++
    private String frequency; // mensualmente/ monthly
    private Double amount; //monto de la cuenta
    private Integer condition; //0 - retiro/ 1 - deposito
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date deposit_date; //fecha deposito
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date departure_date; //fecha retiro
    private Double interest; //interes a favor
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createAt;

    private Card card;
}
