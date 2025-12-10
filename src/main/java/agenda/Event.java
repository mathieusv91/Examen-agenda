package agenda;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;

public class Event {


    /**
     * The myTitle of this event
     */
    private String myTitle;
    
    /**
     * The starting time of the event
     */
    private LocalDateTime myStart;

    /**
     * The durarion of the event 
     */
    private Duration myDuration;

    private Repetition repetition;
    private Termination termination;
    private Set<LocalDate> exceptions = new HashSet<>();

    /**
     * Constructs an event
     *
     * @param title the title of this event
     * @param start the start time of this event
     * @param duration the duration of this event
     */
    public Event(String title, LocalDateTime start, Duration duration) {
        this.myTitle = title;
        this.myStart = start;
        this.myDuration = duration;
    }

    public void setRepetition(ChronoUnit frequency) {
        this.repetition = new Repetition(frequency);
    }

    public void addException(LocalDate date) {
        exceptions.add(date);
    }

    public void setTermination(LocalDate terminationInclusive) {
        this.termination = new Termination(myStart.toLocalDate(), repetition.getFrequency(), terminationInclusive);
    }

    public void setTermination(long numberOfOccurrences) {
        this.termination = new Termination(myStart.toLocalDate(), repetition.getFrequency(), numberOfOccurrences);
    }


    public int getNumberOfOccurrences() {
    if (termination == null) return 0;

    // Terminaison basée sur une date (cas fixedTermination)
    if (termination.terminationDateInclusive() != null) {
        LocalDate start = myStart.toLocalDate();
        LocalDate end = termination.terminationDateInclusive();

        // Toutes les semaines -> nombre = weeksBetween + 1
        return (int) ChronoUnit.WEEKS.between(start, end) + 1;
    }

    // Terminaison basée sur un nombre d'occurrences (cas fixedRepetitions)
    return (int) termination.numberOfOccurrences();
}

    

    public LocalDate getTerminationDate() {
    if (termination == null) return null;

    // Terminaison basée sur un nombre d'occurrences
    if (termination.numberOfOccurrences() > 0) {
        LocalDate start = myStart.toLocalDate();
        long n = termination.numberOfOccurrences();

        // La dernière occurrence = start + (n-1) semaines
        return start.plusWeeks(n - 1);
    }

    // Terminaison basée sur une date
    return termination.terminationDateInclusive();
}




    /**
     * Tests if an event occurs on a given day
     *
     * @param aDay the day to test
     * @return true if the event occurs on that day, false otherwise
     */
public boolean isInDay(LocalDate aDay) {

    LocalDate start = myStart.toLocalDate();

    // ------ CAS des événements simples ------
    if (repetition == null) {
        LocalDate end = myStart.plus(myDuration).toLocalDate();
        return !aDay.isBefore(start) && !aDay.isAfter(end);
    }

    // ------ CAS des répétitifs ------

    // Avant le début → jamais
    if (aDay.isBefore(start)) return false;

    // Exceptions
    if (exceptions.contains(aDay)) return false;

    // Terminaison par date
    if (termination != null && termination.terminationDateInclusive() != null) {
        if (aDay.isAfter(termination.terminationDateInclusive())) return false;
    }

    ChronoUnit freq = repetition.getFrequency();

    switch (freq) {

        case DAYS:
            return true; // tous les jours sauf exceptions

        case WEEKS:
            long weeks = ChronoUnit.WEEKS.between(start, aDay);
            return weeks >= 0;

        case MONTHS:
            long months = ChronoUnit.MONTHS.between(start, aDay);
            return months >= 0;
    }

    return false;
}


    /**
     * @return the myTitle
     */
    public String getTitle() {
        return myTitle;
    }

    /**
     * @return the myStart
     */
    public LocalDateTime getStart() {
        return myStart;
    }


    /**
     * @return the myDuration
     */
    public Duration getDuration() {
        return myDuration;
    }

    @Override
    public String toString() {
        return "Event{title='%s', start=%s, duration=%s}".formatted(myTitle, myStart, myDuration);
    }
}