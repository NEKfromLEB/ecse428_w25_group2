package ca.mcgill.ecse428.jerseycabinet.acceptance.US005;

import static org.junit.jupiter.api.Assertions.*;

import ca.mcgill.ecse428.jerseycabinet.dao.*;
import ca.mcgill.ecse428.jerseycabinet.model.*;
import ca.mcgill.ecse428.jerseycabinet.service.ManageCartService;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@CucumberContextConfiguration
@SpringBootTest
public class ManageCartSteps {
  @Autowired private CartRepository cartRepository;
  @Autowired private CartItemRepository cartItemRepository;
  @Autowired private JerseyRepository jerseyRepository;
  @Autowired private CustomerRepository customerRepository;
  @Autowired private EmployeeRepository employeeRepository;

  @Autowired private ManageCartService manageCartService;

  private List<Jersey> jerseyList;

  private Employee employee;
  private Customer customer;

  private ArrayList<Jersey> viewedCart;

  private Exception lastException;

  @After
  public void cleanUp() {
    Cart custCart = cartRepository.findCartByBuyer_Id(customer.getId());
    ArrayList<CartItem> cartItems = cartItemRepository.findCartItemsByKey_Cart_Id(custCart.getId());
    cartItemRepository.deleteAll(cartItems);
    cartRepository.delete(custCart);
    jerseyRepository.deleteAll(jerseyList);
    employeeRepository.delete(employee);
    customerRepository.delete(customer);
  }

  @Given("the following listings exist in the system")
  public void theFollowingListingsExistInTheSystem(DataTable dataTable) {
    List<Map<String, String>> listings = dataTable.asMaps(String.class, String.class);

    employee = new Employee("admin@jerseycabinet.com", "password");
    customer = new Customer("matpestel@gmail.com", "password", "Montreal");
    customerRepository.save(customer);
    employeeRepository.save(employee);

    jerseyList = new ArrayList<>();

    for (Map<String, String> row : listings) {
      String id = row.get("id");
      String description = row.get("title");
      int price = Integer.parseInt(row.get("price"));
      String color = row.get("color");
      String sport = row.get("sport");

      Jersey jersey =
          new Jersey(
              Jersey.RequestState.Listed,
              description,
              "",
              sport,
              color,
              "",
              "",
              price,
              customer);
      jerseyRepository.save(jersey);
      jerseyList.add(jersey);
    }
  }

  @Given("my cart is empty")
  public void myCartIsEmpty() {
    // new cart
  }

  @And("an item {string} is available in stock")
  public void anItemIsAvailableInStock(String arg0) {
    assertTrue(
        jerseyList.stream().anyMatch(jersey -> Objects.equals(jersey.getDescription(), arg0)));
  }

  @When("I add {string} to my cart")
  public void iAddToMyCart(String arg0) {
    int jersey_id =
        Objects.requireNonNull(
                jerseyList.stream()
                    .filter(jersey -> jersey.getDescription().equals(arg0))
                    .findFirst()
                    .orElse(null))
            .getId();
    try {
      manageCartService.addItemToCart(customer.getId(), jersey_id);
    } catch (Exception e) {
      lastException = e;
    }
  }

  @Then("{string} should be in my cart")
  public void shouldBeInMyCart(String arg0) {
    ArrayList<Jersey> jerseys =
        (ArrayList<Jersey>) manageCartService.findAllJerseysByCustomerID(customer.getId());
    assertTrue(jerseys.stream().anyMatch(jersey -> Objects.equals(jersey.getDescription(), arg0)));
  }

  @And("the cart should contain {int} item total")
  public void theCartShouldContainItemTotal(int arg0) {
    ArrayList<Jersey> jerseys =
        (ArrayList<Jersey>) manageCartService.findAllJerseysByCustomerID(customer.getId());
    assertEquals(arg0, jerseys.size());
  }

