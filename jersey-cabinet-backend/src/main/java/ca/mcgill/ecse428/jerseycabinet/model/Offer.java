package ca.mcgill.ecse428.jerseycabinet.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
public class Offer {

  public enum OfferState { Accepted, Pending, Rejected }

  @EmbeddedId
  private Key key;

  private OfferState offerState;
  private int price;

  public Offer() {
  }

  public Offer(Key key, OfferState offerState, int price) {
    this.key = key;
    this.offerState = offerState;
    this.price = price;
  }

  public Key getKey() {
    return key;
  }

  public void setKey(Key key) {
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

  public void delete() {
    if (this.getKey() != null) {
        Jersey jersey = this.getKey().getJersey();
        if (jersey != null) {
            jersey.removeOffer(this); 
        }
        Employee employee = this.getKey().getEmployee();
        if (employee != null) {
            employee.removeOffer(this); 
        }
    }
  } 


  @Embeddable
  public static class Key implements Serializable {
    @ManyToOne
    private Jersey jersey;
    
    @ManyToOne
    private Employee employee;

    public Key() {
    }

    public Key(Jersey jersey, Employee employee) {
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
      if (!(obj instanceof Key)) {
        return false;
      }
      Key other = (Key) obj;
      return Objects.equals(this.jersey.getId(), other.jersey.getId()) &&
             Objects.equals(this.employee.getId(), other.employee.getId());
    }

    @Override
    public int hashCode() {
      return Objects.hash(jersey.getId(), employee.getId());
    }
  }
}
