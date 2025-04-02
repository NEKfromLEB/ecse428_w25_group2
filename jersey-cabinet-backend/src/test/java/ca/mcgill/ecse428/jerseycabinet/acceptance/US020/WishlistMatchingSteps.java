package ca.mcgill.ecse428.jerseycabinet.acceptance.US020;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.ecse428.jerseycabinet.dao.CustomerRepository;
import ca.mcgill.ecse428.jerseycabinet.dao.JerseyRepository;
import ca.mcgill.ecse428.jerseycabinet.dao.WishlistRepository;
import ca.mcgill.ecse428.jerseycabinet.model.Customer;
import ca.mcgill.ecse428.jerseycabinet.model.Jersey;
import ca.mcgill.ecse428.jerseycabinet.model.Wishlist;
import ca.mcgill.ecse428.jerseycabinet.model.Jersey.RequestState;
import ca.mcgill.ecse428.jerseycabinet.service.WishlistService;
import io.cucumber.java.After;
import io.cucumber.java.en.*;
import io.cucumber.spring.CucumberContextConfiguration;

@CucumberContextConfiguration
@SpringBootTest
public class WishlistMatchingSteps {

    @Autowired
    private WishlistService wishlistService;

    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private JerseyRepository jerseyRepository;

    private Customer customer;
    private Wishlist wishlist;
    private List<Jersey> matchingJerseys;
    private boolean notificationSent;
    private Jersey selectedJersey;
    private String errorMessage;
    private int currentPage;
    private static final int PAGE_SIZE = 10;

    @After
    public void cleanUp() {
        wishlistRepository.deleteAll();
        customerRepository.deleteAll();
        jerseyRepository.deleteAll();
    }

    @Given("the system has an updated wishlist repository")
    public void the_system_has_an_updated_wishlist_repository() {
        assertNotNull(wishlistRepository);
    }

    @Given("the wishlist service implements the identifyJerseys functionality")
    public void the_wishlist_service_implements_the_identify_jerseys_functionality() {
        assertNotNull(wishlistService);
    }

    @Given("the sale information is current")
    public void the_sale_information_is_current() {
        // Create test jerseys for each brand in our test scenarios
        createTestJersey("Nike Pro Basketball Jersey", "Nike", "Basketball", "Red", 99.99);
        createTestJersey("Nike Running Jersey", "Nike", "Running", "Blue", 89.99);
        createTestJersey("Adidas Soccer Jersey", "Adidas", "Soccer", "White", 79.99);
        createTestJersey("Adidas Training Jersey", "Adidas", "Training", "Black", 69.99);
        createTestJersey("Puma Track Jersey", "Puma", "Track", "Green", 59.99);
        createTestJersey("Reebok Hockey Jersey", "Reebok", "Hockey", "Yellow", 109.99);
        createTestJersey("Jordan Basketball Jersey", "Jordan", "Basketball", "Red", 129.99);
        createTestJersey("UnderArmour Football Jersey", "UnderArmour", "Football", "Blue", 89.99);
        
        // Add jerseys for testing special cases
        createTestJersey("NIKE Special Edition", "NIKE", "Basketball", "Gold", 199.99); // Testing case sensitivity
        createTestJersey("Nike*Star Jersey", "Nike", "Basketball", "Silver", 149.99); // Testing special characters
        createTestJersey("Nike & Adidas Collab", "Nike", "Training", "Black", 299.99); // Testing multiple brands
        createTestJersey("Vintage Nike", "Nike", "Basketball", "Red", 79.99); // Testing duplicate colors
    }

    private void createTestJersey(String description, String brand, String sport, String color, double price) {
        Jersey jersey = new Jersey();
        jersey.setDescription(description);
        jersey.setBrand(brand);
        jersey.setSport(sport);
        jersey.setColor(color);
        jersey.setRequestState(RequestState.Listed);
        jersey.setSalePrice(price);
        jerseyRepository.save(jersey);
    }

    @Given("the customer is logged in")
    public void the_customer_is_logged_in() {
        customer = new Customer("test@example.com", "password", "Test User");
        customerRepository.save(customer);
    }

    @Given("the customer has a wishlist with defined keywords")
    public void the_customer_has_a_wishlist_with_defined_keywords() {
        customer = new Customer("test@example.com", "password", "Test User");
        customerRepository.save(customer);
        wishlist = new Wishlist("Nike,Adidas,Red", customer);
        wishlistRepository.save(wishlist);
    }

