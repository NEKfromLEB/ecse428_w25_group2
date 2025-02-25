package ca.mcgill.ecse428.jerseycabinet.service;

import ca.mcgill.ecse428.jerseycabinet.model.Order;
import ca.mcgill.ecse428.jerseycabinet.model.Order.OrderStatus;
import ca.mcgill.ecse428.jerseycabinet.dao.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class FilterOrdersByStatusService {

    @Autowired
    private OrderRepository orderRepository;

    @Transactional
    public List<Order> filterOrdersByStatus(OrderStatus status) {
        return StreamSupport.stream(orderRepository.findAll().spliterator(), false)
                .filter(order -> order.getStatus() == status)
                .collect(Collectors.toList());
    }
}