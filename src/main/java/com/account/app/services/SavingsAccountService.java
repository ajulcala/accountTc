package com.account.app.services;

import com.account.app.models.documents.SavingsAccount;
import com.account.app.models.dto.Card;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

public interface SavingsAccountService {
    public Flux<SavingsAccount> findAll();
    public Mono<SavingsAccount> findById(String id);
    public Mono<SavingsAccount> save(SavingsAccount savingsAccount);
    public Mono<Void> delete(SavingsAccount savingsAccount);
    public Flux<SavingsAccount> findByCard(Card card);
    public Mono<ResponseEntity<Map<String, Object>>> saveSavings(String dni, SavingsAccount savingsAccount);
    public Mono<ResponseEntity<Map<String, Object>>> updateSavings(String id,SavingsAccount savingsAccount);
    public Mono<SavingsAccount> saldoCard(SavingsAccount savingsAccount);
}
