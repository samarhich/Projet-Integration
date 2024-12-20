package com.example.Prestataire_service.repository;

import com.example.Prestataire_service.entity.Provider;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ProviderRepository extends MongoRepository<Provider, String> {
    List<Provider> findByLocation(String location);
    List<Provider> findByLocationAndUsername(String location, String username);
    Provider findByEmail(String email);
}