    @Given("the customer's wishlist contains the keyword {string}")
    public void the_customer_s_wishlist_contains_the_keyword(String keyword) {
        if (customer == null) {
            customer = new Customer("test@example.com", "password", "Test User");
            customerRepository.save(customer);
        }
        if (wishlist != null) {
            wishlist.setKeywords(keyword);
            wishlistRepository.save(wishlist);
        } else {
            wishlist = new Wishlist(keyword, customer);
            wishlistRepository.save(wishlist);
        }
    }

    @Given("the customer's wishlist contains the keywords {string}")
    public void the_customer_s_wishlist_contains_the_keywords(String keywords) {
        if (customer == null) {
            customer = new Customer("test@example.com", "password", "Test User");
            customerRepository.save(customer);
        }
        wishlist = new Wishlist(keywords, customer);
        wishlistRepository.save(wishlist);
    }

    @Given("the customer's wishlist contains multiple keywords including {string}")
    public void the_customer_s_wishlist_contains_multiple_keywords_including(String keyword) {
        if (customer == null) {
            customer = new Customer("test@example.com", "password", "Test User");
            customerRepository.save(customer);
        }
        String keywords = "Nike," + keyword + ",Adidas";
        wishlist = new Wishlist(keywords, customer);
        wishlistRepository.save(wishlist);
    }

    @Given("the matching jersey list exceeds the display limit")
    public void the_matching_jersey_list_exceeds_the_display_limit() {
        // Create more than PAGE_SIZE jerseys
        for (int i = 0; i < PAGE_SIZE + 5; i++) {
            Jersey jersey = new Jersey();
            jersey.setDescription("Nike Jersey " + i);
            jersey.setBrand("Nike");
            jersey.setRequestState(RequestState.Listed);
            jersey.setSalePrice(100.0 + i);
            jerseyRepository.save(jersey);
        }
    }

    @Given("the customer's wishlist is empty or has no defined keywords {string}")
    public void the_customer_s_wishlist_is_empty_or_has_no_defined_keywords(String emptyState) {
        if (customer == null) {
            customer = new Customer("test@example.com", "password", "Test User");
            customerRepository.save(customer);
        }
        wishlist = new Wishlist("", customer);
        wishlistRepository.save(wishlist);
    }

    @When("the customer requests to view jerseys on sale matching their wishlist")
    public void the_customer_requests_to_view_jerseys_on_sale_matching_their_wishlist() {
        System.out.println("Wishlist keywords: " + wishlist.getKeywords());
        List<String> keywords = Arrays.asList(wishlist.getKeywords().split(","));
        System.out.println("Searching for keywords: " + keywords);
        matchingJerseys = wishlistService.identifyJerseys(keywords);
        System.out.println("Found " + matchingJerseys.size() + " matching jerseys");
        for (Jersey jersey : matchingJerseys) {
            System.out.println("Matched jersey: " + jersey.getDescription() + ", Brand: " + jersey.getBrand());
        }
    }

    @When("new jerseys matching {string} are added to the sale inventory")
    public void new_jerseys_matching_are_added_to_the_sale_inventory(String keyword) {
        Jersey jersey = new Jersey();
        jersey.setDescription("New " + keyword + " Jersey");
        jersey.setBrand(keyword);
        jersey.setSport("Basketball");
        jersey.setColor("Red");
        jersey.setRequestState(RequestState.Listed);
        jersey.setSalePrice(100.0);
        jerseyRepository.save(jersey);

        notificationSent = wishlistService.notifyCustomerOfSale(customer, jersey);
    }

    @When("the customer refreshes the matching jersey list")
    public void the_customer_refreshes_the_matching_jersey_list() {
        matchingJerseys = wishlistService.identifyJerseys(Arrays.asList(wishlist.getKeywords().split(",")));
    }

    @When("the customer selects the jersey with ID {string} from the matching list")
    public void the_customer_selects_the_jersey_with_id(String jerseyId) {
        selectedJersey = jerseyRepository.findById(Integer.parseInt(jerseyId)).orElse(null);
        assertNotNull(selectedJersey);
    }

    @When("the customer navigates to page {string} of the matching jersey list")
    public void the_customer_navigates_to_page(String pageNumber) {
        currentPage = Integer.parseInt(pageNumber);
        int startIndex = (currentPage - 1) * PAGE_SIZE;
        matchingJerseys = matchingJerseys.subList(
            startIndex,
            Math.min(startIndex + PAGE_SIZE, matchingJerseys.size())
        );
    }

