package ca.mcgill.ecse428.jerseycabinet.dto.Jersey;

import ca.mcgill.ecse428.jerseycabinet.model.Jersey;

public class JerseyResponseDTO {
    
    private int id;
    private Jersey.RequestState requestState;
    private String description;
    private String brand;
    private String sport;
    private String color;
    private String jerseyImage;
    private String proofOfAuthenticationImage;
    private boolean listedForSale;
    private double salePrice;
    private int sellerId;

    public JerseyResponseDTO() {}

    public JerseyResponseDTO(int id, Jersey.RequestState requestState, String description, 
            String brand, String sport, String color, String jerseyImage, 
            String proofOfAuthenticationImage, boolean listedForSale, 
            double salePrice, int employeeId, int sellerId) {
        this.id = id;
        this.requestState = requestState;
        this.description = description;
        this.brand = brand;
        this.sport = sport;
        this.color = color;
        this.jerseyImage = jerseyImage;
        this.proofOfAuthenticationImage = proofOfAuthenticationImage;
        this.listedForSale = listedForSale;
        this.salePrice = salePrice;
        this.sellerId = sellerId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Jersey.RequestState getRequestState() {
        return requestState;
    }

    public void setRequestState(Jersey.RequestState requestState) {
        this.requestState = requestState;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getJerseyImage() {
        return jerseyImage;
    }

    public void setJerseyImage(String jerseyImage) {
        this.jerseyImage = jerseyImage;
    }

    public String getProofOfAuthenticationImage() {
        return proofOfAuthenticationImage;
    }

    public void setProofOfAuthenticationImage(String proofOfAuthenticationImage) {
        this.proofOfAuthenticationImage = proofOfAuthenticationImage;
    }

    public boolean isListedForSale() {
        return listedForSale;
    }

    public void setListedForSale(boolean listedForSale) {
        this.listedForSale = listedForSale;
    }

    public double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(double salePrice) {
        this.salePrice = salePrice;
    }

    public int getSellerId() {
        return sellerId;
    }

    public void setSellerId(int sellerId) {
        this.sellerId = sellerId;
    }
}
