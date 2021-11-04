package com.account.app.models.dao;

import com.account.app.models.documents.FixedTermAccount;
import com.account.app.models.dto.Card;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface FixedTermAccountDao extends ReactiveMongoRepository<FixedTermAccount, String> {
    Flux<FixedTermAccount> findByDni(String dni);
    Flux<FixedTermAccount> findByCard(Card card);
}
