package com.account.app.controllers;

import com.account.app.models.documents.FixedTermAccount;
import com.account.app.models.dto.Card;
import com.account.app.services.FixedTermAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/fixed")
public class FixedTermAccountController {
    @Autowired
    private FixedTermAccountService service;


    @GetMapping
    public Flux<FixedTermAccount> list(){
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Map<String, Object>>> findByIdFixed(@PathVariable String id){
        return service.findByIdFixed(id);
    }

    @PostMapping("/{dni}")
    public Mono<ResponseEntity<Map<String, Object>>> saveFixed(@PathVariable String dni,@Valid @RequestBody FixedTermAccount fixedTermAccount){
        return service.saveFixed(dni, fixedTermAccount);
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Map<String, Object>>> updateCustomer(@PathVariable String id, @Valid @RequestBody FixedTermAccount fixedTermAccount){
        return service.updateFixed(id,fixedTermAccount);
    }

    @DeleteMapping("/{id}")
    private Mono<ResponseEntity<Void>> delete(@PathVariable String id) {
        return service.findById(id).flatMap(p ->{
            return service.delete(p).then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)));
        }).defaultIfEmpty(new ResponseEntity<Void>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/card")
    public Flux<FixedTermAccount> listCard(@Valid @RequestBody Card card){
        return service.findByCard(card);
    }

    @PutMapping("/saldo")
    public Mono<FixedTermAccount> balanceCard(@Valid @RequestBody FixedTermAccount fixedTermAccount){
        return service.balanceCard(fixedTermAccount);
    }
}
