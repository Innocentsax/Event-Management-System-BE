package dev.Innocent.EventManagementSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import dev.Innocent.EventManagementSystem.model.TicketSale;

public interface TicketSaleRepository extends JpaRepository<TicketSale, Long> {
}
