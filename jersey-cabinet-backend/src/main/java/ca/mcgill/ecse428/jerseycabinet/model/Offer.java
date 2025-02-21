package ca.mcgill.ecse428.jerseycabinet.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
public class Offer {

    @EmbeddedId
    private OfferKey key;

    @Enumerated(EnumType.STRING)
    private OfferState offerState;
    private int price;

    public Offer() {}

    public Offer(OfferKey key, OfferState offerState, int price) {
        this.key = key;
        this.offerState = offerState;
        this.price = price;
    }

    public OfferKey getKey() {
        return key;
    }

    public void setKey(OfferKey key) {
        this.key = key;
    }

    public OfferState getOfferState() {
        return offerState;
    }

    public void setOfferState(OfferState offerState) {
        this.offerState = offerState;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public enum OfferState { Accepted, Pending, Rejected }

    @Embeddable
    public static class OfferKey implements Serializable {
        @ManyToOne
        private Jersey jersey;

        @ManyToOne
        private Employee employee;

        public OfferKey() {}

        public OfferKey(Jersey jersey, Employee employee) {
            this.jersey = jersey;
            this.employee = employee;
        }

        public Jersey getJersey() {
            return jersey;
        }

        public void setJersey(Jersey jersey) {
            this.jersey = jersey;
        }

        public Employee getEmployee() {
            return employee;
        }

        public void setEmployee(Employee employee) {
            this.employee = employee;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof OfferKey)) return false;
            OfferKey other = (OfferKey) obj;
            return Objects.equals(jersey, other.jersey) && Objects.equals(employee, other.employee);
        }

        @Override
        public int hashCode() {
            return Objects.hash(jersey, employee);
        }
    }
}
