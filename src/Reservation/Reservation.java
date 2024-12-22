package javawizzards.officespace.Reservation;

public class Reservation {

    private Long id;
    private String details;

    // Default constructor
    public Reservation() {
    }

    // Constructor for testing
    public Reservation(Long id, String details) {
        this.id = id;
        this.details = details;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

}
