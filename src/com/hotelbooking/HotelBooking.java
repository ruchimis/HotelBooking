package com.hotelbooking;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

import com.hotelbooking.model.Booking;
import com.hotelbooking.model.Room;
import com.hotelbooking.model.SearchResults;


public class HotelBooking {

	static int topResultsLimit = 3; // this can be changed to get the top N results
	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);		
		
		int pax = 0;
		do {
			System.out.println("Enter no. of pax :");
			while(!scanner.hasNextInt()) {
				System.out.println("Invalid Input...please try again.");
				scanner.next();
			}
			pax = scanner.nextInt();
		} while (pax < 0);
		
		scanner.close();
		
		List<Room> roomsDB = new ArrayList<Room>();
		
		try {
			roomsDB = initializeRoomsDB();
		} catch (Exception e) {
			System.out.println("Error Initializing DB data.");
			return;
		}
		
		System.out.println("=================== pax = " + pax + " ===================");
		List<SearchResults> results = allocateRooms(pax, roomsDB);
		
		if(results.size() > 0) {
			results.forEach(System.out::println);
		} else {
			System.out.println("No rooms available.");
		}
		
	}
	
	public static boolean validateInput(int pax) {
		if(Integer.valueOf(pax) < 0) {
			return false;
		}
		
		return true;	
	}
	
	
	/*
	 * this is the main method to retrieve the top N search results
	 * N can be changed by assigned the value to instance var - topResultsLimit
	 */
	public static List<SearchResults> allocateRooms(int pax, List<Room> roomsList) {
		List<SearchResults> topSearchResults = new ArrayList<SearchResults>();
		// check if no rooms available
		if(roomsList.isEmpty()) {
			return topSearchResults;
		}
		SearchResults searchResults = new SearchResults();
		// sort rooms on the basis of price pp. For priceModel - pu, the total room price would remain the same
		// so consider the price pp value to be the total value for priceModel - pu
		Collections.sort(roomsList);
		Room firstIndexRoom = roomsList.get(0);
		int guests = pax;
		List<Booking> allotedRooms = new ArrayList<Booking>();
		int counter = 0;
		do {
			for(int i = 0; i < roomsList.size(); i++) {
				
				Room room = roomsList.get(i);	
				
				for(int j=room.getMaxGuests(); j>=room.getMinGuests(); j--) {
					int roomAvailableOccupants = room.getMaxGuests() - room.getCurrentOccupancy(); // check if room is not already full
					int occupyingGuest = Math.min(guests, j);
					if(roomAvailableOccupants >= j && occupyingGuest >= room.getMinGuests()) {
						
						// first update this room occupancy, so that this room is not available when checking the room availability for remaining guests
						room.setCurrentOccupancy(room.getCurrentOccupancy() + occupyingGuest);
						
						// say if remaining guests = 1 and no room exists with minGuests = 1, then loop again - now with lesser guests
						// so that the remaining guests can now be adjusted in next rooms.
						if(anyRoomsExistsForRemainingPax(guests-occupyingGuest, roomsList)) {
							guests -= occupyingGuest;
							allotedRooms.add(new Booking(room.getRoomType(), occupyingGuest, room.getPrice(), room.getPriceModel()));
						} else {
							// if condition not satisfied, revert back the occupancy.
							room.setCurrentOccupancy(room.getCurrentOccupancy() - occupyingGuest);
							
						}
						
						if(guests <= 0 || room.getCurrentOccupancy() == room.getMaxGuests()) {
							break;
						}
					} 
					// else - when room is full...check next room in list
				}
				
				if(guests <= 0) {
					searchResults = new SearchResults();
					if(!sameBookingExists(topSearchResults, allotedRooms)) {
						searchResults.setBookings(allotedRooms);
						topSearchResults.add(searchResults);
					}
					// search for next best option
				}
				if((i == roomsList.size()-1 && guests > 0) || guests <= 0) {
					//loop over again after rotation
				
					guests = pax;
					
					roomsList.forEach(o -> o.setCurrentOccupancy(0));
	
					Collections.rotate(roomsList, 1);
					
					allotedRooms = new ArrayList<Booking>();
					i=-1; //loop over again from start with new combination
					
					//once one rotation is over swap the rooms each time in incremental order, to get the best possible price option
					if(roomsList.get(0) == firstIndexRoom) { 
						if(counter < roomsList.size() - 1) {
							Collections.swap(roomsList, counter, ++counter);
							firstIndexRoom = roomsList.get(0);
						} else {
							counter++;
						}
						break;
					}
				}
			} 
		} while (counter < roomsList.size()-1);
	
		Collections.sort(topSearchResults);
		
		return getTopNsearchResults(topSearchResults);
	}
	
	/*
	 * get the top N results. This number can be changed by changing topResultsLimit
	 */
	private static List<SearchResults> getTopNsearchResults(List<SearchResults> topSearchResults) {
		
		Collections.sort(topSearchResults);
		
		if(topSearchResults.size() > topResultsLimit) {
			topSearchResults = (List<SearchResults>) topSearchResults.subList(0, topResultsLimit);
		}
		return topSearchResults;
		
	}

	/*
	 * check before allotting a room if there is any room available for remaining guests
	 */
	private static boolean anyRoomsExistsForRemainingPax(int guests, List<Room> list) {
		if(guests <= 0) {
			return true;
		}
		for(Room r : list) {
			if(r.getCurrentOccupancy() != r.getMaxGuests() && r.getMinGuests() <= guests) {
				return true;
			}
		}
		return false;
	}
	
	private static boolean sameBookingExists(List<SearchResults> results, List<Booking> searchFor) {
		
		for(SearchResults record : results) {
			List<Booking> output = record.getBookings();
			output.sort(
				      Comparator.comparing(Booking::getRoomType).thenComparing(Booking::getPax)
				    );
			searchFor.sort(
				      Comparator.comparing(Booking::getRoomType).thenComparing(Booking::getPax)
				    );
			if(output.toString().equals(searchFor.toString())) {
				return true;
			}
		}
		return false;
	
	}
	
	/*
	 * rooms data is currently added in file.
	 * this can later be taken from database.
	 */
	public static List<Room> initializeRoomsDB() throws ParseException, IOException {
        FileInputStream fs = new FileInputStream("src/roomsDB.txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(fs));
        String line;
        Room room;
        List<Room> roomsDB = new ArrayList<Room>();
        
        br.readLine(); //first line as header
        do {
        	line= br.readLine();
        	if( line != null ) {
	        	StringTokenizer st = new StringTokenizer(line, ",");
	        	
	        	if(st.hasMoreTokens()){
	        		Long id = new Long(st.nextToken());
	        		String type = st.nextToken();
	        		Date startDate = sdf.parse(st.nextToken());
	        		Date endDate = sdf.parse(st.nextToken());
	        		int minPax = Integer.parseInt(st.nextToken());
	        		int maxPax = Integer.parseInt(st.nextToken());
	        		Double pricepp = new Double(st.nextToken());
	        		String priceModel = st.nextToken();
	        		room = new Room(id, type, startDate, endDate, minPax, maxPax, pricepp, priceModel);
	        		roomsDB.add(room); 
	            	
	        	}
        	}
        } while (line != null);
        br.close();
        
        return roomsDB;
    }

}
