package com.account.app.models.dao;

import com.account.app.models.documents.SavingsAccount;
import com.account.app.models.dto.Card;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface SavingsAccountDao extends ReactiveMongoRepository<SavingsAccount,String> {
    Flux<SavingsAccount> findByDni(String dni);
    Flux<SavingsAccount> findByCard(Card card);
}
