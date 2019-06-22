package com.hotelbooking.model;

import java.util.List;


/**
 * SearchResults contain list of booking objects 
 * and total cost price of hotel booking. This class implements Comparable Interface
 * which sorts the list of SearchResults based on the total price of each search result.
 * Whenever searchResult is added to list, total price is calculated.
 *  
 * @author ruchi
 * @version 1.0
 */
public class SearchResults implements Comparable< SearchResults > {
	
	List<Booking> bookings;
	Double totalPrice;
	
	public List<Booking> getBookings() {
		return bookings;
	}
	public void setBookings(List<Booking> bookings) {
		// whenever booking option is saved to searchResults, update the total price
		this.bookings = bookings;
		setTotalPrice(getTotal(this.bookings));
	}
	
	public Double getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}
	
	public Double getTotal(List<Booking> bookings) {
		Double totPrice = 0.0;
		for(Booking booking : bookings) {
			totPrice += (booking.getPriceModel().equals("pu")) ? booking.getPricePerPerson() : booking.getPricePerPerson() * booking.getPax();
		}
		return totPrice;
	}
	
	@Override
	public String toString() {
		return "TOTAL PRICE = " + String.format("%.02f", totalPrice) + "\n" + bookings + "\n";
	}
	
	@Override
	public int compareTo(SearchResults obj) {
		return Float.compare(new Float(this.getTotalPrice()), new Float(obj.getTotalPrice()));
	}
	
}
