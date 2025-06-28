package com.oscaradrian.franquicias.infrastructure.repository;

import com.oscaradrian.franquicias.domain.model.Franquicia;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
    public interface FranquiciaRepository extends ReactiveMongoRepository<Franquicia, String> {
}