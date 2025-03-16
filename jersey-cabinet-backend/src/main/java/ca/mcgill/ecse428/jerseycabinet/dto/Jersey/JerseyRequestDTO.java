package ca.mcgill.ecse428.jerseycabinet.dto.Jersey;


public class JerseyRequestDTO {
    private String description; 
    private String brand;
    private String sport;
    private String color;
    private String jerseyImage;
    private String proofOfAuthenticationImage;
    private Double salePrice;

    public JerseyRequestDTO(String desc, String brand, String sport, String color, String jerseyImage, String authImage, double price){
        this.description = desc;
        this.brand = brand;
        this.sport = sport; 
        this.color = color;
        this.jerseyImage = jerseyImage;
        this.proofOfAuthenticationImage = authImage;
        this.salePrice = price;
    }
    public String getDescription(){
        return description;
    }
    public String getBrand(){
        return brand;
    }
    public String getSport(){
        return sport;
    }
    public String getColor(){
        return color;
    }
    public String getJerseyImage(){
        return jerseyImage;
    }
    public String getProofOfAuthenticationImage(){
        return proofOfAuthenticationImage;
    }
    public double getSalePrice(){
        return salePrice;
    }
}
