package ca.mcgill.ecse428.jerseycabinet.acceptance;

import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ca.mcgill.ecse428.jerseycabinet.dao.CustomerRepository;
import ca.mcgill.ecse428.jerseycabinet.dao.JerseyRepository;
import ca.mcgill.ecse428.jerseycabinet.dao.OrderRepository;
import ca.mcgill.ecse428.jerseycabinet.model.Order;
import ca.mcgill.ecse428.jerseycabinet.model.Order.OrderKey;
import ca.mcgill.ecse428.jerseycabinet.model.Order.OrderStatus;
import ca.mcgill.ecse428.jerseycabinet.model.Customer;
import ca.mcgill.ecse428.jerseycabinet.model.Jersey;
import ca.mcgill.ecse428.jerseycabinet.service.ViewAllOrdersService;
import ca.mcgill.ecse428.jerseycabinet.service.FilterOrdersByStatusService;
import ca.mcgill.ecse428.jerseycabinet.service.SearchOrdersService;
import ca.mcgill.ecse428.jerseycabinet.service.MarkOrderAsDeliveredService;
import ca.mcgill.ecse428.jerseycabinet.exceptions.OrderNotFoundException;
import java.sql.Date;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
@CucumberContextConfiguration
@SpringBootTest
public class ReviewOrderSteps {

    @Autowired
    private ViewAllOrdersService viewAllOrdersService;

    @Autowired
    private FilterOrdersByStatusService filterOrdersByStatusService;

    @Autowired
    private SearchOrdersService searchOrdersService;

    @Autowired
    private MarkOrderAsDeliveredService markOrderAsDeliveredService;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private JerseyRepository jerseyRepository;

    @Autowired
    private OrderRepository orderRepository;

    private List<Order> orders;
    private Order searchedOrder;
    private Exception searchException;
    private Order updatedOrder;

    // Helper method to create orders from a data table
    private void createOrdersFromDataTable(io.cucumber.datatable.DataTable dataTable) {
        List<List<String>> rows = dataTable.asLists(String.class);

        for (int i = 1; i < rows.size(); i++) { // Skip header row
            List<String> row = rows.get(i);
            String customerEmail = row.get(0);
            String jerseyDescription = row.get(1);
            Date orderDate = Date.valueOf(row.get(2));
            OrderStatus status = OrderStatus.valueOf(row.get(3));

            Customer customer = new Customer();
            customer.setEmail(customerEmail);
            customerRepository.save(customer);

            Jersey jersey = new Jersey();
            jersey.setDescription(jerseyDescription);
            jerseyRepository.save(jersey);

            OrderKey key = new OrderKey(customer, jersey);
            Order order = new Order(key, orderDate, status);
            orderRepository.save(order);
        }
    }

    @Given("the following orders exist in the system")
    public void the_following_orders_exist_in_the_system(io.cucumber.datatable.DataTable dataTable) {
        createOrdersFromDataTable(dataTable);
    }

    @When("I navigate to the orders page")
    public void i_navigate_to_the_orders_page() {
        orders = viewAllOrdersService.getAllOrders();
    }

    @Then("I see a list of all orders with their status:")
    public void i_see_a_list_of_all_orders_with_their_status(io.cucumber.datatable.DataTable dataTable) {
        List<List<String>> expectedOrders = dataTable.asLists(String.class);
        int expectedSize = expectedOrders.size() - 1;
        assertEquals(expectedSize, orders.size(), "The number of orders should match");

        for (int i = 1; i < expectedOrders.size(); i++) {
            List<String> expectedRow = expectedOrders.get(i);
            String expectedEmail = expectedRow.get(0);
            String expectedJerseyDesc = expectedRow.get(1);
            Date expectedDate = Date.valueOf(expectedRow.get(2));
            OrderStatus expectedStatus = OrderStatus.valueOf(expectedRow.get(3));

            Order actualOrder = orders.get(i - 1);
            assertEquals(expectedEmail, actualOrder.getKey().getCustomer().getEmail());
            assertEquals(expectedJerseyDesc, actualOrder.getKey().getJersey().getDescription());
            assertEquals(expectedDate, actualOrder.getOrderDate());
            assertEquals(expectedStatus, actualOrder.getStatus());
        }
    }

    @Given("there are certain orders in the system:")
    public void there_are_certain_orders_in_the_system(io.cucumber.datatable.DataTable dataTable) {
        createOrdersFromDataTable(dataTable);
    }

    @When("I filter orders by a specific status {string}")
    public void i_filter_orders_by_a_specific_status(String status) {
        OrderStatus orderStatus = OrderStatus.valueOf(status);
        orders = filterOrdersByStatusService.filterOrdersByStatus(orderStatus);
    }

