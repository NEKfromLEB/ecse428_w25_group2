package ca.mcgill.ecse428.jerseycabinet.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ca.mcgill.ecse428.jerseycabinet.dao.WishlistRepository;
import ca.mcgill.ecse428.jerseycabinet.model.Jersey;
import ca.mcgill.ecse428.jerseycabinet.model.Customer;
import ca.mcgill.ecse428.jerseycabinet.model.Wishlist;

@ExtendWith(MockitoExtension.class)
public class WishlistServiceTests {

    @Mock
    private WishlistRepository wishlistRepository;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private WishlistService wishlistService;

    private Jersey jersey1;
    private Jersey jersey2;
    private Customer customer;
    private Wishlist wishlist;

    @BeforeEach
    public void setup() {
        // Create test jerseys
        jersey1 = new Jersey();
        jersey1.setId(1);
        jersey1.setBrand("Nike");
        jersey1.setDescription("Red Hockey Jersey");
        jersey1.setSport("Hockey");
        jersey1.setColor("Red");
        jersey1.setSalePrice(99.99);
        jersey1.setRequestState(Jersey.RequestState.Listed);

        jersey2 = new Jersey();
        jersey2.setId(2);
        jersey2.setBrand("Adidas");
        jersey2.setDescription("Blue Basketball Jersey");
        jersey2.setSport("Basketball");
        jersey2.setColor("Blue");
        jersey2.setSalePrice(79.99);
        jersey2.setRequestState(Jersey.RequestState.Listed);

        // Create test customer and wishlist
        customer = new Customer();
        customer.setEmail("test@example.com");

        wishlist = new Wishlist("", customer);
        wishlist.setId(1);
    }

    @Test
    public void testIdentifyJerseysWithValidKeywords() {
        // Arrange
        List<String> keywords = Arrays.asList("Red", "Hockey");
        when(wishlistRepository.getMatchingJerseys("Red", Jersey.RequestState.Listed)).thenReturn(Collections.singletonList(jersey1));
        when(wishlistRepository.getMatchingJerseys("Hockey", Jersey.RequestState.Listed)).thenReturn(Collections.singletonList(jersey1));

        // Act
        List<Jersey> result = wishlistService.identifyJerseys(keywords);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(jersey1.getId(), result.get(0).getId());
        verify(wishlistRepository, times(2)).getMatchingJerseys(anyString(), eq(Jersey.RequestState.Listed));
    }

    @Test
    public void testIdentifyJerseysWithNoMatches() {
        // Arrange
        List<String> keywords = Arrays.asList("Green", "Football");
        when(wishlistRepository.getMatchingJerseys(anyString(), eq(Jersey.RequestState.Listed))).thenReturn(Collections.emptyList());

        // Act
        List<Jersey> result = wishlistService.identifyJerseys(keywords);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(wishlistRepository, times(2)).getMatchingJerseys(anyString(), eq(Jersey.RequestState.Listed));
    }

    @Test
    public void testIdentifyJerseysWithNullKeywords() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            wishlistService.identifyJerseys(null);
        });
        verify(wishlistRepository, never()).getMatchingJerseys(anyString(), any(Jersey.RequestState.class));
    }

    @Test
    public void testIdentifyJerseysWithEmptyKeywords() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            wishlistService.identifyJerseys(Collections.emptyList());
        });
        verify(wishlistRepository, never()).getMatchingJerseys(anyString(), any(Jersey.RequestState.class));
    }

    @Test
    public void testIdentifyJerseysWithMultipleMatches() {
        // Arrange
        List<String> keywords = Arrays.asList("Jersey");
        when(wishlistRepository.getMatchingJerseys("Jersey", Jersey.RequestState.Listed)).thenReturn(Arrays.asList(jersey1, jersey2));

        // Act
        List<Jersey> result = wishlistService.identifyJerseys(keywords);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(j -> j.getId() == jersey1.getId()));
        assertTrue(result.stream().anyMatch(j -> j.getId() == jersey2.getId()));
        verify(wishlistRepository, times(1)).getMatchingJerseys(anyString(), eq(Jersey.RequestState.Listed));
    }

    @Test
    public void testNotifyCustomerOfSaleWithMatchingJersey() {
        // Arrange
        wishlist = new Wishlist("Hockey,Red,Nike", customer);
        when(wishlistRepository.findWishlistByCustomer(customer)).thenReturn(wishlist);
        when(notificationService.sendNotification(anyString())).thenReturn(true);

        // Act
        boolean result = wishlistService.notifyCustomerOfSale(customer, jersey1);

        // Assert
        assertTrue(result);
        verify(notificationService).sendNotification(contains("Red Hockey Jersey"));
        verify(notificationService).sendNotification(contains("99.99"));
    }

    @Test
    public void testNotifyCustomerOfSaleWithNonMatchingJersey() {
        // Arrange
        wishlist = new Wishlist("Football,Green", customer);
        when(wishlistRepository.findWishlistByCustomer(customer)).thenReturn(wishlist);

        // Act
        boolean result = wishlistService.notifyCustomerOfSale(customer, jersey1);

        // Assert
        assertFalse(result);
        verify(notificationService, never()).sendNotification(anyString());
    }

    @Test
    public void testNotifyCustomerOfSaleWithNullCustomer() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            wishlistService.notifyCustomerOfSale(null, jersey1);
        });
        verify(notificationService, never()).sendNotification(anyString());
    }

    @Test
    public void testNotifyCustomerOfSaleWithNullJersey() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            wishlistService.notifyCustomerOfSale(customer, null);
        });
        verify(notificationService, never()).sendNotification(anyString());
    }

    @Test
    public void testNotifyCustomerOfSaleWithEmptyWishlist() {
        // Arrange
        wishlist = new Wishlist("", customer);
        when(wishlistRepository.findWishlistByCustomer(customer)).thenReturn(wishlist);

        // Act
        boolean result = wishlistService.notifyCustomerOfSale(customer, jersey1);

        // Assert
        assertFalse(result);
        verify(notificationService, never()).sendNotification(anyString());
    }

    @Test
    public void testNotifyCustomerOfSaleWithNotificationFailure() {
        // Arrange
        wishlist = new Wishlist("Hockey,Red", customer);
        when(wishlistRepository.findWishlistByCustomer(customer)).thenReturn(wishlist);
        when(notificationService.sendNotification(anyString())).thenReturn(false);

        // Act
        boolean result = wishlistService.notifyCustomerOfSale(customer, jersey1);

        // Assert
        assertFalse(result);
        verify(notificationService).sendNotification(anyString());
    }
} 