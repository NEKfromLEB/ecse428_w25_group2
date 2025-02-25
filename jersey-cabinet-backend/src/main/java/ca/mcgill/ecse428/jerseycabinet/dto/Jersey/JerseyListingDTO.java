package ca.mcgill.ecse428.jerseycabinet.dto.Jersey;

public class JerseyListingDTO {
    
    private int jerseyId;
    private double salePrice;
    private String description;

    public JerseyListingDTO() {}

    public JerseyListingDTO(int jerseyId, double salePrice, String description) {
        this.jerseyId = jerseyId;
        this.salePrice = salePrice;
        this.description = description;
    }

    public int getJerseyId() {
        return jerseyId;
    }

    public void setJerseyId(int jerseyId) {
        this.jerseyId = jerseyId;
    }

    public double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(double salePrice) {
        this.salePrice = salePrice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}