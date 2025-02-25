package ca.mcgill.ecse428.jerseycabinet.dto;

public class OfferDTO {
    private int jerseyId;
    private int employeeId;
    private int price;
    private String offerState; // or use an enum

    public OfferDTO() { }

    public OfferDTO(int jerseyId, int employeeId, int price, String offerState) {
        this.jerseyId = jerseyId;
        this.employeeId = employeeId;
        this.price = price;
        this.offerState = offerState;
    }

    // Getters & Setters
    public int getJerseyId() { return jerseyId; }
    public void setJerseyId(int jerseyId) { this.jerseyId = jerseyId; }

    public int getEmployeeId() { return employeeId; }
    public void setEmployeeId(int employeeId) { this.employeeId = employeeId; }

    public int getPrice() { return price; }
    public void setPrice(int price) { this.price = price; }

    public String getOfferState() { return offerState; }
    public void setOfferState(String offerState) { this.offerState = offerState; }
}

