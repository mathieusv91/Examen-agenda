package agenda;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class AgendaTest {
    Agenda agenda;

    // November 1st, 2020
    LocalDate nov_1_2020 = LocalDate.of(2020, 11, 1);

    // 120 minutes
    Duration min_120 = Duration.ofMinutes(120);

    // Un événement simple
    Event simple;
    Event overlapping;

    @BeforeEach
    public void setUp() {
        simple = new Event("Simple event", LocalDateTime.of(2020, 11, 1, 22, 30), min_120);
        overlapping = new Event("Overlapping event", LocalDateTime.of(2020, 11, 1, 22, 30), min_120);

        agenda = new Agenda();
        agenda.addEvent(simple);
        agenda.addEvent(overlapping);
    }

    @Test
    public void testFindByTitle() {
        // Ajouter quelques événements avec des titres différents
        Event event1 = new Event("Meeting", LocalDateTime.of(2020, 11, 2, 10, 0), min_120);
        Event event2 = new Event("Meeting", LocalDateTime.of(2020, 11, 3, 14, 0), min_120);
        agenda.addEvent(event1);
        agenda.addEvent(event2);

        // Tester la recherche par titre
        assertEquals(2, agenda.findByTitle("Meeting").size(), "Il y a 2 événements avec le titre 'Meeting'");
        assertEquals(1, agenda.findByTitle("Simple event").size(), "Il y a 1 événement avec le titre 'Simple event'");
    }

    @Test
    public void testIsFreeFor() {
        // Tester un nouvel événement qui chevauche un événement existant
        Event newEvent = new Event("New Event", LocalDateTime.of(2020, 11, 1, 22, 45), min_120);
        assertFalse(agenda.isFreeFor(newEvent), "Il n'y a pas de place pour cet événement (chevauchement)");

        // Tester un nouvel événement qui ne chevauche pas
        Event newEvent2 = new Event("New Event 2", LocalDateTime.of(2020, 11, 1, 0, 0), min_120);
        assertTrue(agenda.isFreeFor(newEvent2), "Il y a de la place pour cet événement (pas de chevauchement)");
    }
}
