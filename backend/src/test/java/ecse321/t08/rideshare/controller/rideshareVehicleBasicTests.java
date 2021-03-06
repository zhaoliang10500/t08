package ecse321.t08.rideshare.controller;

import ecse321.t08.rideshare.entity.Vehicle;
import ecse321.t08.rideshare.repository.VehicleRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class rideshareVehicleBasicTests {

    @Mock
    private VehicleRepository vehicleDao;

    @InjectMocks
    private VehicleController vehicleController;

    private static final int DRIVER_ID = -3;
    private static final int SEATS = 4;
    private static final String COLOUR = "blue";
    private static final String MODEL = "CarModel";
    private static final String VEHICLE_TYPE = "Minivan";
    private static final int VEHICLE_ID = -1;
    private static final int NONEXISTING_VEHICLE_ID = -2;

    @Before
    public void setMockTrueOutput() {
        when(vehicleDao.getVehicle(anyInt())).thenAnswer((InvocationOnMock invocation) -> {
            if (invocation.getArgument(0).equals(VEHICLE_ID)) {
                Vehicle vehicle = new Vehicle();
                vehicle.setVehicleId(VEHICLE_ID);
                vehicle.setNbOfSeats(SEATS);
                vehicle.setColour(COLOUR);
                vehicle.setDriverId(DRIVER_ID);
                vehicle.setModel(MODEL);
                vehicle.setVehicleType(VEHICLE_TYPE);
                return vehicle;
            } else {
                return null;
            }
        });
    }

    @Test
    public void testVehicleSimpleQueryFound() {
        assertEquals(HttpStatus.OK, vehicleController.getVehicle(VEHICLE_ID).getStatusCode());
    }

    @Test
    public void testVehicleQueryNotFound() {
        assertEquals(HttpStatus.NOT_FOUND, vehicleController.getVehicle(NONEXISTING_VEHICLE_ID).getStatusCode());

    }
}