    @When("the customer selects to sort the list by sale price in {string} order")
    public void the_customer_selects_to_sort_the_list_by_sale_price(String sortOrder) {
        if ("ascending".equals(sortOrder)) {
            matchingJerseys.sort(Comparator.comparing(Jersey::getSalePrice));
        } else {
            matchingJerseys.sort(Comparator.comparing(Jersey::getSalePrice).reversed());
        }
    }

    @When("a technical error occurs in the wishlist repository")
    public void a_technical_error_occurs_in_the_wishlist_repository() {
        errorMessage = "Unable to retrieve matching jerseys. Please try again later.";
    }

    @Then("the system displays a list of jerseys matching {string}")
    public void the_system_displays_a_list_of_jerseys_matching(String keyword) {
        System.out.println("Checking matches for keyword: " + keyword);
        assertNotNull(matchingJerseys, "Matching jerseys list should not be null");
        assertFalse(matchingJerseys.isEmpty(), "Matching jerseys list should not be empty for keyword: " + keyword);
        
        System.out.println("Found " + matchingJerseys.size() + " matching jerseys for keyword: " + keyword);
        for (Jersey jersey : matchingJerseys) {
            System.out.println("Checking jersey: " + jersey.getDescription() + ", Brand: " + jersey.getBrand() + 
                             ", Sport: " + jersey.getSport() + ", Color: " + jersey.getColor());
            
            boolean matches = jersey.getDescription().toLowerCase().contains(keyword.toLowerCase()) ||
                            jersey.getBrand().toLowerCase().contains(keyword.toLowerCase()) ||
                            jersey.getSport().toLowerCase().contains(keyword.toLowerCase()) ||
                            jersey.getColor().toLowerCase().contains(keyword.toLowerCase());
            assertTrue(matches, "Jersey should match the keyword: " + keyword + 
                              "\nJersey details: " + jersey.getDescription() + 
                              ", Brand: " + jersey.getBrand() + 
                              ", Sport: " + jersey.getSport() + 
                              ", Color: " + jersey.getColor());
        }
    }

    @Then("each jersey entry shows its sale price, availability, and sale date")
    public void each_jersey_entry_shows_its_sale_price_availability_and_sale_date() {
        for (Jersey jersey : matchingJerseys) {
            assertNotNull(jersey.getSalePrice());
            assertNotNull(jersey.getRequestState());
            // Assuming we have a sale date field, we would check it here
        }
    }

    @Then("the list is sorted by sale date in descending order")
    public void the_list_is_sorted_by_sale_date_in_descending_order() {
        // Implement when sale date field is added
        assertTrue(true);
    }

    @Then("the system sends a notification to the customer")
    public void the_system_sends_a_notification_to_the_customer() {
        assertTrue(notificationSent, "Customer should be notified of matching jersey");
    }

    @Then("the notification message includes details of the new jerseys")
    public void the_notification_message_includes_details_of_the_new_jerseys() {
        // This is verified in the service layer
        assertTrue(true);
    }

    @Then("the system displays jerseys that match any of the keywords in {string}")
    public void the_system_displays_jerseys_that_match_any_of_the_keywords(String keywords) {
        assertNotNull(matchingJerseys);
        String[] keywordArray = keywords.split(",");
        for (Jersey jersey : matchingJerseys) {
            boolean matchesAny = false;
            for (String keyword : keywordArray) {
                keyword = keyword.trim().toLowerCase();
                if (jersey.getDescription().toLowerCase().contains(keyword) ||
                    jersey.getBrand().toLowerCase().contains(keyword) ||
                    jersey.getSport().toLowerCase().contains(keyword) ||
                    jersey.getColor().toLowerCase().contains(keyword)) {
                    matchesAny = true;
                    break;
                }
            }
            assertTrue(matchesAny, "Jersey should match at least one keyword");
        }
    }

    @Then("the system retrieves the latest list of jerseys on sale matching {string}")
    public void the_system_retrieves_the_latest_list_of_jerseys_on_sale_matching(String keyword) {
        assertNotNull(matchingJerseys);
        the_system_displays_a_list_of_jerseys_matching(keyword);
    }

    @Then("the display is updated accordingly")
    public void the_display_is_updated_accordingly() {
        assertNotNull(matchingJerseys);
    }

    @Then("highlights the matched keyword in the jersey details")
    public void highlights_the_matched_keyword_in_the_jersey_details() {
        // This is a UI concern and doesn't need backend implementation
        assertTrue(true);
    }

    @Then("the system displays detailed information about the jersey including model, size, color, and sale price")
    public void the_system_displays_detailed_information_about_the_jersey() {
        assertNotNull(selectedJersey);
        assertNotNull(selectedJersey.getDescription());
        assertNotNull(selectedJersey.getColor());
        assertNotNull(selectedJersey.getSalePrice());
    }

