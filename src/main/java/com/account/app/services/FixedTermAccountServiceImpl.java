package com.account.app.services;

import com.account.app.models.dao.FixedTermAccountDao;
import com.account.app.models.documents.FixedTermAccount;
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
public class FixedTermAccountServiceImpl implements FixedTermAccountService{
    @Value("${config.base.customer}")
    private String url;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Autowired
    private FixedTermAccountDao dao;

    @Override
    public Flux<FixedTermAccount> findAll() {
        return dao.findAll();
    }

    @Override
    public Mono<FixedTermAccount> findById(String id) {
        return dao.findById(id);
    }

    @Override
    public Mono<FixedTermAccount> save(FixedTermAccount fixedTermAccount) {
        return dao.save(fixedTermAccount);
    }

    @Override
    public Mono<Void> delete(FixedTermAccount fixedTermAccount) {
        return dao.delete(fixedTermAccount);
    }

    @Override
    public Mono<ResponseEntity<Map<String, Object>>> findByIdFixed(String id) {
        Map<String, Object> response = new HashMap<>();
        return dao.findById(id).map(p -> {
            response.put("Mensaje:", "Registro encontrado");
            response.put("fixed", p);
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
        }).defaultIfEmpty(new ResponseEntity<Map<String, Object>>(HttpStatus.NOT_FOUND));
    }

    @Override
    public Mono<ResponseEntity<Map<String, Object>>> saveFixed(String dni, FixedTermAccount fixedTermAccount) {
        Map<String, Object> params = new HashMap<String, Object>();
        Map<String, Object> response = new HashMap<>();
        params.put("dni",dni);

        Flux<Customer> customer = webClientBuilder.build().get()
                .uri(url+"{dni}",params)
                .retrieve()
                .bodyToFlux(Customer.class);
        return customer.collectList().flatMap(c ->{
            if(c.isEmpty()){
                response.put("Message", "Usuario no existe en la Base de Datos");
                response.put("Note", "Registre Usuario");
                return Mono.just(new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST));
            }else{
                fixedTermAccount.setDni(dni);
                fixedTermAccount.setType("CUENTA PLAZO FIJO");
                fixedTermAccount.setMaintenance(false);
                fixedTermAccount.setMcommission(0.0);
                fixedTermAccount.setLimittransaction(true);
                fixedTermAccount.setLimit(2);
                if(fixedTermAccount.getCreateAt()==null){
                    fixedTermAccount.setCreateAt(new Date());
                }

                return dao.findByDni(dni).collectList().flatMap(cu ->{

                    if(cu.isEmpty()){
                        if(fixedTermAccount.getCreateAt()==null){
                            fixedTermAccount.setCreateAt(new Date());
                        }
                        return dao.save(fixedTermAccount).flatMap(sa ->{
                            response.put("Account", sa);
                            response.put("Message", "Cuenta registrado con éxito");
                            return Mono.just(new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK));
                        });
                    }else{
                        response.put("Mensaje", "El Cliente ya tiene una Cuenta a plazo Fijo");
                        response.put("Note", "No puede crear otra");
                        return Mono.just(new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST));
                    }
                });

            }
        });
    }

    @Override
    public Mono<ResponseEntity<Map<String, Object>>> updateFixed(String id, FixedTermAccount fixedTermAccount) {
        Map<String, Object> response = new HashMap<>();
        return dao.findById(id).flatMap(c->{
            c.setNaccount(fixedTermAccount.getNaccount());
            c.setCurrency(fixedTermAccount.getCurrency());
            c.setMaintenance(fixedTermAccount.getMaintenance());
            c.setMcommission(fixedTermAccount.getMcommission());
            c.setLimittransaction(fixedTermAccount.getLimittransaction());
            c.setLimit(fixedTermAccount.getLimit());
            c.setFrequency(fixedTermAccount.getFrequency());
            c.setAmount(fixedTermAccount.getAmount());
            c.setCard(fixedTermAccount.getCard());
            c.setDeparture_date(fixedTermAccount.getDeparture_date());
            c.setDeposit_date(fixedTermAccount.getDeposit_date());
            return dao.save(c);
        }).map(fixedUpdated->{
            response.put("Message", "Registro actualizado");
            response.put("Fixed", fixedUpdated);
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
        }).defaultIfEmpty(new ResponseEntity<Map<String, Object>>(HttpStatus.NOT_FOUND));
    }

    @Override
    public Mono<FixedTermAccount> balanceCard(FixedTermAccount fixedTermAccount) {
        Mono<FixedTermAccount> aux = dao.findByCard(fixedTermAccount.getCard()).next();
        return aux.flatMap(sa->{
            return dao.findById(sa.getId()).flatMap(c->{
                if(fixedTermAccount.getCondition().equals(1)){
                    if(sa.getDeposit_date().equals(fixedTermAccount.getCreateAt()) && c.getLimit() > 0){
                        c.setAmount(c.getAmount() + fixedTermAccount.getAmount());
                        c.setLimit(c.getLimit()-1);
                        return dao.save(c);
                    }else{
                        return Mono.empty();
                    }

                }else{
                    if(sa.getDeparture_date().equals(fixedTermAccount.getCreateAt()) && c.getLimit() > 0){
                        c.setAmount(c.getAmount() - fixedTermAccount.getAmount());
                        c.setLimit(c.getLimit()-1);
                        return dao.save(c);
                    }else{
                        return Mono.empty();
                    }
                }

            });
        });
    }

    @Override
    public Flux<FixedTermAccount> findByCard(Card card) {
        return dao.findByCard(card);
    }
}