    @Then("I only see orders that match the selected status:")
    public void i_only_see_orders_that_match_the_selected_status(io.cucumber.datatable.DataTable dataTable) {
        List<List<String>> expectedOrders = dataTable.asLists(String.class);
        int expectedSize = expectedOrders.size() - 1;
        assertEquals(expectedSize, orders.size(), "The number of filtered orders should match");

        for (int i = 1; i < expectedOrders.size(); i++) {
            List<String> expectedRow = expectedOrders.get(i);
            String expectedEmail = expectedRow.get(0);
            String expectedJerseyDesc = expectedRow.get(1);
            Date expectedDate = Date.valueOf(expectedRow.get(2));
            OrderStatus expectedStatus = OrderStatus.valueOf(expectedRow.get(3));

            Order actualOrder = orders.get(i - 1);
            assertEquals(expectedEmail, actualOrder.getKey().getCustomer().getEmail());
            assertEquals(expectedJerseyDesc, actualOrder.getKey().getJersey().getDescription());
            assertEquals(expectedDate, actualOrder.getOrderDate());
            assertEquals(expectedStatus, actualOrder.getStatus());
        }
    }

    @When("I search for an order using {string} and {string}")
    public void i_search_for_an_order_using_customer_email_and_jersey_description(String customerEmail, String jerseyDescription) {
        try {
            searchedOrder = searchOrdersService.searchOrderByCustomerEmailAndJerseyDescription(customerEmail, jerseyDescription);
            searchException = null;
        } catch (OrderNotFoundException e) {
            searchException = e;
            searchedOrder = null;
        }
    }

    @Then("I see the relevant order in the list:")
    public void i_see_the_relevant_order_in_the_list(io.cucumber.datatable.DataTable dataTable) {
        List<List<String>> expectedOrders = dataTable.asLists(String.class);
        assertEquals(2, expectedOrders.size(), "Expected exactly one order in the result");

        List<String> expectedRow = expectedOrders.get(1);
        String expectedEmail = expectedRow.get(0);
        String expectedJerseyDesc = expectedRow.get(1);
        Date expectedDate = Date.valueOf(expectedRow.get(2));
        OrderStatus expectedStatus = OrderStatus.valueOf(expectedRow.get(3));

        assertNotNull(searchedOrder, "Searched order should not be null");
        assertEquals(expectedEmail, searchedOrder.getKey().getCustomer().getEmail());
        assertEquals(expectedJerseyDesc, searchedOrder.getKey().getJersey().getDescription());
        assertEquals(expectedDate, searchedOrder.getOrderDate());
        assertEquals(expectedStatus, searchedOrder.getStatus());
    }

    @Then("I see a message indicating {string}")
    public void i_see_a_message_indicating(String expectedMessage) {
        assertNotNull(searchException, "An exception should have been thrown for non-existent order");
        assertTrue(searchException instanceof OrderNotFoundException, "Exception should be OrderNotFoundException");
        assertEquals(expectedMessage, searchException.getMessage());
    }

    @When("I mark an order as delivered with {string} and {string}")
    public void i_mark_an_order_as_delivered_with_customer_email_and_jersey_description(String customerEmail, String jerseyDescription) {
        try {
            updatedOrder = markOrderAsDeliveredService.markOrderAsDelivered(customerEmail, jerseyDescription);
        } catch (OrderNotFoundException e) {
            // This is a normal flow scenario, so we don't expect an exception here
            throw new RuntimeException("Unexpected OrderNotFoundException in normal flow", e);
        }
    }

    @Then("the order status is updated to \"Delivered\":")
    public void the_order_status_is_updated_to_delivered(io.cucumber.datatable.DataTable dataTable) {
        List<List<String>> expectedOrders = dataTable.asLists(String.class);
        assertEquals(2, expectedOrders.size(), "Expected exactly one order in the result");

        List<String> expectedRow = expectedOrders.get(1);
        String expectedEmail = expectedRow.get(0);
        String expectedJerseyDesc = expectedRow.get(1);
        Date expectedDate = Date.valueOf(expectedRow.get(2));
        OrderStatus expectedStatus = OrderStatus.valueOf(expectedRow.get(3));

        assertNotNull(updatedOrder, "Updated order should not be null");
        assertEquals(expectedEmail, updatedOrder.getKey().getCustomer().getEmail());
        assertEquals(expectedJerseyDesc, updatedOrder.getKey().getJersey().getDescription());
        assertEquals(expectedDate, updatedOrder.getOrderDate());
        assertEquals(expectedStatus, updatedOrder.getStatus());
    }

    @After
    @Transactional
    public void cleanup() {
        try {
            orderRepository.deleteAll();
            jerseyRepository.deleteAll();
            customerRepository.deleteAll();
        } catch (Exception e) {
            System.err.println("Cleanup failed: " + e.getMessage());
            throw e;
        }
    }
}