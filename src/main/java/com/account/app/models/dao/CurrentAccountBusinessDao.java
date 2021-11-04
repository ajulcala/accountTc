package com.account.app.models.dao;

import com.account.app.models.documents.CurrentAccountBusiness;
import com.account.app.models.dto.Card;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface CurrentAccountBusinessDao extends ReactiveMongoRepository<CurrentAccountBusiness,String> {
    Flux<CurrentAccountBusiness> findByRuc(String ruc);
    Flux<CurrentAccountBusiness> findByCard(Card card);
}
