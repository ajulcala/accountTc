package com.account.app.models.dao;

import com.account.app.models.documents.CurrentAccountPersonal;
import com.account.app.models.dto.Card;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface CurrentAccountPersonalDao extends ReactiveMongoRepository<CurrentAccountPersonal,String> {
    Flux<CurrentAccountPersonal> findByDni(String dni);
    Flux<CurrentAccountPersonal> findByCard(Card card);
}
