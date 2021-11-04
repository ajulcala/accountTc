package com.account.app.services;

import com.account.app.models.documents.CurrentAccountBusiness;
import com.account.app.models.dto.Card;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

public interface CurrentAccountBusinessService {
    public Flux<CurrentAccountBusiness> findAll();
    public Mono<CurrentAccountBusiness> findById(String id);
    public Mono<CurrentAccountBusiness> save(CurrentAccountBusiness currentAccountBusiness);
    public Mono<Void> delete(CurrentAccountBusiness currentAccountBusiness);

    public Mono<ResponseEntity<Map<String, Object>>> findByIdCurrentBusiness(String id);
    public Mono<ResponseEntity<Map<String, Object>>> saveCurrentBusiness(String ruc, CurrentAccountBusiness currentAccountBusiness);
    public Mono<ResponseEntity<Map<String, Object>>> updateCurrentBusiness(String id, CurrentAccountBusiness currentAccountBusiness);

    public Flux<CurrentAccountBusiness> findByCard(Card card);
    public Mono<CurrentAccountBusiness> saldoCard(CurrentAccountBusiness currentAccountBusiness);
}
