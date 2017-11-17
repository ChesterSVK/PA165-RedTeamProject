package cz.fi.muni.pa165.teamred.service.facade;

import cz.fi.muni.pa165.teamred.dto.PlaceDTO;
import cz.fi.muni.pa165.teamred.dto.RideCreateDTO;
import cz.fi.muni.pa165.teamred.dto.RideDTO;
import cz.fi.muni.pa165.teamred.dto.UserDTO;
import cz.fi.muni.pa165.teamred.entity.Comment;
import cz.fi.muni.pa165.teamred.entity.Place;
import cz.fi.muni.pa165.teamred.entity.Ride;
import cz.fi.muni.pa165.teamred.entity.User;
import cz.fi.muni.pa165.teamred.service.BeanMappingService;
import cz.fi.muni.pa165.teamred.service.PlaceService;
import cz.fi.muni.pa165.teamred.service.RideService;
import cz.fi.muni.pa165.teamred.service.config.ServiceConfiguration;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.never;

/**
 * @author Šimon Mačejovský
 */
@ContextConfiguration(classes = ServiceConfiguration.class)
public class RideFacadeTest {

    @Mock
    BeanMappingService beanMappingService;

    @Mock
    RideService rideService;

    @Autowired
    @InjectMocks
    RideFacadeImpl rideFacade;

    private RideDTO rideDTO1;
    private RideDTO rideDTO2;

    private Ride ride1;
    private Ride ride2;

    private Place brno;
    private Place prague;

    private User adam;
    private User fero;

    private UserDTO adamDTO;
    private UserDTO feroDTO;

    private Comment comment1;
    private Comment comment2;

    private RideCreateDTO rideCreate1;
    private RideCreateDTO rideCreate2;

    @BeforeClass
    void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @BeforeMethod
    void prepareMethod(){

        adam = new User();
        fero = new User();

        adamDTO = new UserDTO();
        feroDTO = new UserDTO();

        brno = new Place();
        brno.setId(3L);

        prague = new Place();
        prague.setId(4L);

        comment1 = new Comment();
        comment1.setId(2L);

        comment2 = new Comment();
        comment2.setId(2L);

        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(2017, Calendar.NOVEMBER, 14);

        rideCreate1 = new RideCreateDTO();
        rideCreate1.setDeparture(calendar1.getTime());
        rideCreate1.setDriver(adamDTO);

        rideDTO1 = new RideDTO();
        rideDTO1.setId(8L);
        rideDTO1.setAvailableSeats(4);
        rideDTO1.setSeatPrice(5.00);
        rideDTO1.setDeparture(calendar1.getTime());
        rideDTO1.setDriver(adamDTO);

        ride1 = new Ride();
        ride1.setId(8L);
        ride1.setAvailableSeats(4);
        ride1.setSeatPrice(5.00);
        ride1.setDeparture(calendar1.getTime());
        ride1.setSourcePlace(brno);
        ride1.setDestinationPlace(prague);
        ride1.setDriver(adam);
        ride1.addComment(comment1);

        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(2016, Calendar.APRIL, 4);

        rideCreate2 = new RideCreateDTO();
        rideCreate2.setDeparture(calendar2.getTime());
        rideCreate1.setDriver(feroDTO);

        rideDTO2 = new RideDTO();
        rideDTO2.setId(1L);
        rideDTO2.setAvailableSeats(3);
        rideDTO2.setSeatPrice(7.00);
        rideDTO2.setDeparture(calendar2.getTime());
        rideDTO2.setDriver(feroDTO);

        ride2 = new Ride();
        ride2.setId(1L);
        ride2.setAvailableSeats(3);
        ride2.setSeatPrice(7.00);
        ride2.setDeparture(calendar2.getTime());
        ride2.setSourcePlace(prague);
        ride2.setDestinationPlace(brno);
        ride2.setDriver(fero);
        ride2.addComment(comment2);

        Mockito.reset(rideService);
    }


    // Create tests
    @Test
    void rideCreate(){
        when(beanMappingService.mapTo(rideCreate1, Ride.class)).thenReturn(ride1);
        when(rideService.createRide(ride1)).thenReturn(ride1);

        Long verifyId = rideFacade.createRide(rideCreate1);

        verify(rideService).createRide(ride1);
        assertThat(verifyId).isEqualTo(ride1.getId());
    }

    // Update tests
    @Test
    void changePrice() {
        doNothing().when(rideService).updateRide(any());
        when(rideService.findById(ride1.getId())).thenReturn(ride1);

        rideFacade.changePrice(ride1.getId(), 4);

        ride1.setSeatPrice(4);
        verify(rideService).updateRide(ride1);
    }


    @Test
    void changeAvailableSeats() {
        doNothing().when(rideService).updateRide(any());
        when(rideService.findById(ride1.getId())).thenReturn(ride1);

        rideFacade.editAvailableSeats(ride1.getId(), 1);

        ride1.setSeatPrice(1);
        verify(rideService).updateRide(ride1);
    }

    @Test
    void changeDeparture() {
        doNothing().when(rideService).updateRide(any());
        when(rideService.findById(ride1.getId())).thenReturn(ride1);

        Calendar newDeparture = Calendar.getInstance();
        newDeparture.set(2018, Calendar.JANUARY, 10, 15, 30);
        rideFacade.editDeparture(ride1.getId(), newDeparture.getTime());

        ride1.setDeparture(newDeparture.getTime());
        verify(rideService).updateRide(ride1);
    }



    // Delete tests
    @Test
    void deleteRide() {
        doNothing().when(rideService).deleteRide(any());

        rideFacade.deleteRide(ride1.getId());

        Ride testRide = new Ride();
        testRide.setId(ride1.getId());
        verify(rideService).deleteRide(testRide);
    }

    // Finding tests
    @Test
    void getRideWithId() {
        when(rideService.findById(ride1.getId())).thenReturn(ride1);
        when(beanMappingService.mapTo(ride1, RideDTO.class)).thenReturn(rideDTO1);

        RideDTO testRide = rideFacade.getRideWithId(ride1.getId());

        verify(rideService).findById(ride1.getId());
        assertThat(testRide).isEqualToComparingFieldByField(rideDTO1);
    }

    @Test
    void getAllRides() {
        List<Ride> allRides = new ArrayList<>();
        allRides.add(ride1);
        allRides.add(ride2);

        List<RideDTO> allRidesDTO = new ArrayList<>();
        allRidesDTO.add(rideDTO1);
        allRidesDTO.add(rideDTO2);

        when(rideService.findAll()).thenReturn(allRides);
        when(beanMappingService.mapTo(allRides, RideDTO.class)).thenReturn(allRidesDTO);

        List<RideDTO> testRideList = rideFacade.getAllRides();

        verify(rideService).findAll();
        assertThat(testRideList).containsExactlyInAnyOrder(rideDTO1, rideDTO2);
    }
    
}
