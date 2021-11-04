package com.account.app.services;

import com.account.app.models.dao.CurrentAccountPersonalDao;
import com.account.app.models.documents.CurrentAccountPersonal;
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
public class CurrentAccountPersonalServiceImpl implements CurrentAccountPersonalService{
    @Value("${config.base.customer}")
    private String url;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Autowired
    private CurrentAccountPersonalDao dao;
    @Override
    public Flux<CurrentAccountPersonal> findAll() {
        return dao.findAll();
    }

    @Override
    public Mono<CurrentAccountPersonal> findById(String id) {
        return dao.findById(id);
    }

    @Override
    public Mono<CurrentAccountPersonal> save(CurrentAccountPersonal currentAccountPersonal) {
        return dao.save(currentAccountPersonal);
    }

    @Override
    public Mono<Void> delete(CurrentAccountPersonal currentAccountPersonal) {
        return dao.delete(currentAccountPersonal);
    }

    @Override
    public Mono<ResponseEntity<Map<String, Object>>> findByIdCurrentPersonal(String id) {
        Map<String, Object> response = new HashMap<>();
        return dao.findById(id).map(p -> {
            response.put("Mensaje:", "Registro encontrado");
            response.put("CurrentPersonal", p);
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
        }).defaultIfEmpty(new ResponseEntity<Map<String, Object>>(HttpStatus.NOT_FOUND));
    }

    @Override
    public Mono<ResponseEntity<Map<String, Object>>> saveCurrentPersonal(String dni, CurrentAccountPersonal currentAccountPersonal) {
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
                currentAccountPersonal.setDni(dni);
                currentAccountPersonal.setType("CUENTA CORRIENTE PERSONAL");
                currentAccountPersonal.setLimittransaction(false);
                currentAccountPersonal.setLimit(0);
                if(currentAccountPersonal.getCreateAt()==null){
                    currentAccountPersonal.setCreateAt(new Date());
                }

                return dao.findByDni(dni).collectList().flatMap(cu ->{

                    if(cu.isEmpty()){
                        return dao.save(currentAccountPersonal).flatMap(sa ->{
                            response.put("Account", sa);
                            response.put("Message", "Cuenta registrada con éxito");
                            return Mono.just(new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK));
                        });
                    }else{
                        response.put("Message", "El Cliente ya tiene una Cuenta Corriente");
                        response.put("Note", "No puede crear otra");
                        return Mono.just(new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST));
                    }
                });

            }
        });
    }

    @Override
    public Mono<ResponseEntity<Map<String, Object>>> updateCurrentPersonal(String id, CurrentAccountPersonal currentAccountPersonal) {
        Map<String, Object> response = new HashMap<>();
        return dao.findById(id).flatMap(c->{
            c.setNaccount(currentAccountPersonal.getNaccount());
            c.setCurrency(currentAccountPersonal.getCurrency());
            c.setMaintenance(currentAccountPersonal.getMaintenance());
            c.setMcommission(currentAccountPersonal.getMcommission());
            c.setLimittransaction(currentAccountPersonal.getLimittransaction());
            c.setLimit(currentAccountPersonal.getLimit());
            c.setAmount(currentAccountPersonal.getAmount());
            c.setCard(currentAccountPersonal.getCard());
            return dao.save(c);
        }).map(fixedUpdated->{
            response.put("Message", "Registro actualizado");
            response.put("Account", fixedUpdated);
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
        }).defaultIfEmpty(new ResponseEntity<Map<String, Object>>(HttpStatus.NOT_FOUND));
    }

    @Override
    public Flux<CurrentAccountPersonal> findByCard(Card card) {
        return dao.findByCard(card);
    }

    @Override
    public Mono<CurrentAccountPersonal> saldoCard(CurrentAccountPersonal currentAccountPersonal) {
        Mono<CurrentAccountPersonal> aux = dao.findByCard(currentAccountPersonal.getCard()).next();
        return aux.flatMap(sa->{
            return dao.findById(sa.getId()).flatMap(c->{
                if(currentAccountPersonal.getCondition().equals(1)){
                    c.setAmount(c.getAmount() + currentAccountPersonal.getAmount());
                }else{
                    c.setAmount(c.getAmount() - currentAccountPersonal.getAmount());
                }
                return dao.save(c);
            });
        });
    }
}
