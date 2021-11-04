package com.account.app.controllers;

import com.account.app.models.documents.CurrentAccountBusiness;
import com.account.app.models.dto.Card;
import com.account.app.services.CurrentAccountBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/currentbusiness")
public class CurrentAccountBusinessController {
    @Autowired
    private CurrentAccountBusinessService service;

    @GetMapping
    public Flux<CurrentAccountBusiness> list(){
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Map<String, Object>>> findByIdCurrentBusiness(@PathVariable String id){
        return service.findByIdCurrentBusiness(id);
    }

    @PostMapping("/{ruc}")
    public Mono<ResponseEntity<Map<String, Object>>> saveCurrentBusiness(@PathVariable String ruc,@Valid @RequestBody CurrentAccountBusiness currentAccountBusiness){
        return service.saveCurrentBusiness(ruc, currentAccountBusiness);
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Map<String, Object>>> updateCurrentBusines(@PathVariable String id, @Valid @RequestBody CurrentAccountBusiness currentAccountBusiness){
        return service.updateCurrentBusiness(id, currentAccountBusiness);
    }

    @DeleteMapping("/{id}")
    private Mono<ResponseEntity<Void>> delete(@PathVariable String id) {
        return service.findById(id).flatMap(p ->{
            return service.delete(p).then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)));
        }).defaultIfEmpty(new ResponseEntity<Void>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/card")
    public Flux<CurrentAccountBusiness> listCard(@Valid @RequestBody Card card){
        return service.findByCard(card);
    }

    @PutMapping("/saldo")
    public Mono<CurrentAccountBusiness> saldoCard(@Valid @RequestBody CurrentAccountBusiness currentAccountBusiness){
        return service.saldoCard(currentAccountBusiness);
    }
}
