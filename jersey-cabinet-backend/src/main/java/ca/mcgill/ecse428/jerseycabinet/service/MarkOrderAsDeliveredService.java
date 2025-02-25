package ca.mcgill.ecse428.jerseycabinet.service;

import ca.mcgill.ecse428.jerseycabinet.dao.OrderRepository;
import ca.mcgill.ecse428.jerseycabinet.exceptions.OrderNotFoundException;
import ca.mcgill.ecse428.jerseycabinet.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class MarkOrderAsDeliveredService {

    @Autowired
    private OrderRepository orderRepository;

    @Transactional
    public Order markOrderAsDelivered(String customerEmail, String jerseyDescription) throws OrderNotFoundException {
        List<Order> matchingOrders = StreamSupport.stream(orderRepository.findAll().spliterator(), false)
                .filter(order -> order.getKey().getCustomer().getEmail().equals(customerEmail) &&
                        order.getKey().getJersey().getDescription().equals(jerseyDescription))
                .collect(Collectors.toList());

        if (matchingOrders.isEmpty()) {
            throw new OrderNotFoundException("No order found with Customer Email: " + customerEmail +
                    ", Jersey Description: " + jerseyDescription);
        }

        Order order = matchingOrders.get(0); // Get first match
        order.setStatus(Order.OrderStatus.Delivered); // Update status
        return orderRepository.save(order); // Persist the change
    }
}