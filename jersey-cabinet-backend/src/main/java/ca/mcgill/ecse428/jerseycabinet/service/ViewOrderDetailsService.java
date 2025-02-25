package ca.mcgill.ecse428.jerseycabinet.service;

import ca.mcgill.ecse428.jerseycabinet.model.Order;
import ca.mcgill.ecse428.jerseycabinet.model.Order.OrderKey;
import ca.mcgill.ecse428.jerseycabinet.dao.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ViewOrderDetailsService {

    @Autowired
    private OrderRepository orderRepository;

    @Transactional
    public Order findOrderByKey(OrderKey key) {
        return orderRepository.findById(key)
                .orElseThrow(() -> new IllegalArgumentException("Order not found for customer " + key.getCustomer().getEmail() +
                        " and jersey " + key.getJersey().getDescription()));
    }
}