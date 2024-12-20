package com.example.Prestataire_service.service;

import com.example.Prestataire_service.entity.Provider;
import com.example.Prestataire_service.repository.ProviderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProviderService {

    @Autowired
    private ProviderRepository providerRepository;

    // Récupérer tous les prestataires
    public List<Provider> getAllProviders() {
        return providerRepository.findAll();
    }

    // Récupérer un prestataire par ID
    public Provider getProviderById(String providerId) {
        return providerRepository.findById(providerId)
                .orElseThrow(() -> new IllegalArgumentException("Provider not found with ID: " + providerId));
    }

    // Créer un nouveau prestataire
    public Provider createProvider(Provider provider) {
        // Vérifier si l'email existe déjà
        if (providerRepository.findByEmail(provider.getEmail()) != null) {
            throw new IllegalArgumentException("A provider with this email already exists: " + provider.getEmail());
        }
        return providerRepository.save(provider);
    }

    // Mettre à jour un prestataire existant
    public Provider updateProvider(String id, Provider providerDetails) {
        // Vérifier si le prestataire existe
        Provider existingProvider = getProviderById(id);

        // Mettre à jour les informations
        existingProvider.setUsername(providerDetails.getUsername());
        existingProvider.setPassword(providerDetails.getPassword());
        existingProvider.setFirstName(providerDetails.getFirstName());
        existingProvider.setLastName(providerDetails.getLastName());
        existingProvider.setEmail(providerDetails.getEmail());
        existingProvider.setPhone(providerDetails.getPhone());
        existingProvider.setLocation(providerDetails.getLocation());

        // Sauvegarder et retourner l'objet mis à jour
        return providerRepository.save(existingProvider);
    }

    // Supprimer un prestataire par ID
    public boolean deleteProvider(String id) {
        if (!providerRepository.existsById(id)) {
            throw new IllegalArgumentException("Provider not found with ID: " + id);
        }
        providerRepository.deleteById(id);
        return true;
    }
}
