package ca.mcgill.ecse428.jerseycabinet.acceptance.US014;

import static org.junit.jupiter.api.Assertions.*;

import ca.mcgill.ecse428.jerseycabinet.dao.CustomerRepository;
import ca.mcgill.ecse428.jerseycabinet.dao.EmployeeRepository;
import ca.mcgill.ecse428.jerseycabinet.dao.JerseyRepository;
import ca.mcgill.ecse428.jerseycabinet.dao.OrderRepository;
import ca.mcgill.ecse428.jerseycabinet.model.*;
import ca.mcgill.ecse428.jerseycabinet.service.OrderHistoryService;
import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@CucumberContextConfiguration
@SpringBootTest
public class ViewOrderHistorySteps {
  @Autowired JerseyRepository jerseyRepository;
  @Autowired OrderRepository orderRepository;
  @Autowired EmployeeRepository employeeRepository;
  @Autowired CustomerRepository customerRepository;

  @Autowired OrderHistoryService service;

  private Exception lastException;

  Employee employee;
  Customer customer;

  ArrayList<Jersey> jerseyList = new ArrayList<>();

  ArrayList<Order> orderList = new ArrayList<>();

  @After
  public void cleanUp() {
    orderRepository.deleteAll();
    jerseyRepository.deleteAll(jerseyList);
    employeeRepository.delete(employee);
    customerRepository.delete(customer);

    orderList.clear();
    jerseyList.clear();
    employee = null;
    customer = null;
  }

  @Given("I have purchased {int} jerseys")
  public void iHavePurchasedJerseys(int arg0) {
    employee = new Employee("admin@jerseycabinet.com", "password");
    customer = new Customer("matpestel@gmail.com", "password", "Montreal");
    customerRepository.save(customer);
    employeeRepository.save(employee);

    for (int i = 0; i < arg0; i++) {
      Jersey jersey =
          new Jersey(
              Jersey.RequestState.Listed, "Jersey " + i, "", "sport", "color", "", "", 1, customer);
      jerseyRepository.save(jersey);
      jerseyList.add(jersey);

      Order.OrderKey key = new Order.OrderKey(customer, jersey);
      Order order = new Order(key, new Date(1735707600000L), Order.OrderStatus.ToBeShipped);
      orderRepository.save(order);
    }
  }

  @When("I request to view my purchase history")
  public void iRequestToViewMyPurchaseHistory() {
    try {
      orderList = (ArrayList<Order>) service.getOrderHistory(customer.getId());
    } catch (Exception e) {
      lastException = e;
    }
  }

  @And("no orders are listed")
  public void noOrdersAreListed() {
    assertTrue(orderList.isEmpty());
  }

  @Then("I should see a list of my {int} orders")
  public void iShouldSeeAListOfMyOrders(int arg0) {
    assertFalse(orderList.isEmpty());
    assertEquals(orderList.size(), arg0);

    Set<String> actualDescriptions =
        orderList.stream()
            .map(order -> order.getKey().getJersey().getDescription())
            .collect(Collectors.toSet());

    Set<String> expectedDescriptions =
        IntStream.range(0, arg0).mapToObj(i -> "Jersey " + i).collect(Collectors.toSet());

    assertEquals(expectedDescriptions, actualDescriptions);
  }

  @And("the list should be ordered by purchase date in descending order")
  public void theListShouldBeOrderedByPurchaseDateInDescendingOrder() {
    boolean sorted =
        IntStream.range(0, orderList.size() - 1)
            .allMatch(
                i ->
                    orderList.get(i).getOrderDate().compareTo(orderList.get(i + 1).getOrderDate())
                        <= 0);
    assertTrue(sorted);
  }

  @And("the purchases were done on {string} and {string}")
  public void thePurchasesWereDoneOnAnd(String arg0, String arg1) {
    ArrayList<Order> tempOrders = (ArrayList<Order>) service.getOrderHistory(customer.getId());
    tempOrders.getFirst().setOrderDate(Date.valueOf(arg0));
    orderRepository.save(tempOrders.getFirst());

    tempOrders.get(1).setOrderDate(Date.valueOf(arg1));
    orderRepository.save(tempOrders.get(1));
  }

  @When("I apply a date filter from {string} to {string}")
  public void iApplyADateFilterFromTo(String arg0, String arg1) {
    try {
      orderList =
          (ArrayList<Order>) service.getOrderHistoryWithDateFilter(customer.getId(), arg0, arg1);
    } catch (Exception e) {
      lastException = e;
    }
  }

  @Then("I should see only the purchases made on {string}")
  public void iShouldSeeOnlyThePurchasesMadeOn(String arg0) {
    assertFalse(orderList.isEmpty());
    assertTrue(
        orderList.stream()
            .allMatch(order -> order.getOrderDate().compareTo(Date.valueOf(arg0)) == 0));
  }

  @Then("I should see an error message {string}")
  public void iShouldSeeAnErrorMessage(String arg0) {
    assertNotNull(lastException);
    assertEquals(arg0, lastException.getMessage());
  }
}
