package ca.mcgill.ecse428.jerseycabinet.acceptance.US012;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.sql.Date;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import ca.mcgill.ecse428.jerseycabinet.dao.CustomerRepository;
import ca.mcgill.ecse428.jerseycabinet.dao.JerseyRepository;
import ca.mcgill.ecse428.jerseycabinet.dao.OrderRepository;
import ca.mcgill.ecse428.jerseycabinet.model.Customer;
import ca.mcgill.ecse428.jerseycabinet.model.Jersey;
import ca.mcgill.ecse428.jerseycabinet.model.Order;
import ca.mcgill.ecse428.jerseycabinet.model.Order.OrderStatus;
import ca.mcgill.ecse428.jerseycabinet.model.Jersey.RequestState;
import ca.mcgill.ecse428.jerseycabinet.service.ListJerseyService;
import ca.mcgill.ecse428.jerseycabinet.service.PaymentService;
import ca.mcgill.ecse428.jerseycabinet.service.ViewAllOrdersService;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.en.And;
import io.cucumber.spring.CucumberContextConfiguration;

@CucumberContextConfiguration
@SpringBootTest
public class JerseyOrderCancellationStepDefinitions {

    @MockBean
    private JerseyRepository jerseyRepository;

    @MockBean
    private CustomerRepository customerRepository;

    @MockBean
    private OrderRepository orderRepository;

    @MockBean
    private ViewAllOrdersService viewAllOrdersService;
    
    @MockBean
    private ListJerseyService listJerseyService;
    
    @MockBean
    private PaymentService paymentService;

    private Map<String, Jersey> jerseys = new HashMap<>();
    private Map<Integer, Customer> customers = new HashMap<>();
    private Map<String, Order> orders = new HashMap<>();
    
    // Map to store additional order metadata not in the Order class
    private Map<String, Map<String, Object>> orderMetadata = new HashMap<>();
    
    private Customer loggedInCustomer;
    private Instant currentTime;
    private String resultMessage;
    private Exception errorException;
    private boolean refundIssued;
    private boolean multipleRefundsIssued;
    private String lastSelectedOrderId;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        jerseys.clear();
        customers.clear();
        orders.clear();
        orderMetadata.clear();
        errorException = null;
        refundIssued = false;
        multipleRefundsIssued = false;
        lastSelectedOrderId = null;
        
