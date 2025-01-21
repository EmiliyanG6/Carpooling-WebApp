package com.carpooling.carpooling.services;

import com.carpooling.carpooling.models.Travel;
import com.carpooling.carpooling.repositories.TravelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class TravelServiceTest {

    @Mock
    private TravelRepository travelRepository;

    @InjectMocks
    private TravelServiceImpl travelService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAllTravels(){
        List<Travel> mockTrips = new ArrayList<>();
        mockTrips.add(new Travel());
        when(travelRepository.findAll()).thenReturn(mockTrips);

        List<Travel> trips = travelService.getAllTravels();

        assertNotNull(trips);
        assertEquals(1,trips.size());
        verify(travelRepository,times(1)).findAll();
    }

}
