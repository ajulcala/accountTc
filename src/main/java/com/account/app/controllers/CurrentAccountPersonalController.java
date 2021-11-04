package com.account.app.controllers;

import com.account.app.models.documents.CurrentAccountPersonal;
import com.account.app.models.dto.Card;
import com.account.app.services.CurrentAccountPersonalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/currentpersonal")
public class CurrentAccountPersonalController {
    @Autowired
    private CurrentAccountPersonalService service;

    @GetMapping
    public Flux<CurrentAccountPersonal> list(){
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Map<String, Object>>> findByIdCurrentPersonal(@PathVariable String id){
        return service.findByIdCurrentPersonal(id);
    }

    @PostMapping("/{dni}")
    public Mono<ResponseEntity<Map<String, Object>>> saveCurrentPersonal(@PathVariable String dni,@Valid @RequestBody CurrentAccountPersonal currentAccountPersonal){
        return service.saveCurrentPersonal(dni, currentAccountPersonal);
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Map<String, Object>>> updateCurrentPersonal(@PathVariable String id, @Valid @RequestBody CurrentAccountPersonal currentAccountPersonal){
        return service.updateCurrentPersonal(id, currentAccountPersonal);
    }

    @DeleteMapping("/{id}")
    private Mono<ResponseEntity<Void>> delete(@PathVariable String id) {
        return service.findById(id).flatMap(p ->{
            return service.delete(p).then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)));
        }).defaultIfEmpty(new ResponseEntity<Void>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/card")
    public Flux<CurrentAccountPersonal> listCard(@Valid @RequestBody Card card){
        return service.findByCard(card);
    }

    @PutMapping("/saldo")
    public Mono<CurrentAccountPersonal> saldoCard(@Valid @RequestBody CurrentAccountPersonal currentAccountPersonal){
        return service.saldoCard(currentAccountPersonal);
    }
}
