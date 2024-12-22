package javawizzards.officespace.dto;

import java.util.UUID;

public class ReservationDto {
    private UUID id;
    private String details;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}