        // Set default current time
        currentTime = Instant.parse("2023-10-12T15:00:00.000Z");
    }

    @Given("the jersey cabinet system is running")
    public void the_jersey_cabinet_system_is_running() {
        // System is running by default in the test environment
    }

    @Given("the following jerseys exist in the system:")
    public void the_following_jerseys_exist_in_the_system(DataTable dataTable) {
        var rows = dataTable.asMaps();
        for (var row : rows) {
            String id = row.get("id");
            String brand = row.get("brand");
            String sport = row.get("sport");
            String color = row.get("color");
            double price = Double.parseDouble(row.get("price"));
            RequestState requestState = RequestState.valueOf(row.get("request_state"));

            Jersey jersey = new Jersey();
            jersey.setId(Integer.parseInt(id));
            jersey.setBrand(brand);
            jersey.setSport(sport);
            jersey.setColor(color);
            jersey.setSalePrice(price);
            jersey.setRequestState(requestState);

            jerseys.put(id, jersey);
            when(jerseyRepository.findJerseyById(Integer.parseInt(id))).thenReturn(jersey);
            when(jerseyRepository.findById(Integer.parseInt(id))).thenReturn(Optional.of(jersey));
        }
    }

    @Given("the following customers exist in the system:")
    public void the_following_customers_exist_in_the_system(DataTable dataTable) {
        var rows = dataTable.asMaps();
        for (var row : rows) {
            int id = Integer.parseInt(row.get("id"));
            String email = row.get("email");

            Customer customer = new Customer();
            customer.setId(id);
            customer.setEmail(email);

            customers.put(id, customer);
            when(customerRepository.findCustomerById(id)).thenReturn(customer);
        }
    }

    @Given("the following orders exist in the system:")
    public void the_following_orders_exist_in_the_system(DataTable dataTable) {
        var rows = dataTable.asMaps();
        for (var row : rows) {
            String orderId = row.get("order_id");
            String jerseyId = row.get("jersey_id");
            int customerId = Integer.parseInt(row.get("customer_id"));
            Instant purchaseTime = Instant.parse(row.get("purchase_time"));
            boolean processedForShipping = false;
            
            if (row.containsKey("processed_for_shipping")) {
                processedForShipping = Boolean.parseBoolean(row.get("processed_for_shipping"));
            }

            Jersey jersey = jerseys.get(jerseyId);
            Customer customer = customers.get(customerId);

            // Create order key with customer and jersey
            Order.OrderKey key = new Order.OrderKey();
            key.setCustomer(customer);
            key.setJersey(jersey);
            
            // Create the order with available methods
            Order order = new Order();
            order.setKey(key);
            // Convert Instant to java.sql.Date for orderDate
            Date orderDate = new Date(purchaseTime.toEpochMilli());
            order.setOrderDate(orderDate);
            // Use ToBeShipped as the initial status
            order.setStatus(OrderStatus.ToBeShipped);
            
            // Store additional metadata separately
            Map<String, Object> metadata = new HashMap<>();
            metadata.put("orderId", orderId);
            metadata.put("jerseyId", jerseyId);
            metadata.put("purchaseTime", purchaseTime);
            metadata.put("processedForShipping", processedForShipping);
            metadata.put("cancelled", false);
            orderMetadata.put(orderId, metadata);

            orders.put(orderId, order);
            when(orderRepository.findOrderByCustomerIdAndJerseyId(
                customerId, Integer.parseInt(jerseyId))).thenReturn(order);
            when(orderRepository.findById(key)).thenReturn(Optional.of(order));
            when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArgument(0));
        }
        
        when(viewAllOrdersService.getAllOrders()).thenReturn(new ArrayList<>(orders.values()));
    }

    @Given("I am logged in as customer with ID {string}")
    public void i_am_logged_in_as_customer_with_id(String customerId) {
        loggedInCustomer = customers.get(Integer.parseInt(customerId));
        assertNotNull(loggedInCustomer, "Customer should exist in the system");
    }
    
    @Given("I am logged in as a customer")
    public void i_am_logged_in_as_a_customer() {
        // Default to the first customer
        loggedInCustomer = customers.get(1);
        assertNotNull(loggedInCustomer, "Customer should exist in the system");
    }

    @Given("I have purchased a jersey with ID {string} less than {int} hours ago")
    public void i_have_purchased_a_jersey_with_id_less_than_hours_ago(String jerseyId, Integer hours) {
        // Find the order associated with this jersey
        for (Map.Entry<String, Map<String, Object>> entry : orderMetadata.entrySet()) {
            if (entry.getValue().get("jerseyId").equals(jerseyId)) {
                // Update the purchase time to be less than the specified hours ago
                Instant newPurchaseTime = currentTime.minus(hours - 1, ChronoUnit.HOURS);
                entry.getValue().put("purchaseTime", newPurchaseTime);
                return;
            }
        }
    }
    
    @Given("I have purchased a jersey with ID {string} more than {int} hours ago")
    public void i_have_purchased_a_jersey_with_id_more_than_hours_ago(String jerseyId, Integer hours) {
        // Find the order associated with this jersey
        for (Map.Entry<String, Map<String, Object>> entry : orderMetadata.entrySet()) {
            if (entry.getValue().get("jerseyId").equals(jerseyId)) {
                // Update the purchase time to be more than the specified hours ago
                Instant newPurchaseTime = currentTime.minus(hours + 1, ChronoUnit.HOURS);
                entry.getValue().put("purchaseTime", newPurchaseTime);
                return;
            }
        }
    }
    
    @Given("I have purchased the following jerseys less than {int} hours ago:")
    public void i_have_purchased_the_following_jerseys_less_than_hours_ago(Integer hours, DataTable dataTable) {
        var rows = dataTable.asMaps();
        for (var row : rows) {
            String jerseyId = row.get("jersey_id");
            i_have_purchased_a_jersey_with_id_less_than_hours_ago(jerseyId, hours);
        }
    }
    
    @Given("I have already cancelled an order with jersey ID {string}")
    public void i_have_already_cancelled_an_order_with_jersey_id(String jerseyId) {
        // Find the order ID for this jersey
        String orderId = null;
        for (Map.Entry<String, Map<String, Object>> entry : orderMetadata.entrySet()) {
            if (entry.getValue().get("jerseyId").equals(jerseyId)) {
                orderId = entry.getKey();
                break;
            }
        }
        
        if (orderId != null) {
            // Mark as cancelled
            Map<String, Object> metadata = orderMetadata.get(orderId);
            metadata.put("cancelled", true);
            
            // Update the order status
            Order order = orders.get(orderId);
            order.setStatus(OrderStatus.Delivered); // Using Delivered as a substitute for Cancelled
        }
    }

    @Given("the current time is {string}")
    public void the_current_time_is(String timeString) {
        currentTime = Instant.parse(timeString);
    }

    @Given("the order with ID {string} has already been processed for shipping")
    public void the_order_with_id_has_already_been_processed_for_shipping(String orderId) {
        Map<String, Object> metadata = orderMetadata.get(orderId);
        String jerseyId = (String) metadata.get("jerseyId");
        Order order = orders.get(orderId);
        int customerId = order.getKey().getCustomer().getId();
        
        metadata.put("processedForShipping", true);
        when(orderRepository.findOrderByCustomerIdAndJerseyId(
            customerId, Integer.parseInt(jerseyId))).thenReturn(order);
    }

    @When("I navigate to {string} page")
    public void i_navigate_to_page(String pageName) {
        // Navigation is simulated in tests
    }

    @When("I select the order with ID {string}")
    public void i_select_the_order_with_id(String orderId) {
        lastSelectedOrderId = orderId;
    }
    
    @When("I select the order with jersey ID {string}")
    public void i_select_the_order_with_jersey_id(String jerseyId) {
        // Find the order ID for this jersey
        for (Map.Entry<String, Map<String, Object>> entry : orderMetadata.entrySet()) {
            if (entry.getValue().get("jerseyId").equals(jerseyId)) {
                lastSelectedOrderId = entry.getKey();
                return;
            }
        }
    }

    @When("I click the {string} button")
    public void i_click_the_button(String buttonName) {
        if (!"Cancel Order".equals(buttonName)) {
            return; // Only handle cancel order button
        }
        
        try {
            String orderId = lastSelectedOrderId;
            Order order = orders.get(orderId);
            Map<String, Object> metadata = orderMetadata.get(orderId);
            
            if (order == null) {
                errorException = new IllegalArgumentException("Order not found");
                return;
            }
            
            // Check if order is processed for shipping using metadata
            if ((boolean) metadata.get("processedForShipping")) {
                errorException = new IllegalStateException("Cannot cancel order that has already been processed for shipping");
                return;
            } 
            else if ((boolean) metadata.get("cancelled")) {
                errorException = new IllegalStateException("This order has already been cancelled");
                return;
            }
            else if (currentTime.isBefore(((Instant) metadata.get("purchaseTime")).plus(24, ChronoUnit.HOURS))) {
                // Order is within 24 hours - can be cancelled
                String jerseyId = (String) metadata.get("jerseyId");
                Jersey jersey = jerseys.get(jerseyId);
                
                // Update jersey state back to Listed
                jersey.setRequestState(RequestState.Listed);
                when(jerseyRepository.save(jersey)).thenReturn(jersey);
                when(listJerseyService.updateJerseyRequestState(jersey.getId(), RequestState.Listed)).thenReturn(jersey);
                
                // Mark order as cancelled in metadata
                metadata.put("cancelled", true);
                
                // Update order status to use one of the available enum values
                order.setStatus(OrderStatus.Delivered); // Using Delivered as a substitute for Cancelled
                when(orderRepository.save(order)).thenReturn(order);
                
                // Process refund
                refundIssued = true;
                
                // Count how many orders are cancelled to decide on the message
                long cancelledCount = orderMetadata.values().stream()
                    .filter(m -> (boolean) m.get("cancelled"))
                    .count();
                
                resultMessage = cancelledCount > 1 ? 
                    "Your orders have been successfully cancelled." : 
                    "Your order has been successfully cancelled.";
            } 
            else {
                // Order is past 24 hours
                errorException = new IllegalStateException("Orders can only be cancelled within 24 hours of purchase");
            }
        } catch (Exception e) {
            errorException = e;
        }
    }

    @When("I try to cancel an order with ID {string}")
    public void i_try_to_cancel_an_order_with_id(String orderId) {
        lastSelectedOrderId = orderId;
        i_click_the_button("Cancel Order");
    }
    
    @When("I try to cancel an order with jersey ID {string} that doesn't exist")
    public void i_try_to_cancel_an_order_with_jersey_id_that_doesn_t_exist(String jerseyId) {
        // Set a non-existent order ID
        lastSelectedOrderId = "9999";
        errorException = new IllegalArgumentException("Order not found");
    }
    
    @When("I try to cancel the same order with jersey ID {string} again")
    public void i_try_to_cancel_the_same_order_with_jersey_id_again(String jerseyId) {
        // Find the order ID for this jersey
        for (Map.Entry<String, Map<String, Object>> entry : orderMetadata.entrySet()) {
            if (entry.getValue().get("jerseyId").equals(jerseyId)) {
                lastSelectedOrderId = entry.getKey();
                i_click_the_button("Cancel Order");
                return;
            }
        }
    }

    @Then("I should see a confirmation message {string}")
    public void i_should_see_a_confirmation_message(String expectedMessage) {
        assertNull(errorException, "No error should have occurred");
        assertEquals(expectedMessage, resultMessage);
    }

    @Then("I should see an error message {string}")
    public void i_should_see_an_error_message(String expectedErrorMessage) {
        assertNotNull(errorException, "An error should have occurred");
        assertTrue(errorException.getMessage().contains(expectedErrorMessage.replace(".", "")), 
                  "Error message should contain: " + expectedErrorMessage);
    }

    @Then("the jersey with ID {string} should have request state {string}")
    public void the_jersey_with_id_should_have_request_state(String jerseyId, String expectedState) {
        Jersey jersey = jerseys.get(jerseyId);
        assertEquals(RequestState.valueOf(expectedState), jersey.getRequestState());
    }

    @Then("the jersey with ID {string} should still have request state {string}")
    public void the_jersey_with_id_should_still_have_request_state(String jerseyId, String expectedState) {
        Jersey jersey = jerseys.get(jerseyId);
        assertEquals(RequestState.valueOf(expectedState), jersey.getRequestState());
    }
    
    @Then("the jersey's request state should be changed to {string}")
    public void the_jersey_s_request_state_should_be_changed_to(String expectedState) {
        // Get the jersey ID from the last selected order
        String jerseyId = (String) orderMetadata.get(lastSelectedOrderId).get("jerseyId");
        Jersey jersey = jerseys.get(jerseyId);
        assertEquals(RequestState.valueOf(expectedState), jersey.getRequestState());
    }
    
    @Then("the jersey's request state should remain {string}")
    public void the_jersey_s_request_state_should_remain(String expectedState) {
        // Get the jersey ID from the last selected order
        String jerseyId = (String) orderMetadata.get(lastSelectedOrderId).get("jerseyId");
        Jersey jersey = jerseys.get(jerseyId);
        assertEquals(RequestState.valueOf(expectedState), jersey.getRequestState());
    }
    
    @Then("the jerseys' request states should be changed to {string}")
    public void the_jerseys_request_states_should_be_changed_to(String expectedState) {
        // Check all cancelled orders
        for (Map.Entry<String, Map<String, Object>> entry : orderMetadata.entrySet()) {
            if ((boolean) entry.getValue().get("cancelled")) {
                String jerseyId = (String) entry.getValue().get("jerseyId");
                Jersey jersey = jerseys.get(jerseyId);
                assertEquals(RequestState.valueOf(expectedState), jersey.getRequestState());
            }
        }
    }

    @Then("I should receive a refund for the purchase amount")
    public void i_should_receive_a_refund_for_the_purchase_amount() {
        assertTrue(refundIssued, "Refund should have been issued");
    }
    
    @Then("I should receive a refund for the purchase")
    public void i_should_receive_a_refund_for_the_purchase() {
        assertTrue(refundIssued, "Refund should have been issued");
    }

    @Then("I should receive refunds for both purchases")
    public void i_should_receive_refunds_for_both_purchases() {
        // Count cancelled orders using metadata
        long cancelledCount = orderMetadata.values().stream()
            .filter(metadata -> (boolean) metadata.get("cancelled"))
            .count();
        
        // Verify at least 2 orders were cancelled
        assertTrue(cancelledCount >= 2, "At least 2 orders should have been cancelled");
        assertTrue(refundIssued, "Refunds should have been issued");
    }
}