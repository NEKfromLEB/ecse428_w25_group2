package ca.mcgill.ecse428.jerseycabinet.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ca.mcgill.ecse428.jerseycabinet.dao.JerseyRepository;
import ca.mcgill.ecse428.jerseycabinet.dto.Jersey.JerseyListingDTO;
import ca.mcgill.ecse428.jerseycabinet.model.Jersey;
import ca.mcgill.ecse428.jerseycabinet.model.Customer;
import ca.mcgill.ecse428.jerseycabinet.model.Employee;
import ca.mcgill.ecse428.jerseycabinet.service.ListJerseyService;

@ExtendWith(MockitoExtension.class)
public class JerseyListingTests {

    @Mock
    private JerseyRepository jerseyRepository;

    @InjectMocks
    private ListJerseyService jerseyService;

    private Jersey testJersey;
    private Customer testSeller;

    @BeforeEach
    void setUp() {
        testSeller = new Customer();
        testJersey = new Jersey(
            Jersey.RequestState.Unlisted,
            "Test Description",
            "Nike",
            "Hockey",
            "Red",
            "jersey.jpg",
            "auth.jpg",
            120.0,
            testSeller
        );
    }

    @Test
    public void testSuccessfulJerseyListing() {
        // Given
        when(jerseyRepository.findJerseyById(1)).thenReturn(testJersey);
        
        // Create a listed version of the jersey for the mock response
        Jersey listedVersion = new Jersey(
            Jersey.RequestState.Listed,  // Changed from Unlisted to Listed
            testJersey.getDescription(),
            testJersey.getBrand(),
            testJersey.getSport(),
            testJersey.getColor(),
            testJersey.getJerseyImage(),
            testJersey.getProofOfAuthenticationImage(),
            testJersey.getSalePrice(),
            testJersey.getSeller()
        );
        
        when(jerseyRepository.updateJerseyRequestState(1, Jersey.RequestState.Listed))
            .thenReturn(listedVersion);

        // When
        Jersey listedJersey = jerseyService.updateJerseyRequestState(1, Jersey.RequestState.Listed);

        // Then
        assertNotNull(listedJersey);
        assertEquals(Jersey.RequestState.Listed, listedJersey.getRequestState());
        assertEquals(120.0, listedJersey.getSalePrice());
        verify(jerseyRepository).updateJerseyRequestState(1, Jersey.RequestState.Listed);
    }

    @Test
    public void testListingWithMissingPrice() {
        // Given
        JerseyListingDTO listingDetails = new JerseyListingDTO(1, 0.0, "Test Description");

        // When & Then
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            jerseyService.updateJerseyListing(1, listingDetails);
        });
        assertTrue(exception.getMessage().contains("Price is required"));
    }

    @Test
    public void testListingWithMissingBrand() {
        // Given
        testJersey.setBrand(null);
        when(jerseyRepository.findJerseyById(1)).thenReturn(testJersey);

        // When & Then
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            jerseyService.updateJerseyRequestState(1, Jersey.RequestState.Listed);
        });
        assertTrue(exception.getMessage().contains("Brand is required"));
    }

    @Test
    public void testEditExistingJerseyListing() {
        // Given
        double newPrice = 100.0;
        testJersey.setRequestState(Jersey.RequestState.Listed);
        when(jerseyRepository.findJerseyById(1)).thenReturn(testJersey);
        when(jerseyRepository.save(any(Jersey.class))).thenReturn(testJersey);

        // When
        testJersey.setSalePrice(newPrice);
        Jersey updatedJersey = jerseyService.updateJerseyListing(1, 
            new JerseyListingDTO(1, newPrice, testJersey.getDescription()));

        // Then
        assertNotNull(updatedJersey);
        assertEquals(newPrice, updatedJersey.getSalePrice());
        verify(jerseyRepository).save(any(Jersey.class));
    }

    @Test
    public void testEditNonexistentJerseyListing() {
        // Given
        when(jerseyRepository.findJerseyById(999)).thenReturn(null);

        // When & Then
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            jerseyService.updateJerseyListing(999, 
                new JerseyListingDTO(999, 100.0, "Test Description"));
        });
        assertTrue(exception.getMessage().contains("Jersey not found"));
    }

    @Test
    public void testListingVisibilityToOtherUsers() {
        // Given
        testJersey.setRequestState(Jersey.RequestState.Listed);
        when(jerseyRepository.findJerseyById(1)).thenReturn(testJersey);

        // When
        Jersey listedJersey = jerseyService.getJerseyById(1);

        // Then
        assertNotNull(listedJersey);
        assertEquals(Jersey.RequestState.Listed, listedJersey.getRequestState());
        verify(jerseyRepository).findJerseyById(1);
    }
}