  @Given("my cart contains {string}")
  public void myCartContains(String arg0) {
    Cart customerCart = manageCartService.findCartByCustomerID(customer.getId());
    Jersey prevJersey =
        Objects.requireNonNull(
            jerseyList.stream()
                .filter(jersey -> jersey.getDescription().equals(arg0))
                .findFirst()
                .orElse(null));

    CartItem item = new CartItem(new CartItem.CartItemKey(customerCart, prevJersey));
    cartItemRepository.save(item);
  }

  @Then("the cart should contain {string} and {string}")
  public void theCartShouldContainAnd(String arg0, String arg1) {
    ArrayList<Jersey> jerseys =
        (ArrayList<Jersey>) manageCartService.findAllJerseysByCustomerID(customer.getId());
    assertTrue(jerseys.stream().anyMatch(jersey -> Objects.equals(jersey.getDescription(), arg0)));
    assertTrue(jerseys.stream().anyMatch(jersey -> Objects.equals(jersey.getDescription(), arg1)));
  }

  @Then("I should see an error message {string}")
  public void iShouldSeeAnErrorMessage(String arg0) {
    assertNotNull(lastException);
    assertEquals(arg0, lastException.getMessage());
  }

  @Given("an item {string} is marked out of stock")
  public void anItemIsMarkedOutOfStock(String arg0) {
    Jersey outOfStockJersey =
        Objects.requireNonNull(
            jerseyList.stream()
                .filter(jersey -> jersey.getDescription().equals(arg0))
                .findFirst()
                .orElse(null));
    outOfStockJersey.setRequestState(Jersey.RequestState.Bought);
    jerseyRepository.save(outOfStockJersey);
  }

  @Given("my cart contains {string} and {string}")
  public void myCartContainsAnd(String arg0, String arg1) {
    Cart customerCart = manageCartService.findCartByCustomerID(customer.getId());
    Jersey jersey1 =
        Objects.requireNonNull(
            jerseyList.stream()
                .filter(jersey -> jersey.getDescription().equals(arg0))
                .findFirst()
                .orElse(null));
    Jersey jersey2 =
        Objects.requireNonNull(
            jerseyList.stream()
                .filter(jersey -> jersey.getDescription().equals(arg1))
                .findFirst()
                .orElse(null));

    CartItem item1 = new CartItem(new CartItem.CartItemKey(customerCart, jersey1));
    CartItem item2 = new CartItem(new CartItem.CartItemKey(customerCart, jersey2));
    cartItemRepository.save(item1);
    cartItemRepository.save(item2);
  }

  @When("I remove {string} from the cart")
  public void iRemoveFromTheCart(String arg0) {
    int jersey_id =
        Objects.requireNonNull(
                jerseyList.stream()
                    .filter(jersey -> jersey.getDescription().equals(arg0))
                    .findFirst()
                    .orElse(null))
            .getId();
    try {
      manageCartService.removeItemFromCart(customer.getId(), jersey_id);
    } catch (Exception e) {
      lastException = e;
    }
  }

  @Then("{string} should no longer be in my cart")
  public void shouldNoLongerBeInMyCart(String arg0) {
    ArrayList<Jersey> jerseys =
        (ArrayList<Jersey>) manageCartService.findAllJerseysByCustomerID(customer.getId());
    assertFalse(jerseys.stream().anyMatch(jersey -> Objects.equals(jersey.getDescription(), arg0)));
  }

  @When("I view my cart")
  public void iViewMyCart() {
    viewedCart = manageCartService.viewCart(customer.getId());
  }

  @Then("I should see the items {string} and {string}")
  public void iShouldSeeTheItemsAnd(String arg0, String arg1) {
    assertNotNull(viewedCart);
    assertTrue(
        viewedCart.stream().anyMatch(jersey -> Objects.equals(jersey.getDescription(), arg0)));
    assertTrue(
        viewedCart.stream().anyMatch(jersey -> Objects.equals(jersey.getDescription(), arg1)));
  }

  @Then("I should see a message {string}")
  public void iShouldSeeAMessage(String arg0) {
    // Frontend implementation
  }

  @When("I choose to clear my cart")
  public void iChooseToClearMyCart() {
    manageCartService.clearCart(customer.getId());
  }
}
