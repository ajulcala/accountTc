package com.account.app.services;

import com.account.app.models.dao.SavingsAccountDao;
import com.account.app.models.documents.SavingsAccount;
import com.account.app.models.dto.Card;
import com.account.app.models.dto.Customer;
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
public class SavingsAccountServiceImpl implements SavingsAccountService{
    @Value("${config.base.customer}")
    private String url;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Autowired
    private SavingsAccountDao dao;

    @Override
    public Flux<SavingsAccount> findAll() {
        return dao.findAll();
    }

    @Override
    public Mono<SavingsAccount> findById(String id) {
        return dao.findById(id);
    }

    @Override
    public Mono<SavingsAccount> save(SavingsAccount savingsAccount) {
        return dao.save(savingsAccount);
    }

    @Override
    public Mono<Void> delete(SavingsAccount savingsAccount) {
        return dao.delete(savingsAccount);
    }

    @Override
    public Flux<SavingsAccount> findByCard(Card card) {
        return dao.findByCard(card);
    }

    @Override
    public Mono<ResponseEntity<Map<String, Object>>> saveSavings(String dni, SavingsAccount savingsAccount) {
        Map<String, Object> params = new HashMap<String, Object>();
        Map<String, Object> response = new HashMap<>();
        params.put("dni",dni);
        Flux<Customer> customer = webClientBuilder.build().get()
                .uri(url+"{dni}",params)
                .retrieve()
                .bodyToFlux(Customer.class);

        return customer.collectList().flatMap(c ->{
            if(c.isEmpty()){
                response.put("Mensaje", "Usuario no existe en la Base de Datos");
                response.put("Note", "Registre Usuario");
                return Mono.just(new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST));
            }else{
                savingsAccount.setDni(dni);
                savingsAccount.setType("CUENTA AHORRO");
                if(savingsAccount.getCreateAt()==null){
                    savingsAccount.setCreateAt(new Date());
                }
                return dao.findByDni(dni).collectList().flatMap(cu ->{

                    if(cu.isEmpty()){
                        return dao.save(savingsAccount).flatMap(sa ->{
                            response.put("Usuario", sa);
                            response.put("Mensaje", "Cuenta registrado con éxito");
                            return Mono.just(new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK));
                        });
                    }else{
                        response.put("Mensaje", "El Cliente ya tiene una cuenta de ahorro");
                        response.put("Note", "No puede crear otra");
                        return Mono.just(new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST));
                    }
                });
            }
        });
    }

    @Override
    public Mono<ResponseEntity<Map<String, Object>>> updateSavings(String id, SavingsAccount savingsAccount) {
        Map<String, Object> response = new HashMap<>();
        return dao.findById(id).flatMap(c->{
            c.setNaccount(savingsAccount.getNaccount());
            c.setCurrency(savingsAccount.getCurrency());
            c.setMaintenance(savingsAccount.getMaintenance());
            c.setMcommission(savingsAccount.getMcommission());
            c.setLimittransaction(savingsAccount.getLimittransaction());
            c.setLimit(savingsAccount.getLimit());
            c.setFrequency(savingsAccount.getFrequency());
            c.setAmount(savingsAccount.getAmount());
            c.setCard(savingsAccount.getCard());
            return dao.save(c);
        }).map(savingsUpdated->{
            response.put("Mensaje:", "Registro actualizado");
            response.put("Savings:", savingsUpdated);
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
        }).defaultIfEmpty(new ResponseEntity<Map<String, Object>>(HttpStatus.NOT_FOUND));
    }

    @Override
    public Mono<SavingsAccount> saldoCard(SavingsAccount savingsAccount) {
        Mono<SavingsAccount> aux = dao.findByCard(savingsAccount.getCard()).next();
        return aux.flatMap(sa->{
            return dao.findById(sa.getId()).flatMap(c->{
                if(savingsAccount.getCondition().equals(1) && sa.getLimit() > 0){
                    c.setAmount(c.getAmount() + savingsAccount.getAmount());
                    c.setLimit(c.getLimit() - 1);
                    return dao.save(c);
                }else{
                    if(savingsAccount.getCondition().equals(0) && sa.getLimit() > 0){
                        c.setAmount(c.getAmount() - savingsAccount.getAmount());
                        c.setLimit(c.getLimit() - 1);
                        return dao.save(c);
                    }else{
                        return Mono.empty();
                    }
                }
            });
        });
    }
}
