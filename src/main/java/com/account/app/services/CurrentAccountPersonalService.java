package com.account.app.services;

import com.account.app.models.documents.CurrentAccountPersonal;
import com.account.app.models.dto.Card;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

public interface CurrentAccountPersonalService {
    public Flux<CurrentAccountPersonal> findAll();
    public Mono<CurrentAccountPersonal> findById(String id);
    public Mono<CurrentAccountPersonal> save(CurrentAccountPersonal currentAccountPersonal);
    public Mono<Void> delete(CurrentAccountPersonal currentAccountPersonal);

    public Mono<ResponseEntity<Map<String, Object>>> findByIdCurrentPersonal(String id);
    public Mono<ResponseEntity<Map<String, Object>>> saveCurrentPersonal(String dni, CurrentAccountPersonal currentAccountPersonal);
    public Mono<ResponseEntity<Map<String, Object>>> updateCurrentPersonal(String id, CurrentAccountPersonal currentAccountPersonal);
    public Flux<CurrentAccountPersonal> findByCard(Card card);
    public Mono<CurrentAccountPersonal> saldoCard(CurrentAccountPersonal currentAccountPersonal);
}