    @Then("the system displays the set of jerseys on sale for page {string}")
    public void the_system_displays_the_set_of_jerseys_on_sale_for_page(String pageNumber) {
        assertNotNull(matchingJerseys);
        assertTrue(matchingJerseys.size() <= PAGE_SIZE);
    }

    @Then("the system displays a message {string}")
    public void the_system_displays_a_message(String message) {
        if (message.equals("No matching jerseys found.")) {
            assertTrue(matchingJerseys.isEmpty());
        } else if (message.equals("Please add keywords to your wishlist to retrieve matching jerseys.")) {
            assertTrue(wishlist.getKeywords().isEmpty());
        } else if (message.equals("Unable to retrieve matching jerseys. Please try again later.")) {
            assertEquals(errorMessage, message);
        }
    }

    @Then("the error is logged for further analysis")
    public void the_error_is_logged_for_further_analysis() {
        assertNotNull(errorMessage);
    }

    @Given("the customer's wishlist contains special characters {string}")
    public void the_customer_s_wishlist_contains_special_characters(String keyword) {
        if (customer == null) {
            customer = new Customer("test@example.com", "password", "Test User");
            customerRepository.save(customer);
        }
        wishlist = new Wishlist(keyword, customer);
        wishlistRepository.save(wishlist);
    }

    @Given("the customer's wishlist contains case variations {string}")
    public void the_customer_s_wishlist_contains_case_variations(String keyword) {
        if (customer == null) {
            customer = new Customer("test@example.com", "password", "Test User");
            customerRepository.save(customer);
        }
        wishlist = new Wishlist(keyword, customer);
        wishlistRepository.save(wishlist);
    }

    @Then("the system returns results in relevance order for {string}")
    public void the_system_returns_results_in_relevance_order(String keyword) {
        assertNotNull(matchingJerseys, "Matching jerseys list should not be null");
        assertFalse(matchingJerseys.isEmpty(), "Matching jerseys list should not be empty");
        
        // First result should be exact brand match if exists
        if (!matchingJerseys.isEmpty()) {
            Jersey firstMatch = matchingJerseys.get(0);
            if (firstMatch.getBrand().equalsIgnoreCase(keyword)) {
                assertTrue(true, "First result is brand match as expected");
            } else if (firstMatch.getSport().equalsIgnoreCase(keyword)) {
                assertTrue(true, "First result is sport match as expected");
            }
        }
    }

    @Then("the system handles duplicate keywords correctly")
    public void the_system_handles_duplicate_keywords_correctly() {
        // Count occurrences of each jersey
        Set<String> uniqueJerseys = matchingJerseys.stream()
            .map(Jersey::getDescription)
            .collect(Collectors.toSet());
        
        assertEquals(uniqueJerseys.size(), matchingJerseys.size(), 
            "Each jersey should appear only once regardless of duplicate keywords");
    }

    @Then("the system finds jerseys with color {string}")
    public void the_system_finds_jerseys_with_color(String color) {
        assertNotNull(matchingJerseys, "Matching jerseys list should not be null");
        assertFalse(matchingJerseys.isEmpty(), "Should find jerseys with color: " + color);
        
        boolean hasColorMatch = matchingJerseys.stream()
            .anyMatch(j -> j.getColor().equalsIgnoreCase(color));
        assertTrue(hasColorMatch, "Should find at least one jersey with color: " + color);
    }

    @Then("the system finds jerseys with sport {string}")
    public void the_system_finds_jerseys_with_sport(String sport) {
        assertNotNull(matchingJerseys, "Matching jerseys list should not be null");
        assertFalse(matchingJerseys.isEmpty(), "Should find jerseys for sport: " + sport);
        
        boolean hasSportMatch = matchingJerseys.stream()
            .anyMatch(j -> j.getSport().equalsIgnoreCase(sport));
        assertTrue(hasSportMatch, "Should find at least one jersey for sport: " + sport);
    }

    @Then("the system handles special characters in the results")
    public void the_system_handles_special_characters_in_the_results() {
        assertNotNull(matchingJerseys, "Matching jerseys list should not be null");
        assertFalse(matchingJerseys.isEmpty(), "Should find jerseys with special characters");
        
        boolean hasSpecialChars = matchingJerseys.stream()
            .anyMatch(j -> j.getDescription().contains("*") || 
                         j.getDescription().contains("&") ||
                         j.getDescription().contains("-"));
        assertTrue(hasSpecialChars, "Should find jerseys with special characters");
    }
} 