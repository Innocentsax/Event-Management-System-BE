package dev.Innocent.EventManagementSystem.repository;

import dev.Innocent.EventManagementSystem.model.Event;
import dev.Innocent.EventManagementSystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    Optional<Event> findById(Long eventId);

    List<Event> findByPublishedTrueAndCategoryContainingIgnoreCase(String eventCategory);

    Optional<Event> findByEventTitle(String eventTitle);

    Optional<List<Event>> findAllByUser(User user);


    List<Event> findByPublishedTrue();

    List<Event> findByUserId(Long userId);
@Query("SELECT e FROM Event e WHERE e.startDateTime = :date")
    List<Event> findByStartDateIs7DaysOld(@Param("date")Date date);

    Optional<Event> findByUser(User user);
}
