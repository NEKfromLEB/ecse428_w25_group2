package ca.mcgill.ecse428.jerseycabinet.acceptance.US019;


import ca.mcgill.ecse428.jerseycabinet.model.Jersey;
import ca.mcgill.ecse428.jerseycabinet.dao.JerseyRepository;
import ca.mcgill.ecse428.jerseycabinet.service.discountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JerseyServiceTest {

    @Mock
    private JerseyRepository jerseyRepository;

    @InjectMocks
    private discountService jerseyService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testApplyCategoryWideDiscount_NormalFlow() {
        Jersey jersey1 = new Jersey();
        jersey1.setId(1);
        jersey1.setSport("Soccer");
        jersey1.setSalePrice(100.00);

        Jersey jersey2 = new Jersey();
        jersey2.setId(2);
        jersey2.setSport("Soccer");
        jersey2.setSalePrice(150.00);

        List<Jersey> soccerJerseys = Arrays.asList(jersey1, jersey2);
        when(jerseyRepository.findBySport("Soccer")).thenReturn(soccerJerseys);

        jerseyService.applyCategoryWideDiscount("Soccer", 20.0);

        assertEquals(80.00, jersey1.getSalePrice());
        assertEquals(120.00, jersey2.getSalePrice());
        verify(jerseyRepository, times(1)).saveAll(soccerJerseys);
    }

    @Test
    void testApplyCategoryWideDiscount_AlternateFlow_NoJerseysInCategory() {
        when(jerseyRepository.findBySport("Soccer")).thenReturn(Collections.emptyList());
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                jerseyService.applyCategoryWideDiscount("Soccer", 30.0));
        assertEquals("No jerseys found in the specified category", exception.getMessage());
        verify(jerseyRepository, never()).saveAll(any());
    }

    @Test
    void testApplyCategoryWideDiscount_ErrorFlow_InvalidDiscountPercentage() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                jerseyService.applyCategoryWideDiscount("Soccer", -10.0));
        assertEquals("Invalid discount percentage: Discount must be between 0% and 100%", exception.getMessage());

        exception = assertThrows(IllegalArgumentException.class, () ->
                jerseyService.applyCategoryWideDiscount("Soccer", 110.0));
        assertEquals("Invalid discount percentage: Discount must be between 0% and 100%", exception.getMessage());

        verify(jerseyRepository, never()).findBySport(anyString());
        verify(jerseyRepository, never()).saveAll(any());
    }

    @Test
    void testApplyCategoryWideDiscount_ErrorFlow_NegativeSalePrice() {
        Jersey jersey = new Jersey();
        jersey.setId(1);
        jersey.setSport("Soccer");
        jersey.setSalePrice(10.00);

        List<Jersey> soccerJerseys = Collections.singletonList(jersey);
        when(jerseyRepository.findBySport("Soccer")).thenReturn(soccerJerseys);

        jerseyService.applyCategoryWideDiscount("Soccer", 90.0);

        assertEquals(0.9999999999999998, jersey.getSalePrice()); // No exception should be thrown
        verify(jerseyRepository, times(1)).saveAll(soccerJerseys);
    }
}