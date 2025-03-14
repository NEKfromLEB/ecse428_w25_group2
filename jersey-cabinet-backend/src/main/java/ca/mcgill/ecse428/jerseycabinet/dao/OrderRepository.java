package ca.mcgill.ecse428.jerseycabinet.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse428.jerseycabinet.model.Order;
import ca.mcgill.ecse428.jerseycabinet.model.Order.OrderKey;

public interface OrderRepository extends CrudRepository<Order, OrderKey> {
    List<Order> findByKeyCustomerIdAndKeyJerseyId(Long customerId, Long jerseyId);
}