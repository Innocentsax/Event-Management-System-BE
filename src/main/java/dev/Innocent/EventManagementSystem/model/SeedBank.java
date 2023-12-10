package dev.Innocent.EventManagementSystem.model;

import dev.Innocent.EventManagementSystem.serviceImpl.DatabaseSeedServiceImpl;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAsync
@EnableScheduling
@SpringBootApplication()
public class SeedBank implements CommandLineRunner {

    private final DatabaseSeedServiceImpl databaseSeedService;

    public SeedBank(DatabaseSeedServiceImpl databaseSeedService) {
        this.databaseSeedService = databaseSeedService;
    }

    @Override
    public void run(String[] args) throws Exception {
       databaseSeedService.seedBanks();
    }
}
