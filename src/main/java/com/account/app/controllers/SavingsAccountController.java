package com.account.app.controllers;

import com.account.app.models.documents.SavingsAccount;
import com.account.app.models.dto.Card;
import com.account.app.services.SavingsAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/savings")
public class SavingsAccountController {
    @Autowired
    private SavingsAccountService service;

    @GetMapping
    public Flux<SavingsAccount> list(){
        return service.findAll();
    }

    @PostMapping("/card")
    public Flux<SavingsAccount> listCard(@Valid @RequestBody Card card){
        return service.findByCard(card);
    }

    @PutMapping("/saldo")
    public Mono<SavingsAccount> saldoCard(@Valid @RequestBody SavingsAccount savingsAccount){
        return service.saldoCard(savingsAccount);
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<SavingsAccount>> ver(@PathVariable String id){
        return service.findById(id).map(p -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(p))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping("/{dni}")
    public Mono<ResponseEntity<Map<String, Object>>> saveaAvings(@PathVariable String dni, @Valid @RequestBody SavingsAccount savingsAccount){
        return service.saveSavings(dni, savingsAccount);
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Map<String, Object>>> updateSavings(@PathVariable String id, @Valid @RequestBody SavingsAccount costomer){
        return service.updateSavings(id,costomer);
    }

    @DeleteMapping("/{id}")
    private Mono<ResponseEntity<Void>> delete(@PathVariable String id) {
        return service.findById(id).flatMap(p ->{
            return service.delete(p).then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)));
        }).defaultIfEmpty(new ResponseEntity<Void>(HttpStatus.NOT_FOUND));
    }
}
