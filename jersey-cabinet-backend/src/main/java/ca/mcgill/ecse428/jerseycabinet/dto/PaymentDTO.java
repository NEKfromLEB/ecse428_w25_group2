package ca.mcgill.ecse428.jerseycabinet.dto;

public class PaymentDTO {
    
    private double amount;
    private String cardNumber;
    private String cardHolderName;
    private String expirationDate;
    private String cvv;
    
    // Default constructor
    public PaymentDTO() { }
    
    // Parameterized constructor
    public PaymentDTO(double amount, String cardNumber, String cardHolderName, String expirationDate, String cvv) {
        this.amount = amount;
        this.cardNumber = cardNumber;
        this.cardHolderName = cardHolderName;
        this.expirationDate = expirationDate;
        this.cvv = cvv;
    }
    
    // Getters and Setters
    
    public double getAmount() {
        return amount;
    }
    
    public void setAmount(double amount) {
        this.amount = amount;
    }
    
    public String getCardNumber() {
        return cardNumber;
    }
    
    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }
    
    public String getCardHolderName() {
        return cardHolderName;
    }
    
    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }
    
    public String getExpirationDate() {
        return expirationDate;
    }
    
    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }
    
    public String getCvv() {
        return cvv;
    }
    
    public void setCvv(String cvv) {
        this.cvv = cvv;
    }
    
    @Override
    public String toString() {
        return "PaymentDTO [amount=" + amount + ", cardNumber=" + cardNumber 
                + ", cardHolderName=" + cardHolderName + ", expirationDate=" 
                + expirationDate + ", cvv=" + cvv + "]";
    }
}
