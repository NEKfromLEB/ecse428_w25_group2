package ca.mcgill.ecse428.jerseycabinet.model;

import java.io.Serializable;
import java.sql.Date;
import java.util.Objects;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "orders")
public class Order {

    public enum OrderStatus { ToBeShipped, Shipped, Delivered }

    @EmbeddedId
    private OrderKey key;

    private Date orderDate;

    public Order() {}

    public Order(OrderKey key, Date orderDate) {
        this.key = key;
        this.orderDate = orderDate;
    }

    public OrderKey getKey() {
        return key;
    }

    public void setKey(OrderKey key) {
        this.key = key;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    @Embeddable
    public static class OrderKey implements Serializable {

        @ManyToOne
        private Customer customer;

        @ManyToOne
        private Jersey jersey;

        public OrderKey() {}

        public OrderKey(Customer customer, Jersey jersey) {
            this.customer = customer;
            this.jersey = jersey;
        }

        public Customer getCustomer() {
            return customer;
        }

        public void setCustomer(Customer customer) {
            this.customer = customer;
        }

        public Jersey getJersey() {
            return jersey;
        }

        public void setJersey(Jersey jersey) {
            this.jersey = jersey;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            OrderKey that = (OrderKey) obj;
            return Objects.equals(customer, that.customer) && Objects.equals(jersey, that.jersey);
        }

        @Override
        public int hashCode() {
            return Objects.hash(customer, jersey);
        }
    }
}
