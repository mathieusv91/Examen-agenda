package agenda;

import java.time.LocalDate;
import java.time.LocalDateTime;  // Assurez-vous d'importer cette classe
import java.time.Duration;
import java.util.*;

public class Agenda {
    private List<Event> events = new ArrayList<>();

    /**
     * Adds an event to this agenda
     *
     * @param e the event to add
     */
    public void addEvent(Event e) {
        events.add(e);
    }

    /**
     * Computes the events that occur on a given day
     *
     * @param day the day to test
     * @return a list of events that occur on that day
     */
    public List<Event> eventsInDay(LocalDate day) {
        List<Event> eventsInDay = new ArrayList<>();
        for (Event event : events) {
            if (event.isInDay(day)) {
                eventsInDay.add(event);
            }
        }
        return eventsInDay;
    }

    /**
     * Find events by their title.
     *
     * @param title the title to search for
     * @return a list of events with the matching title
     */
    public List<Event> findByTitle(String title) {
        List<Event> matchingEvents = new ArrayList<>();
        for (Event event : events) {
            if (event.getTitle().equalsIgnoreCase(title)) {
                matchingEvents.add(event);
            }
        }
        return matchingEvents;
    }

    /**
     * Determines if there is room for a new event in the agenda (no overlap).
     *
     * @param e the event to test
     * @return true if there is room for the event, false otherwise
     */
    public boolean isFreeFor(Event e) {
        for (Event existingEvent : events) {
            // Vérification si les événements chevauchent
            if (existingEvent.getStart().toLocalDate().equals(e.getStart().toLocalDate())) {
                // Si les événements sont sur le même jour, vérifier l'heure
                LocalDateTime startExisting = existingEvent.getStart();
                LocalDateTime endExisting = startExisting.plus(existingEvent.getDuration());

                LocalDateTime startNew = e.getStart();
                LocalDateTime endNew = startNew.plus(e.getDuration());

                // Vérifier s'il y a un chevauchement
                if (startNew.isBefore(endExisting) && startExisting.isBefore(endNew)) {
                    return false; // Il y a un conflit de temps
                }
            }
        }
        return true; // Aucun conflit trouvé
    }
}
