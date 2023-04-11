package model;


/**
 * Represents a recipe application event
 * Copied from AlarmSystem
 */
public class Event {
    private static final int HASH_CONSTANT = 13;
    private String description;
    private static int num = 1;

    /**
     * Creates an event with the given description
     * and the current number of the event
     *
     * @param description a description of the event
     */
    public Event(String description) {
        this.description = description;
    }

    /**
     * Gets the number of the event (starts at 1)
     *
     * @return the number of the event
     */
    public int getNum() {
        return num;
    }

    /**
     * Gets the description of this event.
     *
     * @return the description of the event
     */
    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (other.getClass() != this.getClass()) {
            return false;
        }
        Event otherEvent = (Event) other;

        return (this.num == otherEvent.num
                && this.description.equals(otherEvent.description));
    }

    @Override
    public int hashCode() {
        return (HASH_CONSTANT + description.hashCode());
    }

    @Override
    public String toString() {
        int prevNum = num;
        num++;
        return prevNum + ":" + description;
    }
}
