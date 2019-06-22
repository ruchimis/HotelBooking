package com.hotelbooking.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.hotelbooking.HotelBooking;
import com.hotelbooking.model.Room;
import com.hotelbooking.model.SearchResults;

@RunWith(Parameterized.class)
public class HotelBookingTest {

	static List<Room> roomsList;
	private Integer pax;
	private Double[] expectedResults;
	
	
	@Parameters
    public static Collection<Object[]> data(){
        return Arrays.asList(new Object[][] {
          {1, new Double[] {95.0}},
          {2, new Double[] {140.00, 190.00, 240.00}},
          {3, new Double[] {210.00, 285.00, 335.00}},
          {4, new Double[] {305.00, 330.00, 380.00}},
          {5, new Double[] {400.00, 425.00, 450.00}},
          {6, new Double[] {495.00, 520.00, 540.00}},
          {7, new Double[] {590.00, 610.00, 640.00}},
          {8, new Double[] {705.00, 730.00, 735.00}},
          {9, new Double[] {800.00, 825.00, 830.00}},
          {10, new Double[] {895.00, 920.00, 940.00}}
        });
    }
    
    
    public HotelBookingTest(Integer pax, Double[] expectedResults){
        this.pax = pax;
        this.expectedResults = expectedResults; 
    }
	

	@BeforeClass
	public static void setUpDefaults() throws Exception {
		System.out.println("setting up defaults....");
		roomsList = HotelBooking.initializeRoomsDB();
		assertNotNull(roomsList);
		System.out.println("setting up....complete");
	}

	
	@Test
	public void testMain() {
		
		List<SearchResults> topSearchResults = HotelBooking.allocateRooms(pax, roomsList);
		if(topSearchResults.isEmpty()) {
			assertNull(expectedResults);
		}
		for(int i = 0; i < topSearchResults.size(); i++) {
			SearchResults searchResult = topSearchResults.get(i);
			assertTrue(expectedResults[i].doubleValue() == searchResult.getTotalPrice());
		}
		System.out.println("Pax = " + pax + " [ Best Prices = " + Arrays.toString(expectedResults) + " ]");
	}

}
