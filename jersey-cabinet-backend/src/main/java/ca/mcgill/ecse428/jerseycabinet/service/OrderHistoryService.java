package ca.mcgill.ecse428.jerseycabinet.service;

import ca.mcgill.ecse428.jerseycabinet.dao.CustomerRepository;
import ca.mcgill.ecse428.jerseycabinet.dao.OrderRepository;
import ca.mcgill.ecse428.jerseycabinet.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Comparator;

@Service
public class OrderHistoryService {

  @Autowired
  OrderRepository orderRepository;

  @Transactional
  public Iterable<Order> getOrderHistory(int customerId) {
    ArrayList<Order> orders = (ArrayList<Order>) orderRepository.findByKeyCustomerId(customerId);

    if (orders.isEmpty()) {
      throw new IllegalArgumentException("No order history");
    } else {
      orders.sort(Comparator.comparing(Order::getOrderDate));
      return orders;
    }
  }

  @Transactional
  public Iterable<Order> getOrderHistoryWithDateFilter(int customerId, String min, String max) {
    Date minDate = Date.valueOf(min);
    Date maxDate = Date.valueOf(max);


    if (maxDate.before(minDate)) {
      throw new IllegalArgumentException("Invalid date range. End date must be after start date.");
    }

    ArrayList<Order> orders = (ArrayList<Order>) orderRepository.findByOrderDateBetween(minDate, maxDate);

    if (orders.isEmpty()) {
      throw new IllegalArgumentException("No orders between " + minDate.toString() + " and "+ maxDate);
    } else {
      orders.sort(Comparator.comparing(Order::getOrderDate));
      return orders;
    }
  }

}
