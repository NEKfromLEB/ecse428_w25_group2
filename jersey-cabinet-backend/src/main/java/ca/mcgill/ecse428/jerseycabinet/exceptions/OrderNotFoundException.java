package ca.mcgill.ecse428.jerseycabinet.service;

public class OrderNotFoundException extends Exception {
    public   OrderNotFoundException(String message) {
        super(message);
    }
}
