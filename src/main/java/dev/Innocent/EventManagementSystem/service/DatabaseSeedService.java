package dev.Innocent.EventManagementSystem.service;

import org.springframework.http.ResponseEntity;

public interface DatabaseSeedService {
    ResponseEntity<?> seedBanks();
}
