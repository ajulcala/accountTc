package com.account.app.services;

import com.account.app.models.documents.FixedTermAccount;
import com.account.app.models.dto.Card;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

public interface FixedTermAccountService {
    public Flux<FixedTermAccount> findAll();
    public Mono<FixedTermAccount> findById(String id);
    public Mono<FixedTermAccount> save(FixedTermAccount fixedTermAccount);
    public Mono<Void> delete(FixedTermAccount fixedTermAccount);

    public Mono<ResponseEntity<Map<String, Object>>> findByIdFixed(String id);
    public Mono<ResponseEntity<Map<String, Object>>> saveFixed(String dni, FixedTermAccount fixedTermAccount);
    public Mono<ResponseEntity<Map<String, Object>>> updateFixed(String id, FixedTermAccount fixedTermAccount);
    public Mono<FixedTermAccount> balanceCard(FixedTermAccount fixedTermAccount);
    public Flux<FixedTermAccount> findByCard(Card card);
}
