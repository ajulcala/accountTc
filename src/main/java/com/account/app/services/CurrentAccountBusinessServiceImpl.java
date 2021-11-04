package com.account.app.services;

import com.account.app.models.dao.CurrentAccountBusinessDao;
import com.account.app.models.documents.CurrentAccountBusiness;
import com.account.app.models.dto.Card;
import com.account.app.models.dto.CustomerBusiness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class CurrentAccountBusinessServiceImpl implements CurrentAccountBusinessService{
    @Value("${config.base.customerbusiness}")
    private String url;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Autowired
    private CurrentAccountBusinessDao dao;

    @Override
    public Flux<CurrentAccountBusiness> findAll() {
        return dao.findAll();
    }

    @Override
    public Mono<CurrentAccountBusiness> findById(String id) {
        return dao.findById(id);
    }

    @Override
    public Mono<CurrentAccountBusiness> save(CurrentAccountBusiness currentAccountBusiness) {
        return dao.save(currentAccountBusiness);
    }

    @Override
    public Mono<Void> delete(CurrentAccountBusiness currentAccountBusiness) {
        return dao.delete(currentAccountBusiness);
    }

    @Override
    public Mono<ResponseEntity<Map<String, Object>>> findByIdCurrentBusiness(String id) {
        Map<String, Object> response = new HashMap<>();
        return dao.findById(id).map(p -> {
            response.put("Mensaje:", "Registro encontrado");
            response.put("CurrentPersonal", p);
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
        }).defaultIfEmpty(new ResponseEntity<Map<String, Object>>(HttpStatus.NOT_FOUND));
    }

    @Override
    public Mono<ResponseEntity<Map<String, Object>>> saveCurrentBusiness(String ruc, CurrentAccountBusiness currentAccountBusiness) {
        Map<String, Object> params = new HashMap<String, Object>();
        Map<String, Object> response = new HashMap<>();
        params.put("ruc",ruc);

        Flux<CustomerBusiness> customer = webClientBuilder.build().get()
                .uri(url+"{ruc}",params)
                .retrieve()
                .bodyToFlux(CustomerBusiness.class);
        return customer.collectList().flatMap(c ->{
            if(c.isEmpty()){
                response.put("Mensaje", "Usuario no existe en la Base de Datos");
                response.put("Note", "Registre Usuario");
                return Mono.just(new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST));
            }else{
                currentAccountBusiness.setRuc(ruc);
                currentAccountBusiness.setType("CUENTA CORRIENTE BUSINESS");
                currentAccountBusiness.setLimittransaction(false);
                currentAccountBusiness.setLimit(0);
                if(currentAccountBusiness.getCreateAt()==null){
                    currentAccountBusiness.setCreateAt(new Date());
                }
                return dao.save(currentAccountBusiness).flatMap(sa ->{
                    response.put("Account", sa);
                    response.put("Message", "Cuenta registrada con éxito");
                    return Mono.just(new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK));
                });
            }
        });
    }

    @Override
    public Mono<ResponseEntity<Map<String, Object>>> updateCurrentBusiness(String id, CurrentAccountBusiness currentAccountBusiness) {
        Map<String, Object> response = new HashMap<>();
        return dao.findById(id).flatMap(c->{
            c.setNaccount(currentAccountBusiness.getNaccount());
            c.setCurrency(currentAccountBusiness.getCurrency());
            c.setMaintenance(currentAccountBusiness.getMaintenance());
            c.setMcommission(currentAccountBusiness.getMcommission());
            c.setLimittransaction(currentAccountBusiness.getLimittransaction());
            c.setLimit(currentAccountBusiness.getLimit());
            c.setAmount(currentAccountBusiness.getAmount());
            c.setCard(currentAccountBusiness.getCard());
            c.setOwners(currentAccountBusiness.getOwners());
            c.setSignatories(currentAccountBusiness.getSignatories());
            return dao.save(c);
        }).map(AccountUpdated->{
            response.put("Message", "Registro actualizado");
            response.put("Account", AccountUpdated);
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
        }).defaultIfEmpty(new ResponseEntity<Map<String, Object>>(HttpStatus.NOT_FOUND));
    }

    @Override
    public Flux<CurrentAccountBusiness> findByCard(Card card) {
        return dao.findByCard(card);
    }

    @Override
    public Mono<CurrentAccountBusiness> saldoCard(CurrentAccountBusiness currentAccountBusiness) {
        Mono<CurrentAccountBusiness> aux = dao.findByCard(currentAccountBusiness.getCard()).next();
        return aux.flatMap(sa->{
            return dao.findById(sa.getId()).flatMap(c->{
                if(currentAccountBusiness.getCondition().equals(1)){
                    c.setAmount(c.getAmount() + currentAccountBusiness.getAmount());
                }else{
                    c.setAmount(c.getAmount() - currentAccountBusiness.getAmount());
                }
                return dao.save(c);
            });
        });
    }
}
