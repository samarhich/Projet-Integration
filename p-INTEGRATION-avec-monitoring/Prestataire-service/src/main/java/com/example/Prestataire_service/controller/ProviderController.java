package com.example.Prestataire_service.controller;

import com.example.Prestataire_service.entity.Provider;
import com.example.Prestataire_service.service.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/providers")
public class ProviderController {

    @Autowired
    private ProviderService providerService;

    // Récupérer tous les prestataires
    @GetMapping

    public List<Provider> getAllProviders() {
        return providerService.getAllProviders();
    }

    // Récupérer un prestataire par ID
    @GetMapping("/{id}")

    public ResponseEntity<Provider> getProviderById(@PathVariable String id) {
        Provider provider = providerService.getProviderById(id);
        if (provider != null) {
            return new ResponseEntity<>(provider, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Retour si prestataire non trouvé
        }
    }

    // Créer un nouveau prestataire
    @PostMapping

    public ResponseEntity<Provider> createProvider(@RequestBody Provider provider) {
        try {
            Provider createdProvider = providerService.createProvider(provider);
            return new ResponseEntity<>(createdProvider, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            // Si un prestataire avec le même email existe déjà
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);  // Retourne 400 si email existe déjà
        }
    }

    // Mettre à jour un prestataire par ID
    @PutMapping("/{id}")

    public ResponseEntity<Provider> updateProvider(@PathVariable String id, @RequestBody Provider provider) {
        try {
            Provider updatedProvider = providerService.updateProvider(id, provider);
            return new ResponseEntity<>(updatedProvider, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            // Retourne 404 si le prestataire n'existe pas
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Supprimer un prestataire par ID
    @DeleteMapping("/{id}")

    public ResponseEntity<HttpStatus> deleteProvider(@PathVariable String id) {
        boolean isDeleted = providerService.deleteProvider(id);
        if (isDeleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);  // 204 No Content
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);   // 404 Not Found si pas de prestataire trouvé
        }
    }

    @GetMapping("/auth")
    public Principal authentication(Principal principal){
        return principal; }
}
