package ca.mcgill.ecse428.jerseycabinet.dao;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import ca.mcgill.ecse428.jerseycabinet.model.Order;
import ca.mcgill.ecse428.jerseycabinet.model.Order.OrderKey;

public interface OrderRepository extends CrudRepository<Order, OrderKey> {
    List<Order> findByKeyCustomerIdAndKeyJerseyId(Long customerId, Long jerseyId);
    List<Order> findByKeyCustomerId(int customerId);
    List<Order> findByOrderDateBetween(Date orderDate, Date orderDate2);

    @Query(value = "SELECT o FROM Order o WHERE o.key.customer.id = :customerId AND o.key.jersey.id = :jerseyId")
    Order findOrderByCustomerIdAndJerseyId(@Param("customerId") int customerId, @Param("jerseyId") int jerseyId);
}